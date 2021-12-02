package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * WhosTurnManager Model class
 *
 * Singleton class which keeps a list of configured tasks
 * Also keeps a list of completed tasks
 */
public class WhosTurnManager {
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<TaskData> taskHistory = new ArrayList<>();
    private ArrayList<TaskData> specificTaskHistory = new ArrayList<>();
    private static WhosTurnManager instance;

    public static WhosTurnManager getInstance() {
        if (instance == null) {
            instance = new WhosTurnManager();
        }
        return instance;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    private WhosTurnManager() {
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
    }

    public void setTaskList(ArrayList<Task> newTask) {
        this.tasks = newTask;
    }

    public void addTaskData(TaskData newTask){
        taskHistory.add(newTask);
    }

    public ArrayList<TaskData> getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(ArrayList<TaskData> taskHistory) {
        this.taskHistory = taskHistory;
    }

    public ArrayList<TaskData> getTasksByIndex(int i){
        specificTaskHistory.clear();
        for (TaskData taskData : taskHistory){
            if (taskData.getTaskIndex() == i){
                specificTaskHistory.add(taskData);
            }
        }
        return specificTaskHistory;
    }

    public void removeTaskDataByTaskIndex(int i){
        int arrayIndex = 0;
        while (arrayIndex < taskHistory.size()){
            if (taskHistory.get(arrayIndex).getTaskIndex() == i){
                taskHistory.remove(arrayIndex);
            }
            else{
                arrayIndex++;
            }
        }
        for (TaskData taskData : taskHistory){
            if (i < taskData.getTaskIndex()){
                taskData.decrementTaskIndex();
            }
        }
    }

    public void removeTaskDataByChildIndex(int i){
        int arrayIndex = 0;
        while (arrayIndex < taskHistory.size()){
            if (taskHistory.get(arrayIndex).getChildIndex() == i){
                taskHistory.remove(arrayIndex);
            }
            else{
                arrayIndex++;
            }
        }
        for (TaskData taskData : taskHistory){
            if (i < taskData.getChildIndex()){
                taskData.decrementChildIndex();
            }
        }
    }
}
