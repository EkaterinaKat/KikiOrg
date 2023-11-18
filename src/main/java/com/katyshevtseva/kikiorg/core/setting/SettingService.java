package com.katyshevtseva.kikiorg.core.setting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingService {
    public static final String FIN_PLAN_ACC_GR_ID = "fin_plan_acc_gr_id";
    public static final String FIN_PLAN_UPPER_LIMIT = "fin_plan_upper_limit";
    private final SettingEntityRepo repo;

    public Optional<String> getValue(String key) {
        return repo.findByKey(key).map(SettingEntity::getValue);
    }

    public void save(String key, String value) {
        SettingEntity setting = repo.findByKey(key).orElse(new SettingEntity(key));
        setting.setValue(value);
        repo.save(setting);
    }
}
