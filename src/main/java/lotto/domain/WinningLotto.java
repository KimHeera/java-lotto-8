package lotto.domain;

import java.util.List;

public class WinningLotto {
    private final Lotto winningLotto;
    private final int bonusNum;

    public WinningLotto(List<Integer> winningNumbers, int bonusNumer){
        this.winningLotto = new Lotto(winningNumbers);

        //보너스 번호 검증
        LottoValidator.validateSingleNum(bonusNumer);
        LottoValidator.validateBonusDup(winningNumbers, bonusNumer);

        this.bonusNum = bonusNumer;
    }

    public LottoRank determineRank(Lotto putchasedLotto){
        // 일치하는 번호 개수 계산
        int matchCnt = putchasedLotto.match(winningLotto);

        // 보너스 번호 일치 여부
        boolean matchBonus = isBonusMatched(putchasedLotto);

        return LottoRank.valueOf(matchCnt, matchBonus);
    }

    private boolean isBonusMatched(Lotto purchasedLotto){
        return purchasedLotto.getNumbers().contains(bonusNum);
    }
}
