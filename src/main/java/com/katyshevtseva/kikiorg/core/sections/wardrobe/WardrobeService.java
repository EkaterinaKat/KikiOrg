package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.repo.OutfitRepo;
import com.katyshevtseva.kikiorg.core.repo.PieceRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
