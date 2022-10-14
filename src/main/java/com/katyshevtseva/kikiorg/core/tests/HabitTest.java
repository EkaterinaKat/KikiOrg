package com.katyshevtseva.kikiorg.core.tests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitTest implements TestClass {

    public boolean test() {
        boolean success = true;
        return success;
    }
}
