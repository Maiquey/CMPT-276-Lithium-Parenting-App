package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * ChildManager class:
 *
 * Singleton
 * Stores Arraylist of configured children
 * Stores Arraylist of CoinFlip objects representing a history of coin flips done by children
 * Stores an index representing the next child that gets to flip the coin
 */
public class ChildManager {

    private static ChildManager instance;
    private ArrayList<Child> childList;
    private ArrayList<CoinFlipData> coinFlipHistory;
    private int pickingChildIndex;

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    private ChildManager() {
        childList = new ArrayList<>();
        coinFlipHistory = new ArrayList<>();
        pickingChildIndex = 0;
    }

    public ArrayList<Child> getChildList(){
        return childList;
    }

    public boolean noChildren(){
        return childList.isEmpty();
    }

    public int numOfChildren() {
        return childList.size();
    }

    public void addChild(Child child) {
        childList.add(child);
    }

    public void addCoinFlip(CoinFlipData coinFlip){
        coinFlipHistory.add(coinFlip);
    }

    public void removeChild(Child child) {
        int childIndex = childList.indexOf(child);
        if (childIndex < pickingChildIndex){
            pickingChildIndex--;
        }
        childList.remove(child);
    }

    public void removeChildAtIndex(int i){
        if (i < pickingChildIndex){
            pickingChildIndex--;
        }
        childList.remove(i);
    }

    public void editChild(int i, String newName) {
        childList.get(i).setName(newName);
    }

    public Child getChild(int i) {
        return childList.get(i);
    }

    public String getChildName(int i) {
        Child temp = childList.get(i);
        return temp.getName();
    }

    public Child getPickingChild(){
        return childList.get(pickingChildIndex);
    }

    public void updatePickingChild() {
        pickingChildIndex++;
        if (pickingChildIndex >= numOfChildren()){
            pickingChildIndex = 0;
        }
    }

    public int getPickingChildIndex() {
        return pickingChildIndex;
    }

    //temporary method for unit tests
    //should be removed later
    public void cleanSingleton(){
        childList.clear();
        pickingChildIndex = 0;
    }

    public ArrayList<CoinFlipData> getCoinFlipHistory() {
        return coinFlipHistory;
    }
}