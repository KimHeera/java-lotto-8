package lotto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import lotto.domain.PurchaseAmount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PurchaseAmount 클래스 테스트")
class PurchaseAmountTest {
    private static final int LOTTO_PRICE = 1000;

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTest {

        @ParameterizedTest(name = "정상 금액 {0}원 입력 시 예외가 발생하지 않는다.")
        @ValueSource(strings = {"1000", "50000", "100000"})
        void constructor_유효한_금액(String amount) {
            assertThatCode(() -> new PurchaseAmount(amount))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest(name = "유효하지 않은 금액 {0}원 입력 시 IllegalArgumentException이 발생한다.")
        @ValueSource(strings = {"900", "1500", "0", "-1000", "abcd"})
        @DisplayName("유효하지 않은 금액 입력 시 예외 발생")
        void constructor_유효하지_않은_금액(String amount) {
            assertThatThrownBy(() -> new PurchaseAmount(amount))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("getLottoCount 메서드 테스트")
    class GetLottoCountTest {

        @Test
        @DisplayName("5000원 입력 시 로또 수량이 5개로 정확히 계산된다.")
        void getLottoCount_5000원() {
            String inputAmount = "5000";
            PurchaseAmount purchaseAmount = new PurchaseAmount(inputAmount);

            int lottoCount = purchaseAmount.getLottoCnt();

            assertThat(lottoCount).isEqualTo(5000 / LOTTO_PRICE); // 5개
        }

        @Test
        @DisplayName("10만원 입력 시 로또 수량이 100개로 정확히 계산된다.")
        void getLottoCount_10만원() {
            String inputAmount = "100000";
            PurchaseAmount purchaseAmount = new PurchaseAmount(inputAmount);

            int lottoCount = purchaseAmount.getLottoCnt();

            assertThat(lottoCount).isEqualTo(100000 / LOTTO_PRICE); // 100개
        }
    }
}
