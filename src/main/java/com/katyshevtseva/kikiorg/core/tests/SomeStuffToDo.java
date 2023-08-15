package com.katyshevtseva.kikiorg.core.tests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class SomeStuffToDo {

//    @PostConstruct
    public void execute() {
        System.out.println("******************************************************************");
    }
}
