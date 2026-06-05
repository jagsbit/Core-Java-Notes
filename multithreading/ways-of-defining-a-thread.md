# Core Java: Multi-Threading — Part 2 (Ways of Defining a Thread)

This session focuses on the fundamentals of Multi-Threading in Java, specifically detailing how to **define** and **start** threads.

---

## 1. What is a Thread?

### Definition

> A thread is a **separate flow of execution** within a program.

It is also referred to as:
- An **independent job**
- A **lightweight process**

### Key Concept

In a Java application:
- Every thread is responsible for performing a **specific, independent job**
- If multiple independent jobs exist, multiple threads can execute them **simultaneously**
- This improves overall application performance by completing tasks in **less time**

### Default Behavior

> Every Java program has **at least one thread** by default: the **Main Thread**.

---

## 2. Ways to Define a Thread

There are **two primary ways** to define a thread in Java:

| Method                            | Approach                         |
|-----------------------------------|----------------------------------|
| Extending the `Thread` class      | Class-based                      |
| Implementing the `Runnable` interface | Interface-based              |

---

## 3. Case Studies and Detailed Concepts

---

### Case 1: Defining a Thread by Extending `Thread` Class

To define a thread, create a class that:
- **extends** `java.lang.Thread`
- **overrides** the `run()` method

#### The Job

> The code written inside the `run()` method constitutes the **thread's job**.

#### Implementation

```java
class MyThread extends Thread {
    public void run() {
        // Thread job here
        System.out.println("Child Thread is running");
    }
}

public class Test {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start(); // creates a new thread and calls run()
        System.out.println("Main Thread");
    }
}
```

#### Possible Output

```
Main Thread
Child Thread is running
```

OR

```
Child Thread is running
Main Thread
```

> Output order is **not guaranteed** — depends on the Thread Scheduler.

---

### Case 2: The Role of the Thread Scheduler

The **Thread Scheduler** is a part of the **JVM** responsible for deciding the execution order of threads.

#### Important Points

- You **cannot predict** the order of execution
- The algorithm used by the scheduler **varies by JVM**
- In multi-threaded programs, there is **no guarantee** for the sequence of output

> This behavior is called **Non-deterministic execution**.

---

### Case 3: `start()` vs `run()` Method

> ⭐ This is a **highly important interview question**.

#### `t.start()`

```java
t.start();
```

- **Creates a new thread**
- Internally calls the `run()` method
- The new thread executes the job **simultaneously**
- Actual **multi-threading** happens

#### `t.run()`

```java
t.run();
```

- Does **NOT** create a new thread
- Simply executes `run()` like a **normal method call**
- Runs within the **existing thread** (usually the main thread)
- **No multi-threading** happens

#### Comparison Table

| Feature              | `t.start()`                        | `t.run()`                     |
|----------------------|------------------------------------|-------------------------------|
| New thread created   | ✅ Yes                              | ❌ No                          |
| Multi-threading      | ✅ Yes                              | ❌ No                          |
| Who calls `run()`    | JVM internally                     | Called directly by programmer |
| Execution thread     | New child thread                   | Current (main) thread         |

> ⚠️ **Interview Tip:** Always remember that without calling `start()`, **no new thread is created**.

---

### Case 4: Importance of `Thread.start()`

> `start()` is considered the **heart of multi-threading**.

#### Responsibilities of `start()`

1. **Registers** the thread with the Thread Scheduler
2. **Performs** mandatory internal activities
3. **Invokes** the `run()` method

Without `start()`, none of these happen — no new thread is born.

---

### Case 5: Overloading the `run()` Method

Overloading `run()` is **possible**, but:

- The `Thread` class `start()` method will **always invoke** the **no-argument** `run()` method
- Overloaded versions must be **called explicitly** like standard methods

#### Example

```java
class MyThread extends Thread {

    public void run() {
        System.out.println("No-arg run() — called by start()");
    }

    public void run(int x) {
        System.out.println("Overloaded run() with: " + x);
    }
}

public class Test {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();        // calls no-arg run()
        t.run(10);        // normal method call — no new thread
    }
}
```

