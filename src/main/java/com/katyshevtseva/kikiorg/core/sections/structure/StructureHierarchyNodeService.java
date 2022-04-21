package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.polarperiod.PeriodUtils;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
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

    public String getNodeFullDesc(HierarchyNode node) {
        if (node instanceof Target) {
            Target target = (Target) node;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Target: ").append(target.getTitle()).append("\n\n");
            stringBuilder.append("Status: ").append(target.getStatus()).append("\n\n");
            if (target.getStatus() == TargetStatus.STARTED) {
                stringBuilder.append(PeriodUtils.getFullPeriodInfo(target.getPeriod())).append("\n\n");
            }
            stringBuilder.append(target.getDescription());
            return stringBuilder.toString();
        }
        if (node instanceof TargetGroup) {
            TargetGroup group = (TargetGroup) node;
            return "Group: " + group.getTitle() + "\n\n" +
                    "Status: " + group.getStatus() + "\n\n" +
                    group.getDescription();
        }
        throw new RuntimeException();
    }

    public static TargetStatus getNodeStatus(HierarchyNode node) {
        if (node instanceof Target) {
            return ((Target) node).getStatus();
        }
        if (node instanceof TargetGroup) {
            return ((TargetGroup) node).getStatus();
        }
        throw new RuntimeException();
    }

    public void edit(HierarchyNode node, String title, String desc) {
        if (node instanceof Target) {
            targetService.edit(((Target) node), title, desc);
            return;
        }
        if (node instanceof TargetGroup) {
            targetGroupService.edit(((TargetGroup) node), title, desc);
            return;
        }
        throw new RuntimeException();
    }

    public void reject(HierarchyNode node) {
        if (node instanceof Target) {
            targetService.reject(((Target) node));
            return;
        }
        if (node instanceof TargetGroup) {
            targetGroupService.reject(((TargetGroup) node));
            return;
        }
        throw new RuntimeException();
    }

    public void delete(HierarchyNode node, CourseOfAction course) {
        if (node instanceof Target) {
            targetService.delete(((Target) node));
            return;
        }
        if (node instanceof TargetGroup) {
            targetGroupService.moveChildrenToRootAndDeleteGroup(((TargetGroup) node), course);
            return;
        }
        throw new RuntimeException();
    }

    public void done(HierarchyNode node) {
        if (node instanceof Target) {
            targetService.done(((Target) node));
            return;
        }
        if (node instanceof TargetGroup) {
            targetGroupService.done(((TargetGroup) node));
            return;
        }
        throw new RuntimeException();
    }
}
