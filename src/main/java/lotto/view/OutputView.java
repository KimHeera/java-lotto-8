package lotto.view;

import java.text.DecimalFormat;
import java.util.List;

import java.util.Map;
import lotto.domain.Lotto;
import lotto.domain.LottoRank;

public class OutputView {
    private static final String COUNT_MESSAGE = "%d개를 구매했습니다.";
    private static final String RESULT_HEADER = "당첨 통계\n---";
    private static final String RANK_RESULT_FORMAT = "%s - %d개";
    private static final String PROFIT_RATE_FORMAT = "총 수익률은 %.1f%%입니다.";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.0");

    private OutputView() {}

    public static void printLottos(List<Lotto> purchasedLottos){
        System.out.println(String.format(COUNT_MESSAGE, purchasedLottos.size()));
        purchasedLottos.stream()
                .map(Lotto::toString) // Lotto의 toString() 사용 (정렬된 번호 출력)
                .forEach(System.out::println);
    }

    public static void printResults(Map<LottoRank, Integer> statistics, double profitRate){
        System.out.println(RESULT_HEADER);

        printStatistics(statistics);
        printProfitRate(profitRate);
    }

    private static void printStatistics(Map<LottoRank, Integer> statistics){
        for(LottoRank rank : LottoRank.getPrintableRanks()){
            int cnt = statistics.getOrDefault(rank, 0);

            System.out.println(String.format(RANK_RESULT_FORMAT, rank.getMessage(), cnt));
        }
    }

    private static void printProfitRate(double profitRate){
        String formattedRate = String.format("%.1f", profitRate);
        System.out.println(String.format(PROFIT_RATE_FORMAT, Double.parseDouble(formattedRate)));
    }
}
