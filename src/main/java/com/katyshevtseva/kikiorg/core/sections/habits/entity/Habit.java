package com.katyshevtseva.kikiorg.core.sections.habits.entity;

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

    private String description;

    @Enumerated(EnumType.STRING)
    private HabitType type;

    private boolean active;

    @OneToMany(mappedBy = "habit", fetch = FetchType.EAGER)
    public List<EnumElement> enumElements;

    public String getExtendedTitle() {
        return title + " " + String.format("%s (%s)", type == HabitType.enumeration ? getEnumString(enumElements) : "",
                active ? "active" : "inactive");
    }

    public static String getEnumString(List<EnumElement> enumElements) {
        if (enumElements == null || enumElements.size() == 0)
            return "";
        String result = "[";
        for (int i = 0; i < enumElements.size() - 1; i++) {
            result += (enumElements.get(i).getTitle() + ", ");
        }
        result += enumElements.get(enumElements.size() - 1).getTitle() + "]";
        return result;
    }
}
