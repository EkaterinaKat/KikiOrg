package com.katyshevtseva.kikiorg.core.sections.finance;

public enum Owner {
    K("Екатерина"), M("Камиль"), C("Семья");

    private String name;

    Owner(String name) {
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
