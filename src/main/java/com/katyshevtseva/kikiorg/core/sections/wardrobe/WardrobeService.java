package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.CollageEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.*;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.CollageEntityRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.ComponentEntityRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.OutfitRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.PieceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSupertype.getSupertypesBySubtype;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

@Service
@RequiredArgsConstructor
public class WardrobeService {
    private final PieceRepo pieceRepo;
    private final OutfitRepo outfitRepo;
    private final CollageEntityRepo collageEntityRepo;
    private final ComponentEntityRepo componentEntityRepo;
    private final DateService dateService;

    public List<Piece> getAllPieces() {
        return pieceRepo.findAll().stream()
                .sorted(Comparator.comparing(Piece::getStartDate, nullsFirst(naturalOrder())))
                .collect(Collectors.toList());
    }

    public List<Piece> getActivePieces() {
        return pieceRepo.findByEndDateIsNull().stream()
                .sorted(Comparator.comparing(Piece::getStartDate, nullsFirst(naturalOrder())))
                .collect(Collectors.toList());
    }

    public Page<Piece> getPiecePage(int pageNum, PieceType pieceType, PieceState pieceState, PieceCategory pieceCategory) {
        Pageable pageable = PageRequest.of(pageNum, 9, Sort.by("id").descending());
        Specification<Piece> pieceSpec = new PieceSpec(pieceType, pieceState, pieceCategory);
        org.springframework.data.domain.Page<Piece> piecePage = pieceRepo.findAll(pieceSpec, pageable);
        return new Page<>(piecePage.getContent(), pageNum, piecePage.getTotalPages());
    }

    public List<Piece> getPiecesToAddToOutfit(PieceType pieceType) {
        // пока здесь в параметрах захардкожено PieceCategory.GOING_OUT, так как пока оставим возможность составлять
        // аутфиты только для одежды на выход, в будущем может быть сделаем разные виду аутфитов: аутфит для дома,
        // аутфит для спорта. Так как мы решили никак не смешивать предметы разных категорий
        Specification<Piece> pieceSpec = new PieceSpec(pieceType, PieceState.ACTIVE, PieceCategory.GOING_OUT);
        return pieceRepo.findAll(pieceSpec, Sort.by("id").descending());
    }

    public List<Piece> getPiecesAvailableToAddToExistingComponent(Piece piece) {
        Set<PieceSubtype> subtypes = getSupertypesBySubtype(piece.getType()).stream()
                .flatMap(pieceSupertype -> pieceSupertype.getTypes().stream())
                .collect(Collectors.toSet());
        subtypes.add(piece.getType());
        return subtypes.stream().flatMap(subtype -> getPiecesToAddToOutfit(subtype).stream()).collect(Collectors.toList());
    }

    public Page<Outfit> getOutfitPage(int pageNum, OutfitPurpose purpose, OutfitSeason season) {
        Pageable pageable = PageRequest.of(pageNum, 4, Sort.by("id").descending());
        org.springframework.data.domain.Page<Outfit> outfitPage =
                outfitRepo.findAll(new OutfitSpec(season, purpose), pageable);
        return new Page<>(outfitPage.getContent(), pageNum, outfitPage.getTotalPages());
    }

    public Page<Outfit> getOutfitsByPiece(int pageNum, Piece piece) {
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by("id").descending());
        org.springframework.data.domain.Page<Outfit> outfitPage = outfitRepo.findOutfitsByPiece(piece, pageable);
        return new Page<>(outfitPage.getContent(), pageNum, outfitPage.getTotalPages());
    }

    public Piece savePiece(Piece existing,
                           String description,
                           String imageFileName,
                           PieceSubtype type,
                           PieceCategory category,
                           Date start,
                           Date end) {

        if (existing == null)
            existing = new Piece();
        existing.setImageFileName(imageFileName);
        existing.setDescription(description);
        existing.setStartDate(dateService.createIfNotExistAndGetDateEntity(start));
        existing.setEndDate(dateService.createIfNotExistAndGetDateEntity(end));
        existing.setType(type);
        existing.setCategory(category);

        return pieceRepo.save(existing);
    }

    public CollageEntity saveCollage(CollageEntity existing) {
        if (existing == null)
            existing = new CollageEntity();

        return collageEntityRepo.save(existing);
    }

    @Transactional
    public void saveComponents(List<ComponentEntity> components, CollageEntity collageEntity) {
        componentEntityRepo.deleteByCollageEntity(collageEntity);

        for (ComponentEntity componentEntity : components) {
            componentEntityRepo.save(componentEntity);
        }
    }

    public Outfit saveOutfit(Outfit existing, String comment, OutfitSeason season, OutfitPurpose purpose, CollageEntity collageEntity) {
        if (season == null || purpose == null) {
            throw new RuntimeException("Цель или сезон не заполнены");
        }

        if (existing == null)
            existing = new Outfit();
        existing.setComment(comment != null ? comment.trim() : null);
        existing.setPurpose(purpose);
        existing.setSeason(season);
        existing.setCollageEntity(collageEntity);

        return outfitRepo.save(existing);
    }

    public void deleteOutfit(Outfit outfit) {
        for (ComponentEntity componentEntity : outfit.getCollageEntity().getComponents()) {
            componentEntityRepo.delete(componentEntity);
        }
        outfit.getCollageEntity().setComponents(new HashSet<>());
        outfitRepo.delete(outfit);
        collageEntityRepo.delete(outfit.getCollageEntity());
    }

    public void archivePiece(Piece piece) {
        if (piece.getEndDate() != null) {
            throw new RuntimeException("Вещь уже архивирована");
        }
        if (!pieceAvailableForArchive(piece)) {
            throw new RuntimeException("Невозможно архивировать используемую вещь");
        }

        piece.setEndDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        pieceRepo.save(piece);
    }

    public void returnToWork(Piece piece) {
        if (piece.getEndDate() == null) {
            throw new RuntimeException("Вещь уже в работе");
        }

        piece.setEndDate(null);
        pieceRepo.save(piece);
    }

    public boolean pieceAvailableForArchive(Piece piece) {
        for (ComponentEntity componentEntity : componentEntityRepo.findAll()) {
            if (componentEntity.getPieces().contains(piece)) {
                return false;
            }
        }
        return true;
    }
}
