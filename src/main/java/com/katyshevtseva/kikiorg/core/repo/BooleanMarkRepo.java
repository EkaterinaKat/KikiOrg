package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.habits.entity.BooleanMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooleanMarkRepo extends JpaRepository<BooleanMark, Long> {
}
