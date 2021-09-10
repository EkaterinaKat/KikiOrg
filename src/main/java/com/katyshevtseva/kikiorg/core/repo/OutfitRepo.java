package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutfitRepo extends JpaRepository<Outfit, Long> {

    @Query(value = "SELECT DISTINCT o FROM Outfit o " +
            "JOIN o.seasons s JOIN o.purposes p " +
            "WHERE s IN :seasons AND p IN :purposes ")
    Page<Outfit> findByPurposesAndSeasons(@Param("purposes") List<Purpose> purposes,
                                          @Param("seasons") List<Season> seasons,
                                          Pageable pageable);
}
