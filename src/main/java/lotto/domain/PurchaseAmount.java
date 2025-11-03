package lotto.domain;

public class PurchaseAmount {
    private final int amount;

    public PurchaseAmount(String inputAmount){
        LottoValidator.validatePurchaseAmount(inputAmount);

        inputAmount = inputAmount.trim();
        this.amount = Integer.parseInt(inputAmount);
    }

    public int getLottoCnt(){
        return amount / LottoConstants.PRICE;
    }

    public int getAmount(){
        return amount;
    }
}
