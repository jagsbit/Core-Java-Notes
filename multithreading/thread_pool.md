# Thread Pools & Executor Framework in Java

This is one of the **MOST IMPORTANT** modern multithreading topics in Java.

In real enterprise applications:
- Developers **rarely** create threads manually using `new Thread()`
- Instead they use the **Executor Framework**

Introduced in: **Java 1.5**

This framework internally manages:
- Thread creation
- Thread reuse
- Task scheduling
- Thread lifecycle

---

## 1. Problem with Creating Threads Manually

Suppose for every task you do:

```java
new Thread(task).start();
```

### Why Is This Bad?

Creating threads is **expensive** because JVM and OS must:
- Allocate memory
- Create a stack
- Register thread with scheduler
- Manage lifecycle

### Imagine

A web server receives **10,000 requests**.

Would creating **10,000 new threads** be efficient?

> **NO** — Huge memory overhead, CPU overhead, and context switching cost.

---

### Real-Life Analogy — Taxi Service

| Approach      | Description                                            |
|---------------|--------------------------------------------------------|
| ❌ Wrong        | Buy a new car for every customer. Destroy after trip. |
| ✅ Better       | Maintain a **pool of taxis**. Reuse after each trip.  |

Similarly in Java:

> Instead of creating threads repeatedly — **reuse existing threads**.

This is: **Thread Pool**

---

## 2. What is a Thread Pool?

### Definition

> A thread pool is a **collection of pre-created reusable threads** used to execute multiple tasks.

### Internal Working

```
Task arrives
     ↓
Thread from pool executes task
     ↓
Task completed
     ↓
Thread returns back to pool (NOT destroyed)
     ↓
Thread reused for next task
```

### Advantages

| Benefit                   | Explanation                                   |
|---------------------------|-----------------------------------------------|
| Better Performance        | Thread reuse — no repeated creation           |
| Lower Memory Usage        | Fewer thread objects alive at once            |
| Faster Execution          | No overhead of repeated thread creation       |
| Better Resource Management| Controlled concurrency — fixed pool size      |

---

## 3. Executor Framework

Java introduced the **Executor Framework** to manage thread pools easily.

**Package:** `java.util.concurrent`

### Main Components

| Component        | Purpose                                        |
|------------------|------------------------------------------------|
| `Executors`      | Factory class for creating thread pools        |
| `ExecutorService`| Interface that manages the thread pool         |
| `Runnable`       | Task interface — no return value               |
| `Callable`       | Task interface — returns a result              |
| `Future`         | Holds the result of an asynchronous task       |

---

## 4. Creating a Thread Pool

Most common method:

```java
ExecutorService service = Executors.newFixedThreadPool(3);
```

### Meaning

- Create a thread pool with **3 threads**
- Maximum **3 tasks execute simultaneously**
- Remaining tasks **wait in queue**

### Visualization

```
Pool:
[T1] [T2] [T3]

Incoming Tasks:
Job1  Job2  Job3  Job4  Job5

Execution:
T1 → Job1
T2 → Job2
T3 → Job3

Job4 waits...
Job5 waits...

After T1 finishes Job1:
T1 reused → Job4
```

---

## 5. Runnable Tasks with `ExecutorService`

### Task Definition

```java
class MyTask implements Runnable {

    private String name;

    MyTask(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println(name + " started by "
            + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (Exception e) { }
        System.out.println(name + " completed");
    }
}
```

### Main Class

```java
import java.util.concurrent.*;

public class ExecutorDemo {

    public static void main(String[] args) {

        ExecutorService service = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 6; i++) {
            MyTask task = new MyTask("Job-" + i);
            service.submit(task);
        }

        service.shutdown();
    }
}
```

### Possible Output

```
Job-1 started by pool-1-thread-1
Job-2 started by pool-1-thread-2
Job-3 started by pool-1-thread-3
Job-1 completed
Job-4 started by pool-1-thread-1
```

> ⭐ Notice: **same thread is reused** (`pool-1-thread-1` handles Job-1 and Job-4).

---

## 6. `shutdown()` Method

> ⭐ **VERY IMPORTANT.**

### Why Is It Needed?

Thread pool threads remain **alive** after tasks complete.

After work is done:

```java
service.shutdown();
```

This tells the executor:
- **No more tasks** will be submitted
- Allow **existing tasks to finish**
- Then gracefully terminate threads

> **`shutdown()` does NOT kill running tasks immediately** — it waits for them to complete.

---

## 7. `Runnable` vs `Callable`

> ⭐ **VERY IMPORTANT interview question.**

| Feature            | `Runnable`          | `Callable`                   |
|--------------------|---------------------|------------------------------|
| Introduced         | Java 1.0            | Java 1.5                     |
| Method             | `run()`             | `call()`                     |
| Return Type        | `void`              | Generic (e.g., `Integer`)    |
| Checked Exception  | ❌ Not allowed       | ✅ Allowed                    |
| Package            | `java.lang`         | `java.util.concurrent`       |
| Can return result  | ❌ No                | ✅ Yes                        |

### `Runnable` — No Return Value

```java
public void run() {
    // no return
}
```

### `Callable` — Returns a Value

```java
public Integer call() {
    return 5050; // can return any object
}
```

---

## 8. Why is `Callable` Needed?

Suppose a task calculates the **sum of 1 to 100** and the main thread needs the **result back**.

`Runnable` **cannot** provide this — it returns `void`.

> **`Callable` solves it** by returning a result via `Future`.

---

## 9. `Callable` Example

### Task

```java
import java.util.concurrent.Callable;

class SumTask implements Callable<Integer> {

    public Integer call() {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        return sum;
    }
}
```

### Main Class