---

### Case 6: Not Overriding `run()`

If you define a class extending `Thread` but **do not override** `run()`:

- The program will **compile and execute** without errors
- But it will **do nothing**
- Because the default `Thread.run()` has an **empty implementation**

#### Example

```java
class MyThread extends Thread {
    // run() not overridden
}

public class Test {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start(); // executes, but does nothing
    }
}
```

> Always override `run()` to define specific thread tasks.

---

### Case 7: Overriding `start()`

If you override `start()`:

- You **must** explicitly call `super.start()` to ensure a new thread is created
- If you **do not** call `super.start()`, your custom `start()` executes as a **normal method**
- **No new thread** will be born

#### Example

```java
class MyThread extends Thread {

    public void start() {
        super.start(); // MUST call this to create new thread
        System.out.println("Custom start() called");
    }

    public void run() {
        System.out.println("Thread running");
    }
}
```

> ⚠️ Forgetting `super.start()` is a common mistake. Without it, no new thread is created.

---

### Case 8: Thread Life Cycle

A thread passes through the following states:

#### States

| State         | Trigger                                      | Description                          |
|---------------|----------------------------------------------|--------------------------------------|
| **New/Born**  | `MyThread t = new MyThread();`               | Thread object created, not started   |
| **Runnable**  | `t.start();`                                 | Thread is ready and waiting for CPU  |
| **Running**   | Thread Scheduler allocates the processor     | `run()` begins execution             |
| **Dead**      | `run()` method completes                     | Thread finishes and cannot be reused |

#### Lifecycle Flow

```
New/Born State
    |
  t.start()
    |
Runnable State  ←──────────────┐
    |                          |
Scheduler allocates CPU        |
    |                          |
Running State                  |
    |                          |
  (sleep/wait/yield) ──────────┘
    |
  run() completes
    |
Dead State
```

---

### Case 9: Restarting a Thread

> ⭐ **Crucial Rule**

You **cannot restart** a thread that has:
- **Already started** (currently running)
- **Already finished** (in dead state)

Attempting to do so will throw:

```
java.lang.IllegalThreadStateException  (Runtime Exception)
```

#### Example

```java
MyThread t = new MyThread();
t.start(); // valid — first start

// After thread completes:
t.start(); // ❌ throws IllegalThreadStateException
```

> Once a thread is dead, it cannot be revived. Create a **new thread object** instead.

---

## 4. Summary

This topic provides a deep dive into the mechanics of **thread creation and management** in Java. It highlights the critical distinction between the `start()` and `run()` methods, explains the behavior of the JVM Thread Scheduler, and outlines the lifecycle of a thread.

---

## Key Takeaways

| Concept                        | Rule / Behavior                                                        |
|-------------------------------|-------------------------------------------------------------------------|
| `start()` vs `run()`          | Use `start()` for multi-threading; `run()` is just a normal method call |
| Thread Scheduler              | Execution order is **non-deterministic**                                |
| Override `run()`              | Always override to define thread's task                                 |
| Overloading `run()`           | Allowed, but `start()` always calls no-arg version                      |
| Not overriding `run()`        | Program runs but does nothing                                           |
| Overriding `start()`          | Must call `super.start()` to create a new thread                        |
| Restarting a thread           | ❌ Invalid — throws `IllegalThreadStateException`                        |

---

## Interview Quick Revision

### `start()` is the Heart of Multi-Threading because it:

1. **Registers** the thread with the Thread Scheduler
2. **Performs** all mandatory internal activities
3. **Invokes** `run()` in a **new thread**

### Thread States in Order:

```
New → Runnable → Running → Dead
```

### Most Asked Interview Questions from this Topic:

- What is the difference between `start()` and `run()`?
- Can you restart a dead thread?
- What happens if you don't override `run()`?
- What happens if you override `start()` without calling `super.start()`?
- Is overloading `run()` allowed? Which version does `start()` call?
