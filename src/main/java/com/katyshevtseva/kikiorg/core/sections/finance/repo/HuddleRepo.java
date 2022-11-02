package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Huddle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HuddleRepo extends JpaRepository<Huddle, Long> {
}
