package io.feelgood.taskman.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import io.feelgood.taskman.model.*;

import java.util.List;

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    // Тесты для проверки равенства экземпляров Task
    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task(1, "Таск 1", "Описание 1", Status.NEW);
        Task task2 = new Task(1, "Таск 1", "Описание 1", Status.NEW);
        assertEquals(task1, task2, "Задачи с одинаковыми id должны быть равны.");
    }

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask subtask1 = new Subtask(1, "Сабтаск 1", "Описание 1", Status.IN_PROGRESS, 2);
        Subtask subtask2 = new Subtask(1, "Сабтаск 1", "Описание 1", Status.IN_PROGRESS, 2);
        assertEquals(subtask1, subtask2, "Подзадачи с одинаковыми id должны быть равны.");
    }

    // Тест невозможности добавления Epic как подзадачи в самого себя
    @Test
    void epicCannotAddItselfAsSubtask() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        Subtask subtask = new Subtask(taskManager.generateId(), "Сабтаск", "Не может быть эпиком",
                Status.NEW, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        assertFalse(epic.getSubtaskIds().contains(epic.getId()),
                "Эпик не должен содержать самого себя как подзадачу.");
    }

    // Тест невозможности назначения подзадачи своим эпиком
    @Test
    void subtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        Subtask subtask = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание", Status.NEW,
                epic.getId());
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        assertNotEquals(subtask.getId(), subtask.getEpicId(), "Подзадача не может быть своим же эпиком.");
    }

    // Проверка, что класс Managers всегда возвращает менеджеры
    @Test
    void managersShouldReturnInitializedManagers() {
        assertNotNull(Managers.getDefault(), "TaskManager должен быть инициализирован.");
        assertNotNull(Managers.getDefaultHistory(), "HistoryManager должен быть инициализирован.");
    }

    // Тест добавления задач в InMemoryTaskManager и нахождения их по id
    @Test
    void shouldAddAndFindTaskById() {
        Task task = new Task(1, "Таск 1", "Описание", Status.NEW);
        taskManager.addTask(task);
        assertNotNull(taskManager.getTask(1), "Задача должна быть найдена по id.");
    }

    // Тест уникальности id задач и предотвращения конфликтов
    @Test
    void shouldNotConflictWithGeneratedId() {
        Task task1 = new Task(taskManager.generateId(), "Таск 1", "Описание 1", Status.NEW);
        Task task2 = new Task(taskManager.generateId(), "Таск 2", "Описание 2", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertNotEquals(task1.getId(), task2.getId(), "Задачи не должны иметь одинаковые id.");
    }

    // Тест неизменности задачи при добавлении в менеджер
    @Test
    void taskShouldRemainUnchangedWhenAddedToManager() {
        Task originalTask = new Task(1, "Оригинальный Таск", "Описание", Status.NEW);
        taskManager.addTask(originalTask);
        Task savedTask = taskManager.getTask(1);
        assertEquals(originalTask.getTitle(), savedTask.getTitle(), "Название задачи не должно измениться.");
        assertEquals(originalTask.getDescription(), savedTask.getDescription(),
                "Описание задачи не должно измениться.");
        assertEquals(originalTask.getStatus(), savedTask.getStatus(), "Статус задачи не должен измениться.");
    }

    // Тест добавления задач в историю
    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task(1, "Таск для добавления в историю", "Описание", Status.NEW);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История должна быть инициализирована.");
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task, history.getFirst(), "Задача в истории должна совпадать с добавленной.");
    }

    // Тест обновления истории при повторном обращении к задаче
    @Test
    void shouldUpdateHistoryWhenTaskIsRevisited() {
        Task task = new Task(taskManager.generateId(), "Задача 1", "Описание задачи", Status.NEW);
        historyManager.add(task);
        historyManager.add(task);  // Повторный запрос
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
    }

    // Тест удаления задачи из истории по id
    @Test
    void shouldRemoveTaskFromHistoryById() {
        Task task1 = new Task(taskManager.generateId(), "Задача 1", "Описание задачи", Status.NEW);
        Task task2 = new Task(taskManager.generateId(), "Задача 2", "Описание задачи", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(task1), "Задача 1 должна быть удалена из истории.");
        assertTrue(history.contains(task2), "Задача 2 должна остаться в истории.");
    }

    // Проверка, что задачи в истории сохраняют прежнюю версию данных
    @Test
    void taskInHistoryShouldRetainPreviousVersion() {
        Task task = new Task(1, "Оригинальный Таск", "Описание", Status.NEW);
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        taskManager.updateTask(new Task(1, "Оригинальный Таск", "Описание", Status.IN_PROGRESS));
        List<Task> history = taskManager.getHistory();
        assertEquals(Status.NEW, history.getFirst().getStatus(), "Статус задачи в истории не должен измениться" +
                " после изменения оригинала.");
    }

    // Тест удаления всех задач
    @Test
    void shouldDeleteAllTasks() {
        Task task1 = new Task(taskManager.generateId(), "Таск 1", "Описание 1", Status.NEW);
        Task task2 = new Task(taskManager.generateId(), "Таск 2", "Описание 2", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Все задачи должны быть удалены.");
    }

    // Тест удаления задачи по идентификатору
    @Test
    void shouldDeleteTaskById() {
        Task task = new Task(taskManager.generateId(), "Таск на удаление", "Описание", Status.NEW);
        taskManager.addTask(task);
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()), "Задача должна быть удалена.");
    }

    // Тест добавления эпика и подзадач к нему
    @Test
    void shouldAddEpicAndSubtasks() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание 1", Status.NEW,
                epic.getId());
        Subtask subtask2 = new Subtask(taskManager.generateId(), "Сабтаск 2", "Описание 2", Status.NEW,
                epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasksByEpicId(epic.getId()).size(),
                "Эпик должен содержать две подзадачи.");
    }

    // Тест обновления статуса эпика до NEW на основе подзадач
    @Test
    void shouldUpdateEpicStatusToNewBasedOnSubtasks() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание 1", Status.NEW,
                epic.getId());
        Subtask subtask2 = new Subtask(taskManager.generateId(), "Сабтаск 2", "Описание 2", Status.NEW,
                epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Status.NEW, epic.getStatus(),
                "Статус эпика должен быть DONE, если все подзадачи выполнены.");
    }

    // Тест обновления статуса эпика до IN_PROGRESS на основе подзадач
    @Test
    void shouldUpdateEpicStatusToInProgressBasedOnSubtasks() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание 1",
                Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask(taskManager.generateId(), "Сабтаск 2", "Описание 2",
                Status.IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(),
                "Статус эпика должен быть DONE, если все подзадачи выполнены.");
    }

    // Тест обновления статуса эпика до DONE на основе подзадач
    @Test
    void shouldUpdateEpicStatusToDoneBasedOnSubtasks() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание 1", Status.DONE,
                epic.getId());
        Subtask subtask2 = new Subtask(taskManager.generateId(), "Сабтаск 2", "Описание 2", Status.DONE,
                epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Status.DONE, epic.getStatus(),
                "Статус эпика должен быть DONE, если все подзадачи выполнены.");
    }

    // Тест получения всех эпиков
    @Test
    void shouldGetAllEpics() {
        Epic epic1 = new Epic(taskManager.generateId(), "Эпик 1", "Описание 1");
        Epic epic2 = new Epic(taskManager.generateId(), "Эпик 2", "Описание 2");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(2, epics.size(), "Должно быть добавлено два эпика.");
    }

    // Тест обновления подзадачи
    @Test
    void shouldUpdateSubtask() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание", Status.NEW,
                epic.getId());
        taskManager.addSubtask(subtask);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        assertEquals(Status.DONE, taskManager.getSubtask(subtask.getId()).getStatus(),
                "Статус подзадачи должен обновиться на DONE.");
    }

    // Тест сохранения последнего просмотра задачи при повторном добавлении
    @Test
    void shouldRemoveOldVisitWhenTaskIsRevisited() {
        Task task = new Task(1, "Таск 1", "Описание", Status.NEW);
        historyManager.add(task);
        historyManager.add(new Task(2, "Таск 2", "Описание", Status.NEW));
        historyManager.add(task); //при повторном добавлении ожидается удаление старого экземпляра

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать только последние просмотры.");
        assertTrue(history.contains(task), "История должна содержать задачу с id = 1.");
        assertTrue(history.contains(new Task(2, "Таск 2", "Описание", Status.NEW)),
                "История должна содержать задачу с id = 2.");
    }

    //Тест добавления и удаления задач из истории
    @Test
    void shouldAddAndRemoveTaskVisitsCorrectly() {
        Task task1 = new Task(1, "Таск 1", "Описание", Status.NEW);
        Task task2 = new Task(2, "Таск 2", "Описание", Status.NEW);

        historyManager.add(task1);  // Добавим первую задачу
        historyManager.add(task2);  // Добавим вторую задачу
        historyManager.remove(task1.getId());  // Удалим первую задачу

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать только одну задачу.");
        assertFalse(history.contains(task1), "Задача с id = 1 должна быть удалена.");
        assertTrue(history.contains(task2), "Задача с id = 2 должна остаться.");
    }

    //Тест на отсутствие использования id задачи другими объектами, после ее удаления
    @Test
    void removedSubtasksShouldNotRetainOldIds() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание", Status.NEW,
                epic.getId());
        taskManager.addSubtask(subtask);

        int oldSubtaskId = subtask.getId();
        taskManager.deleteSubtask(subtask.getId());  // Удалим подзадачу
        assertNull(taskManager.getSubtask(oldSubtaskId), "Удалённая подзадача не должна оставаться в системе.");
    }

    //Тест на удаление подзадачи из эпика и отсутствия ее id в списке подзадач эпика
    @Test
    void epicShouldNotRetainOutdatedSubtaskIds() {
        Epic epic = new Epic(taskManager.generateId(), "Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask(taskManager.generateId(), "Сабтаск 1", "Описание", Status.NEW,
                epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.deleteSubtask(subtask.getId());  // Удалим подзадачу
        assertFalse(epic.getSubtaskIds().contains(subtask.getId()),
                "Эпик не должен содержать удалённую подзадачу.");
    }
}
