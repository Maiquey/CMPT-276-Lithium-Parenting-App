package ca.cmpt276.parentapp.model;

import java.time.LocalDateTime;

/**
 * CoinFlip class:
 *
 * Stores DateTime of the flip
 * Stores information of a coin flip: who picked, what they picked, result of flip, whether picker won or lost
 * Model functionality of the coin flip
 */
public class CoinFlip {

    private final LocalDateTime timeOfFlip;
    private final String whoPicked;
    private final String whoPickedPicture;
    private boolean isHeads;
    private boolean pickerPickedHeads;
    private boolean pickerWon;
    private final boolean noChildren;
    private final ChildManager childManager = ChildManager.getInstance();

    public CoinFlip() {
        this.timeOfFlip = LocalDateTime.now();
        if (childManager.noChildren()){
            this.noChildren = true;
            this.whoPicked = null;
            this.whoPickedPicture = null;
        }
        else{
            this.noChildren = false;
            this.whoPicked = childManager.getPickingChild().getName();
            this.whoPickedPicture = childManager.getPickingChild().getPhoto();
        }
    }

    public void doCoinFlip(){
        int randomInt = (int) (Math.random() * 100);
        isHeads = (randomInt % 2 == 0);
        pickerWon = (isHeads == pickerPickedHeads);
        childManager.updateQueue();
    }

    public void randomFlip(){
        int randomInt = (int) (Math.random() * 100);
        isHeads = (randomInt % 2 == 0);
    }

    public void setPickerPickedHeads(boolean pickedHeads) {
        this.pickerPickedHeads = pickedHeads;
    }

    public String getWhoPicked() {
        return whoPicked;
    }

    public String getWhoPickedPicture(){
        return whoPickedPicture;
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
