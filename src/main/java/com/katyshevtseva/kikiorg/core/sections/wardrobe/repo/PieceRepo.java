package com.katyshevtseva.kikiorg.core.sections.wardrobe.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceRepo extends JpaRepository<Piece, Long>, JpaSpecificationExecutor<Piece> {
    Page<Piece> findByEndDateIsNotNull(Pageable pageable);

    List<Piece> findByEndDateIsNull();

    Long countByType(PieceSubtype type);

    @Query("SELECT p FROM Piece p " +
            "WHERE p.id NOT IN (SELECT c.piece.id FROM ComponentEntity c) " +
            "AND p.endDate IS NULL " +
            "AND p.type IN :types ")
    Page<Piece> findUnusedPieces(@Param("types") List<PieceSubtype> types, Pageable pageable);
}
