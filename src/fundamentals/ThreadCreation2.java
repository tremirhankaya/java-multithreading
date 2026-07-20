package fundamentals;

public class ThreadCreation2 {
    public static void main(String[] args) throws InterruptedException {
        newThread t = new newThread();// Thread extend eden class.
        t.start();

        Thread t1 = new Thread(new newThread2()); //Burada Thread içine Runnable implement eden sınıf verilir.
        t1.start();

    }
    public static class newThread extends Thread{ //Extend edilerek thread oluşturabilir.
        @Override
        public void run() {
            System.out.println("New way to create a new thread (extend)");
        }
    }

    public static class newThread2 implements Runnable{ //Runnable interface implement edilerek thread oluşturulabilir.
        @Override
        public void run() {
            System.out.println("New way to create a new thread(implements)");
        }
    }
}
