package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
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
    Season season;

    @Enumerated(EnumType.STRING)
    Purpose purpose;

    @OneToOne
    @JoinColumn(name = "collage_id")
    private CollageEntity collageEntity;

    public String getFullDesc() {
        StringBuilder fullDesc = new StringBuilder("(").append(id).append(") ").append(comment != null ? comment : "");

        if (season != null || purpose != null) {
            fullDesc.append("\n\n");
        }

        if (season != null) {
            fullDesc.append(season).append("\n");
        }
        if (purpose != null) {
            fullDesc.append(purpose);
        }

        return fullDesc.toString();
    }
}
