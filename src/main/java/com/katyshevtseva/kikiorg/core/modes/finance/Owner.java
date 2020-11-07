package com.katyshevtseva.kikiorg.core.modes.finance;

public enum Owner {
    K("Екатерина"), M("Камиль"), COMMON("Общее");

    private String name;

    Owner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
