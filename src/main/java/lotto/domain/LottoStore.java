package lotto.domain;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LottoStore {
    private final List<Lotto> purchasedLottos;

    public LottoStore(PurchaseAmount purchaseAmount){
        int cnt = purchaseAmount.getLottoCnt();

        this.purchasedLottos = issueLottos(cnt);
    }

    // 구매 수량만큼의 로또 발행, List<Lotto>로 반환
    public static List<Lotto> issueLottos(int cnt){
        return IntStream.range(0, cnt)
                .mapToObj(i -> createSingleLotto())
                .collect(Collectors.toList());
    }

    // Randoms를 사용해 단일 로또 번호 목록 생성
    private static Lotto createSingleLotto(){
        List<Integer> numbers = Randoms.pickUniqueNumbersInRange(
                LottoConstants.MIN_NUM,
                LottoConstants.MAX_NUM,
                LottoConstants.SIZE
        );

        return new Lotto(numbers);
    }

    // 구매한 모든 로또를 당첨 번호와 비교하여 최종 통계 계싼
    // * @param winningLotto: 당첨 번호 및 보너스 번호를 포함하는 객체
    // * @return 등수별 당첨 횟수를 담은 맵
    public Map<LottoRank, Integer> calculateStatistics(WinningLotto winningLotto){
        return purchasedLottos.stream()
                .map(winningLotto::determineRank)
                .filter(rank -> rank != LottoRank.MISS)
                .collect(Collectors.groupingBy(
                        rank -> rank,
                        Collectors.summingInt(rank -> 1)
                ));
    }

    // 통계 맵을 기반으로 총 상금 계산
    public Long calculateTotalPrize(Map<LottoRank, Integer> statistics){
        return statistics.entrySet().stream()
                .mapToLong(entry -> entry.getKey().getPrize() * entry.getValue())
                .sum();
    }

    // 수익률 계산
    public double calculateProfitRate(long totalPrize, int purchaseAmount){
        return (double) totalPrize / (double) purchaseAmount * 100;
    }

    public List<Lotto> getPurchasedLottos(){
        return Collections.unmodifiableList(purchasedLottos);
    }
}
