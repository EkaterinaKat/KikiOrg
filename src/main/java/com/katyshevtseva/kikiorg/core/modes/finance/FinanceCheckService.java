package com.katyshevtseva.kikiorg.core.modes.finance;

import com.katyshevtseva.kikiorg.core.modes.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.CheckLine;
import com.katyshevtseva.kikiorg.core.repo.AccountRepo;
import com.katyshevtseva.kikiorg.core.repo.CheckLineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceCheckService {
    @Autowired
    private CheckLineRepo checkLineRepo;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private AccountRepo accountRepo;

    public List<CheckLine> getCheckLines(Account account) {
        return checkLineRepo.findAllByAccount(account);
    }

    public void rewriteCheckLines(List<CheckLine> checkLines, Account account) {
        checkLineRepo.findAllByAccount(account).forEach(cL -> checkLineRepo.delete(cL));
        checkLineRepo.saveAll(checkLines);
    }

    public String check(Account account, int amount) {
        long accountAmount = accountRepo.findById(account.getId()).get().getAmount();
        long diff = amount - accountAmount;
        String result = String.format("По расчетам: %s. По Факту: %s. ", accountAmount, amount, diff);
        if (diff == 0)
            result += "Разница: 0   (*°▽°*) ";
        if (diff > 0)
            result += String.format("По факту больше чем по расчетам на: %d :О", diff);
        if (diff < 0)
            result += String.format("По расчету больше чем по факту на: %d T.T", Math.abs(diff));
        return result;
    }

//    (* ^ ω ^)   (´･ᴗ･ ) (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ (o^▽^o) (⌒▽⌒)☆ ヽ(・∀・)ﾉ
//    (´｡• ω •｡) (o･ω･o) (＠＾◡＾) (^人^) (o´▽o) ( ´ ω )  (๑˘︶˘๑)
//    (((o(*°▽°*)o))) (´• ω •) (＾▽＾) ╰(▔∀▔)╯ (─‿‿─) (✯◡✯) (◕‿◕)
//    (⌒‿⌒) ＼(≧▽≦)／ (*°▽°*) ٩(｡•́‿•̀｡)۶ (´｡• ᵕ •｡) ( ´ ▽ ) ヽ(>∀<☆)ノ o(≧▽≦)o
//    ＼(￣▽￣)／ (*¯︶¯*) (o˘◡˘o) \(★ω★)/ (╯✧▽✧)╯ o(>ω<)o ( ‾́ ◡ ‾́ ) (ﾉ´ヮ)ﾉ*: ･ﾟ
}
