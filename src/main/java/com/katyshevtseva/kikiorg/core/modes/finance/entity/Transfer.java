package com.katyshevtseva.kikiorg.core.modes.finance.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account to;

    private Long amount;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @Enumerated(EnumType.STRING)
    private Owner owner;
}
