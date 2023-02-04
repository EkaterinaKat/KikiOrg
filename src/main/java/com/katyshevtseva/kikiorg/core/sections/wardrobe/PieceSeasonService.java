package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.OutfitRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PieceSeasonService {
    private final OutfitRepo outfitRepo;

    public Season getPieceSeasonOrNull(Piece piece) {
        List<Season> seasons = outfitRepo.findOutfitsByPiece(piece).stream()
                .map(Outfit::getSeason).distinct().collect(Collectors.toList());
        if (seasons.size() == 1)
            return seasons.get(0);
        return null;
    }
}
