package com.katyshevtseva.kikiorg.core.polarperiod;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolarPeriodService {
    private final PolarPeriodRepo repo;

    public PolarPeriod save(PolarPeriod period) {
        return repo.save(period);
    }

    public void delete(PolarPeriod period) {
        if (period != null) {
            repo.delete(period);
        }
    }
}
