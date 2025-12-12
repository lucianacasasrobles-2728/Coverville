/*does some stuff like check if the person got enough points to buy a trade
also does the deduction math. */
package com.greenvile.viewmodel;

import com.greenvile.model.*;
import java.util.List;

public class TradeViewModel {
    private DataManager dataManager;

    public TradeViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public List<Trade> getTradeList() {
        return dataManager.getTrades();
    }

    public void addTrade(Trade trade) {
        trade.setId(dataManager.generateNewTradeId());
        dataManager.getTrades().add(trade);
    }

    public void updateTrade(Trade trade) {
        for (int i = 0; i < dataManager.getTrades().size(); i++) {
            if (dataManager.getTrades().get(i).getId() == trade.getId()) {
                dataManager.getTrades().set(i, trade);
                break;
            }
        }
    }

    public void deleteTrade(int id) {
        dataManager.getTrades().removeIf(t -> t.getId() == id);
    }

    public boolean completeTrade(int tradeId, int buyerId) {
        for (Trade t : dataManager.getTrades()) {
            if (t.getId() == tradeId) {
                Resident buyer = dataManager.getResidentById(buyerId);
                Resident seller = dataManager.getResidentById(t.getResidentId());
                
                if (buyer == null) {
                    return false;
                }
                
                if (buyer.getPersonalPoints() < t.getPointsCost()) {
                    return false;
                }
                
                buyer.deductPoints(t.getPointsCost());
                
                if (seller != null) {
                    seller.addPoints(t.getPointsCost());
                }
                
                t.setBuyerId(buyerId);
                t.setCompleted(true);
                return true;
            }
        }
        return false;
    }

    public void toggleWebsiteDisplay(int id) {
        for (Trade t : dataManager.getTrades()) {
            if (t.getId() == id) {
                t.setDisplayOnWebsite(!t.isDisplayOnWebsite());
                break;
            }
        }
    }

    public List<Resident> getAllResidents() {
        return dataManager.getResidents();
    }

    public Resident getResidentById(int id) {
        return dataManager.getResidentById(id);
    }
}
