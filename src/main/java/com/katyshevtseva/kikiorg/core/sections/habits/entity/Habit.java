package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Deprecated
    private String description;

    @Enumerated(EnumType.STRING)
    private HabitType type;

    @Enumerated(EnumType.STRING)
    private HabitGroup habitGroup;

    private boolean active;

    @OneToMany(mappedBy = "habit", fetch = FetchType.EAGER)
    private List<EnumElement> enumElements;

    @OneToOne(mappedBy = "habit")
    private StabilityCriterion stabilityCriterion;

    @OneToOne
    @JoinColumn(name = "current_desc_id")
    private Description currentDescription;

    @Enumerated(EnumType.STRING)
    private StabilityStatus stabilityStatus;

    public String getTitleWithActiveInfoAndEnumElements() {
        return title + " " + String.format("%s (%s)", type == HabitType.enumeration ? getEnumString(enumElements) : "",
                active ? "active" : "inactive");
    }

    public static String getEnumString(List<EnumElement> enumElements) {
        if (enumElements == null || enumElements.size() == 0)
            return "";
        String result = "[";
        for (int i = 0; i < enumElements.size() - 1; i++) {
            result += (enumElements.get(i).toString() + ", ");
        }
        result += enumElements.get(enumElements.size() - 1).toString() + "]";
        return result;
    }
}
