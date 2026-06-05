# Inter-Thread Communication in Java

This is one of the **MOST IMPORTANT** and **MOST CONFUSING** topics in Java multithreading.

Many students understand thread creation, synchronization, and sleep/join — but struggle with `wait()`, `notify()`, and `notifyAll()` because this topic involves **communication between threads**.

---

## 1. What is Inter-Thread Communication?

### Definition

> Inter-thread communication means **threads communicating and coordinating with each other**.

### Why is it Needed?

Sometimes one thread **produces data** and another thread **consumes that data**.

The consumer thread should **wait until data becomes available** — instead of **checking continuously**.

### Main Goal

> **Avoid unnecessary waiting and CPU wastage.**

---

## 2. Real-Life Analogy — Post Box

> ⭐ This analogy is **VERY IMPORTANT**.

### Scenario Without Communication (Bad Approach)

Suppose you are waiting for a letter.

Every 10 minutes you check the post box:

```
Check... No letter
Check... No letter
Check... No letter
```

This wastes time and energy.

Similarly in programming — **continuous checking wastes CPU**.

This is called: **Busy Waiting / Polling**

### Better Approach

Instead:
- Sleep peacefully
- Postman **notifies** you when the letter arrives

No unnecessary checking. Efficient.

### Mapping to Java

| Real Life              | Java Equivalent                     |
|------------------------|-------------------------------------|
| You waiting            | Thread calling `wait()`             |
| Postman arrives        | Another thread calls `notify()`     |
| You wake up and act    | Waiting thread resumes execution    |

> **Thread wakes only when needed → Efficient CPU utilization.**

---

## 3. Methods Used for Communication

Java provides **3 important methods**:

| Method         | Purpose                          |
|----------------|----------------------------------|
| `wait()`       | Makes the current thread wait    |
| `notify()`     | Wakes one waiting thread         |
| `notifyAll()`  | Wakes all waiting threads        |

### ⭐ VERY IMPORTANT

These methods belong to the **`Object` class** — **NOT** the `Thread` class.

---

## 4. Why `wait()`/`notify()` Are in the `Object` Class?

> ⭐ Very famous interview question.

### Wrong Thinking

> *"Threads are communicating, so methods should be in the Thread class."*

### Correct Logic

Communication happens based on the **shared object/resource** — NOT merely on threads.

**Example:**
- Producer thread updates a `Queue` object
- Consumer thread waits on the same `Queue` object

Communication is tied to the **shared object's state**.

Therefore, the methods are placed in the **`Object` class**.

### Important Understanding

A thread can call `obj.wait()` on **ANY object**.

So every object must support wait/notify → Hence methods are in `Object` class.

---

## 5. `wait()` Method

### Definition

> `wait()` causes the current thread to **release the object's lock** and enter the **waiting state** until notified.

### Syntax

```java
public final void wait() throws InterruptedException

public final void wait(long ms) throws InterruptedException
```

### Internal Behavior When `obj.wait()` is Called

| Step   | What Happens                         |
|--------|--------------------------------------|
| Step 1 | Thread **releases** the object lock  |
| Step 2 | Thread enters **WAITING** state      |
| Step 3 | Thread wakes only when notified      |

### ⭐ VERY IMPORTANT

> **`wait()` releases the lock immediately.**

This is one of the **MOST IMPORTANT** concepts and a top interview question.

---

## 6. `notify()` Method

### Definition

> `notify()` **wakes up one waiting thread** on the same object.

### Syntax

```java
public final native void notify()
```

### Important Point

Suppose 5 threads are waiting — `notify()` wakes **only ONE** thread, chosen by the JVM scheduler.

### ⭐ `notify()` Does NOT Immediately Transfer the Lock

Students commonly misunderstand this.

**What actually happens:**
- The waiting thread becomes **eligible**
- BUT it **cannot continue immediately**
- Because the notifying thread may still hold the lock

The waiting thread must **reacquire the lock** before it can proceed.

---

## 7. `notifyAll()` Method

### Definition

> `notifyAll()` **wakes up all waiting threads** on the same object.

### Syntax

```java
public final native void notifyAll()
```

### What Happens?

- All waiting threads become **eligible**
- They all **compete again** for the lock
- But **only one** gets the lock at a time

### Difference

