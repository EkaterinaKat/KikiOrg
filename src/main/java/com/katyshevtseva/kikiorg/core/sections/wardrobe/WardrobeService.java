package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.CollageEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesSubtype;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
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

import static com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesSupertype.getSupertypesBySubtype;
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

    public enum PieceFilter {
        ARCHIVE, ACTIVE, UNUSED
    }

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

    public Page<Piece> getPiecePage(int pageNum, ClothesType clothesType, PieceFilter pieceFilter) {
        Pageable pageable = PageRequest.of(pageNum, 9, Sort.by("id").descending());
        Specification<Piece> pieceSpec = new PieceSpec(clothesType, pieceFilter);
        org.springframework.data.domain.Page<Piece> piecePage = pieceRepo.findAll(pieceSpec, pageable);
        return new Page<>(piecePage.getContent(), pageNum, piecePage.getTotalPages());
    }

    public List<Piece> getPiecesToAddToOutfit(ClothesType clothesType) {
        Specification<Piece> pieceSpec = new PieceSpec(clothesType, PieceFilter.ACTIVE);
        return pieceRepo.findAll(pieceSpec, Sort.by("id").descending());
    }

    public List<Piece> getPiecesAvailableToAddToExistingComponent(Piece piece) {
        Set<ClothesSubtype> subtypes = getSupertypesBySubtype(piece.getType()).stream()
                .flatMap(clothesSupertype -> clothesSupertype.getTypes().stream())
                .collect(Collectors.toSet());
        subtypes.add(piece.getType());
        return subtypes.stream().flatMap(subtype -> getPiecesToAddToOutfit(subtype).stream()).collect(Collectors.toList());
    }

    public Page<Outfit> getOutfitPage(int pageNum, Purpose purpose, Season season) {
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
                           ClothesSubtype type,
                           Date start,
                           Date end) {

        if (existing == null)
            existing = new Piece();
        existing.setImageFileName(imageFileName);
        existing.setDescription(description);
        existing.setStartDate(dateService.createIfNotExistAndGetDateEntity(start));
        existing.setEndDate(dateService.createIfNotExistAndGetDateEntity(end));
        existing.setType(type);

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

    public Outfit saveOutfit(Outfit existing, String comment, Season season, Purpose purpose, CollageEntity collageEntity) {
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

    public boolean pieceAvailableForArchive(Piece piece) {
        for (ComponentEntity componentEntity : componentEntityRepo.findAll()) {
            if (componentEntity.getPieces().contains(piece)) {
                return false;
            }
        }
        return true;
    }
}
