package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.Imagable;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Piece implements Imagable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String imageName;

    @Enumerated(EnumType.STRING)
    private ClothesType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "pieces_seasons", joinColumns = @JoinColumn(name = "piece_id"))
    @Enumerated(EnumType.STRING)
    Set<Season> seasons;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "pieces_purposes", joinColumns = @JoinColumn(name = "piece_id"))
    @Enumerated(EnumType.STRING)
    Set<Purpose> purposes;

    public String getFullDesc() {
        String fullDesc = description + "\n\n" + "Type: " + type + "\n\n" + "Seasons: ";
        for (Season season : seasons)
            fullDesc += season + " ";
        fullDesc += "\n\nPurposes: ";
        for (Purpose purpose : purposes)
            fullDesc += purpose + " ";
        return fullDesc;
    }
}
