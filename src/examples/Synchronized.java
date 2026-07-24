package examples;

public class Synchronized{

    // Özel kilitler
    private final Object kitchenLock = new Object();
    private final Object bathroomLock = new Object();


    //1.this ile kilirleme
    public void cleanKitchenBad() {
        synchronized(this) {
            // Çalışırken banyoya kimse giremz
        }
    }

    public void cleanBathroomBad() {
        synchronized(this) {
            // Mutfak temizlenene kadar bu kod beklemek zorunda
        }
    }

    //2.object bazlı kilit. En çok nu tercih edilir


    public void cleanKitchenGood() {
        synchronized(kitchenLock) {
            //  çalışırken mutfağa yeni erişimi engeller
        }
    }

    public void cleanBathroomGood() {
        synchronized(bathroomLock) {
            // Mutfak temizlenirken burası da çalışır.
        }
    }



    //3. direkt method üzerinden kilit
    public synchronized void cleanBathroom(){
        // iş kodu
    }
    //iki koddan biris çalışırken diğer kod çalışamaz. Bu yüzden bu yöntem tavsiye edilmez
    public synchronized void cleanKitchen(){
        // iş kodu
    }
}
