package com.katyshevtseva.kikiorg.core.setting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingEntityRepo extends JpaRepository<SettingEntity, Long> {

    Optional<SettingEntity> findByKey(String key);
}
