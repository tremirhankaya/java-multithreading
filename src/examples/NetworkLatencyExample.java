package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NetworkLatencyExample {
    public static void main(String[] args) throws InterruptedException {
        // Örnek URL listesi
        List<String> urls = List.of(
                "service/A", "service/B", "service/C", "service/D", "service/E"
        );

        int simulatedLatencyMs = 200; // gecikme ms

        System.out.println("Başlangıç örnek: " + urls);

        long t0 = System.currentTimeMillis();
        List<String> seq = fetchSequential(urls, simulatedLatencyMs);
        long seqTime = System.currentTimeMillis() - t0;
        System.out.println("Single Thread sonuç : " + seq.get(0));
        System.out.println("Single Thread süre: " + seqTime + " ms\n");

        t0 = System.currentTimeMillis();
        List<String> par = fetchParallelWithJoin(urls, simulatedLatencyMs);
        long parTime = System.currentTimeMillis() - t0;
        System.out.println("MultiThread sonuç : " + par.get(0));
        System.out.println("MultiThread süre: " + parTime + " ms\n");

    }

    // her isteği sırayla yap
    static List<String> fetchSequential(List<String> urls, int latencyMs) {
        List<String> results = new ArrayList<>();
        for (String url : urls) {
            results.add(simulateNetworkCall(url, latencyMs));
        }
        return results;
    }

    // her URL için Thread oluşturup başlatıyoruz, sonra join ile bekliyoruz
    static List<String> fetchParallelWithJoin(List<String> urls, int latencyMs) throws InterruptedException {
        int n = urls.size();

        List<String> results = new ArrayList<>(n);
        for (int i = 0; i < n; i++) results.add(null);


        List<Thread> threads = new ArrayList<>(n);

        // Her URL için bir worker thread
        for (int i = 0; i < n; i++) {
            final int idx = i;
            final String url = urls.get(i);

            Thread t = new Thread(() -> {
                // gecikme simülasyonu
                results.set(idx, simulateNetworkCall(url, latencyMs));
            }, "worker-" + i);

            threads.add(t);
            t.start();
        }

        // Tüm thread'lerin bitmesini bekle (join)
        for (Thread t : threads) {
            t.join();
        }

        // results zaten istenen sırada, doğrudan döndür
        return results;
    }

    static String simulateNetworkCall(String url, int latencyMs) {
        try {
            Thread.sleep(latencyMs); // ağ gecikmesi
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "INTERRUPTED:" + url;
        }
        return "DATA_FROM_" + url;
    }
}
