package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ItemHierarchyLeaf implements ItemHierarchyNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "parent_group_id")
    private ItemGroup parentGroup;

    @Enumerated(EnumType.STRING)
    private OwnerService.Owner owner;

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String getTitle() {
        return item.getTitle();
    }

    @Override
    public String toString() {
        return item.getTitle();
    }
}
