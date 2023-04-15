package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.kikiorg.core.sections.structure.GoalTopicality;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private GoalTopicality topicality;

    public void setValues(String title, String description, GoalTopicality topicality) {
        this.title = title;
        this.description = description;
        this.topicality = topicality;
    }

    @Override
    public String toString() {
        return title;
    }
}
