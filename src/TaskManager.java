import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int idCounter = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    // создаем счетчик
    public int generateId() {
        int newId = idCounter;
        idCounter++;
        return newId;
    }

    // получаем список всех задач
    public HashMap<Integer, Task> getAllTasks() {
        return new HashMap<>(tasks);
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return new HashMap<>(epics);
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return new HashMap<>(subtasks);
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

    // создаем задачи
    public void addTask(String title, String description, Status status) {
        int id = generateId();
        Task task = new Task(id, title, description, status);
        tasks.put(id, task);
    }

    public void addEpic(String title, String description) {
        int id = generateId();
        Epic epic = new Epic(id, title, description);
        epics.put(id, epic);
    }

    public void addSubtask(String title, String description, Status status, int epicId) {
        if (epics.containsKey(epicId)) {
            int id = generateId();
            Subtask subtask = new Subtask(id, title, description, status, epicId);
            subtasks.put(id, subtask);
            epics.get(epicId).addSubtask(subtask);
        } else {
            System.out.println("Epic с id " + epicId + " не найден.");
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
            }
        } else {
            System.out.println("Subtask с id " + subtask.getId() + " не найден.");
        }
    }

    public void updateEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.updateStatus();
        } else {
            System.out.println("Epic с id " + id + " не найден.");
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
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
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
            }
        } else {
            System.out.println("Subtask с id " + id + " не найден.");
        }
    }

    // получаем задачи определенного эпика
    public ArrayList<Subtask> getAllSubtasksByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return new ArrayList<>(epic.getSubtasks());
        } else {
            System.out.println("Epic с id " + id + " не найден.");
            return new ArrayList<>();
        }
    }
}