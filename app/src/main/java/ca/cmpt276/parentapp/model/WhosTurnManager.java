package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

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

}
