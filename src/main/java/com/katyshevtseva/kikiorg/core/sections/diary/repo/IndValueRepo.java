package com.katyshevtseva.kikiorg.core.sections.diary.repo;

import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndValueRepo extends JpaRepository<IndValue, Long> {

    List<IndValue> findAllByIndicator(Indicator indicator);

    void deleteByIndicator(Indicator indicator);
}
