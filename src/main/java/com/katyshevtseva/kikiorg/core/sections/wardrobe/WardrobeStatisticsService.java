package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.repo.OutfitRepo;
import com.katyshevtseva.kikiorg.core.repo.PieceRepo;
import com.katyshevtseva.kikiorg.core.report.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WardrobeStatisticsService {
    private final PieceRepo pieceRepo;
    private final OutfitRepo outfitRepo;

    public List<List<ReportCell>> getOutfitStatistics() {
        List<List<ReportCell>> report = new ArrayList<>();

        report.add(getHeadLine());
        for (int i = 0; i < Purpose.values().length; i++) {
            Purpose purpose = Purpose.values()[i];
            List<ReportCell> line = new ArrayList<>();
            line.add(ReportCell.filled(purpose.toString(), ReportCell.Color.WHITE, 200));
            for (Season season : Season.values()) {
                int count = getCount(season, purpose);
                line.add(ReportCell.filled("" + count, count == 0 ? ReportCell.Color.WHITE : ReportCell.Color.SLATE_BLUE));
            }
            report.add(line);
        }

        return report;
    }

    private int getCount(Season season, Purpose purpose) {
        return (int) outfitRepo.findByPurposesAndSeasons(
                Collections.singletonList(purpose),
                Collections.singletonList(season),
                PageRequest.of(0, 1)).getTotalElements();
    }

    private List<ReportCell> getHeadLine() {
        List<ReportCell> headLine = new ArrayList<>();
        headLine.add(ReportCell.empty());
        for (Season season : Season.values()) {
            headLine.add(ReportCell.columnHead(season.toString()));
        }
        return headLine;
    }

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
        for (ClothesType clothesType : ClothesType.getSortedByTitleValues()) {
            stringBuilder.append(" * ").append(clothesType.getTitle()).append(": ").append(getCount(clothesType)).append("\n");
        }
        return stringBuilder.toString();
    }

    private int getCount(ClothesType type) {
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
        return (int) pieceRepo.findUnusedPieces(Arrays.asList(ClothesType.values()), PageRequest.of(0, 1)).getTotalElements();
    }
}
