package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Huddle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long amount;

    private String title;

    @ManyToMany
    @JoinTable(name = "huddle_accounts",
            joinColumns = @JoinColumn(name = "huddle_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts;
}
