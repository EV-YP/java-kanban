package io.feelgood.taskman.service;

public class Managers {

    // Возвращаем через getDefault реализацию TaskManager по умолчанию
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    // Возвращаем через getDefaultHistory реализацию HistoryManager по умолчанию
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}