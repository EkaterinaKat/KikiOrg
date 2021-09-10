package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.report.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WardrobeStatisticsService {

    public List<List<ReportCell>> getOutfitStatistics() {
        List<List<ReportCell>> report = new ArrayList<>();

        report.add(getHeadLine());
        for (int i = 0; i < Purpose.values().length; i++) {
            Purpose purpose = Purpose.values()[i];
            List<ReportCell> line = new ArrayList<>();
            line.add(ReportCell.filled(purpose.toString(), ReportCell.Color.WHITE, 200));
            for (Season season : Season.values()) {
                line.add(ReportCell.filled("" + getCount(season, purpose), ReportCell.Color.WHITE));
            }
            report.add(line);
        }

        return report;
    }

    private int getCount(Season season, Purpose purpose) {
        return 3;
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
        for (ClothesType clothesType : ClothesType.values()) {
            stringBuilder.append(" * ").append(clothesType.getTitle()).append(": ").append(getCount(clothesType)).append("\n");
        }
        return stringBuilder.toString();
    }

    private int getCount(ClothesType type) {
        return 8;
    }

    private int getAllCount() {
        return 0;
    }

    private int getArchivedCount() {
        return 0;
    }

    private int getActiveCount() {
        return 0;
    }

    private int getUsedCount() {
        return 0;
    }

    private int getUnusedCount() {
        return 0;
    }
}
