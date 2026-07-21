package examples;

import java.util.List;
// Multi executor example
public class MultiTaskExecutorExample {

    private final List<Runnable> tasks;


    public MultiTaskExecutorExample(List<Runnable> tasks) {
        this.tasks = tasks;
    }
    public void executeTasks() {
        for (Runnable task : tasks) {
            Thread thread = new Thread(task);
            thread.start();
        }

}
}
