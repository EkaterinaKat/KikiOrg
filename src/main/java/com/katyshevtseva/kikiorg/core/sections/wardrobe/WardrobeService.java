package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.repo.OutfitRepo;
import com.katyshevtseva.kikiorg.core.repo.PieceRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WardrobeService {
    @Autowired
    private PieceRepo pieceRepo;
    @Autowired
    private OutfitRepo outfitRepo;

    public void savePiece(Piece piece) {
        pieceRepo.save(piece);
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
