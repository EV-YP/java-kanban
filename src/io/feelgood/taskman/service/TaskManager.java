package io.feelgood.taskman.service;

import io.feelgood.taskman.model.*;

import java.util.List;

public interface TaskManager {

    // создаем счетчик
    int generateId();

    // получаем список всех задач
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    // удаляем все задачи
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    // получаем задачи по идентификатору
    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    // добавляем задачи
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    // обновляем статус задачи
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(int id);

    void updateEpicStatus(int epicId);

    // удаляем задачи по идентификатору
    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    // получаем задачи определенного эпика
    List<Subtask> getSubtasksByEpicId(int epicId);

    // получаем историю просмотренных задач
    List<Task> getHistory();
}