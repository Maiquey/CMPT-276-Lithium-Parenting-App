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
    private ArrayList<Child> coinFlipQueue;
    private ArrayList<Integer> queueOrder;
    private boolean nextFlipEmpty = false;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String path;

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    private ChildManager() {
        childList = new ArrayList<>();
        coinFlipHistory = new ArrayList<>();
        coinFlipQueue = new ArrayList<>();
        queueOrder = new ArrayList<>();
    }

    public ArrayList<Child> getChildList(){
        return childList;
    }

    public void setChildList(ArrayList<Child> childList){
        this.childList = childList;
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

    public void addIndexToQueueOrder(int i){
        queueOrder.add(i);
    }

    public void addCoinFlip(CoinFlipData coinFlip){
        coinFlipHistory.add(coinFlip);
    }

    public void removeChild(Child child) {
        childList.remove(child);
    }

    public void removeChildAtIndex(int i){
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
        return coinFlipQueue.get(0);
    }

    public void updateQueue() {
        Child child = coinFlipQueue.get(0);
        coinFlipQueue.remove(0);
        coinFlipQueue.add(child);

        int index = queueOrder.get(0);
        queueOrder.remove(0);
        queueOrder.add(index);
    }

    public void moveChildToFrontOfQueue(int i){
        int index = queueOrder.get(i);
        queueOrder.remove(i);
        queueOrder.add(0, index);
        loadQueue();
    }
    public ArrayList<Child> children() {
        return childList;
    }

    //method for unit tests
//    public void cleanSingleton(){
//        childList.clear();
//        pickingChildIndex = 0;
//    }

    public ArrayList<CoinFlipData> getCoinFlipHistory() {
        return coinFlipHistory;
    }

    public void setCoinFlipHistory(ArrayList<CoinFlipData> coinFlipHistory) {
        this.coinFlipHistory = coinFlipHistory;
    }

    public ArrayList<Child> getCoinFlipQueue() {
        return coinFlipQueue;
    }

    public ArrayList<Integer> getQueueOrder() {
        return queueOrder;
    }

    public void setQueueOrder(ArrayList<Integer> queueOrder) {
        this.queueOrder = queueOrder;
    }

    public void loadQueue(){
        coinFlipQueue.clear();
        for (int i : queueOrder){
            coinFlipQueue.add(childList.get(i));
        }
    }

    public void fixQueueOrderIndices(int removedIndex){
        queueOrder.remove((Integer) removedIndex);

        for (int i = 0; i < queueOrder.size(); i++){
            if (removedIndex < queueOrder.get(i)){
                queueOrder.set(i, queueOrder.get(i) - 1);
            }
        }
    }

    public void setNextFlipEmpty(){
        nextFlipEmpty = true;
    }

    public void setNextFlipNotEmpty(){
        nextFlipEmpty = false;
    }

    public boolean isNextFlipEmpty(){
        return nextFlipEmpty;
    }
}