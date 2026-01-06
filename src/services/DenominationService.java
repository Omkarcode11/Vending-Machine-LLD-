package services;

import java.util.Map;

public class DenominationService {
    DenominationChainService chain;

    public DenominationService(DenominationChainService chain) {
        this.chain = chain;
    }

    public void withdrawMoney(int amount) {
        if (chain.canWithdrawMoney(amount)) {
            chain.withdrawMoney(amount);
        }else{
            System.out.println("Money cannot be withdrawn");
        }
    }

    public void addMoney(Map<Integer, Integer> money) {
        if (chain.canAddMoney(money)) {
            chain.addMoney(money);
        }else{
            System.out.println("Money cannot be added");
        }
    }

    public void giveChange(int amount) {
        if (chain.canWithdrawMoney(amount)) {
            chain.withdrawMoney(amount);
        }else{
            System.out.println("Money cannot be given");
        }
    }

    public boolean canWithdrawMoney(int amount) {
        return chain.canWithdrawMoney(amount);
    }

    public boolean canAddMoney(Map<Integer, Integer> money) {
        return chain.canAddMoney(money);
    }
}
