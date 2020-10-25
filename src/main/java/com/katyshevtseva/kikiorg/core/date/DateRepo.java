package com.katyshevtseva.kikiorg.core.date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DateRepo extends JpaRepository<DateEntity, Long> {
    Optional<DateEntity> findByValue(Date value);
}
