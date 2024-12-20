package io.feelgood.taskman.service;

import io.feelgood.taskman.model.*;
// добавлен код логирования ошибок
// код закоммичен, поскольку не проходит автотесты Яндекса на гитхабе (package org.slf4j does not exist)
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

//    private static final Logger logger = LoggerFactory.getLogger(InMemoryTaskManager.class);

    int idCounter = 1;
    final HashMap<Integer, Task> tasks = new HashMap<>();
    final HashMap<Integer, Epic> epics = new HashMap<>();
    final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HistoryManager taskHistoryMap = Managers.getDefaultHistory();

    @Override
    public int generateId() {
        return idCounter++;
    }

    // получаем список всех задач
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // удаляем все задачи
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
        }
    }

    // получаем задачи по идентификатору и записываем в список просмотренных задач
    @Override
    public Task getTask(int id) {
        taskHistoryMap.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        taskHistoryMap.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        taskHistoryMap.add(subtasks.get(id));
        return subtasks.get(id);
    }

    // добавляем задачи
    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtask(subtask);
            updateEpicStatus(subtask.getEpicId());
//        } else {
//            logger.warn("Epic с id {} не найден.", subtask.getEpicId());
        }
    }

    // обновляем статус задачи
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
//        } else {
//            logger.warn("Task с id {} не найден.", task.getId());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = subtasks.get(subtask.getId());
        if (oldSubtask != null) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(oldSubtask);
                epic.addSubtask(subtask);
                if (!oldSubtask.getStatus().equals(subtask.getStatus())) {
                    updateEpicStatus(subtask.getEpicId());
                }
            }
//        } else {
//            logger.warn("Subtask с id {} не найден.", subtask.getId());
        }
    }

    @Override
    public void updateEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            updateEpicStatus(id);
//        } else {
//            logger.warn("Epic с id {} не найден.", id);
        }
    }

    @Override
    public void updateEpicStatus(int epicId) {
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
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            taskHistoryMap.remove(id);
            tasks.remove(id);
//        } else {
//            logger.warn("Task с id {} не найден.", id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            taskHistoryMap.remove(id);
            for (int subtaskId : epic.getSubtaskIds()) {
                taskHistoryMap.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
//        } else {
//            logger.warn("Epic с id {} не найден.", id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                taskHistoryMap.remove(id);
                epic.removeSubtask(subtask);
                updateEpicStatus(subtask.getEpicId());
            }
//        } else {
//            logger.warn("Subtask с id {} не найден.", id);
        }
    }

    // получаем задачи определенного эпика
    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    epicSubtasks.add(subtask);
                }
            }
//        } else {
//            logger.warn("Epic с id {} не найден.", epicId);
        }
        return epicSubtasks;
    }

    // Используем HistoryManager для получения истории просмотренных задач
    @Override
    public List<Task> getHistory() {
        return taskHistoryMap.getHistory();
    }
}