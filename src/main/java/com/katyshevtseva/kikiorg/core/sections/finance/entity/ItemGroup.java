package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyNode;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ItemGroup implements ItemHierarchyNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "parent_group_id")
    private ItemGroup parentGroup;

    @Override
    public boolean isLeaf() {
        return false;
    }
}
