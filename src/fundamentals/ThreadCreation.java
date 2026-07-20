package fundamentals;

public class ThreadCreation {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" is running");
            }
        });
        t1.setName("CustomThread");
        t1.setPriority(Thread.MAX_PRIORITY);
        System.out.println(Thread.currentThread().getName());
        t1.start();
        System.out.println(Thread.currentThread().getName());
    }
}
