package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.HuddleRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Huddle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HuddleCheckService {
    private final HuddleRepo huddleRepo;

    public List<Huddle> getAllHuddles() {
        return huddleRepo.findAll();
    }

    public Huddle createHuddle(String title) {
        Huddle huddle = new Huddle();
        huddle.setTitle(title);
        huddle.setAmount(0L);
        return huddleRepo.save(huddle);
    }

    public String checkAndRewriteHuddleInfo(Huddle huddle, long newAmount, List<Account> accounts) {
        huddle.setAmount(newAmount);
        huddle.setAccounts(accounts);
        huddleRepo.save(huddle);

        long accountsSum = 0;
        for (Account account : accounts) {
            accountsSum += account.getAmount();
        }

        return String.format("Accounts sum = %d \n%d - %d = %d", accountsSum, accountsSum, newAmount, accountsSum - newAmount);
    }
}
