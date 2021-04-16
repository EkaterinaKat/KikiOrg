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

    public void saveOrRewriteWorkLog(WorkArea workArea, int minutes, Date date) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        List<WorkLog> workLogs = workLogRepo.findByDateEntityAndWorkArea(dateEntity, workArea);
        if (workLogs.size() > 0)
            workLogRepo.delete(workLogs.get(0));

        WorkLog workLog = new WorkLog();
        workLog.setDateEntity(dateEntity);
        workLog.setWorkArea(workArea);
        workLog.setMinutes(minutes);

        workLogRepo.save(workLog);
    }

    public WorkLog getWorkLogOrNull(WorkArea workArea, Date date) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        List<WorkLog> workLogs = workLogRepo.findByDateEntityAndWorkArea(dateEntity, workArea);
        if (workLogs.size() == 0)
            return null;
        return workLogs.get(0);
    }
}
