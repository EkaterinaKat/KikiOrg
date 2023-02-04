package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.sections.finance.*;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService;
import com.katyshevtseva.kikiorg.core.sections.finance.search.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.habits.*;
import com.katyshevtseva.kikiorg.core.sections.structure.StructureService;
import com.katyshevtseva.kikiorg.core.sections.tracker.BoardSortService;
import com.katyshevtseva.kikiorg.core.sections.tracker.TrackerService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.PieceSeasonService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Core implements InitializingBean {
    private static Core INSTANCE;
    private final FinanceService financeService;
    private final HabitsService habitsService;
    private final HabitsReportService habitsReportService;
    private final ScuttleCheckService scuttleCheckService;
    private final FinanceOperationService financeOperationService;
    private final HabitMarkService habitMarkService;
    private final WardrobeService wardrobeService;
    private final AnalysisService analysisService;
    private final StabilityCriterionService stabilityCriterionService;
    private final HuddleCheckService huddleCheckService;
    private final FinanceSearchService financeSearchService;
    private final TrackerService trackerService;
    private final BoardSortService boardSortService;
    private final WardrobeStatisticsService wardrobeStatisticsService;
    private final FinanceReportService financeReportService;
    private final ReportPeriodService reportPeriodService;
    private final ItemHierarchyService itemHierarchyService;
    private final TransferService transferService;
    private final StructureService structureService;
    private final AccountGroupService accountGroupService;
    private final AccountDeleteService accountDeleteService;
    private final ItemMergeService itemMergeService;
    private final PieceSeasonService pieceSeasonService;

    public static Core getInstance() {
        while (INSTANCE == null) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() {
        INSTANCE = this;
    }

    public PieceSeasonService pieceSeasonService() {
        return pieceSeasonService;
    }

    public ItemMergeService itemMergeService() {
        return itemMergeService;
    }

    public AccountDeleteService accountDeleteService() {
        return accountDeleteService;
    }

    public AccountGroupService accountGroupService() {
        return accountGroupService;
    }

    public TransferService currencyService() {
        return transferService;
    }

    public FinanceService financeService() {
        return financeService;
    }

    public HabitsService habitsService() {
        return habitsService;
    }

    public HabitsReportService habitsReportService() {
        return habitsReportService;
    }

    public ScuttleCheckService scuttleCheckService() {
        return scuttleCheckService;
    }

    public FinanceOperationService financeOperationService() {
        return financeOperationService;
    }

    public HabitMarkService habitMarkService() {
        return habitMarkService;
    }

    public WardrobeService wardrobeService() {
        return wardrobeService;
    }

    public AnalysisService analysisService() {
        return analysisService;
    }

    public StabilityCriterionService stabilityCriterionService() {
        return stabilityCriterionService;
    }

    public HuddleCheckService huddleCheckService() {
        return huddleCheckService;
    }

    public FinanceSearchService financeSearchService() {
        return financeSearchService;
    }

    public TrackerService trackerService() {
        return trackerService;
    }

    public BoardSortService boardSortService() {
        return boardSortService;
    }

    public WardrobeStatisticsService wardrobeStatisticsService() {
        return wardrobeStatisticsService;
    }

    public FinanceReportService financeReportService() {
        return financeReportService;
    }

    public ReportPeriodService reportPeriodService() {
        return reportPeriodService;
    }

    public ItemHierarchyService itemHierarchyService() {
        return itemHierarchyService;
    }

    public StructureService structureService() {
        return structureService;
    }
}
