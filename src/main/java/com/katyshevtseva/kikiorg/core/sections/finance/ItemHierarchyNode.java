package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;

public interface ItemHierarchyNode {
    long getId();

    boolean isLeaf();

    String getTitle();

    ItemHierarchyNode getParentGroup();

    void setParentGroup(ItemGroup group);
}
