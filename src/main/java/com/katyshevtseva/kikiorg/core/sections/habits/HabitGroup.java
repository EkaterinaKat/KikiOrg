package com.katyshevtseva.kikiorg.core.sections.habits;

public enum HabitGroup {
    O("Организация"), E("Образование"), H("Здоровье"),
    IH("Информационная гигиена");

    private String name;

    HabitGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
