package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnumMarkRepo extends JpaRepository<EnumMark, Long> {
}
