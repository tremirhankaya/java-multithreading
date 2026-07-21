package examples;

import java.util.List;
// Multi executor example
public class Example2 {

    private final List<Runnable> tasks;


    public Example2(List<Runnable> tasks) {
        this.tasks = tasks;
    }
    public void executeTasks() {
        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            thread.start();
        }

}
}
