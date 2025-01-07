package com.katyshevtseva.kikiorg.core;

import com.katyshevtseva.kikiorg.core.sections.diary.DairyTableService;
import com.katyshevtseva.kikiorg.core.sections.diary.DiaryChartService;
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
import com.katyshevtseva.kikiorg.core.sections.study.*;
import com.katyshevtseva.kikiorg.core.sections.tracker.SphereService;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.PieceSeasonService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeStatisticsService;
import com.katyshevtseva.kikiorg.core.setting.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Core implements InitializingBean {
    private static Core INSTANCE;
    public final FinanceService financeService;
    public final HabitsService habitsService;
    public final HabitsReportService habitsReportService;
    public final ScuttleCheckService scuttleCheckService;
    public final FinanceOperationService financeOperationService;
    public final HabitMarkService habitMarkService;
    public final WardrobeService wardrobeService;
    public final AnalysisService analysisService;
    public final HuddleCheckService huddleCheckService;
    public final FinanceSearchService financeSearchService;
    public final WardrobeStatisticsService wardrobeStatisticsService;
    public final FinanceReportService financeReportService;
    public final ReportPeriodService reportPeriodService;
    public final ItemHierarchyService itemHierarchyService;
    public final AccountGroupService accountGroupService;
    public final AccountDeleteService accountDeleteService;
    public final ItemMergeService itemMergeService;
    public final PieceSeasonService pieceSeasonService;
    public final SphereService sphereService;
    public final TaskService taskService;
    public final DynamicsReportService dynamicsReportService;
    public final DiaryService diaryService;
    public final DairyTableService dairyTableService;
    public final PlanningService planningService;
    public final SettingService settingService;
    public final OperationDeletionService operationDeletionService;
    public final DiaryChartService diaryChartService;
    public final StudyChartService studyChartService;
    public final StudyService studyService;
    public final StudyTableService studyTableService;
    public final CircsService circsService;
    public final PlanService planService;
    public final PlanMarkService pmService;

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
}
