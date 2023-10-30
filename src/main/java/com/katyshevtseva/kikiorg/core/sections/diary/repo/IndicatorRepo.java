package com.katyshevtseva.kikiorg.core.sections.diary.repo;

import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndicatorRepo extends JpaRepository<Indicator, Long> {

    List<Indicator> findAllByOrderByIndOrder();
}