| Method         | Wakes              |
|----------------|--------------------|
| `notify()`     | **One** thread     |
| `notifyAll()`  | **All** threads    |

---

## 8. ⭐ MOST IMPORTANT RULE

> **`wait()`, `notify()`, `notifyAll()` MUST be called inside a `synchronized` area.**

Otherwise: **`IllegalMonitorStateException`** is thrown.

### ❌ Wrong — Outside `synchronized`

```java
class Test {
    public static void main(String[] args) throws Exception {
        Test t = new Test();
        t.wait(); // ❌ ERROR — thread does not own the lock
    }
}
```

### ✅ Correct — Inside `synchronized`

```java
synchronized (obj) {
    obj.wait(); // ✅ thread owns obj's lock
}
```

---

## 9. Ownership Rule

A thread **must own the object's lock** before calling `wait()`, `notify()`, or `notifyAll()`.

This means the thread must be inside `synchronized(obj)` for the **same object**.

### ✅ Correct

```java
synchronized (obj) {
    obj.wait(); // thread owns obj's lock ✅
}
```

### ❌ Wrong

```java
synchronized (this) {
    obj.wait(); // ❌ thread owns 'this' lock, NOT 'obj' lock
}
```

---

## 10. ⭐ Important Lock Behavior Comparison

> This is an **EXTREMELY IMPORTANT** interview topic.

| Method     | Releases Lock? |
|------------|----------------|
| `wait()`   | ✅ **YES** — releases immediately |
| `sleep()`  | ❌ NO           |
| `join()`   | ❌ NO           |
| `yield()`  | ❌ NO           |

> **ONLY `wait()` releases the object's lock.**

---

## 11. Thread State Transition

### When `wait()` is Called

```
RUNNING  →  WAITING
```

### When `notify()` is Called

> ⚠️ The waiting thread does **NOT** directly become RUNNING.

**Actual Flow:**

```
WAITING
   ↓
Waiting to Reacquire Lock
   ↓
RUNNABLE
   ↓
RUNNING
```

### Why?

Because the thread must **reacquire the lock** first.

### Example Flow

1. Thread-1 calls `wait()` → releases lock → enters WAITING
2. Thread-2 calls `notify()` → Thread-1 awakened
3. But Thread-2 **still holds the lock** inside its synchronized block
4. Thread-1 waits again for the lock
5. Thread-2 exits synchronized block → lock released
6. Thread-1 reacquires lock → continues execution

---

## 12. Real-Life Example: Food Delivery Restaurant

> ⭐ The **BEST** way to understand inter-thread communication.

### Setup

| Person  | Thread Type      |
|---------|------------------|
| Chef    | Producer Thread  |
| Waiter  | Consumer Thread  |

### ❌ Wrong Approach (Without wait/notify — Busy Waiting)

Waiter continuously enters kitchen every second:

```
"Food ready?"
"Food ready?"
"Food ready?"
```

This wastes time and energy → **Busy Waiting**

### ✅ Better Approach (Using wait/notify)

Waiter says:
> *"Chef, I will wait. Notify me when food is ready."*

Waiter **sleeps peacefully** until notified.

---

### Lock Analogy — Kitchen Key

| Real Life             | Java Concept               |
|-----------------------|----------------------------|
| Restaurant/Kitchen    | Shared Object              |
| Kitchen key           | Object Lock                |
| Chef                  | Producer Thread            |
| Waiter                | Consumer Thread            |
| Food                  | Shared Data                |
| Waiter waits for food | `wait()`                   |
| Chef calls waiter     | `notify()`                 |

**Waiter enters kitchen → takes key → acquires object lock**

Waiter calls `wait()`:
- **Returns kitchen key** (releases lock)
- Goes to waiting room (enters WAITING state)

**Why release the key?**

> Because chef also needs kitchen access.
> If waiter kept the lock, chef cannot enter → food never prepared → deadlock-like situation.
> So `wait()` releases lock **automatically**.

**Chef enters kitchen → acquires lock → prepares food → calls `notify()`**

Waiter wakes up — **BUT still cannot continue immediately** because chef still inside kitchen (still holds lock).

Only after chef exits → lock released → waiter reacquires lock → continues serving.

---

### Java Code Example (Producer-Consumer Style)

