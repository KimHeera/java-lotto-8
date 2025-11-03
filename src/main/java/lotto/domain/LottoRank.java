package lotto.domain;

import java.util.Arrays;

public enum LottoRank {
    FIRST(6, false, 2_000_000_000, "6개 일치 (2,000,000,000원)"),
    SECOND(5, true, 30_000_000, "5개 일치, 보너스 볼 일치 (30,000,000원)"),
    THIRD(5, false, 1_500_000, "5개 일치 (1,500,000원)"),
    FOURTH(4, false, 50_000, "4개 일치 (50,000원)"),
    FIFTH(3, false, 5_000, "3개 일치 (5,000원)"),
    MISS(0, false, 0, "낙첨"); //방어 설계

    private final int matchCount;
    private final boolean matchBonus;
    private final long prize;
    private final String message;

    LottoRank(int matchCount, boolean matchBonus, long prize, String message){
        this.matchCount = matchCount;
        this.matchBonus = matchBonus;
        this.prize = prize;
        this.message = message;
    }

    public static LottoRank[] getPrintableRanks() {
        return new LottoRank[]{FIFTH, FOURTH, THIRD, SECOND, FIRST};
    }

    public static LottoRank valueOf(int matchCount, boolean matchBonus){
        if(matchCount < FIFTH.matchCount){
            return MISS;
        }

        return Arrays.stream(values())
                .filter(rank -> rank != MISS) // miss 제외한 당첨 등수만 검색
                .filter(rank -> rank.matchCount == matchCount) // matchCount가 일치하는 등수만 남김
                .filter(rank -> matchCount != 5 || rank.matchBonus == matchBonus) // 5개 일치(2, 3등)인 경우, 보너스 일치 여부로 구분
                .findFirst() //필터링 통과한 1, 2등 반환
                .orElse(MISS); //모두 조건에 안맞으면 MISS 반환
    }
}
