package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParamValueRepo extends JpaRepository<ParamValue, Long> {
}
