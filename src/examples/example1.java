package examples;

import java.util.List;
import java.util.Random;

public class example1 {
    public static void main(String[] args) {
        Random rand = new Random();
        Vault vault= new Vault(String.valueOf(rand.nextInt(9999)));
        List<Thread> threads = List.of(
                new AscendingHackerThread(vault),
                new DescendingHackerThread(vault),
                new PoliceThread()
        );
        for (Thread t : threads) {
            t.start();
        }
    }
    public static class Vault{
        private String password;
        public Vault(String password){
            this.password = password;
        }
        public boolean checkPassword(String password){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.password.equals(password);
        }
    }
    public static abstract class HackerThread extends Thread{
        protected Vault vault;
        public HackerThread(Vault vault){
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
        }
        @Override
        public void start(){
            System.out.println("Starting hacker thread");
            super.start();
        }
    }

    private static class AscendingHackerThread extends HackerThread{
        public AscendingHackerThread(Vault vault){
            super(vault);
        }
        @Override
        public void run() {
            for(int i=0;i<9999;i++){
                if(vault.checkPassword(String.valueOf(i))){
                    System.out.println(this.getName()+" guessed the password "+i);
                    System.exit(0);
                }
            }
        }
    }
    private static class DescendingHackerThread extends HackerThread{
        public DescendingHackerThread(Vault vault){
            super(vault);
        }
        @Override
        public void run() {
            for(int i=9999;i>=0;i--){
                if(vault.checkPassword(String.valueOf(i))){
                    System.out.println(this.getName()+" guessed the password "+i);
                    System.exit(0);
                }
            }
        }

    }
    private static class PoliceThread extends Thread{
      @Override
        public void run() {
          for(int i=10;i>=0;i--){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
          }
          System.out.println("Game over!");
      }

    }
}

