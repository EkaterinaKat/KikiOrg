package com.katyshevtseva.kikiorg.core.sections.wardrobe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class CollageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "collageEntity", fetch = FetchType.EAGER)
    private Set<ComponentEntity> components;

}
