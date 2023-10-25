package com.katyshevtseva.kikiorg.core.sections.diary.repo;

import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndValueRepo extends JpaRepository<IndValue, Long> {
}
