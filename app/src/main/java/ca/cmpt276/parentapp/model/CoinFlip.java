package ca.cmpt276.parentapp.model;

import java.time.LocalDateTime;

public class CoinFlip {

    private final LocalDateTime timeOfFlip;
    private final Child whoPicked;
    private boolean isHeads;
    private boolean pickerPickedHeads;
    private boolean pickerWon;
    private final boolean noChildren;
    private final ChildManager childManager;

    public CoinFlip() {
        this.childManager = ChildManager.getInstance();
        this.timeOfFlip = LocalDateTime.now();
        if (childManager.noChildren()){
            this.noChildren = true;
            this.whoPicked = null;
        }
        else{
            this.noChildren = false;
            this.whoPicked = childManager.getPickingChild();
        }
    }

    public void doCoinFlip(){
        int randomInt = (int) (Math.random() * 100);
        isHeads = (randomInt % 2 == 0);
        pickerWon = (isHeads == pickerPickedHeads);
        childManager.updatePickingChild();
    }

    public void randomFlip(){
        int randomInt = (int) (Math.random() * 100);
        isHeads = (randomInt % 2 == 0);
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

    public boolean isNoChildren() {
        return noChildren;
    }

    public boolean isPickerPickedHeads() {
        return pickerPickedHeads;
    }
}
