package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParamRepo extends JpaRepository<Param, Long> {
}
