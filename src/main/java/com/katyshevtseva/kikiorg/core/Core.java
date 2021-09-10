package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.sections.finance.*;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ExpensesReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.IncomeReportService;
import com.katyshevtseva.kikiorg.core.sections.habits.*;
import com.katyshevtseva.kikiorg.core.sections.tracker.BoardSortService;
import com.katyshevtseva.kikiorg.core.sections.tracker.TrackerService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeStatisticsService;
import com.katyshevtseva.kikiorg.core.sections.work.WorkReportService;
import com.katyshevtseva.kikiorg.core.sections.work.WorkService;
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
    private final ItemHierarchyService itemHierarchyService;
    private final ItemSchemaService itemSchemaService;
    private final ExpensesReportService expensesReportService;
    private final IncomeReportService incomeReportService;
    private final HabitMarkService habitMarkService;
    private final WardrobeService wardrobeService;
    private final AnalysisService analysisService;
    private final StabilityCriterionService stabilityCriterionService;
    private final WorkService workService;
    private final WorkReportService workReportService;
    private final HuddleCheckService huddleCheckService;
    private final FinanceSearchService financeSearchService;
    private final TrackerService trackerService;
    private final BoardSortService boardSortService;
    private final WardrobeStatisticsService wardrobeStatisticsService;

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
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
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

    public ItemHierarchyService itemHierarchyService() {
        return itemHierarchyService;
    }

    public ItemSchemaService itemSchemaService() {
        return itemSchemaService;
    }

    public ExpensesReportService expensesReportService() {
        return expensesReportService;
    }

    public IncomeReportService incomeReportService() {
        return incomeReportService;
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

    public WorkService workService() {
        return workService;
    }

    public WorkReportService workReportService() {
        return workReportService;
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
}
