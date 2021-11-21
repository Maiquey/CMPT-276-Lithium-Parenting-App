package ca.cmpt276.parentapp.model;

import java.time.LocalDateTime;

/**
 * CoinFlipData Class:
 *
 * Used to store important data from coin flips to be saved/loaded
 */
public class CoinFlipData {

    private final LocalDateTime timeOfFlip;
    private final String whoPicked;
    private final String whoPickedPicture;
    private boolean isHeads;
    private boolean pickerPickedHeads;
    private boolean pickerWon;

    public CoinFlipData(LocalDateTime timeOfFlip, String whoPicked, String whoPickedPicture, boolean isHeads, boolean pickerPickedHeads, boolean pickerWon) {
        this.timeOfFlip = timeOfFlip;
        this.whoPicked = whoPicked;
        this.whoPickedPicture = whoPickedPicture;
        this.isHeads = isHeads;
        this.pickerPickedHeads = pickerPickedHeads;
        this.pickerWon = pickerWon;
    }

    public LocalDateTime getTimeOfFlip() {
        return timeOfFlip;
    }

    public String getWhoPicked() {
        return whoPicked;
    }

    public String getWhoPickedPicture() {
        return whoPickedPicture;
    }

    public boolean isHeads() {
        return isHeads;
    }

    public boolean isPickerPickedHeads() {
        return pickerPickedHeads;
    }

    public boolean isPickerWon() {
        return pickerWon;
    }
}
