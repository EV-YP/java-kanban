package io.feelgood.taskman.service;

import io.feelgood.taskman.model.*;

import java.util.List;

public interface HistoryManager {

    //помечаем задачи как просмотренные
    void add(Task task);

    //удаляем задачи из истории
    void remove (int id);

    // получаем историю просмотренных задач
    List<Task> getHistory();

}