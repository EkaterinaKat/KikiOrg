package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class AccountGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "group_accounts",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts;

    public AccountGroup() {
    }

    public AccountGroup(String title, List<Account> accounts) {
        this.title = title;
        this.accounts = accounts;
    }

    public String getFullInfo() {
        return title + " " + accounts.toString();
    }
}
