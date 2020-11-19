package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.sections.finance.*;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsReportService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Core implements InitializingBean {
    private static Core INSTANCE;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private FinanceReportService financeReportService;
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

    public FinanceReportService financeReportService() {
        return financeReportService;
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
}
