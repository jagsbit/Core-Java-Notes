# Introduction to Multi-Threading in Java

Multi-threading is one of the most important concepts in Core Java because it helps programs perform multiple tasks at the same time.

Unlike topics such as Collections, where you mainly learn APIs and methods, multi-threading requires **logical understanding** of how programs execute internally.

It is heavily asked in interviews because it tests:

- Java fundamentals
- JVM understanding
- Problem-solving ability
- Synchronization and concurrency knowledge

---

## 1. What is Multi-Tasking?

### Definition

Multitasking means:

> **Performing multiple tasks simultaneously.**

### Example

- Listening to music
- Downloading a file
- Typing in VS Code
- Watching a YouTube video

All happening together on your computer.

---

## 2. Types of Multitasking

There are two major types:

### A. Process-Based Multitasking

#### Definition

Executing multiple **independent programs** simultaneously.

Each program is called a **process**.

#### Examples

- Chrome browser
- Spotify
- VS Code
- WhatsApp

All are separate processes.

#### Real-Life Example

Suppose you:
- Edit code in VS Code
- Play songs on Spotify
- Download a movie

These are separate applications running independently.

#### Characteristics

**1. OS Level Concept**

Handled by the Operating System.

**2. Independent Programs**

Each process has:
- Separate memory
- Separate resources

**3. Failure Isolation**

If one process crashes, others continue running.

> **Example:** If Chrome crashes, VS Code still works.

#### Diagram

```
Operating System
    |
------------------------------------------------
|               |               |              |
Chrome       VS Code         Spotify       WhatsApp
(Process)    (Process)       (Process)     (Process)
```

---

### B. Thread-Based Multitasking

#### Definition

Executing multiple parts of the **SAME program** simultaneously.

These smaller units are called **threads**.

#### Example

Inside a browser:
- One thread loads UI
- Another downloads images
- Another executes JavaScript

All belong to the same application.

#### Another Example

Suppose a program has:
- File reading
- Data processing
- Report generation

Instead of executing one after another, they can execute **simultaneously** using threads.

#### Why Use Threads?

Suppose:
- Task 1 takes 5 hours
- Task 2 takes 5 hours

**Sequential execution:**
```
5 + 5 = 10 hours
```

**Using threads:**
```
Both run simultaneously ≈ 5–6 hours
```

This improves performance significantly.

---

### Process vs Thread

| Feature         | Process               | Thread                      |
|-----------------|-----------------------|-----------------------------|
| Meaning         | Independent program   | Part of a program           |
| Memory          | Separate memory       | Shared memory               |
| Communication   | Expensive             | Faster                      |
| Managed By      | OS                    | JVM + OS                    |
| Failure Impact  | Isolated              | Can affect whole app        |
| Example         | Chrome, VS Code       | Download thread in browser  |

---

## 3. What is a Thread?

### Definition

A thread is:

> **The smallest unit of execution inside a process.**

OR

> **A lightweight subprocess.**

### Important Point

A Java program always starts with **one main thread**.

```java
public class Test {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}
```

The `main()` method itself runs inside a thread called the **main thread**.

---

### Detailed Understanding

**A thread is NOT the code itself.**

A thread is:
> The **execution path** (or execution unit) that **executes** a part of the program.

So in simple words:
- **Program** → collection of code
- **Thread** → something that executes that code

### Easy Analogy

Imagine a restaurant kitchen:

- The **restaurant** = program
- **Different workers** cooking food = threads

Each worker performs different tasks simultaneously:
- One makes pizza
- One prepares drinks
- One packs orders

The workers are **not** the tasks themselves — they are the ones **executing** the tasks.

Similarly:
- **Program** = complete application
- **Thread** = execution unit working on part of the application

### Thread Components

A thread has:
- Its own **execution stack**
- **Program counter**
- **Execution flow**

But threads **share**:
- Heap memory
- Objects
- Resources of the same process

That is why threads are **lightweight and fast**.

### Example — Music App

| Thread   | Work             |
|----------|------------------|
| Thread-1 | Handle UI        |
| Thread-2 | Play music       |
| Thread-3 | Download songs   |

### One-Line Interview Answer

> **A thread is the smallest unit of execution that executes a part of a program independently.**

---

## 4. Why Multi-Threading is Needed

### Main Goal — Reduce CPU Idle Time

**Without multithreading:**
- CPU waits frequently
- Resources remain unused

**With multithreading:**
- CPU utilization improves
- Performance improves

### Improve Response Time

Applications become:
- Smoother
- Faster
- More responsive

### Example

**Without threads:**
1. Load file
2. Then play music
3. Then render graphics

