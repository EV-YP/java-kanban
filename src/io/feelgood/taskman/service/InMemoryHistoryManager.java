package io.feelgood.taskman.service;

import io.feelgood.taskman.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    // объявляем двусвязный список
    private final Node head = new Node(null);  // голова списка
    private final Node tail = new Node(null);  // хвост списка

    // создаем мапу для быстрого доступа к узлам по id
    private final Map<Integer, Node> taskHistoryMap = new HashMap<>();

    //связываем голову с хвостом и хвост с головой
    public InMemoryHistoryManager() {
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void add(Task task) {

        if (task == null) { // проверяем, что задача не null
        } else {
            int taskId = task.getId();
            // удаляем задачу из истории, если задачу уже просматривали
            if (taskHistoryMap.containsKey(taskId)) {
                removeNode(taskHistoryMap.get(taskId));
            }

            // создаём новый узел с задачей
            Node newNode = new Node(task);
            taskHistoryMap.put(taskId, newNode);

            // добавляем новый узел в конец списка
            linkLast(newNode);
        }
    }

    // вставляем узел в конец двусвязного списка
    private void linkLast(Node node) {
        Node last = tail.prev;
        last.next = node;
        node.prev = last;
        node.next = tail;
        tail.prev = node;
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = taskHistoryMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }

    // удаляем узел из связного списка
    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        taskHistoryMap.remove(node.task.getId());  // удаляем из карты
    }

    // получаем историю просмотренных задач
    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head.next;
        while (current != tail) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }
}
