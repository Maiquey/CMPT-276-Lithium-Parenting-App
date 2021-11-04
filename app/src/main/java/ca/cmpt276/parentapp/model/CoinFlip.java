package ca.cmpt276.parentapp.model;

import java.time.LocalDateTime;

public class CoinFlip {

    private final LocalDateTime timeOfFlip;
    private final Child whoPicked;
    private boolean isHeads;
    private boolean pickerPickedHeads;
    private boolean pickerWon;
    private final ChildManager childManager;

    public CoinFlip() {
        this.childManager = ChildManager.getInstance();
        this.timeOfFlip = LocalDateTime.now();
        this.whoPicked = childManager.getPickingChild();
    }

    public void initiateCoinFlip(){
        int randomInt = (int) (Math.random() * 100);
        isHeads = (randomInt % 2 == 0);
        pickerWon = (isHeads == pickerPickedHeads);
        childManager.updatePickingChild();
    }

    public void setPickerPickedHeads(boolean pickedHeads) {
        this.pickerPickedHeads = pickedHeads;
    }

    public LocalDateTime getTimeOfFlip() {
        return timeOfFlip;
    }

    public Child getWhoPicked() {
        return whoPicked;
    }

    public boolean isHeads() {
        return isHeads;
    }

    public boolean isPickerWon() {
        return pickerWon;
    }
}
