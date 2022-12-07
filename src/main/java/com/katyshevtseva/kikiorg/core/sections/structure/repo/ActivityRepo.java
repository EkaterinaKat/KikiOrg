package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Long> {

    @Query("SELECT a FROM Activity a  " +
            "WHERE  :value MEMBER OF a.paramValues ")
    List<Activity> findByParamValues(@Param("value") ParamValue paramValue);
}
