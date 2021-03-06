package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceRepo extends JpaRepository<Source, Long> {
    List<Source> findAllByOwner(Owner owner);
}
