package com.katyshevtseva.kikiorg.core.sections.work;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.WorkLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WorkService {
    @Autowired
    private WorkLogRepo workLogRepo;
    @Autowired
    private DateService dateService;

    public void saveOrSumWithExistingWorkLog(WorkArea workArea, int minutes, Date date) {
        saveWorkLog(workArea, minutes, date, true);
    }

    public void saveOrRewriteExistingWorkLog(WorkArea workArea, int minutes, Date date) {
        saveWorkLog(workArea, minutes, date, false);
    }

    private void saveWorkLog(WorkArea workArea, int minutes, Date date, boolean sumWithExistingWorkLog) {
        if (minutes < 0)
            throw new RuntimeException();

        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        List<WorkLog> workLogs = workLogRepo.findByDateEntityAndWorkArea(dateEntity, workArea);
        WorkLog workLog;

        if (workLogs.size() == 0) {
            workLog = new WorkLog();
            workLog.setDateEntity(dateEntity);
            workLog.setWorkArea(workArea);
            workLog.setMinutes(minutes);
        } else {
            workLog = workLogs.get(0);
            workLog.setMinutes(sumWithExistingWorkLog ? workLog.getMinutes() + minutes : minutes);
        }

        if (workLog.getMinutes() > 0) {
            workLogRepo.save(workLog);
        } else {
            workLogRepo.delete(workLog);
        }
    }

    WorkLog getWorkLogOrNull(WorkArea workArea, Date date) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        List<WorkLog> workLogs = workLogRepo.findByDateEntityAndWorkArea(dateEntity, workArea);
        if (workLogs.size() == 0)
            return null;
        return workLogs.get(0);
    }
}
