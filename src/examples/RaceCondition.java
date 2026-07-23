package examples;

public class RaceCondition {
    public static void main(String[] args) {
        InventoryCounter inventoryCounter = new InventoryCounter(); //shared object
        Import importThread = new Import(inventoryCounter);
        Export exportThread = new Export(inventoryCounter);

        importThread.start();
        exportThread.start();

       System.out.println("Final inventory count: " + inventoryCounter.getItems());


    }
    private static class Import extends Thread {
        private InventoryCounter inventoryCounter;
        private Import (InventoryCounter inventoryCounter) {
        this.inventoryCounter = inventoryCounter;
        }
        private int count = 0;

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.increment();
            }
        }

        public int getCount() {
            return count;
        }
    }
    private static class Export extends Thread {
        private InventoryCounter inventoryCounter;
        private Export (InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }
        private int count = 0;

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }

        public int getCount() {
            return count;
        }
    }
    private static class InventoryCounter {
        private int items = 0;

        public void increment() {
            items++;
        }

        public void decrement() {
            items--;
        }

        public int getItems() {
            return items;
        }
    }
}