Everything feels slow.

**With threads:**
- All tasks happen together → much better user experience.

---

## 5. Real-World Applications of Multi-Threading

### A. Multimedia Applications

Animation movies/games use threads for:
- Character movement
- Background music
- Rain effects
- Physics calculations

All simultaneously.

### B. Web Servers

> ⭐ Very important interview topic.

Servers like **Apache Tomcat** and **Netty** handle thousands of users simultaneously using threads.

**Example:**

1000 users request a website.

**Without threads:**
- One user at a time → terrible performance

**With threads:**
- Each request handled by a separate thread

### C. Large Computations

Examples:
- Searching huge files
- AI processing
- Image rendering
- Scientific calculations

Threads divide work into smaller tasks.

---

## 6. Why Java is Excellent for Multi-Threading

Java provides **rich built-in support**.

You do **NOT** need to manage:
- CPU scheduling
- Low-level memory handling
- OS-level thread management

Java APIs handle most complexity.

### Important Java APIs

| API              | Purpose                              |
|------------------|--------------------------------------|
| `Thread`         | Class for thread creation            |
| `Runnable`       | Interface to define thread task      |
| `ThreadGroup`    | Manage multiple threads together     |

### Key Advantage

> Only ~10% logic is written by the programmer.  
> Remaining ~90% is handled internally by Java.

**Example:**

Starting a thread is simple:
```java
thread.start();
```

But internally Java handles:
- Thread scheduling
- Memory allocation
- Stack creation
- OS communication
- Lifecycle management

Thousands of lines of internal JVM logic.

---

## 7. How Threads Work Internally

When you call:
```java
t.start();
```

JVM:
1. Creates new thread
2. Allocates stack memory
3. Registers thread with scheduler
4. Moves thread to runnable state
5. Eventually executes `run()` method

---

### ⭐ VERY IMPORTANT INTERVIEW CONCEPT — `start()` vs `run()`

> This is one of the **MOST ASKED** interview questions.

#### `run()` Method

```java
t.run();
```

- This is just a **normal method call**.
- **No new thread** is created.
- Execution happens in the **current thread** only.

#### `start()` Method

```java
t.start();
```

- Creates a **new thread**.
- Internally calls `run()`.
- Actual multithreading happens.

#### Example

**❌ Wrong Way**
```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread running");
    }
}

public class Test {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.run(); // normal method call — NO new thread created
    }
}
```

**✅ Correct Way**
```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread running");
    }
}

public class Test {
    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start(); // creates a new thread
    }
}
```

Now JVM creates a **separate thread**.

---

## 8. Two Ways to Create Threads

> ⭐ Very important topic.

### Method 1: Extending Thread Class

```java
class MyThread extends Thread {

    public void run() {
        System.out.println("Child Thread");
    }
}

public class Test {

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();
        System.out.println("Main Thread");
    }
}
```

#### Output

Possible outputs:

```
Main Thread
Child Thread
```

OR

```
Child Thread
Main Thread
```

#### Why Output Changes?

Because thread execution order is controlled by:
- Thread Scheduler
- JVM
- OS

This is called: **Non-deterministic execution**

---

### Method 2: Implementing Runnable Interface

```java
class MyRunnable implements Runnable {

    public void run() {
        System.out.println("Runnable Thread");
    }
}

public class Test {

    public static void main(String[] args) {
        MyRunnable r = new MyRunnable();
        Thread t = new Thread(r);
        t.start();
    }
}
```

### Which is Better?

Usually **Runnable is preferred** because:

- Java supports **single inheritance**
- If you extend `Thread`, you cannot extend another class
- Implementing `Runnable` is more flexible

---

## 9. Thread Lifecycle

A thread goes through multiple states.

### States of a Thread

1. `NEW`
2. `RUNNABLE`
3. `RUNNING`
4. `BLOCKED / WAITING`
5. `TERMINATED`

### Lifecycle Flow

```
NEW
  |
start()
  |
RUNNABLE
  |
Scheduler selects
  |
RUNNING
  |
sleep() / wait() / blocked
  |
RUNNABLE
  |
run() completes
  |
TERMINATED
```

### Explanation

| State              | Description                                               |
|--------------------|-----------------------------------------------------------|
| **NEW**            | Thread object created but not started                     |
| **RUNNABLE**       | After `t.start()` — thread is ready to run               |
| **RUNNING**        | CPU is currently executing the thread                     |
| **WAITING/BLOCKED**| Thread paused due to `sleep()`, `wait()`, or I/O          |
| **TERMINATED**     | Thread execution completed                                |

---

## 10. Thread Scheduler

