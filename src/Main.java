public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager manager = new TaskManager();

        // Создание задач
        manager.addTask("Переезд", "Собрать коробки и упаковать вещи", Status.NEW);

        // Создание эпиков и подзадач
        manager.addEpic("Важный эпик 1", "Завершить проект");
        manager.addSubtask("Подготовка к задаче", "Собрать команду", Status.IN_PROGRESS, 2);
        manager.addSubtask("Разработка", "Начать работу над проектом", Status.NEW, 2);

        // Проверка работы менеджера
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());

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
}