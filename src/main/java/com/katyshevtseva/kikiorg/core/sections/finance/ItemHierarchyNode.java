package com.katyshevtseva.kikiorg.core.sections.finance;

public interface ItemHierarchyNode {
    long getId();

    boolean isLeaf();

    String getTitle();

    ItemHierarchyNode getParentGroup();
}
