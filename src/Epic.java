import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks;

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatus();
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        updateStatus();
    }

    public void clearSubtasks() {
        subtasks.clear();
        updateStatus();
    }

    public void updateStatus() {
        if (subtasks.isEmpty()) {
            status = Status.NEW;
        } else {
            boolean allNew = true;
            boolean allDone = true;

            for (Subtask subtask : subtasks) {
                if (subtask.getStatus() != Status.NEW) {
                    allNew = false;
                }
                if (subtask.getStatus() != Status.DONE) {
                    allDone = false;
                }
            }

            if (allNew) {
                status = Status.NEW;
            } else if (allDone) {
                status = Status.DONE;
            } else {
                status = Status.IN_PROGRESS;
            }
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", subtasks=" + subtasks +
                '}';
    }
}