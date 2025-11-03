package lotto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import lotto.domain.LottoValidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LottoValidator 검증 테스트")
public class LottoValidatorTest {
    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final int PRICE = 1000;
    private static final int SIZE = 6;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;

    @Nested
    @DisplayName("구입 금액 검증")
    class ValidatePurchaseAmountTest {

        @Test
        @DisplayName("성공: 1000원 단위의 양수 금액은 예외를 발생시키지 않는다.")
        void validatePurchaseAmount_정상() {
            assertThatCode(() -> LottoValidator.validatePurchaseAmount("3000"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("실패: 숫자가 아닌 문자열 입력 시 예외 발생")
        void validatePurchaseAmount_숫자_아님() {
            String invalidInput = "3000a";
            assertThatThrownBy(() -> LottoValidator.validatePurchaseAmount(invalidInput))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(ERROR_PREFIX);
        }

        @Test
        @DisplayName("실패: 1000원 단위가 아닌 금액 입력 시 예외 발생")
        void validatePurchaseAmount_단위_오류() {
            String invalidInput = "3500";
            assertThatThrownBy(() -> LottoValidator.validatePurchaseAmount(invalidInput))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ERROR_PREFIX + "구입 금액은 " + PRICE + "원 단위여야 합니다.");
        }

        @Test
        @DisplayName("실패: 0 또는 음수 금액 입력 시 예외 발생")
        void validatePurchaseAmount_양수_오류() {
            String invalidInput = "-1000";
            assertThatThrownBy(() -> LottoValidator.validatePurchaseAmount(invalidInput))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ERROR_PREFIX + "구입 금액은 양수여야 합니다.");
        }
    }

    @Nested
    @DisplayName("로또 번호 목록 검증")
    class ValidateLottoNumbersTest {

        @Test
        @DisplayName("성공: 유효한 6개 번호는 예외를 발생시키지 않는다.")
        void validateLottoNumbers_정상() {
            List<Integer> validNumbers = List.of(1, 2, 3, 4, 5, 6);
            assertThatCode(() -> LottoValidator.validateLottoNum(validNumbers))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("실패: 번호 개수가 6개가 아닌 경우 예외 발생")
        void validateLottoNumbers_개수_오류() {
            List<Integer> invalidNumbers = List.of(1, 2, 3, 4, 5);
            assertThatThrownBy(() -> LottoValidator.validateLottoNum(invalidNumbers))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ERROR_PREFIX + "로또 번호는 " + SIZE + "개여야 합니다.");
        }

        @Test
        @DisplayName("실패: 번호가 1-45 범위를 벗어난 경우 예외 발생")
        void validateLottoNumbers_범위_오류() {
            List<Integer> invalidNumbers = List.of(1, 2, 3, 4, 5, 46);
            assertThatThrownBy(() -> LottoValidator.validateLottoNum(invalidNumbers))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(ERROR_PREFIX + "로또 번호는 " + MIN_NUMBER + "부터 " + MAX_NUMBER + " 사이의 숫자여야 합니다.");
        }

        @Test
        @DisplayName("실패: 번호에 중복된 숫자가 있는 경우 예외 발생")
        void validateLottoNumbers_중복_오류() {
            List<Integer> invalidNumbers = List.of(1, 2, 3, 4, 5, 5);
            assertThatThrownBy(() -> LottoValidator.validateLottoNum(invalidNumbers))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ERROR_PREFIX + "로또 번호에 중복된 숫자가 있습니다.");
        }
    }

    @Nested
    @DisplayName("보너스 번호 검증")
    class ValidateBonusNumberTest {

        private final List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);

        @Test
        @DisplayName("성공: 1-45 범위 내의 중복 없는 번호는 예외를 발생시키지 않는다.")
        void validateBonusNumber_정상() {
            assertThatCode(() -> LottoValidator.validateSingleNum(45))
                    .doesNotThrowAnyException();
            assertThatCode(() -> LottoValidator.validateBonusDup(winningNumbers, 7))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("실패: 번호가 1-45 범위를 벗어난 경우 예외 발생")
        void validateBonusNumber_범위_오류() {
            int invalidNumber = 0;
            assertThatThrownBy(() -> LottoValidator.validateSingleNum(invalidNumber))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(ERROR_PREFIX + "로또 번호는 " + MIN_NUMBER + "부터 " + MAX_NUMBER + " 사이의 숫자여야 합니다.");
        }

        @Test
        @DisplayName("실패: 당첨 번호와 보너스 번호가 중복된 경우 예외 발생")
        void validateBonusNumber_중복_오류() {
            int duplicateNumber = 6;
            assertThatThrownBy(() -> LottoValidator.validateBonusDup(winningNumbers, duplicateNumber))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(ERROR_PREFIX + "보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }
}
