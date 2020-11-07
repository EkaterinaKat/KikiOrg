package com.katyshevtseva.kikiorg.core.modes.finance.entity;

import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class AccountPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long amount;

    private String title;

    @Enumerated(EnumType.STRING)
    private Owner owner;
}
