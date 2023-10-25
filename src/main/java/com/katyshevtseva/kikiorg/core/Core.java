package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.sections.diary.DiaryService;
import com.katyshevtseva.kikiorg.core.sections.finance.*;
import com.katyshevtseva.kikiorg.core.sections.finance.report.DynamicsReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService;
import com.katyshevtseva.kikiorg.core.sections.finance.search.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.habits.AnalysisService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsReportService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsService;
import com.katyshevtseva.kikiorg.core.sections.tracker.SphereService;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskService;
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
    private final HuddleCheckService huddleCheckService;
    private final FinanceSearchService financeSearchService;
    private final WardrobeStatisticsService wardrobeStatisticsService;
    private final FinanceReportService financeReportService;
    private final ReportPeriodService reportPeriodService;
    private final ItemHierarchyService itemHierarchyService;
    private final TransferService transferService;
    private final AccountGroupService accountGroupService;
    private final AccountDeleteService accountDeleteService;
    private final ItemMergeService itemMergeService;
    private final PieceSeasonService pieceSeasonService;
    private final SphereService sphereService;
    private final TaskService taskService;
    private final DynamicsReportService dynamicsReportService;
    private final DiaryService diaryService;

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

    public DiaryService diaryService() {
        return diaryService;
    }

    public DynamicsReportService dynamicsReportService() {
        return dynamicsReportService;
    }

    public TaskService taskService() {
        return taskService;
    }

    public SphereService sphereService() {
        return sphereService;
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

    public HuddleCheckService huddleCheckService() {
        return huddleCheckService;
    }

    public FinanceSearchService financeSearchService() {
        return financeSearchService;
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
}
