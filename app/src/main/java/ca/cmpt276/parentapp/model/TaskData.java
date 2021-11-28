package ca.cmpt276.parentapp.model;

import java.time.LocalDateTime;

/**
 * TaskData model class:
 *
 * model class which stores the index of a task and child as well as
 * the LocalDatTime of when a task was completed by specified child.
 * Used to display who's turn history.
 */
public class TaskData {

    private LocalDateTime timeOfTask;
    private int childIndex;
    private int taskIndex;

    public TaskData(LocalDateTime timeOfTask, int childIndex, int taskIndex) {
        this.timeOfTask = timeOfTask;
        this.childIndex = childIndex;
        this.taskIndex = taskIndex;
    }

    public LocalDateTime getTimeOfTask() {
        return timeOfTask;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public void decrementChildIndex(){
        childIndex--;
    }

    public void decrementTaskIndex(){
        taskIndex--;
    }
}
