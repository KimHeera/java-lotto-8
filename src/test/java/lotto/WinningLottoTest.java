package lotto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;

import lotto.domain.Lotto;
import lotto.domain.LottoConstants;
import lotto.domain.LottoRank;
import lotto.domain.WinningLotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("WinningLotto 클래스 테스트")
public class WinningLottoTest {
    private final List<Integer> VALID_WINNING_NUMBERS = List.of(1, 2, 3, 4, 5, 6);
    private final int VALID_BONUS_NUMBER = 7;
    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final int MIN_NUM = 1;
    private static final int MAX_NUM = 45;

    @Nested
    @DisplayName("생성자 검증 테스트")
    class ConstructorValidationTest {

        @Test
        @DisplayName("성공: 유효한 당첨 번호와 보너스 번호로 객체를 생성한다.")
        void createWinningLotto_정상() {
            assertThatCode(() -> new WinningLotto(VALID_WINNING_NUMBERS, VALID_BONUS_NUMBER))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("실패: 당첨 번호와 보너스 번호가 중복되면 예외가 발생한다.")
        void createWinningLotto_보너스_중복_오류() {
            int duplicateBonus = 6;
            assertThatThrownBy(() -> new WinningLotto(VALID_WINNING_NUMBERS, duplicateBonus))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ERROR_PREFIX + "보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }

        @Test
        @DisplayName("실패: 보너스 번호가 1-45 범위를 벗어나면 예외가 발생한다.")
        void createWinningLotto_보너스_범위_오류() {
            int invalidBonus = 46;
            assertThatThrownBy(() -> new WinningLotto(VALID_WINNING_NUMBERS, invalidBonus))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(ERROR_PREFIX + "로또 번호는 " + MIN_NUM + "부터 " + MAX_NUM + " 사이의 숫자여야 합니다.");
        }

        @Nested
        @DisplayName("determineRank 메서드 (등수 판별)")
        class DetermineRankTest {

            // 당첨 번호: 1, 2, 3, 4, 5, 6 / 보너스 번호: 7
            private final WinningLotto winningLotto = new WinningLotto(VALID_WINNING_NUMBERS, VALID_BONUS_NUMBER);

            @ParameterizedTest(name = "구매 로또 {0}는 등수 {1}에 해당한다.")
            @CsvSource(value = {
                    "1, 2, 3, 4, 5, 6 | FIRST",    // 1등: 6개 일치 (1~6)
                    "1, 2, 3, 4, 5, 7 | SECOND",   // 2등: 5개 일치 (1~5) + 보너스 일치 (7)
                    "1, 2, 3, 4, 5, 8 | THIRD",    // 3등: 5개 일치 (1~5)
                    "1, 2, 3, 4, 8, 9 | FOURTH",   // 4등: 4개 일치 (1~4)
                    "1, 2, 3, 8, 9, 10 | FIFTH",   // 5등: 3개 일치 (1~3)
                    "1, 2, 8, 9, 10, 11 | MISS",   // 낙첨: 2개 일치
                    "8, 9, 10, 11, 12, 13 | MISS"  // 낙첨: 0개 일치
            }, delimiter = '|')
            void 모든_등수를_정확하게_판별한다(String purchasedNumbersStr, LottoRank expectedRank) {
                // given
                List<Integer> purchasedNumbers = parseNumbers(purchasedNumbersStr);
                Lotto purchasedLotto = new Lotto(purchasedNumbers);

                // when
                LottoRank actualRank = winningLotto.determineRank(purchasedLotto);

                // then
                assertThat(actualRank).isEqualTo(expectedRank);
            }

            // 헬퍼 메서드: 문자열을 List<Integer>로 변환
            private List<Integer> parseNumbers(String numbersStr) {
                return List.of(numbersStr.split(", "))
                        .stream()
                        .map(Integer::parseInt)
                        .toList();
            }
        }
    }
}
