# Producer-Consumer Problem in Java

The **Producer-Consumer Problem** is a classic multithreading synchronization problem.

- A **Producer** thread produces data/items and puts them into a shared queue.
- A **Consumer** thread removes and consumes items from the queue.
- The queue has a **limited size** (buffer size).

---

## Problem Conditions

| Thread | Waits When | Reason |
|--------|-----------|--------|
| **Producer** | Queue is **full** | No more items can be inserted |
| **Consumer** | Queue is **empty** | Nothing to consume |

Java provides:

| Method | Description |
|--------|-------------|
| `wait()` | Pauses the thread |
| `notify()` | Wakes one waiting thread |
| `notifyAll()` | Wakes all waiting threads |

> These methods are available in the **`Object` class**.

---

## Real-Life Example

> 🍽️ **Producer** = Chef preparing food
> 🧑 **Consumer** = Customer eating food
> 🪑 **Queue** = Table with limited plates

- If the table is **full** → Chef **waits**
- If the table is **empty** → Customer **waits**

---

## Important Concepts

### 1. Shared Resource (Queue)

Both producer and consumer access the **same queue**.

```java
Queue<Integer> queue = new LinkedList<>();
```

### 2. Synchronization

Both threads should **not** modify the queue at the same time.

```java
synchronized
```

### 3. `wait()`

```java
wait();
```

- Releases the lock
- Thread goes into **waiting state**
- Must be called inside a `synchronized` block/method

### 4. `notify()`

```java
notify();
```

- Wakes **one** waiting thread

---

## Complete Java Program

```java
import java.util.LinkedList;
import java.util.Queue;

class SharedBuffer {

    private Queue<Integer> queue = new LinkedList<>();
    private int capacity = 5;

    // Producer method
    public synchronized void produce(int item) throws InterruptedException {

        // wait if queue is full
        while (queue.size() == capacity) {
            System.out.println("Queue is Full. Producer is waiting...");
            wait();
        }

        // add item
        queue.add(item);
        System.out.println("Produced: " + item);

        // notify consumer
        notify();
    }

    // Consumer method
    public synchronized void consume() throws InterruptedException {

        // wait if queue is empty
        while (queue.isEmpty()) {
            System.out.println("Queue is Empty. Consumer is waiting...");
            wait();
        }

        // remove item
        int item = queue.remove();
        System.out.println("Consumed: " + item);

        // notify producer
        notify();
    }
}

class Producer extends Thread {

    private SharedBuffer buffer;

    Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {

        int value = 1;

        while (true) {
            try {
                buffer.produce(value++);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {

    private SharedBuffer buffer;

    Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {

        while (true) {
            try {
                buffer.consume();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {

        SharedBuffer buffer = new SharedBuffer();

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();
    }
}
```

---

## Step-by-Step Working

| Step | Action | Description |
|------|--------|-------------|
| 1 | Shared queue created | `Queue<Integer> queue = new LinkedList<>()` — shared between both threads |
| 2 | Producer inserts items | `queue.add(item)` — adds to queue |
| 3 | Queue becomes full | `queue.size() == capacity` → producer calls `wait()` |
| 4 | Consumer removes item | `queue.remove()` — consumes item |
| 5 | Consumer calls `notify()` | Producer wakes up and resumes producing |

---

## Why `while` Instead of `if`?

```java
// ✅ Correct
while (queue.size() == capacity) {
    wait();
}

// ❌ Wrong
if (queue.size() == capacity) {
    wait();
}
```

After waking up, **another thread may have already changed the condition**, so the condition must be re-checked.

> This is called **Spurious Wakeup Handling**.

---

## Flow Diagram

```
Producer Thread                     Consumer Thread
      |                                    |
      v                                    v
Check Queue Full?               Check Queue Empty?
      |                                    |
   Yes → wait()                        Yes → wait()
      |                                    |
      No                                   No
      |                                    |
  Add Item                           Remove Item
      |                                    |
  notify()                            notify()
```

---

## Output Example

```
Produced: 1
Consumed: 1
Produced: 2
Produced: 3
Consumed: 2
Produced: 4
Produced: 5
Queue is Full. Producer is waiting...
Consumed: 3
Produced: 6
```

---

## Advantages & Disadvantages

| Advantages | Disadvantages |
|-----------|--------------|
| Prevents race conditions | Complex for large systems |
| Efficient thread communication | Risk of deadlock if synchronization is wrong |
| Proper synchronization | — |
| Avoids busy waiting | — |

---

## Modern Alternative

Instead of manually using `wait()` and `notify()`, Java provides:

> **`BlockingQueue`** — handles synchronization internally.

```java
BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

// Producer
queue.put(item);    // waits if full

// Consumer
queue.take();       // waits if empty
```

---

## Interview Questions

### Q1. Why are `wait()`, `notify()`, `notifyAll()` in the `Object` class?

> Because **every object can act as a monitor (lock)**. Since locks belong to objects, these methods must be in the `Object` class.

### Q2. Difference between `sleep()` and `wait()`?

| Feature | `sleep()` | `wait()` |
|---------|----------|---------|
| Belongs To | `Thread` class | `Object` class |
| Releases Lock | ❌ No | ✅ Yes |
| Used For | Adding delay | Inter-thread communication |

### Q3. Why is `synchronized` needed?

> To avoid **multiple threads modifying shared data simultaneously** — only one thread can access the critical section at a time.

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Producer** | Adds items; waits when queue is full |
| **Consumer** | Removes items; waits when queue is empty |
| **`wait()`** | Releases lock, thread sleeps until notified |
| **`notify()`** | Wakes one waiting thread |
| **`synchronized`** | Ensures mutual exclusion on shared resource |
| **`while` loop** | Handles spurious wakeups by re-checking condition |

---

## 🎯 Short Definition for Exams

> **Producer-Consumer Problem** is a synchronization problem where a producer thread adds data into a shared buffer and a consumer thread removes data from it. The producer waits when the buffer is **full** and the consumer waits when the buffer is **empty**, using `wait()` and `notify()` methods.
