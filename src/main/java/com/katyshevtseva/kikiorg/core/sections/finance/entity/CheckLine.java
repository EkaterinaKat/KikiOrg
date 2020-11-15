package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CheckLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long amount;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
}
