package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.ItemGroupRepo;
import com.katyshevtseva.kikiorg.core.repo.ItemHierarchyLeafRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemHierarchyLeaf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemHierarchyService {
    private final ItemGroupRepo itemGroupRepo;
    private final FinanceService financeService;
    private final ItemHierarchyLeafRepo itemHierarchyLeafRepo;

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

    private List<ItemHierarchyLeaf> getTopLevelLeaves() {
        List<Item> items = financeService.getAllItems();
        List<ItemHierarchyLeaf> leaves = new ArrayList<>();
        for (Item item : items) {
            Optional<ItemHierarchyLeaf> optionalLeaf = itemHierarchyLeafRepo.findByItem(item);
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

    public List<ItemHierarchyNode> getNodesByParent(ItemHierarchyNode parentNode) {
        List<ItemHierarchyNode> nodes = new ArrayList<>();
        if (parentNode.isLeaf())
            return nodes;
        nodes.addAll(itemHierarchyLeafRepo.findByParentGroup((ItemGroup) parentNode));
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
            itemHierarchyLeafRepo.save((ItemHierarchyLeaf) node);
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
