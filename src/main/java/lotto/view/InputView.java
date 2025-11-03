package lotto.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String purchaseAmount(){
        System.out.println("구입금액을 입력해 주세요.");
        String input = Console.readLine();
        printNewLine();
        return input;
    }

    public String inputOfWinningNum(){
        System.out.println("당첨 번호를 입력해 주세요.");
        String input = Console.readLine();
        printNewLine();
        return input;
    }

    public String inputOfBonusNum(){
        System.out.println("보너스 번호를 입력해 주세요.");
        String input = Console.readLine();
        printNewLine();
        return input;
    }

    private void printNewLine() {
        System.out.println();
    }
}