### Definition

Component responsible for deciding:
- Which thread executes
- When it executes

### Important Point

Scheduling behavior:
- Differs across OS
- **Not guaranteed**

Therefore thread output order may change.

---

## 11. Thread Priority

Java provides priorities from **1 to 10**.

| Constant                    | Value |
|-----------------------------|-------|
| `Thread.MIN_PRIORITY`       | 1     |
| `Thread.NORM_PRIORITY`      | 5     |
| `Thread.MAX_PRIORITY`       | 10    |

**Default priority:** `5`

```java
Thread t = new Thread();
t.setPriority(10);
```

> Higher priority = higher **chance** of execution. **NOT guaranteed.**

---

## 12. Important Thread Methods

| Method           | Description                                            |
|------------------|--------------------------------------------------------|
| `sleep(ms)`      | Pauses current thread for given milliseconds           |
| `yield()`        | Current thread gives chance to other threads           |
| `join()`         | One thread waits for another thread to finish          |

```java
Thread.sleep(2000); // 2-second pause
```

---

## 13. Synchronization

> ⭐ One of the **MOST IMPORTANT** concepts.

Used when **multiple threads access shared data**.

### Problem Without Synchronization

Suppose two threads update the **same bank account**.

Without synchronization → **data inconsistency** occurs.

This is called: **Race Condition**

### Synchronization Solution

Allows only **one thread at a time** to access a block.

```java
synchronized void deposit() {
    // only one thread executes at a time
}
```

---

## 14. Inter-Thread Communication

Threads communicate using:

| Method          | Purpose                            |
|-----------------|------------------------------------|
| `wait()`        | Makes thread wait                  |
| `notify()`      | Wakes up one waiting thread        |
| `notifyAll()`   | Wakes up all waiting threads       |

Used in:
- Producer-Consumer problem
- Thread coordination

---

## 15. Deadlock

> ⭐ Very important interview topic.

### What is Deadlock?

Two threads **waiting forever** for each other.

**Example:**
- Thread-1 holds **Lock-A**, waiting for **Lock-B**
- Thread-2 holds **Lock-B**, waiting for **Lock-A**

Neither proceeds → **Program hangs.**

---

## 16. Advanced Topics

### Daemon Thread

Background service threads.

Examples:
- Garbage collector
- Monitoring threads

### Reentrant Lock

Advanced locking mechanism from `java.util.concurrent.locks`.

Provides more flexibility than `synchronized`.

### ThreadLocal

Each thread gets its own **separate copy** of a variable.

### Executor Framework

Modern way to manage threads efficiently.

Instead of manually creating:
```java
new Thread()
```

Use **thread pools** via Executor Framework.

> Very important in enterprise applications.

---

## 17. Important Interview Revision Points

### Thread vs Runnable

| Feature      | Thread               | Runnable                   |
|--------------|----------------------|----------------------------|
| Type         | Class                | Interface                  |
| Approach     | Uses inheritance     | Uses interface implementation |
| Flexibility  | Less flexible        | More flexible              |
| Preference   | Older approach       | Preferred approach         |

### `start()` vs `run()`

| Feature              | `start()`                          | `run()`                    |
|----------------------|------------------------------------|----------------------------|
| Thread creation      | Creates new thread                 | Normal method call         |
| Multithreading       | Multithreading happens             | No multithreading          |
| Internal behavior    | JVM internally calls `run()`       | Direct execution           |

### Process vs Thread

| Feature  | Process                    | Thread                    |
|----------|----------------------------|---------------------------|
| Meaning  | Independent program        | Part of same program      |

### Main Goals of Multithreading

- Better CPU utilization
- Reduced response time
- Improved performance

---

## Final Summary

Multi-threading allows Java applications to execute **multiple tasks simultaneously** using threads.

Java provides powerful built-in support through:
- `Thread`
- `Runnable`
- Synchronization APIs
- Concurrent utilities

### Core Concepts You MUST Master

| Concept                     | Importance   |
|-----------------------------|--------------|
| Thread creation             | ⭐⭐⭐⭐⭐      |
| Thread lifecycle            | ⭐⭐⭐⭐⭐      |
| `start()` vs `run()`        | ⭐⭐⭐⭐⭐      |
| Synchronization             | ⭐⭐⭐⭐⭐      |
| Inter-thread communication  | ⭐⭐⭐⭐       |
| Deadlock                    | ⭐⭐⭐⭐⭐      |
| Executor Framework          | ⭐⭐⭐⭐       |

These concepts are extremely important for:
- Interviews
- Backend development
- Enterprise applications
- Server-side programming
- High-performance systems
