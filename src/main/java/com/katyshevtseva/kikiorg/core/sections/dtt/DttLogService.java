package com.katyshevtseva.kikiorg.core.sections.dtt;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DttLog;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.DttLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class DttLogService {
    private final DttLogRepo repo;
    private final DateService dateService;

    public void saveDtDeletionLog(DatelessTask task) {
        DttLog log = new DttLog();
        log.setDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        log.setContent(" * Was deleted * \n" + task.getLogString());
        repo.save(log);
    }

    public Page<DttLog> getLogs(int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 20, Sort.by("id").descending());

        org.springframework.data.domain.Page<DttLog> actionPage =
                repo.findAll(pageRequest);
        return new Page<>(actionPage.getContent(), pageNum, actionPage.getTotalPages());
    }
}
