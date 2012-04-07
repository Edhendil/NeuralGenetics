/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game.forex;

/**
 *
 * @author Edhendil
 */
public class ForexPlayerState {
    
    private double money;
    private ForexAction action;
    private double lastTransactionOpening;

    public ForexPlayerState(double money) {
        this.money = money;
        action = ForexAction.NOTHING;
    }

    public double getLastTransactionOpening() {
        return lastTransactionOpening;
    }

    public void setLastTransactionOpening(double lastTransactionOpening) {
        this.lastTransactionOpening = lastTransactionOpening;
    }

    public ForexAction getAction() {
        return action;
    }

    public void setAction(ForexAction action) {
        this.action = action;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
    
}
