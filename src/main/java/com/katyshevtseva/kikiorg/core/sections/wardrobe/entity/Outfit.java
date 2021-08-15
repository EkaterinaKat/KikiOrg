package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "outfits_seasons", joinColumns = @JoinColumn(name = "outfit_id"))
    @Enumerated(EnumType.STRING)
    Set<Season> seasons;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "outfits_purposes", joinColumns = @JoinColumn(name = "outfit_id"))
    @Enumerated(EnumType.STRING)
    Set<Purpose> purposes;

    @OneToOne
    @JoinColumn(name = "collage_id")
    private CollageEntity collageEntity;

    public String getFullDesc() {
        StringBuilder fullDesc = new StringBuilder("Seasons: ");
        for (Season season : seasons)
            fullDesc.append(season).append(", ");
        fullDesc.delete(fullDesc.length() - 2, fullDesc.length());
        fullDesc.append(".");
        fullDesc.append("\n\nPurposes: ");
        for (Purpose purpose : purposes)
            fullDesc.append(purpose).append(", ");
        fullDesc.delete(fullDesc.length() - 2, fullDesc.length());
        return fullDesc.toString();
    }
}
