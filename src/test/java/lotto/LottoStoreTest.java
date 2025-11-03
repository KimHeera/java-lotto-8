package lotto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import java.util.Map;
import lotto.domain.Lotto;
import lotto.domain.LottoRank;
import lotto.domain.LottoStore;
import lotto.domain.PurchaseAmount;
import lotto.domain.WinningLotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LottoStore 클래스 테스트")
public class LottoStoreTest {
    private static final int LOTTO_PRICE = 1000;

    private final List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
    private final int bonusNumber = 7;
    private final WinningLotto winningLotto = new WinningLotto(winningNumbers, bonusNumber);

    @Nested
    @DisplayName("통계 계산 메서드 테스트")
    class CalculateStatisticsTest {

        /**
         * 1등(1개), 2등(1개), 3등(1개), 4등(1개), 5등(1개)이 발생한 상황을 가정하여 테스트.
         * Randoms 의존성 때문에 Mocking을 사용하지 않고
         * 테스트용 List<Lotto>를 직접 생성하여 결과를 검증합니다.
         */
        @Test
        @DisplayName("모든 등수에 대해 정확한 당첨 횟수를 계산한다.")
        void calculateStatistics_모든_등수_카운트() {
            List<Lotto> purchasedLottos = List.of(
                    // 1등 (6개 일치)
                    new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                    // 2등 (5개 일치 + 보너스 7 일치)
                    new Lotto(List.of(1, 2, 3, 4, 5, 7)),
                    // 3등 (5개 일치)
                    new Lotto(List.of(1, 2, 3, 4, 5, 8)),
                    // 4등 (4개 일치)
                    new Lotto(List.of(1, 2, 3, 4, 8, 9)),
                    // 5등 (3개 일치)
                    new Lotto(List.of(1, 2, 3, 9, 10, 11))
            );

            LottoStore lottoStore = new LottoStore(purchasedLottos, 5000);

            Map<LottoRank, Integer> statistics = lottoStore.calculateStatistics(winningLotto);

            assertThat(statistics).hasSize(5)
                    .containsEntry(LottoRank.FIRST, 1)
                    .containsEntry(LottoRank.SECOND, 1)
                    .containsEntry(LottoRank.THIRD, 1)
                    .containsEntry(LottoRank.FOURTH, 1)
                    .containsEntry(LottoRank.FIFTH, 1);
        }

        @Test
        @DisplayName("당첨이 하나도 없을 경우 빈 Map을 반환한다.")
        void calculateStatistics_당첨_없음() {
            List<Lotto> purchasedLottos = List.of(
                    new Lotto(List.of(8, 9, 10, 11, 12, 13)),
                    new Lotto(List.of(14, 15, 16, 17, 18, 19)),
                    new Lotto(List.of(20, 21, 22, 23, 24, 25)),
                    new Lotto(List.of(26, 27, 28, 29, 30, 31)),
                    new Lotto(List.of(32, 33, 34, 35, 36, 37))
            );

            LottoStore lottoStore = new LottoStore(new PurchaseAmount("5000"));
            Map<LottoRank, Integer> statistics = lottoStore.calculateStatistics(winningLotto);

            assertThat(statistics).isEmpty();
        }
    }

    @Nested
    @DisplayName("수익률 계산 메서드 테스트")
    class CalculateProfitRateTest {

        @Test
        @DisplayName("수익률이 62.5%일 때 정확한 값을 계산한다.")
        void calculateProfitRate_62_5퍼센트() {
            long totalPrize = 5000L;
            int purchaseAmount = 8000;
            LottoStore lottoStore = new LottoStore(new PurchaseAmount("8000"));

            double profitRate = lottoStore.calculateProfitRate(totalPrize, purchaseAmount);

            assertThat(profitRate).isEqualTo(62.5);
        }

        @Test
        @DisplayName("손해를 볼 때 (수익률 0) 정확한 값을 계산한다.")
        void calculateProfitRate_손해_0퍼센트() {
            long totalPrize = 0L;
            int purchaseAmount = 10000;
            LottoStore lottoStore = new LottoStore(new PurchaseAmount("10000"));

            double profitRate = lottoStore.calculateProfitRate(totalPrize, purchaseAmount);

            assertThat(profitRate).isEqualTo(0.0);
        }

        @Test
        @DisplayName("수익률이 25%일 때 정확한 값을 계산한다.")
        void calculateProfitRate_25퍼센트() {
            long totalPrize = 2500L;
            int purchaseAmount = 10000;
            LottoStore lottoStore = new LottoStore(new PurchaseAmount("10000"));

            double profitRate = lottoStore.calculateProfitRate(totalPrize, purchaseAmount);

            assertThat(profitRate).isEqualTo(25.0);
        }
    }
}
