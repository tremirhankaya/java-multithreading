package examples;

public class JoinExample {
    public static void main(String[] args) throws InterruptedException {
        Thread isci = new Thread(() -> {
            System.out.println("İşçi dosya indirmeye başladı...");
            try {
                Thread.sleep(3000); //
            } catch (InterruptedException e) {}
            System.out.println("İşçi dosya indirmeyi BİTİRDİ!");
        });

        isci.start();

        // İşçi bitene kadar main Thread bekler.
        isci.join();

        System.out.println("Ana program (Main) bitti!");
    }
}