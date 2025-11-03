package lotto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import lotto.domain.LottoRank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("LottoRank Enum 테스트")
public class LottoValidatorTest {
    @Nested
    @DisplayName("valueOf 메서드는")
    class ValueOfTest {

        @ParameterizedTest(name = "일치 개수 {0}개, 보너스 {1}일 때, 등수는 {2}이다.")
        @CsvSource(value = {
                "6, false, FIRST",  // 1등: 6개 일치 (보너스는 상관없지만 false로 표기)
                "5, true, SECOND",   // 2등: 5개 일치 + 보너스 일치
                "5, false, THIRD",  // 3등: 5개 일치
                "4, false, FOURTH", // 4등: 4개 일치
                "3, false, FIFTH",  // 5등: 3개 일치
                "2, false, MISS",   // 낙첨: 2개 일치
                "0, true, MISS"     // 낙첨: 0개 일치
        })
        void 모든_당첨_조건을_정확하게_판별한다(int matchCount, boolean matchBonus, LottoRank expectedRank) {
            // when
            LottoRank actualRank = LottoRank.valueOf(matchCount, matchBonus);

            // then
            assertThat(actualRank).isEqualTo(expectedRank);
        }

        @Test
        @DisplayName("2개 이하 일치 시 무조건 MISS를 반환한다.")
        void _2개_이하_일치_시_MISS_반환() {
            // when
            LottoRank rank1 = LottoRank.valueOf(2, true);
            LottoRank rank2 = LottoRank.valueOf(1, false);
            LottoRank rank3 = LottoRank.valueOf(0, false);

            // then
            assertThat(rank1).isEqualTo(LottoRank.MISS);
            assertThat(rank2).isEqualTo(LottoRank.MISS);
            assertThat(rank3).isEqualTo(LottoRank.MISS);
        }
    }
}
