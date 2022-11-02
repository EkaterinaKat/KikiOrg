package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.repo.AccountRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.CheckLineRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.CheckLine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScuttleCheckService {
    private final CheckLineRepo checkLineRepo;
    private final AccountRepo accountRepo;

    public List<CheckLine> getCheckLines(Account account) {
        return checkLineRepo.findAllByAccount(account);
    }

    public void rewriteCheckLines(List<CheckLine> checkLines, Account account) {
        checkLineRepo.findAllByAccount(account).forEach(checkLineRepo::delete);
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
