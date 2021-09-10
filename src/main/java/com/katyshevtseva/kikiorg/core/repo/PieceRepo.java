package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceRepo extends JpaRepository<Piece, Long> {
    Page<Piece> findByTypeAndEndDateIsNotNull(ClothesType type, Pageable pageable);

    Page<Piece> findByTypeAndEndDateIsNull(ClothesType type, Pageable pageable);

    Page<Piece> findByEndDateIsNotNull(Pageable pageable);

    Page<Piece> findByEndDateIsNull(Pageable pageable);

    List<Piece> findByEndDateIsNull();
}
