package com.katyshevtseva.kikiorg.database;

import com.katyshevtseva.kikiorg.core.finance.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepo extends JpaRepository<Source, Long> {
}
