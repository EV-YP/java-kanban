package io.feelgood.taskman.model;

import io.feelgood.taskman.service.*;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
        this.subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtask(Subtask subtask) {
        subtaskIds.add(subtask.getId());
    }

    public void removeSubtask(Subtask subtask) {
        subtaskIds.remove((Integer) subtask.getId());
    }

    public void clearSubtasks() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", subtasks id=" + subtaskIds +
                '}';
    }
}