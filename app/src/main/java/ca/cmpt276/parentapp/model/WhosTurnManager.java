package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * WhosTurnManager Model class
 *
 * Singleton class which keeps a list of configured tasks
 */
public class WhosTurnManager {
    private ArrayList<Task> tasks = new ArrayList<>();
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

}