```java
class Restaurant {

    boolean foodReady = false;

    synchronized void waiter() {
        try {
            while (!foodReady) {
                System.out.println("Waiter waiting for food...");
                wait();
            }
            System.out.println("Waiter serving food");
        } catch (InterruptedException e) { }
    }

    synchronized void chef() {
        System.out.println("Chef preparing food...");
        foodReady = true;
        notify();
        System.out.println("Chef notified waiter");
    }
}

public class Test {

    public static void main(String[] args) {

        Restaurant r = new Restaurant();

        Thread waiterThread = new Thread(() -> r.waiter());
        Thread chefThread = new Thread(() -> r.chef());

        waiterThread.start();

        try {
            Thread.sleep(2000);
        } catch (Exception e) { }

        chefThread.start();
    }
}
```

### Output

```
Waiter waiting for food...
Chef preparing food...
Chef notified waiter
Waiter serving food
```

---

### Step-by-Step Internal Flow

| Step | Action                                                              |
|------|---------------------------------------------------------------------|
| 1    | Waiter thread enters `waiter()` → acquires `Restaurant` object lock |
| 2    | `foodReady` is false → calls `wait()` → releases lock → WAITING    |
| 3    | Chef thread enters `chef()` → acquires same object lock             |
| 4    | Chef sets `foodReady = true`                                        |
| 5    | Chef calls `notify()` → waiter awakened but cannot proceed yet      |
| 6    | Chef exits synchronized method → lock released                      |
| 7    | Waiter reacquires lock → continues → prints "Waiter serving food"  |

---

## 13. Producer-Consumer Concept

Inter-thread communication is heavily used in the **Producer-Consumer pattern**.

| Role     | Responsibility                         |
|----------|----------------------------------------|
| Producer | Produces/generates data                |
| Consumer | Consumes/uses the data                 |

**Problem:**
- Consumer should **wait** until data is available
- Producer should **notify** after data is produced

### Simple Structure

```java
synchronized (obj) {

    while (conditionNotSatisfied) {
        obj.wait(); // wait for notification
    }

    // do work

    obj.notify(); // signal other thread
}
```

> Using `while` (not `if`) is a best practice to guard against **spurious wakeups**.

---

## 14. Method Prototypes Summary

```java
// wait methods
public final void wait() throws InterruptedException
public final void wait(long ms) throws InterruptedException
public final void wait(long ms, int ns) throws InterruptedException

// notify methods
public final native void notify()
public final native void notifyAll()
```

> `wait()` throws `InterruptedException` → **try-catch is required**.

---

## 15. ⭐ Important Interview Questions

### Q1. Why are `wait()`/`notify()` in the `Object` class?

> Because communication is tied to the **shared object/resource**, not just the threads. Every object can be a shared resource, so every object must support wait/notify.

### Q2. Can `wait()` be called outside a `synchronized` block?

> **No.** `IllegalMonitorStateException` is thrown.

### Q3. Which method releases the lock immediately?

> **`wait()`** — the only method that releases the object lock.

### Q4. Does `notify()` release the lock immediately?

> **No.** The lock is released only after the synchronized block ends.

### Q5. Difference between `sleep()` and `wait()`?

| Feature          | `sleep()`                        | `wait()`                            |
|------------------|----------------------------------|-------------------------------------|
| Class            | `Thread`                         | `Object`                            |
| Method type      | `static`                         | Instance                            |
| Releases lock    | ❌ No                             | ✅ Yes                               |
| Purpose          | Time-based pause                 | Communication-based waiting         |
| Requires sync    | ❌ No                             | ✅ Yes (must be in synchronized)     |

### Q6. Difference between `notify()` and `notifyAll()`?

| Feature          | `notify()`          | `notifyAll()`            |
|------------------|---------------------|--------------------------|
| Wakes            | **One** thread      | **All** waiting threads  |
| Lock competition | Only one reacquires | All compete for lock     |

---

## 16. Final Revision Table

| Method         | Purpose               | Releases Lock?        | Requires `synchronized`? |
|----------------|-----------------------|-----------------------|--------------------------|
| `wait()`       | Wait for notification | ✅ YES                 | ✅ YES                    |
| `notify()`     | Wake one thread       | ❌ Not immediately     | ✅ YES                    |
| `notifyAll()`  | Wake all threads      | ❌ Not immediately     | ✅ YES                    |
| `sleep()`      | Pause for time        | ❌ NO                  | ❌ No                     |

