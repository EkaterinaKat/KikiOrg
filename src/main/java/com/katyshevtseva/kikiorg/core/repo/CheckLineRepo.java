package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.finance.entity.CheckLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckLineRepo extends JpaRepository<CheckLine, Long> {
}
