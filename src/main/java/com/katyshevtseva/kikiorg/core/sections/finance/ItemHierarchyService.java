package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.ItemGroupRepo;
import com.katyshevtseva.kikiorg.core.repo.ItemRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemHierarchyService {
    private final ItemGroupRepo itemGroupRepo;
    private final FinanceService financeService;
    private final ItemRepo itemRepo;

    public void addGroup(String name) {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setTitle(name);
        itemGroupRepo.save(itemGroup);
    }

    public List<ItemGroup> getAllItemGroups() {
        return itemGroupRepo.findAll();
    }

    public void destroyTreeAndDeleteGroup(ItemGroup itemGroup) {
        destroyTreeWithRootNode(itemGroup);
        itemGroupRepo.delete(itemGroup);
    }

    public List<ItemHierarchyNode> getTopLevelNodes() {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        nodes.addAll(getTopLevelLeaves());
        nodes.addAll(itemGroupRepo.findByParentGroupIsNull());
        return nodes;
    }

    private List<Item> getTopLevelLeaves() {
        return financeService.getAllItems().stream().filter(item -> item.getParentGroup() == null).collect(Collectors.toList());
    }

    public List<Item> getAllDescendantItemsByHierarchyNode(ItemHierarchyNode root) {
        if (root.isLeaf())
            return Collections.singletonList((Item) root);

        List<Item> items = new ArrayList<>();
        for (ItemHierarchyNode node : getNodesByParent(root)) {
            items.addAll(getAllDescendantItemsByHierarchyNode(node));
        }
        return items;
    }

    public List<ItemHierarchyNode> getNodesByParent(ItemHierarchyNode parentNode) {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        if (parentNode.isLeaf())
            return nodes;
        nodes.addAll(itemRepo.findByParentGroup((ItemGroup) parentNode));
        nodes.addAll(itemGroupRepo.findByParentGroup((ItemGroup) parentNode));
        return nodes;
    }

    void destroyTreeWithRootNode(ItemHierarchyNode node) {
        node.setParentGroup(null);
        saveModifiedNode(node);

        if (node.isLeaf())
            return;

        for (ItemHierarchyNode childNode : getNodesByParent(node))
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

        for (ItemHierarchyNode childNode : getNodesByParent(root))
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
