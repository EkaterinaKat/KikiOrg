package com.katyshevtseva.kikiorg.core.sections.diary.repo;

import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicatiorRepo extends JpaRepository<Indicator, Long> {
}
