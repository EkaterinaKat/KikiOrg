package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.history.HasHistory;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.Sphere;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(schema = "structure")
public class CourseOfAction implements HasHistory<CourseChangeAction> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "root_target_group_id")
    private TargetGroup rootTargetGroup;

    @Enumerated(EnumType.STRING)
    private CourseOfActionStatus status;

    @Enumerated(EnumType.STRING)
    private Sphere sphere;

    @OneToMany(mappedBy = "courseOfAction", fetch = FetchType.EAGER)
    private List<CourseChangeAction> history;
}
