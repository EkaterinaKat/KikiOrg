package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "outfits_arrays",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "arrays_id")
    )
    private List<PieceArray> arrays;


    @ElementCollection
    @JoinTable(name = "outfits_seasons", joinColumns = @JoinColumn(name = "outfit_id"))
    @Enumerated(EnumType.STRING)
    List<Season> seasons;

    @ElementCollection
    @JoinTable(name = "outfits_purposes", joinColumns = @JoinColumn(name = "outfit_id"))
    @Enumerated(EnumType.STRING)
    List<Purpose> purposes;
}
