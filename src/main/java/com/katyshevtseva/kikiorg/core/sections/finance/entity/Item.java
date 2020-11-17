package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.Owner;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Item implements ItemHierarchyNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @Override
    public String toString() {
        return title;
    }

    @Enumerated(EnumType.STRING)
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "parent_group_id")
    private ItemGroup parentGroup;

    @Override
    public boolean isLeaf() {
        return true;
    }
}