---

## One-Line Interview Definitions

**`wait()`**
> `wait()` causes the current thread to release the object's lock and wait until another thread calls `notify()` or `notifyAll()` on the same object.

**`notify()`**
> `notify()` wakes up one thread that is waiting on the same object's lock.

**`notifyAll()`**
> `notifyAll()` wakes up all threads that are waiting on the same object's lock.

**Inter-Thread Communication**
> A mechanism where threads coordinate with each other using `wait()`, `notify()`, and `notifyAll()` to avoid busy waiting and improve CPU efficiency.

---

## Revision Checklist

- [ ] Do I know why `wait()`/`notify()` are in the `Object` class and not `Thread` class?
- [ ] Can I explain what happens internally when `wait()` is called?
- [ ] Do I know that `wait()` releases the lock but `sleep()` does NOT?
- [ ] Do I understand why `wait()`/`notify()` must be inside a `synchronized` block?
- [ ] Can I explain the ownership rule — calling `wait()` on a different object than the one locked?
- [ ] Do I know the difference between `notify()` and `notifyAll()`?
- [ ] Can I trace the step-by-step flow of the restaurant (Producer-Consumer) example?
- [ ] Do I understand that a notified thread must reacquire the lock before continuing?
- [ ] Can I explain why `while` is preferred over `if` when checking the condition before `wait()`?

---

---

# Inter-Thread Communication — Part 2

This part moves from theory → **real practical coordination**.

Topics covered:
- How threads actually cooperate
- Why `wait()` and `notify()` are better than `sleep()`
- Difference between `notify()` and `notifyAll()`
- Common mistakes asked in interviews
- Producer-Consumer problem with Queue

---

## 1. Recall the 5 Most Important Rules

| Rule | Description |
|------|-------------|
| 1 | Threads communicate using `wait()`, `notify()`, `notifyAll()` |
| 2 | These methods belong to `Object` class — NOT `Thread` class |
| 3 | Must be called inside `synchronized` area — else `IllegalMonitorStateException` |
| 4 | `wait()` releases lock immediately and enters waiting state |
| 5 | `sleep()`, `yield()`, `join()` do **NOT** release the object lock |

---

## 2. Main Practical Problem

**Scenario:**
- Child thread calculates `1 + 2 + 3 + ... + 100`
- Main thread needs the final result

**How should the main thread wait?**

---

### ❌ Wrong Approach 1 — Using `sleep()`

```java
Thread.sleep(5000);
System.out.println(total);
```

**Problems:**
- If calculation completes in 1 second → 4 seconds **wasted**
- If calculation takes 10 seconds → main thread wakes too early → **incomplete result**

> **`sleep()` is unreliable for thread communication.**

---

### ❌ Wrong Approach 2 — Using `join()`

```java
t.join();
```

**Problem:**
- Main thread waits until child thread **completely finishes**
- Child may have unnecessary extra work after calculation
- Main thread cannot proceed earlier with just the result

---

### ✅ Better Solution — Use `wait()` and `notify()`

---

## 3. Practical Example — Calculation Thread

### Goal

- Child thread calculates total
- Main thread waits for result using `wait()`
- Child thread signals using `notify()`

### Code

```java
class Total extends Thread {

    int total = 0;

    public void run() {
        synchronized (this) {
            for (int i = 1; i <= 100; i++) {
                total = total + i;
            }
            notify();
        }
    }
}

public class Test {

    public static void main(String[] args) throws InterruptedException {

        Total t = new Total();
        t.start();

        synchronized (t) {
            System.out.println("Main thread waiting...");
            t.wait();
            System.out.println("Total = " + t.total);
        }
    }
}
```

### Output

```
Main thread waiting...
Total = 5050
```

---

### Step-by-Step Internal Flow

| Step | Action |
|------|--------|
| 1 | Main thread creates `Total t` |
| 2 | Child thread starts via `t.start()` |
| 3 | Main thread enters `synchronized(t)` → acquires lock of object `t` |
| 4 | Main thread calls `t.wait()` → **releases lock** → enters WAITING state |
| 5 | Child thread enters `synchronized(this)` (same as `t`) → acquires lock |
| 6 | Child thread calculates total |
| 7 | Child thread calls `notify()` → main thread awakened — but **cannot continue yet** |
| 8 | Child thread exits synchronized block → **lock released** |
| 9 | Main thread **reacquires lock** → prints `Total = 5050` |