```java
import java.util.concurrent.*;

public class CallableDemo {

    public static void main(String[] args) throws Exception {

        ExecutorService service = Executors.newFixedThreadPool(2);

        SumTask task = new SumTask();

        Future<Integer> future = service.submit(task);

        Integer result = future.get(); // waits for result

        System.out.println("Result = " + result);

        service.shutdown();
    }
}
```

### Output

```
Result = 5050
```

---

## 10. What is `Future`?

> ⭐ **VERY IMPORTANT concept.**

### Definition

> `Future` is an object representing the **result of an asynchronous computation**.

### Simple Meaning

Suppose:
- A task is running in the background
- Result is **not ready immediately**

`Future` acts like a **placeholder** for the future result.

### Analogy

You order food online.

- Restaurant gives you a **token number**
- Food is not ready yet
- Later, you use the token to **collect your food**

Similarly: `Future` represents a **pending result** you can collect later.

---

## 11. `future.get()`

```java
Integer result = future.get();
```

Used to **retrieve the actual result**.

> ⚠️ **Important:** If the result is not ready, `future.get()` **blocks and waits** until the task completes.

---

## 12. Executor Factory Methods

| Method                                  | Description                                      |
|-----------------------------------------|--------------------------------------------------|
| `Executors.newFixedThreadPool(n)`       | Fixed number of threads in pool                  |
| `Executors.newCachedThreadPool()`       | Creates threads dynamically as needed            |
| `Executors.newSingleThreadExecutor()`   | Only one thread — tasks execute sequentially     |
| `Executors.newScheduledThreadPool(n)`   | For delayed or repeated/scheduled tasks          |

### When to Use Each

| Use Case                          | Factory Method                        |
|-----------------------------------|---------------------------------------|
| Controlled concurrency            | `newFixedThreadPool(n)`               |
| Short-lived, bursty tasks         | `newCachedThreadPool()`               |
| Sequential task processing        | `newSingleThreadExecutor()`           |
| Periodic/delayed background tasks | `newScheduledThreadPool(n)`           |

---

## 13. Real-World Usage

> ⭐ Thread pools are heavily used in enterprise applications.

### Example — Web Server (Apache Tomcat)

Suppose Tomcat receives **1000 requests**.

Tomcat does **NOT** create 1000 new threads.

Instead:
- A **thread pool** handles requests
- After each request completes → **thread reused** for the next

Used in:
- Web servers (Tomcat, Netty)
- Spring Boot applications
- REST APIs
- Database connection pools
- Microservices
- Message queue consumers

---

## 14. Core Internal Understanding

The Executor Framework **separates**:

| Concern           | Who Handles It     |
|-------------------|--------------------|
| Task submission   | Developer          |
| Thread management | Framework          |

**Developer focuses on:** Tasks (what to do)

**Framework handles:** Thread creation, scheduling, reuse, lifecycle

---

## 15. ⭐ Important Interview Questions

### Q1. Why are thread pools needed?

> To avoid repeated thread creation, improve performance, and optimize memory usage.

### Q2. Difference between `Runnable` and `Callable`?

| | `Runnable` | `Callable` |
|-|------------|------------|
| Return value | `void` — no result | Generic type — returns result |
| Checked exception | Not allowed | Allowed |

### Q3. What is `Future`?

> An object representing an **asynchronous computation result** that can be retrieved later via `get()`.

### Q4. Why is `shutdown()` important?

> To properly **terminate the executor service** after all tasks are submitted — otherwise threads remain alive indefinitely.

### Q5. Pool size = 3, 10 tasks submitted — what happens?

> **3 execute immediately.** The remaining 7 **wait in queue** and are picked up as threads become free.

### Q6. Can `Callable` throw checked exceptions?

> **Yes.** Unlike `Runnable`, `Callable.call()` can throw checked exceptions.

### Q7. Does `future.get()` block?

> **Yes.** If the result is not ready, it **waits** until the task completes.

---

## Final Revision Table

| Concept          | Key Point                                             |
|------------------|-------------------------------------------------------|
| Thread Pool      | Collection of reusable threads                        |
| `ExecutorService`| Interface that manages the pool                       |
| `submit()`       | Submits a task to the pool                            |
| `shutdown()`     | Stop accepting tasks; finish existing ones            |
| `Runnable`       | Task with no return value (`run()`)                   |
| `Callable`       | Task that returns a result (`call()`)                 |
| `Future`         | Async result holder — use `get()` to retrieve         |
| Thread reuse     | Core advantage — threads not destroyed after tasks    |

---

## One-Line Interview Definitions

**Thread Pool**
> A thread pool is a collection of reusable worker threads used to execute multiple tasks efficiently without the overhead of repeatedly creating and destroying threads.

**Executor Framework**
> The Executor Framework is a high-level Java API (`java.util.concurrent`) for managing asynchronous task execution and thread pools.

**`Callable`**
> `Callable` is a task interface (introduced in Java 1.5) that can return a result and throw checked exceptions, unlike `Runnable`.

**`Future`**
> `Future` represents the result of an asynchronous computation that can be retrieved later using `get()`, which blocks until the result is available.

---

## Revision Checklist

- [ ] Can I explain why creating `new Thread()` for every task is inefficient?
- [ ] Can I define a thread pool and explain how it works internally?
- [ ] Do I know how to create a fixed thread pool using `Executors.newFixedThreadPool(n)`?
- [ ] Can I explain what happens when more tasks than pool size are submitted?
- [ ] Do I know the difference between `Runnable` and `Callable`?
- [ ] Can I implement a `Callable` task and retrieve the result using `Future`?
- [ ] Do I know that `future.get()` blocks until the result is ready?
- [ ] Do I know why `shutdown()` must be called?
- [ ] Can I list all 4 `Executors` factory methods and when to use each?
- [ ] Can I explain the separation of task submission and thread management in the Executor Framework?
