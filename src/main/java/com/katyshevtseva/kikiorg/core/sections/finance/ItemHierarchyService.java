package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.ItemGroupRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemHierarchyLeaf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemHierarchyService {
    @Autowired
    private OwnerAdapterService adapter;
    @Autowired
    private ItemGroupRepo itemGroupRepo;

    public void addGroup(String name) {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setTitle(name);
        adapter.saveItemGroup(itemGroup);
    }

    public List<ItemGroup> getItemGroupsForCurrentUser() {
        return adapter.getItemGroupsForCurrentUser();
    }

    public void destroyTreeAndDeleteGroup(ItemGroup itemGroup) {
        destroyTreeWithRootNode(itemGroup);
        itemGroupRepo.delete(itemGroup);
    }

    public List<ItemHierarchyNode> getTopLevelNodesForCurrentUser() {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        nodes.addAll(getTopLevelLeavesForCurrentUser());
        nodes.addAll(adapter.getTopLevelGroupsForCurrentUser());
        return nodes;
    }

    private List<ItemHierarchyLeaf> getTopLevelLeavesForCurrentUser() {
        List<Item> items = adapter.getItemsForCurrentOwner();
        List<ItemHierarchyLeaf> leaves = new ArrayList<>();
        for (Item item : items) {
            Optional<ItemHierarchyLeaf> optionalLeaf = adapter.getLeafByItemForCurrentUser(item);
            if (optionalLeaf.isPresent() && optionalLeaf.get().getParentGroup() == null)
                leaves.add(optionalLeaf.get());
            else if (!optionalLeaf.isPresent()) {
                ItemHierarchyLeaf leaf = new ItemHierarchyLeaf();
                leaf.setItem(item);
                leaves.add(leaf);
            }
        }
        return leaves;
    }

    public List<ItemHierarchyNode> getNodesByParentForCurrentUser(ItemHierarchyNode parentNode) {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        if (parentNode.isLeaf())
            return nodes;
        nodes.addAll(adapter.getLeavesByParentForCurrentUser((ItemGroup) parentNode));
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
            adapter.saveItemHierarchyLeaf((ItemHierarchyLeaf) node);
        else
            adapter.saveItemGroup((ItemGroup) node);
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
