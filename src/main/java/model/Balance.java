package model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Balance {

    private BigDecimal balance;

    private String balanceText;

    private String balanceAdd;

    public BigDecimal getBalance() {
        return balance;
    }

    public String setBalance(BigDecimal balance) {
        this.balance = balance;
        return balance + "";
    }

    public String getBalanceAdd() {
        return balanceAdd;
    }

    public void setBalanceAdd(String balanceAdd) {
        this.balanceAdd = balanceAdd;
    }

    public String getBalanceText() {
        return balanceText;
    }

    public void setBalanceText(String balanceText) {
        this.balanceText = balanceText;
    }
}
