package ca.cmpt276.parentapp.model;

import java.util.ArrayList;
import java.util.List;

/*
    ChildManager class - stores ArrayList of children and keeps track of which child gets to flip coin next
 */
public class ChildManager {

    private static ChildManager instance;
    private ArrayList<Child> childList;
    private ArrayList<CoinFlip> coinFlipHistory;
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

    public boolean noChildren(){
        return childList.isEmpty();
    }

    public int numOfChildren() {
        return childList.size();
    }

    public void addChild(Child child) {
        childList.add(child);
    }

    public void addCoinFlip(CoinFlip coinFlip){
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
        Child child = childList.get(pickingChildIndex);
        pickingChildIndex++;
        updateChildTracker();
        return child;
    }

    private void updateChildTracker(){
        if (pickingChildIndex >= numOfChildren()){
            pickingChildIndex = 0;
        }
    }

    public int getPickingChildIndex() {
        return pickingChildIndex;
    }

    public List children() {
        return childList;
    }

    //temporary method for unit tests
    //should be removed later
    public void cleanSingleton(){
        childList.clear();
        pickingChildIndex = 0;
    }
}