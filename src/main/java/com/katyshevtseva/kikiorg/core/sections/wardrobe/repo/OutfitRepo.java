package com.katyshevtseva.kikiorg.core.sections.wardrobe.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutfitRepo extends JpaRepository<Outfit, Long>, JpaSpecificationExecutor<Outfit> {

    @Query(value = "SELECT  o FROM Outfit o " +
            "JOIN o.collageEntity c JOIN c.components comp " +
            "WHERE comp.piece = :piece ")
    Page<Outfit> findOutfitsByPiece(@Param("piece") Piece piece, Pageable pageable);

    @Query(value = "SELECT  o FROM Outfit o " +
            "JOIN o.collageEntity c JOIN c.components comp " +
            "WHERE comp.piece = :piece ")
    List<Outfit> findOutfitsByPiece(@Param("piece") Piece piece);
}
