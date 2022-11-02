package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepo extends JpaRepository<Source, Long> {

}
