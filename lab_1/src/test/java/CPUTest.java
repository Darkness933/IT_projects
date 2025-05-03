import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class CPUTest {

    @Test
    public void testAddTaskToQueue() {
        Queue<Integer> ownQueue = new LinkedList<>();
        Queue<Integer> otherQueue = new LinkedList<>();
        CPU cpu = new CPU("TestCPU", ownQueue, otherQueue);

        cpu.addTask(1);
        cpu.addTask(2);

        assertEquals(2, ownQueue.size(), "Очікується 2 завдання в черзі");
    }

    @Test
    public void testAddTaskQueueOverflow() {
        Queue<Integer> ownQueue = new LinkedList<>();
        Queue<Integer> otherQueue = new LinkedList<>();
        CPU cpu = new CPU("TestCPU", ownQueue, otherQueue);

        // Додаємо більше завдань, ніж MAX_QUEUE_SIZE (5)
        for (int i = 0; i < 7; i++) {
            cpu.addTask(i);
        }

        assertEquals(5, ownQueue.size(), "Черга має містити максимум 5 завдань");
    }
}
