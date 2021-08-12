package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.PieceRepo;
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

@Service
@RequiredArgsConstructor
public class WardrobeService {
    private final PieceRepo pieceRepo;
    private final DateService dateService;

    public List<Piece> getAllPieces() {
        return pieceRepo.findAll().stream().sorted(Comparator.comparing(Piece::getStartDate)).collect(Collectors.toList());
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
}
