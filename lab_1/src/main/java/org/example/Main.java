import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class CPU extends Thread {
    private Queue<Integer> ownQueue;     // Черга завдань цього CPU
    private Queue<Integer> otherQueue;   // Черга завдань іншого CPU
    private String cpuName;              // Назва CPU
    private static final int MAX_QUEUE_SIZE = 5; // Максимальний розмір черги
    private int maxQueueSize = 0;        // Максимальний досягнутий розмір черги

    CPU(String name, Queue<Integer> ownQueue, Queue<Integer> otherQueue) {
        this.cpuName = name;
        this.ownQueue = ownQueue;
        this.otherQueue = otherQueue;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
//            synchronized (ownQueue) {
            if (!ownQueue.isEmpty()) {
                // Обробляємо завдання з власної черги
                int task = ownQueue.poll();
                System.out.println(cpuName + " обробляє власне завдання: " + task);
            } else {
                // Якщо власна черга порожня, перевіряємо чергу іншого CPU
//                    synchronized (otherQueue) {
                if (!otherQueue.isEmpty()) {
                    int task = otherQueue.poll();
                    System.out.println(cpuName + " обробляє чужий процес: " + task);
                } else {
                    System.out.println(cpuName + " чекає завдання...");
                }
//                    }
            }
//            }

            try {
                Thread.sleep(random.nextInt(200) + 100); // Імітація часу обробки
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Метод для додавання завдань в чергу
    public void addTask(int task) {
        synchronized (ownQueue) {
            if (ownQueue.size() < MAX_QUEUE_SIZE) {
                ownQueue.offer(task);
                System.out.println("Завдання " + task + " додано до черги " + cpuName);
                // Оновлення максимального розміру черги
                if (ownQueue.size() > maxQueueSize) {
                    maxQueueSize = ownQueue.size();
                }
            } else {
                System.out.println("Черга " + cpuName + " повна, завдання " + task + " не додано");
            }
        }
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }
}

public class Main {
    public static void main(String[] args) {
        Queue<Integer> queue1 = new LinkedList<>(); // Черга для CPU1
        Queue<Integer> queue2 = new LinkedList<>(); // Черга для CPU2

        CPU cpu1 = new CPU("CPU1", queue1, queue2);
        CPU cpu2 = new CPU("CPU2", queue2, queue1);

        cpu1.start();
        cpu2.start();

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            // Додаємо завдання у випадкову чергу
            int task = i + 1;
            if (random.nextBoolean()) {
                cpu1.addTask(task);
            } else {
                cpu2.addTask(task);
            }

            try {
                Thread.sleep(150); // Імітація часу між надходженням завдань
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Припиняємо роботу
        try {
            Thread.sleep(3000); // Чекаємо, поки всі завдання будуть оброблені
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        cpu1.interrupt();
        cpu2.interrupt();

        // Виведення максимального розміру черги для кожного CPU
        System.out.println("Максимальний розмір черги CPU1: " + cpu1.getMaxQueueSize());
        System.out.println("Максимальний розмір черги CPU2: " + cpu2.getMaxQueueSize());
    }
    // Код для другого коміту
}
