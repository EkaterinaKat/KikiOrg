package com.katyshevtseva.kikiorg.core.sections.wardrobe;

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
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
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

    public Piece savePiece(Piece existing,
                           String description,
                           String imageFileName,
                           ClothesType type,
                           Date start,
                           Date end,
                           Set<Season> seasons,
                           Set<Purpose> purposes) {

        if (existing == null)
            existing = new Piece();
        existing.setImageFileName(imageFileName);
        existing.setDescription(description);
        existing.setStartDate(dateService.createIfNotExistAndGetDateEntity(start));
        existing.setEndDate(dateService.createIfNotExistAndGetDateEntity(end));
        existing.setType(type);
        existing.setPurposes(purposes);
        existing.setSeasons(seasons);

        return pieceRepo.save(existing);
    }

    public List<Outfit> getAllOutfits() {
        return outfitRepo.findAll();
    }

    public CollageEntity saveCollage(CollageEntity existing, int width, int height, String color) {
        if (existing == null)
            existing = new CollageEntity();
        existing.setWidth(width);
        existing.setHeight(height);
        existing.setColor(color);

        return collageEntityRepo.save(existing);
    }

    public ComponentEntity saveComponent(ComponentEntity existing, int x, int y,
                                         double relativeWidth, List<Piece> pieces, CollageEntity collageEntity) {
        if (existing == null)
            existing = new ComponentEntity();
        existing.setX(x);
        existing.setY(y);
        existing.setRelativeWidth(relativeWidth);
        existing.setPieces(pieces);
        existing.setCollageEntity(collageEntity);

        return componentEntityRepo.save(existing);
    }

    public Outfit saveOutfit(Outfit existing, Set<Season> seasons, Set<Purpose> purposes, CollageEntity collageEntity) {
        if (existing == null)
            existing = new Outfit();
        existing.setPurposes(purposes);
        existing.setSeasons(seasons);
        existing.setCollageEntity(collageEntity);

        return outfitRepo.save(existing);
    }
}
