package lotto;

import lotto.domain.Lotto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LottoTest {
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    @DisplayName("Lotto 생성 및 불변성 테스트")
    class ConstructorAndImmutabilityTest {

        @DisplayName("로또 번호가 오름차순으로 정렬되어 저장된다.")
        @Test
        void 로또_번호_정렬_테스트() {
            // given: 무작위 순서의 번호
            List<Integer> unsortedNumbers = List.of(45, 10, 20, 1, 30, 40);

            // when
            Lotto lotto = new Lotto(unsortedNumbers);

            // then: 내부 번호가 1, 10, 20, 30, 40, 45 순서로 저장되었는지 확인
            assertThat(lotto.getNumbers()).containsExactly(1, 10, 20, 30, 40, 45);
        }

        @DisplayName("로또 번호가 1-45 범위를 벗어나면 예외가 발생한다.")
        @Test
        void 로또_번호_범위_초과_테스트() {
            // given: 46은 유효 범위를 초과
            List<Integer> invalidNumbers = List.of(1, 2, 3, 4, 5, 46);

            // then: LottoValidator에 의해 IllegalArgumentException이 발생
            assertThatThrownBy(() -> new Lotto(invalidNumbers))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("로또 객체 생성 후, getNumbers()로 반환된 리스트는 수정이 불가능하다.")
        @Test
        void 로또_번호_불변성_테스트() {
            // given
            Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
            List<Integer> numbers = lotto.getNumbers();

            // then: 반환된 리스트에 add 시 UnsupportedOperationException이 발생
            assertThatThrownBy(() -> numbers.add(7))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("match 메서드 테스트 (일치 개수 계산)")
    class MatchMethodTest {

        private final Lotto purchasedLotto = new Lotto(List.of(1, 10, 20, 30, 40, 45));

        @DisplayName("6개 번호가 모두 일치하면 6을 반환한다.")
        @Test
        void match_6개_일치() {
            // given: 6개 번호가 모두 일치하는 당첨 로또
            Lotto winningLotto = new Lotto(List.of(1, 10, 20, 30, 40, 45));

            // when
            int matchCount = purchasedLotto.match(winningLotto);

            // then
            assertThat(matchCount).isEqualTo(6);
        }

        @DisplayName("3개 번호만 일치하면 3을 반환한다.")
        @Test
        void match_3개_일치() {
            // given: 3개 번호(1, 10, 20)만 일치하는 당첨 로또
            Lotto winningLotto = new Lotto(List.of(1, 10, 20, 11, 21, 31));

            // when
            int matchCount = purchasedLotto.match(winningLotto);

            // then
            assertThat(matchCount).isEqualTo(3);
        }

        @DisplayName("일치하는 번호가 하나도 없으면 0을 반환한다.")
        @Test
        void match_0개_일치() {
            // given: 일치하는 번호가 없는 당첨 로또
            Lotto winningLotto = new Lotto(List.of(7, 8, 9, 11, 12, 13));

            // when
            int matchCount = purchasedLotto.match(winningLotto);

            // then
            assertThat(matchCount).isEqualTo(0);
        }
    }
}
