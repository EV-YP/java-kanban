package io.feelgood.taskman.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.feelgood.taskman.service.*;

public class EpicTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    // Тест добавления подзадачи в эпик
    @Test
    void shouldAddSubtaskToEpic() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание Эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        assertTrue(epic.getSubtaskIds().contains(subtask.getId()), "Подзадача должна быть добавлена в эпик.");
    }

    // Тест удаления подзадачи из эпика
    @Test
    void shouldRemoveSubtaskFromEpic() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание Эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.deleteSubtask(subtask.getId());
        assertFalse(epic.getSubtaskIds().contains(subtask.getId()), "Подзадача должна быть удалена из эпика.");
    }

    // Тест очистки всех подзадач в эпике
    @Test
    void shouldClearAllSubtasksFromEpic() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание Эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание 1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask(taskManager.generateId(), "Сабтаск 2", "Описание 2", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        epic.clearSubtasks();
        assertTrue(epic.getSubtaskIds().isEmpty(), "Все подзадачи должны быть удалены из эпика.");
    }
}