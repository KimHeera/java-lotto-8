package lotto.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lotto.domain.LottoConstants;
import lotto.domain.LottoRank;
import lotto.domain.LottoStore;
import lotto.domain.PurchaseAmount;
import lotto.domain.WinningLotto;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {
    private final InputView inputView;
    private final OutputView outputView;

    public LottoController(InputView inputView, OutputView outputView){
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run(){
        LottoStore lottoStore = purchaseLotto();

        WinningLotto winningLotto = inputWinningLotto();

        calculateAndPrintResults(lottoStore, winningLotto);
    }

    private LottoStore purchaseLotto(){
        while(true){
            try{
                String amountStr = inputView.purchaseAmount();
                PurchaseAmount purchaseAmount = new PurchaseAmount(amountStr);

                LottoStore lottoStore = new LottoStore(purchaseAmount);

                outputView.printLottos(lottoStore.getPurchasedLottos());

                return lottoStore;
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private WinningLotto inputWinningLotto(){
        List<Integer> winningNumbers = inputWinningNumbers();
        int bonusNumber = inputBonusNum(winningNumbers);

        return new WinningLotto(winningNumbers, bonusNumber);
    }

    private List<Integer> inputWinningNumbers(){
        while(true){
            try{
                String input = inputView.inputOfWinningNum();
                return parseWinningNumbers(input);
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private int inputBonusNum(List<Integer> winningNumbers){
        while(true){
            try {
                String input = inputView.inputOfBonusNum();
                int bonusNum = Integer.parseInt(input.trim());

                return bonusNum;
            } catch (NumberFormatException e){
                System.out.println(LottoConstants.ERROR_PREFIX + "보너스 번호는 숫자만 입력 가능합니다.");
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private List<Integer> parseWinningNumbers(String input){
        List<Integer> numbers = Arrays.stream(input.split(","))
                .map(String::trim)
                .map(this::safeParseInt)
                .collect(Collectors.toList());

        return numbers;
    }

    private int safeParseInt(String s){
        try{
            return Integer.parseInt(s);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(LottoConstants.ERROR_PREFIX + "로또 번호는 숫자만 입력 가능합니다.");
        }
    }

    private void calculateAndPrintResults(LottoStore lottoStore, WinningLotto winningLotto){
        Map<LottoRank, Integer> statistics = lottoStore.calculateStatistics(winningLotto);

        long totalPrize = lottoStore.calculateTotalPrize(statistics);

        int purchaseAmount = lottoStore.getTotalPurchaseAmount();
        double profitRate = lottoStore.calculateProfitRate(totalPrize, purchaseAmount);

        outputView.printResults(statistics, profitRate);
    }
}
