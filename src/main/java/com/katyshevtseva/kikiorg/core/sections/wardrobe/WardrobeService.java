package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.OutfitRepo;
import com.katyshevtseva.kikiorg.core.repo.PieceRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WardrobeService {
    private final PieceRepo pieceRepo;
    private final OutfitRepo outfitRepo;
    private final DateService dateService;

    public Piece savePiece(Piece existing, String imageName, String description, Date start, Date end, ClothesType type,
                           Set<Purpose> purposes, Set<Season> seasons) {

        if (existing == null)
            existing = new Piece();
        existing.setImageName(imageName);
        existing.setDescription(description);
        existing.setStartDate(dateService.createIfNotExistAndGetDateEntity(start));
        existing.setEndDate(dateService.createIfNotExistAndGetDateEntity(end));
        existing.setType(type);
        existing.setPurposes(purposes);
        existing.setSeasons(seasons);

        return pieceRepo.save(existing);
    }

    public void saveOutfit(Outfit outfit) {
        outfitRepo.save(outfit);
    }

    public List<Piece> getAllPieces() {
        return pieceRepo.findAll();
    }

    public List<Outfit> getAllOutfits() {
        return outfitRepo.findAll();
    }

    public List<Outfit> getOutfitsBySeasonsAndPurposes(List<Season> seasons, List<Purpose> purposes) {
        List<Outfit> outfits = getAllOutfits();
        List<Outfit> resultOutfitList = new ArrayList<>();
        for (Outfit outfit : outfits) {
            for (Season season : seasons)
                if (outfit.getSeasons().contains(season)) {
                    resultOutfitList.add(outfit);
                    break;
                }
            for (Purpose purpose : purposes)
                if (outfit.getPurposes().contains(purpose)) {
                    resultOutfitList.add(outfit);
                    break;
                }
        }
        return resultOutfitList;
    }
}
