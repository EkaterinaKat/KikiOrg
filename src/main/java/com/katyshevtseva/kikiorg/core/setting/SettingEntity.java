package com.katyshevtseva.kikiorg.core.setting;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class SettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String key;

    private String value;

    public SettingEntity(String key) {
        this.key = key;
    }

    public SettingEntity() {
    }
}