### ⭐ Most Important Concept

> **Notification alone is NOT enough.**
> The waiting thread must **reacquire the lock** before it can continue.

---

## 4. Why `wait(timeout)` Exists

If `wait()` is called but **no thread ever calls `notify()`**, the thread waits **forever**.

**Solution:**

```java
wait(5000); // wait at most 5 seconds
```

After timeout, the thread **wakes automatically** — even without notification.

> **Benefit:** Avoids infinite waiting / thread starvation.

---

## 5. `notify()` vs `notifyAll()`

> ⭐ **VERY IMPORTANT interview question.**

### `notify()`

```java
notify();
```

- Wakes **ONE** waiting thread
- Which thread? → **JVM decides** (unpredictable)

### `notifyAll()`

```java
notifyAll();
```

- Wakes **ALL** waiting threads
- But they all compete for the lock → **only one gets it at a time**

### ⚠️ Important Point About `notifyAll()`

```
WAITING
   ↓
notifyAll()
   ↓
All threads move to BLOCKED (waiting for lock)
   ↓
One thread gets lock → executes
   ↓
Next thread gets lock → executes
```

**Analogy:**

Suppose 50 students are waiting outside a classroom.

Teacher says: *"All come in!"*

But the door is small → students enter **one by one**.

Similarly, all notified threads compete but only one acquires the lock at a time.

### When to Use `notifyAll()`

Use when:
- Multiple threads are waiting
- Different conditions exist
- Safer in complex systems where you're unsure which thread to wake

---

## 6. ⭐ Common Interview Pitfall — Wrong Object

### ❌ Wrong Code

```java
synchronized (s1) {
    s2.wait(); // ❌ ERROR
}
```

**Why wrong?**

Current thread owns `s1`'s lock — NOT `s2`'s lock.

So `s2.wait()` throws: **`IllegalMonitorStateException`**

### ✅ Correct Code

```java
synchronized (s1) {
    s1.wait(); // ✅ correct — same object
}
```

### Golden Rule

> **Always call `wait()`/`notify()` on the SAME object whose lock you own.**

---

## 7. Producer-Consumer Problem with Queue

> ⭐ This is a **CLASSIC interview problem**.

### Problem Understanding

| Thread   | Responsibility               |
|----------|------------------------------|
| Producer | Adds items into the queue    |
| Consumer | Removes items from the queue |

**Queue is the shared resource.**

| Situation    | Action                              |
|--------------|-------------------------------------|
| Queue full   | Producer calls `wait()`             |
| Queue empty  | Consumer calls `wait()`             |
| Item added   | Producer calls `notify()`           |
| Item removed | Consumer calls `notify()`           |

---

### Real-Life Analogy

| Real Life            | Java Concept     |
|----------------------|------------------|
| Chef                 | Producer thread  |
| Waiter               | Consumer thread  |
| Food counter         | Queue            |
| Counter full → chef waits  | `wait()` in producer |
| Counter empty → waiter waits | `wait()` in consumer |

---

### Complete Java Implementation

#### Shared Queue Class

```java
import java.util.LinkedList;
import java.util.Queue;

class SharedQueue {

    private Queue<Integer> queue = new LinkedList<>();
    private int capacity = 5;

    // Producer Method
    public synchronized void produce(int value) throws InterruptedException {

        while (queue.size() == capacity) {
            System.out.println("Queue Full -> Producer waiting...");
            wait();
        }

        queue.add(value);
        System.out.println("Produced : " + value);
        notify();
    }

    // Consumer Method
    public synchronized void consume() throws InterruptedException {

        while (queue.isEmpty()) {
            System.out.println("Queue Empty -> Consumer waiting...");
            wait();
        }

        int value = queue.remove();
        System.out.println("Consumed : " + value);
        notify();
    }
}
```

#### Producer Thread

