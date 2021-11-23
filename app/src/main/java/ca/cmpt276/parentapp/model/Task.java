package ca.cmpt276.parentapp.model;

/**
 * Task Model Class:
 *
 * Stores different information about a configured task
 * keeps track of which child's turn it is to do the task by holding an index to childList
 */
public class Task {
    private String taskName;
    private String childName;
    private int currentChildID;
    private String childImgID;

    public Task(String taskName, String childName, int currentChildID, String childImgID) {
        this.taskName = taskName;
        this.childName = childName;
        this.currentChildID = currentChildID;
        this.childImgID = childImgID;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getChildName() {
        return childName;
    }

    public int getCurrentChildID() {
        return currentChildID;
    }

    public void setCurrentChildID(int currentChildID) {
        this.currentChildID = currentChildID;
    }

    public void setChildName(String currentChildName) {
        this.childName = currentChildName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getChildImgID() {
        return childImgID;
    }

    public void setChildImgID(String id) {
        this.childImgID = id;
    }
}
