package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceRepo extends JpaRepository<Piece, Long> {
    Page<Piece> findByTypeAndEndDateIsNotNull(ClothesType type, Pageable pageable);

    Page<Piece> findByTypeAndEndDateIsNull(ClothesType type, Pageable pageable);

    Page<Piece> findByEndDateIsNotNull(Pageable pageable);

    Page<Piece> findByEndDateIsNull(Pageable pageable);

    List<Piece> findByEndDateIsNull();

    Long countByType(ClothesType type);

    @Query("SELECT p FROM Piece p " +
            "WHERE p.id NOT IN (SELECT cp.id FROM ComponentEntity c JOIN c.pieces cp) " +
            "AND p.endDate IS NULL " +
            "AND p.type IN :types ")
    Page<Piece> findUnusedPieces(@Param("types") List<ClothesType> types, Pageable pageable);
}
