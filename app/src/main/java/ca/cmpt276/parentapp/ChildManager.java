package ca.cmpt276.parentapp;

import java.util.ArrayList;
import java.util.List;

public class ChildManager {
    private static ChildManager instance;
    public List<Child> childList = new ArrayList<>();
    public List<Integer> orderList = new ArrayList<>();

    public static ChildManager getInstance() {
        if (instance == null) {
            instance = new ChildManager();
        }
        return instance;
    }

    private ChildManager() {

    }

    public int numOfChild() {
        return childList.size();
    }

    public void add (Child child) {
        orderList.add(new Integer(childList.size()));
        childList.add(child);
    }

    public void remove(Child child) {
        childList.remove(child);
    }

    public void removeIndex(int i) {
        orderList.remove(new Integer(i));
        for(int j=0; j<orderList.size(); j++) {
            if (orderList.get(j) > new Integer(i)) {
                orderList.set(j,orderList.get(j-1));
            }
        }
    }

    public void edit (int i, String newName) {
        childList.get(i).setName(newName);
    }

    public int getIndex (int i) {
        return orderList.get(i).intValue();
    }

    public Child get(int i) {
        return childList.get(i);
    }

    public Child getByName(String childName) {
        Child childObject = null;
        for (int i=0; i<childList.size(); i++) {
            if(childName.equals(childList.get(i).getName())) {
                childObject = childList.get(i);
            }
        }
        return childObject;
    }

    public String getInfo(int i) {
        Child temp = childList.get(i);
        String info = temp.getName();
        return info;
    }
}