```java
class Producer extends Thread {

    private SharedQueue sq;

    Producer(SharedQueue sq) {
        this.sq = sq;
    }

    public void run() {
        int value = 1;
        while (true) {
            try {
                sq.produce(value++);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

#### Consumer Thread

```java
class Consumer extends Thread {

    private SharedQueue sq;

    Consumer(SharedQueue sq) {
        this.sq = sq;
    }

    public void run() {
        while (true) {
            try {
                sq.consume();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

#### Main Class

```java
public class ProducerConsumerDemo {

    public static void main(String[] args) {

        SharedQueue sq = new SharedQueue();

        Producer p = new Producer(sq);
        Consumer c = new Consumer(sq);

        p.start();
        c.start();
    }
}
```

### Sample Output

```
Produced : 1
Consumed : 1
Produced : 2
Produced : 3
Consumed : 2
Produced : 4
Produced : 5
Queue Full -> Producer waiting...
Consumed : 3
Produced : 6
```

---

### Step-by-Step Internal Flow

| Step | Action |
|------|--------|
| 1 | Producer enters `produce()` → acquires queue lock → adds item |
| 2 | Producer calls `notify()` → signals consumer if waiting |
| 3 | Consumer enters `consume()` → removes item → calls `notify()` |
| 4 | Queue reaches capacity (5) → producer calls `wait()` → **releases lock** → WAITING |
| 5 | Consumer consumes item → calls `notify()` → producer wakes up |
| 6 | Producer reacquires lock → adds next item |

### ⭐ Most Important Concept

> When producer calls `wait()`, it **releases the lock immediately**.
>
> Without this, the consumer could **never enter** the queue methods → **deadlock**.

---

### ⭐ Why `while` Instead of `if`?

> This is a **VERY IMPORTANT interview question.**

### ❌ Wrong

```java
if (queue.isEmpty()) {
    wait();
}
```

### ✅ Correct

```java
while (queue.isEmpty()) {
    wait();
}
```

**Why?**

After waking up, the condition **may still be invalid** due to:
- Another thread consuming the item before this thread gets the lock
- **Spurious wakeups** (JVM may wake a thread without `notify()`)

> Always **recheck** the condition after waking up → use `while`.

---

### Interview Questions on Producer-Consumer

| Question | Answer |
|----------|--------|
| Why use `wait()`/`notify()` here? | To avoid busy waiting and coordinate producer and consumer |
| Why are queue methods synchronized? | Queue is a shared mutable resource |
| Why `wait()` inside `while` loop? | To recheck condition after wakeup (spurious wakeups) |
| Why does `wait()` release lock? | So the other thread can access the shared queue |
| What if `wait()` didn't release lock? | Producer and consumer deadlock — system stuck |

---

## 8. Modern Alternative — `BlockingQueue`

In real enterprise applications, instead of manually using `wait()`/`notify()`, developers use `BlockingQueue` from `java.util.concurrent`:

```java
BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

queue.put(value);  // waits automatically if full
queue.take();      // waits automatically if empty
```

**Benefits:**
- Internally handles synchronization, waiting, and notification
- Much simpler and safer
- Thread-safe by design

---

## 9. Real Enterprise Usage

These concepts are used internally in:
- Message queues
- Thread pools
- Blocking queues
- Database connection pools
- Kafka-like systems
- Producer-consumer pipelines

---

## Final Revision Table (Part 2)

| Method          | Purpose                        |
|-----------------|--------------------------------|
| `wait()`        | Wait for an event              |
| `notify()`      | Wake one waiting thread        |
| `notifyAll()`   | Wake all waiting threads       |
| `wait(timeout)` | Avoid infinite waiting         |

---

## Updated Revision Checklist

- [ ] Can I explain why `sleep()` and `join()` are unreliable for thread communication?
- [ ] Can I trace the step-by-step flow of the Calculation Thread example?
- [ ] Do I understand why the notified thread must reacquire the lock before continuing?
- [ ] Do I know when to use `notify()` vs `notifyAll()`?
- [ ] Can I explain the `IllegalMonitorStateException` pitfall with wrong object?
- [ ] Can I implement the Producer-Consumer problem using `wait()`/`notify()`?
- [ ] Do I know why `while` must be used instead of `if` before `wait()`?
- [ ] Do I understand what a spurious wakeup is?
- [ ] Do I know the modern alternative — `BlockingQueue`?
