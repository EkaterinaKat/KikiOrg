package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.sections.finance.*;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ExpensesReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.IncomeReportService;
import com.katyshevtseva.kikiorg.core.sections.habits.*;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.work.WorkReportService;
import com.katyshevtseva.kikiorg.core.sections.work.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Core implements InitializingBean {
    private static Core INSTANCE;
    private FinanceService financeService;
    private HabitsService habitsService;
    private HabitsReportService habitsReportService;
    private ScuttleCheckService scuttleCheckService;
    private FinanceOperationService financeOperationService;
    private ItemHierarchyService itemHierarchyService;
    private ItemSchemaService itemSchemaService;
    private OwnerService ownerService;
    private ExpensesReportService expensesReportService;
    private IncomeReportService incomeReportService;
    private HabitMarkService habitMarkService;
    private WardrobeService wardrobeService;
    private AnalysisService analysisService;
    private StabilityCriterionService stabilityCriterionService;
    private WorkService workService;
    private WorkReportService workReportService;
    private HuddleCheckService huddleCheckService;

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

    public ScuttleCheckService financeCheckService() {
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

    public OwnerService ownerService() {
        return ownerService;
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
}
