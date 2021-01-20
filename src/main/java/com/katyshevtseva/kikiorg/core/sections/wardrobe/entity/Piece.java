package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.HavingImage;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Piece implements HavingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String imageName;

    @Enumerated(EnumType.STRING)
    private ClothesType type;

    @ElementCollection
    @JoinTable(name = "pieces_seasons", joinColumns = @JoinColumn(name = "piece_id"))
    @Enumerated(EnumType.STRING)
    List<Season> seasons;

    @ElementCollection
    @JoinTable(name = "pieces_purposes", joinColumns = @JoinColumn(name = "piece_id"))
    @Enumerated(EnumType.STRING)
    List<Purpose> purposes;
}
