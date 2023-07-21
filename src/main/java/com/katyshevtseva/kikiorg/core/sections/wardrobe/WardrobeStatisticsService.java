package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.PieceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class WardrobeStatisticsService {
    private final PieceRepo pieceRepo;

    public String getPieceStatistics() {
        return
                "Всего вещей: " + getAllCount() + "\n" +
                        "Архивированных вещей: " + getArchivedCount() + "\n" +
                        "Действующих вещей: " + getActiveCount() + "\n" +
                        "Используемых вещей: " + getUsedCount() + "\n" +
                        "Неиспользуемых вещей: " + getUnusedCount() + "\n\n" +
                        getClothesTypesStatistics();
    }

    private String getClothesTypesStatistics() {
        StringBuilder stringBuilder = new StringBuilder("Категории вещей:\n");
        for (PieceSubtype pieceSubtype : PieceSubtype.getSortedByTitleValues()) {
            stringBuilder.append(" * ").append(pieceSubtype.getTitle()).append(": ").append(getCount(pieceSubtype)).append("\n");
        }
        return stringBuilder.toString();
    }

    private int getCount(PieceSubtype type) {
        return pieceRepo.countByType(type).intValue();
    }

    private int getAllCount() {
        return (int) pieceRepo.count();
    }

    private int getArchivedCount() {
        return (int) pieceRepo.findByEndDateIsNotNull(PageRequest.of(0, 1)).getTotalElements();
    }

    private int getActiveCount() {
        return getAllCount() - getArchivedCount();
    }

    private int getUsedCount() {
        return getActiveCount() - getUnusedCount();
    }

    private int getUnusedCount() {
        return (int) pieceRepo.findUnusedPieces(Arrays.asList(PieceSubtype.values()), PageRequest.of(0, 1)).getTotalElements();
    }
}
