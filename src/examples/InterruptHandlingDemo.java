package examples;

/**
 * InterruptHandlingDemo
 *
 * Bu sınıf örneği interrupt mekanizmasını gösterir.
 * Aşağıdaki yorumlar özellikle hangi durumlarda "catch (InterruptedException)" kullanılması
 * gerektiğini (bloklanabilen çağrılar: sleep/wait/join/IO) ve hangi durumlarda
 * "isInterrupted()" ile per-iteration kontrol edilmesi gerektiğini (CPU-bound döngüler)
 * açıkça belirtir.
 */
public class InterruptHandlingDemo {
    public static void main(String[] args) throws InterruptedException {
        // Worker isimli bir thread oluşturuyoruz ve çalıştırıyoruz
        Thread worker = new Thread(new Worker(), "worker");
        worker.start();

        // Bir süre çalışmasına izin ver (1.5s), sonra interrupt isteği gönder
        Thread.sleep(1500);
        System.out.println("Main: interrupting worker");
        worker.interrupt(); // worker'ın interrupt bayrağını true yapar (eğer uyuyorsa InterruptedException atılır)

        // Worker'ın bitmesini bekle
        worker.join();
        // join sonrası worker'ın interrupt bayrağını kontrol et (true/false)
        System.out.println("Main: worker terminated. worker.isInterrupted()=" + worker.isInterrupted());
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            try {
                // 10 adımlık iş döngüsü
                for (int i = 0; i < 10; i++) {
                    // ---------- CPU-bound (aktif) kontrol örneği ----------
                    // Ne zaman kullanılır?
                    // - Eğer kod BLOKLANMIYORSA (ör. ağır hesaplama, tight loop),
                    //   interrupt geldiğinde JVM InterruptedException fırlatmaz; sadece interrupt bayrağını set eder.
                    // - Bu yüzden CPU-bound kodlarda per-iteration isInterrupted() yapılır
                    // yoğun matematiksel hesaplama, veri işleme döngüleri
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Worker: detected interrupt via isInterrupted(), exiting loop");
                        return; // temiz çıkış
                    }

                    System.out.println("Worker: working step " + i);

                    // ---------- Bloklanabilen çağrı örneği (blocking) ----------
                    // Ne zaman catch kullanılır?
                    // - Thread.sleep(), Object.wait(), Thread.join(), veya bloklayıcı I/O çağrıları gibi
                    //   yöntemleri çağırdığında thread bekleme durumuna (blocked) girer.
                    // - Eğer bu sırada başka bir thread interrupt() çağırırsa JVM InterruptedException fırlatır.
                    // - Bu tür çağrıları try-catch(InterruptedException) ile sar ve yakaladığında genelde bayrağı restore et:
                    //       catch (InterruptedException e) { Thread.currentThread().interrupt(); /* cleanup; return; */ }
                    //   Böylece interrupt bilgisini üst katmanlara iletmiş (propagate) olursun.
                    try {
                        // Bu satır BLOKLANABİLİR: interrupt gelirse InterruptedException fırlatılır
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        /*
                         Buraya gelmek demek: thread uyurken interrupt geldi.

                         Önemli noktalar:
                         - someThread.interrupt() çağrıldığında önce bayrak TRUE olur.
                         - Eğer thread bloklanmışsa (sleep/wait/join/IO), JVM burada InterruptedException
                           fırlatır VE exception fırlatılırken JVM otomatik olarak interrupt bayrağını temizler (FALSE yapar).
                         - Eğer metot interrupt'ı üst katmana atamıyorsa (throws InterruptedException değilse),
                           yakaladığımız yerde bayrağı tekrar set ederek (Thread.currentThread().interrupt())
                           interrupt bilgisini üst katmanlara iletmeliyiz (propagate). Aksi halde üst kod
                           bunu göremez (bayrak temizlenmiş olur).

                         Bu catch bloğunda yapılan:
                         1) Bilgilendirme (log/print)
                         2) Thread.currentThread().interrupt() ile bayrağı yeniden TRUE yapma (restore)
                         3) Nazikçe dönme (return) ile temiz kapatma
                        */
                        System.out.println("Worker: sleep interrupted, restoring interrupt flag and exiting");
                        Thread.currentThread().interrupt(); // bayrağı tekrar true yap (restore)
                        return; // nazik sonlandırma
                    }
                    // Eğer sleep kesilmediyse döngü devam eder; bir sonraki iterasyonda isInterrupted() tekrar kontrol edilir.
                }
            } finally {
                // Her durumda çalışacak cleanup bloğu (kaynak serbest bırakma vb.)
                System.out.println("Worker: finally cleanup");
            }
        }
    }

    /* Ek notlar:
       - isInterrupted(): sadece okur, bayrağı temizlemez. (ör: someThread.isInterrupted())
       - Thread.interrupted(): statik, çağıran thread'in bayrağını okur ve temizler.
       - InterruptedException yakalandığında JVM bayrağı temizler; eğer bu bilgiyi korumak istiyorsan
         catch içinde Thread.currentThread().interrupt() ile bayrağı geri koy.
       - Kütüphane/metod yazıyorsan interrupt'ı yutmamalısın: ya throws InterruptedException ile geçir ya da
         bayrağı restore edip temiz bir şekilde çık.
    */
}
