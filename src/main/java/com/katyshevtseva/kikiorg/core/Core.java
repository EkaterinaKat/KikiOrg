package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.modes.finance.FinanceReportService;
import com.katyshevtseva.kikiorg.core.modes.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.modes.habits.HabitsReportService;
import com.katyshevtseva.kikiorg.core.modes.habits.HabitsService;
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
}
