package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.CollageEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Category;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.OutfitSeason;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceState;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
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

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Piece> getPieces(PieceState pieceState, Category category) {
        Specification<Piece> pieceSpec = new PieceSpec(null, pieceState, category);
        return pieceRepo.findAll(pieceSpec);
    }

    public Page<Piece> getPiecePage(int pageNum, PieceType pieceType, PieceState pieceState, Category category) {
        Pageable pageable = PageRequest.of(pageNum, 9, Sort.by("id").descending());
        Specification<Piece> pieceSpec = new PieceSpec(pieceType, pieceState, category);
        org.springframework.data.domain.Page<Piece> piecePage = pieceRepo.findAll(pieceSpec, pageable);
        return new Page<>(piecePage.getContent(), pageNum, piecePage.getTotalPages());
    }

    public List<Piece> getPiecesToAddToOutfit(PieceType pieceType, Category category) {
        Specification<Piece> pieceSpec = new PieceSpec(pieceType, PieceState.ACTIVE, category);
        return pieceRepo.findAll(pieceSpec, Sort.by("id").descending());
    }

    public Page<Outfit> getOutfitPage(int pageNum, OutfitSeason season, Category category) {
        Pageable pageable = PageRequest.of(pageNum, 4, Sort.by("id").descending());
        org.springframework.data.domain.Page<Outfit> outfitPage =
                outfitRepo.findAll(new OutfitSpec(season, category), pageable);
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
                           Category category,
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

    public Outfit saveOutfit(Outfit existing, String comment,
                             OutfitSeason season,
                             CollageEntity collageEntity, Category category) {
        if (season == null) {
            throw new RuntimeException("Сезон не заполнены");
        }

        if (existing == null) {
            existing = new Outfit();
            existing.setCategory(category);
        }
        existing.setComment(comment != null ? comment.trim() : null);
        existing.setSeason(season);
        existing.setCollageEntity(collageEntity);
        existing.setCategory(category);

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
            if (componentEntity.getPiece().equals(piece)) {
                return false;
            }
        }
        return true;
    }
}
