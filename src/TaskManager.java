import java.util.HashMap;

public class TaskManager {
    public int count = 0;
    public HashMap<Integer, Task> task = new HashMap<>();
    public HashMap<Integer, Epic> epic = new HashMap<>();
    public HashMap<Integer, Subtask> subtask = new HashMap<>();

    public int setId(int count) {
        count++;
        return count;
    }

}