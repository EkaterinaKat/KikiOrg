package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitRepo extends JpaRepository<Outfit, Long> {
}
