package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.ItemGroupRepo;
import com.katyshevtseva.kikiorg.core.repo.ItemRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemHierarchyService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ItemGroupRepo itemGroupRepo;
    @Autowired
    private OwnerAdapterService adapter;

    public void addGroup(String name) {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setTitle(name);
        adapter.saveItemGroup(itemGroup);
    }

    public List<ItemHierarchyNode> getTopLevelNodesForCurrentUser() {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        nodes.addAll(adapter.getTopLevelItemsForCurrentUser());
        nodes.addAll(adapter.getTopLevelGroupsForCurrentUser());
        return nodes;
    }

    List<ItemHierarchyNode> getNodesByParentForCurrentUser(ItemHierarchyNode parentNode) {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        if (parentNode instanceof Item)
            return nodes;
        nodes.addAll(adapter.getItemsByParentForCurrentUser((ItemGroup) parentNode));
        nodes.addAll(adapter.getGroupsByParentForCurrentUser((ItemGroup) parentNode));
        return nodes;
    }

    void destroyTreeWithRootNode(ItemHierarchyNode node) {
        node.setParentGroup(null);
        saveModifiedNode(node);

        if (node.isLeaf())
            return;

        for (ItemHierarchyNode childNode : getNodesByParentForCurrentUser(node))
            destroyTreeWithRootNode(childNode);
    }

    void saveModifiedNode(ItemHierarchyNode node) {
        if (node.isLeaf())
            itemRepo.save((Item) node);
        else
            itemGroupRepo.save((ItemGroup) node);
    }

    boolean treeWithRootContainsNode(ItemHierarchyNode root, ItemHierarchyNode nodeToSearch) {
        if (root.equals(nodeToSearch))
            return true;

        if (root.isLeaf())
            return false;

        for (ItemHierarchyNode childNode : getNodesByParentForCurrentUser(root))
            if (treeWithRootContainsNode(childNode, nodeToSearch))
                return true;

        return false;
    }

    public interface ItemHierarchyNode {
        long getId();

        boolean isLeaf();

        String getTitle();

        ItemHierarchyNode getParentGroup();

        void setParentGroup(ItemGroup group);
    }
}
