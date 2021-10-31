package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

public class ChildManager {

    private ArrayList<Child> childList;
    int numOfChildren;
    private static ChildManager instance;

    private ChildManager(){
        this.numOfChildren = 0;
        this.childList = new ArrayList<Child>();
    }

    public static ChildManager getInstance(){
        if (instance == null){
            instance = new ChildManager();
        }
        return instance;
    }

    public boolean noChildren(){
        return childList.isEmpty();
    }

    public Child getPickingChild(){
        return null;
    }

}
