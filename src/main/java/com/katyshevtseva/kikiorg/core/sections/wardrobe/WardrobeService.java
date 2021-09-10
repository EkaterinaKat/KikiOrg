package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.CollageEntityRepo;
import com.katyshevtseva.kikiorg.core.repo.ComponentEntityRepo;
import com.katyshevtseva.kikiorg.core.repo.OutfitRepo;
import com.katyshevtseva.kikiorg.core.repo.PieceRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.CollageEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public enum PieceStatus {
        ARCHIVE, ACTIVE
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

    public Page<Piece> getPiecePage(int pageNum, ClothesType clothesType, PieceStatus pieceStatus) {
        Pageable pageable = PageRequest.of(pageNum, 9, Sort.by("id").descending());

        org.springframework.data.domain.Page<Piece> piecePage;

        if (pieceStatus == PieceStatus.ARCHIVE)
            piecePage = clothesType == null ? pieceRepo.findByEndDateIsNotNull(pageable) : pieceRepo.findByTypeAndEndDateIsNotNull(clothesType, pageable);
        else
            piecePage = clothesType == null ? pieceRepo.findByEndDateIsNull(pageable) : pieceRepo.findByTypeAndEndDateIsNull(clothesType, pageable);

        return new Page<>(piecePage.getContent(), pageNum, piecePage.getTotalPages());
    }

    public Page<Outfit> getOutfitPage(int pageNum) {
        org.springframework.data.domain.Page<Outfit> outfitPage = outfitRepo.findAll(PageRequest.of(pageNum, 4));
        return new Page<>(outfitPage.getContent(), pageNum, outfitPage.getTotalPages());
    }

    public Piece savePiece(Piece existing,
                           String description,
                           String imageFileName,
                           ClothesType type,
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

    public List<Outfit> getAllOutfits() {
        return new ArrayList<>(outfitRepo.findAll());
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

    public Outfit saveOutfit(Outfit existing, Set<Season> seasons, Set<Purpose> purposes, CollageEntity collageEntity) {
        if (existing == null)
            existing = new Outfit();
        existing.setPurposes(purposes);
        existing.setSeasons(seasons);
        existing.setCollageEntity(collageEntity);

        return outfitRepo.save(existing);
    }

    public void archivePiece(Piece piece) {
        piece.setEndDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        pieceRepo.save(piece);
        deletePieceFromAllComponents(piece);
    }

    private void deletePieceFromAllComponents(Piece piece) {
        for (ComponentEntity componentEntity : componentEntityRepo.findAll()) {
            if (componentEntity.getPieces().contains(piece)) {
                componentEntity.getPieces().remove(piece);
                if (componentEntity.getPieces().isEmpty()) {
                    componentEntityRepo.delete(componentEntity);
                } else {
                    if (componentEntity.getFrontPiece().equals(piece)) {
                        componentEntity.setFrontPiece(new ArrayList<>(componentEntity.getPieces()).get(0));
                    }
                    componentEntityRepo.save(componentEntity);
                }
            }
        }
    }
}
