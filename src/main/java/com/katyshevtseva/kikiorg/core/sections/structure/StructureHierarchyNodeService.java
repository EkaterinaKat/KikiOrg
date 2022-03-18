package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StructureHierarchyNodeService {
    private final TargetService targetService;
    private final TargetGroupService targetGroupService;

    public TargetStatus getNodeStatus(HierarchyNode node) {
        if (node instanceof Target) {
            return ((Target) node).getStatus();
        }
        if (node instanceof TargetGroup) {
            return ((TargetGroup) node).getStatus();
        }
        throw new RuntimeException();
    }

//    public void deleteNode(HierarchyNode node) {
//        if (node instanceof Target) {
//            targetService.delete((Target) node);
//            return;
//        }
//        if(node instanceof TargetGroup) {
//            targetGroupService.delete((TargetGroup) node);
//            return;
//        }
//        throw new RuntimeException();
//    }
}
