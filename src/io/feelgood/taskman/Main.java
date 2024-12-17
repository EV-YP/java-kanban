package io.feelgood.taskman;

import io.feelgood.taskman.model.*;
import io.feelgood.taskman.service.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = Managers.getDefault();

        // создаем эпики и подзадачи
        manager.addEpic(new Epic(manager.generateId(),"Epic with Subtasks", "Эпик с тремя подзадачами"));
        manager.addSubtask(new Subtask(manager.generateId(),"Subtask 1", "Описание подзадачи 1", Status.NEW, 1));
        manager.addSubtask(new Subtask(manager.generateId(),"Subtask 2", "Описание подзадачи 2", Status.NEW, 1));
        manager.addSubtask(new Subtask(manager.generateId(),"Subtask 3", "Описание подзадачи 3", Status.NEW, 1));
        manager.addEpic(new Epic(manager.generateId(),"Epic without Subtasks", "Эпик без подзадач"));

        // запрашиваем эпики и подзадачи в разном порядке и выводим историю
        manager.getSubtask(2);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.getSubtask(3);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.getSubtask(4);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.getSubtask(2);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.getSubtask(4);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.getEpic(1);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        manager.getEpic(5);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        // удаляем задачу и выводим историю (без этой задачи)
        manager.deleteSubtask(2);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        // удаляем эпик с задачами и проверяем, что в истории остался только эпик без задач
        manager.deleteEpic(1);
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}