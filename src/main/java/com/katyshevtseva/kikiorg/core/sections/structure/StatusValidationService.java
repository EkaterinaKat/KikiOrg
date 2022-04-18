package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetGroupRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class StatusValidationService {
    private final TargetGroupRepo targetGroupRepo;
    private final TargetRepo targetRepo;

    public void validateStart(CourseOfAction courseOfAction, HierarchyNode node) throws Exception {
        validateStatusChange(courseOfAction, node, TargetStatus.STARTED);
    }

    private void validateStatusChange(CourseOfAction courseOfAction, HierarchyNode node, TargetStatus status) throws Exception {
        StructureHierarchyService hierarchyService = new StructureHierarchyService(courseOfAction, targetGroupRepo, targetRepo);

        if (!hierarchyService.nodeBelongToCourse(node)) {
            throw new RuntimeException();
        }

        if (!isFinished(status)) {
            TargetGroup parent = (TargetGroup) node.getParentGroup();
            while (parent.getParentGroup() != null) {
                if (isFinished(parent.getStatus())) {
                    throw new Exception(String.format("Для %s невозможно установить статус %s, так как %s имеет статус %s",
                            node.getTitle(), status, parent.getTitle(), parent.getStatus()));
                }
                parent = parent.getParent();
            }
        }
    }

    private boolean isFinished(TargetStatus targetStatus) {
        return Arrays.asList(TargetStatus.DONE, TargetStatus.REJECTED).contains(targetStatus);
    }
}
