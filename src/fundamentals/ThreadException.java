package fundamentals;

public class ThreadException {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
           throw new RuntimeException("unexpected exception");
        });
        t1.setUncaughtExceptionHandler((thread, exception) -> {
            System.out.println("Exception in thread " + thread.getName() + ": " + exception.getMessage());
        });
        //program içinde yakalanmayan hataları thread yakalar.
        t1.start();
    }
}
