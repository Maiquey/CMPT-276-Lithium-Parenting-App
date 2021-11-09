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
    private boolean isHeads;
    private boolean pickerPickedHeads;
    private boolean pickerWon;

    public CoinFlipData(LocalDateTime timeOfFlip, String whoPicked, boolean isHeads, boolean pickerPickedHeads, boolean pickerWon) {
        this.timeOfFlip = timeOfFlip;
        this.whoPicked = whoPicked;
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
