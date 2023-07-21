package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.fx.Styler.StandardColor;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.OutfitPurpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.OutfitSeason;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.OutfitRepo;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.PieceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WardrobeStatisticsService {
    private final PieceRepo pieceRepo;
    private final OutfitRepo outfitRepo;

    public List<List<ReportCell>> getOutfitStatistics() {
        List<List<ReportCell>> report = new ArrayList<>();

        report.add(getHeadLine());
        for (int i = 0; i < OutfitPurpose.values().length; i++) {
            OutfitPurpose purpose = OutfitPurpose.values()[i];
            List<ReportCell> line = new ArrayList<>();
            line.add(ReportCell.filled(purpose.toString(), StandardColor.WHITE, 200));
            for (OutfitSeason season : OutfitSeason.values()) {
                int count = getCount(season, purpose);
                line.add(ReportCell.filled("" + count, count == 0 ? StandardColor.WHITE : StandardColor.BLUE));
            }
            report.add(line);
        }

        return report;
    }

    private int getCount(OutfitSeason season, OutfitPurpose purpose) {
        return (int) outfitRepo.findByPurposeAndSeason(purpose, season, PageRequest.of(0, 1)).getTotalElements();
    }

    private List<ReportCell> getHeadLine() {
        List<ReportCell> headLine = new ArrayList<>();
        headLine.add(ReportCell.empty());
        for (OutfitSeason season : OutfitSeason.values()) {
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
