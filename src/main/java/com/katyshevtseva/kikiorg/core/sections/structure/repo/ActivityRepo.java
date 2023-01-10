package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Long> {

    @Query("SELECT a FROM Activity a " +
            "WHERE :value MEMBER OF a.paramValues ")
    List<Activity> findByParamValue(@Param("value") ParamValue paramValue);

    @Query("SELECT DISTINCT a FROM Activity a JOIN a.paramValues v " +
            "WHERE v IN :values ")
    List<Activity> findByParamValues(@Param("values") List<ParamValue> paramValues);
}
