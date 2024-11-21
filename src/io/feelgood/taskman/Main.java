package io.feelgood.taskman;

import io.feelgood.taskman.model.*;
import io.feelgood.taskman.service.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();

        // Создание задач
        manager.addTask(new Task(manager.generateId(),"Переезд", "Собрать коробки и упаковать вещи", Status.NEW));

        // Создание эпиков и подзадач
        manager.addEpic(new Epic(manager.generateId(),"Важный эпик 1", "Завершить проект"));
        manager.addSubtask(new Subtask(manager.generateId(),"Подготовка к задаче", "Собрать команду", Status.IN_PROGRESS, 2));
        manager.addSubtask(new Subtask(manager.generateId(),"Разработка", "Начать работу над проектом", Status.NEW, 2));

        // Проверка работы менеджера
        printAllTasks(manager);

        // Обновление статуса задач
        manager.updateTask(new Task(1,"Переезд", "Собрать коробки и упаковать вещи", Status.IN_PROGRESS));
        System.out.println("Все задачи: " + manager.getAllTasks());
        manager.updateSubtask(new Subtask(3,"Подготовка к задаче", "Собрать команду", Status.DONE, 2));
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());
        System.out.println("Обновленный эпик: " + manager.getEpic(2));
        manager.updateSubtask(new Subtask(4,"Разработка", "Начать работу над проектом", Status.DONE, 2));
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());
        System.out.println("Обновленный эпик: " + manager.getEpic(2));

        // Обновление статуса эпика
        manager.updateEpic(2);
        System.out.println("Обновленный эпик: " + manager.getEpic(2));

        // Удаление задачи
        manager.deleteTask(1);
        System.out.println("Все задачи после удаления: " + manager.getAllTasks());
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}