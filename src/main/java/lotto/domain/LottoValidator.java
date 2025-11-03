package lotto.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LottoValidator {
    private LottoValidator(){}

    //구입 금액 검증
    public static void validatePurchaseAmount(String amountStr){
        int amount = validateNumeric(amountStr, "구입 금액은 숫자만 입력 가능합니다.");

        if(amount <= 0){
            throw new IllegalArgumentException(LottoConstants.ERROR_PREFIX + "구입 금액은 양수여야 합니다.");
        }

        if(amount % LottoConstants.PRICE != 0){
            throw new IllegalArgumentException(LottoConstants.ERROR_PREFIX + "구입 금액은 " + LottoConstants.PRICE + "원 단위여야 합니다.");
        }
    }

    //로또 번호 검증
    public static void validateLottoNum(List<Integer> numbers){
        if(numbers.size() != LottoConstants.SIZE){
            throw new IllegalArgumentException(LottoConstants.ERROR_PREFIX + "로또 번호는 " + LottoConstants.SIZE + "개여야 합니다.");
        }

        validateNumRangeAndDup(numbers);
    }

    //단일 번호 검증(당첨과 보너스 모두 사용)
    public static void validateSingleNum(int number){
        if(number < LottoConstants.MIN_NUM || number > LottoConstants.MAX_NUM) {
            throw new IllegalArgumentException(
                    LottoConstants.ERROR_PREFIX + "로또 번호는 " + LottoConstants.MIN_NUM + "부터 " + LottoConstants.MAX_NUM + " 사이의 숫자여야 합니다.");
        }
    }

    //보너스 번호 중복 검증
    public static void validateBonusDup(List<Integer> WinningNumbers, int bonusNum){
        if(WinningNumbers.contains(bonusNum)){
            throw new IllegalArgumentException(LottoConstants.ERROR_PREFIX + "보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }

    //String to int
    private static int validateNumeric(String input, String msg){
        try{
            input = input.trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(LottoConstants.ERROR_PREFIX + msg);
        }
    }

    //범위 & 중복 검사
    private static void validateNumRangeAndDup(List<Integer> numbers){
        Set<Integer> uniqueNum = numbers.stream()
                .peek(LottoValidator::validateSingleNum)
                .collect(Collectors.toSet());

        if(uniqueNum.size() != numbers.size()){
            throw new IllegalArgumentException(LottoConstants.ERROR_PREFIX + "로또 번호에 중복된 숫자가 있습니다.");
        }
    }
}
