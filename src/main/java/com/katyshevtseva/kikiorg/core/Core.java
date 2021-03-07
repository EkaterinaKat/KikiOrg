package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.sections.finance.*;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ExpensesReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.IncomeReportService;
import com.katyshevtseva.kikiorg.core.sections.habits.AnalysisService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsReportService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Core implements InitializingBean {
    private static Core INSTANCE;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private HabitsService habitsService;
    @Autowired
    private HabitsReportService habitsReportService;
    @Autowired
    private FinanceCheckService financeCheckService;
    @Autowired
    private FinanceOperationService financeOperationService;
    @Autowired
    private ItemHierarchyService itemHierarchyService;
    @Autowired
    private ItemSchemaService itemSchemaService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private ExpensesReportService expensesReportService;
    @Autowired
    private IncomeReportService incomeReportService;
    @Autowired
    private HabitMarkService habitMarkService;
    @Autowired
    private WardrobeService wardrobeService;
    @Autowired
    private AnalysisService analysisService;

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

    public FinanceCheckService financeCheckService() {
        return financeCheckService;
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
}
