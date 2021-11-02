package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

public class ChildManager {

    private static ChildManager instance;
    public ArrayList<Child> childList;
    private int pickingChildIndex;

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    private ChildManager() {
        childList = new ArrayList<>();
        pickingChildIndex = 0;
    }

    public int numOfChildren() {
        return childList.size();
    }

    public void addChild(Child child) {
        childList.add(child);
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

    //temporary method for unit tests
    //should be removed later
    public void cleanSingleton(){
        childList.clear();
        pickingChildIndex = 0;
    }
}