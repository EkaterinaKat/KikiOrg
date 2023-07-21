package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Category;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.OutfitSeason;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @Enumerated(EnumType.STRING)
    OutfitSeason season;

    @Enumerated(EnumType.STRING)
    Category category;

    @OneToOne
    @JoinColumn(name = "collage_id")
    private CollageEntity collageEntity;

    public String getFullDesc() {
        StringBuilder fullDesc = new StringBuilder("(").append(id).append(") ").append(comment != null ? comment : "");

        if (season != null) {
            fullDesc.append(season).append("\n");
            fullDesc.append("\n\n");
        }

        return fullDesc.toString();
    }
}
