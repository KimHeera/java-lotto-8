package lotto.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        LottoValidator.validateLottoNum(numbers);

        List<Integer> sortedNumbers = new ArrayList<>(numbers);
        Collections.sort(sortedNumbers);
        this.numbers = Collections.unmodifiableList(sortedNumbers);
    }

    /*
    * 다른 Lotto와 비교하여 일치하는 번호 개수를 계산
    * @param winningLotto 비교 대상 Lotto 객체
    * @return 일치하는 번호 개수(int)
     */
    public int match(Lotto winningLotto){
        return (int)numbers.stream()
                .filter(winningLotto.numbers::contains)
                .count();
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    @Override
    public String toString() {
        return numbers.toString();
    }
}
