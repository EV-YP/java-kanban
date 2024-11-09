package io.feelgood.taskman.service;

import io.feelgood.taskman.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int idCounter = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    // создаем счетчик
    public int generateId() {
        return idCounter++;
    }

    // получаем список всех задач
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // удаляем все задачи
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
       epics.clear();
       subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
        }
    }

    // получаем задачи по идентификатору
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtask(subtask);
            updateEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Epic с id " + subtask.getEpicId() + " не найден.");
        }
    }

    // обновляем статус задачи

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Task с id " + task.getId() + " не найден.");
        }
    }

    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = subtasks.get(subtask.getId());
        if (oldSubtask != null) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(oldSubtask);
                epic.addSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
            }
        } else {
            System.out.println("Subtask с id " + subtask.getId() + " не найден.");
        }
    }

    public void updateEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            updateEpicStatus(id);
        } else {
            System.out.println("Epic с id " + id + " не найден.");
        }
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            boolean allNew = true;
            boolean allDone = true;
            for (int subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    if (subtask.getStatus() != Status.NEW) {
                        allNew = false;
                    }
                    if (subtask.getStatus() != Status.DONE) {
                        allDone = false;
                    }
                }
            }
            if (allNew) {
                epic.setStatus(Status.NEW);
            } else if (allDone) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    // удаляем задачи по идентификатору
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Task с id " + id + " не найден.");
        }
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        } else {
            System.out.println("Epic с id " + id + " не найден.");
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
            }
        } else {
            System.out.println("Subtask с id " + id + " не найден.");
        }
    }

    // получаем задачи определенного эпика
    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    epicSubtasks.add(subtask);
                }
            }
        } else {
            System.out.println("Epic with id " + epicId + " not found.");
        }
        return epicSubtasks;
    }
}