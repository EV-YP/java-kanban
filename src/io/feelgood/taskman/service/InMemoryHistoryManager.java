package io.feelgood.taskman.service;

import io.feelgood.taskman.model.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    static final int TASKS_IN_HISTORY = 10;

    List<Task> tasksInHistory = new ArrayList<>(TASKS_IN_HISTORY);

    @Override
    public void add(Task task) {
        if (!tasksInHistory.contains(task)) {
            if (tasksInHistory.size() < TASKS_IN_HISTORY) {
                tasksInHistory.add(task);
            } else {
                tasksInHistory.removeFirst();
                tasksInHistory.add(task);
            }
        }
    }

    // получаем историю просмотренных задач
    @Override
    public List<Task> getHistory() {
        return tasksInHistory;
    }
}
