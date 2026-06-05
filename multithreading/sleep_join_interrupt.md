# Preventing Thread Execution in Java — `join()`, `sleep()`, `interrupt()`

In multithreading, sometimes we do **NOT** want a thread to continue execution immediately.

We may want:
- A thread to wait
- A thread to pause temporarily
- One thread to finish before another
- A sleeping thread to wake up

Java provides important methods for this:

| Method        | Purpose                                      |
|---------------|----------------------------------------------|
| `yield()`     | Give CPU chance to other threads             |
| `join()`      | Wait for another thread to finish            |
| `sleep()`     | Pause execution for a specific time          |
| `interrupt()` | Wake up a sleeping or waiting thread         |

> This session focuses mainly on `join()`, `sleep()`, and `interrupt()`.

---

## 1. `join()` Method

### Core Concept

`join()` is used when one thread wants to **wait until another thread completes** execution.

### Simple Meaning

Suppose:
- Thread A **depends** on Thread B

Then Thread A should **wait** until Thread B finishes:

```java
t.join();
```

### Syntax

```java
public final void join() throws InterruptedException
```

### Overloaded Versions

```java
public final void join(long ms)
public final void join(long ms, int ns)
```

### Important Characteristics

| Property    | Explanation                        |
|-------------|------------------------------------|
| Method Type | Instance method                    |
| `final`     | Cannot be overridden               |
| Exception   | `InterruptedException`             |
| Purpose     | Wait for another thread            |
| State Change| Running → Waiting                  |

### ⭐ Very Important Point

When `t1.join()` is written:

> The **current thread** waits for `t1` — **NOT** the other way around.

---

### Internal Working

Suppose the main thread is running and executes `t.join()`:

```
Main Thread: RUNNING → WAITING
         (waits until child thread completes)
                    ↓
Child thread finishes
                    ↓
Main Thread: WAITING → RUNNABLE → RUNNING
```

---

### Case 1: Main Thread Waiting for Child Thread

```java
class MyThread extends Thread {

    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Child Thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
        }
    }
}

class Test {

    public static void main(String[] args) throws InterruptedException {

        MyThread t = new MyThread();

        t.start();

        t.join(); // main thread waits here

        for (int i = 0; i < 5; i++) {
            System.out.println("Main Thread");
        }
    }
}
```

#### Output

```
Child Thread
Child Thread
Child Thread
Child Thread
Child Thread
Main Thread
Main Thread
Main Thread
Main Thread
Main Thread
```

#### Step-by-Step Explanation

| Step | Action                                      |
|------|---------------------------------------------|
| 1    | Main thread creates child thread            |
| 2    | Main thread calls `t.start()`               |
| 3    | Main thread calls `t.join()` → starts waiting |
| 4    | Child thread executes fully                 |
| 5    | Child completes → main thread resumes       |

> **Without `join()`** → main and child execute **simultaneously**
> **With `join()`** → main **waits** for child to complete

---

### Case 2: Child Thread Waiting for Main Thread

> ⚠️ A common misconception — only the main thread can wait for a child thread.

**This is WRONG.** A child thread can also wait for the main thread.

```java
class MyThread extends Thread {

    static Thread mt; // holds reference to main thread

    public void run() {
        try {
            mt.join(); // child waits for main thread
        } catch (InterruptedException e) { }

        for (int i = 0; i < 5; i++) {
            System.out.println("Child Thread");
        }
    }
}

class Test {

    public static void main(String[] args) throws InterruptedException {

        MyThread.mt = Thread.currentThread(); // store main thread reference

        MyThread t = new MyThread();
        t.start();

        for (int i = 0; i < 5; i++) {
            System.out.println("Main Thread");
            Thread.sleep(1000);
        }
    }
}
```

#### ⭐ Very Important Line

```java
Thread.currentThread()
```

Returns the **currently executing thread object**.

Inside `main()` → current thread = **main thread**

So we store the reference of the main thread inside `mt`, and the child thread calls `mt.join()` to wait for it.

#### Output

```
Main Thread
Main Thread
Main Thread
Main Thread
Main Thread
Child Thread
Child Thread
Child Thread
Child Thread
Child Thread
```

---

### Case 3: Deadlock with `join()`

#### What is Deadlock?

> Threads **wait forever for each other**. Program hangs permanently.

#### Scenario A: Thread Joining Itself

```java
public class Test {

    public static void main(String[] args) throws InterruptedException {

        Thread.currentThread().join(); // main thread waits for itself

        System.out.println("Never Executes");
    }
}
```

**What Happens?**

- `Thread.currentThread()` returns the **main thread**
- So internally: main thread waits for main thread
- This is an **impossible condition**
- **Result: Deadlock** — program hangs forever

#### Scenario B: Cyclic Waiting

```
t1 waits for t2
t2 waits for t1
```

Neither can continue → **Deadlock**

---

## 2. `sleep()` Method

### Core Concept

`sleep()` is used when a thread wants to **pause execution for a specific time**.

### Syntax

```java
public static native void sleep(long ms) throws InterruptedException
```

### Important Characteristics

| Property   | Explanation                       |
|------------|-----------------------------------|
| `static`   | Belongs to `Thread` class         |
| `native`   | OS-level implementation           |
| Exception  | `InterruptedException` (checked)  |
| Purpose    | Pause execution for a fixed time  |
| State Change | Running → Timed Waiting         |

### Example

```java
class Test {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 1; i <= 5; i++) {
            System.out.println(i);
            Thread.sleep(2000); // pause 2 seconds
        }
    }
}
```

#### Output

```
1
(wait 2 sec)
2
(wait 2 sec)
3
...
```

### Internal State Transition

```
Thread calls Thread.sleep(2000)
        ↓
RUNNING → TIMED WAITING
        ↓
(after 2 seconds)
TIMED WAITING → RUNNABLE
```

### ⭐ Very Important Point — `sleep()` Does NOT Release Locks

If a thread owns a `synchronized` lock and calls `sleep()`:

> The lock **remains held** during the entire sleep period.

Other threads that need that lock must **wait**.

> This is a **common interview question**.

---

### Difference Between `sleep()` and `join()`

| Feature        | `sleep()`                   | `join()`                          |
|----------------|-----------------------------|-----------------------------------|
| Purpose        | Pause for a fixed time      | Wait for another thread           |
| Method Type    | `static`                    | Instance                          |
| Based On       | Time                        | Thread dependency                 |
| State          | Timed Waiting               | Waiting                           |

---

## 3. `interrupt()` Method

### Core Concept

One thread can **interrupt another thread** using `interrupt()`.

### Syntax

```java
public void interrupt()
```

### Why is it Needed?

Suppose a thread is:
- Sleeping
- Waiting
- Blocked

Another thread may want to **wake it up immediately**.

---

### Internal Working of `interrupt()`

#### Case 1: Thread is Sleeping / Waiting

If the thread is inside `sleep()`, `join()`, or `wait()`:

- Interrupt **immediately breaks** that state
- `InterruptedException` is **thrown**

#### Case 2: Thread is Running Normally

If the thread is actively executing code:

- **No exception immediately**
- Java sets the **interrupt status flag = `true`**

---

### Example: Interrupting a Sleeping Thread

```java
class MyThread extends Thread {

    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("I am lazy thread : " + i);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.out.println("I got interrupted!");
        }
    }
}

class Test {

    public static void main(String[] args) {

        MyThread t = new MyThread();
        t.start();
        t.interrupt();

        System.out.println("End of Main");
    }
}
```

#### Step-by-Step Execution

| Step | Action                                                 |
|------|--------------------------------------------------------|
| 1    | Child thread starts, prints `"I am lazy thread : 0"`  |
| 2    | Child enters `Thread.sleep(2000)` → TIMED WAITING      |
| 3    | Main thread executes `t.interrupt()`                   |
| 4    | Interrupt breaks the sleep immediately                 |
| 5    | `InterruptedException` is thrown                       |
| 6    | Control jumps to `catch` block                         |

#### Output

```
I am lazy thread : 0
End of Main
I got interrupted!
```

---

### ⭐ Very Important Interview Questions

#### Q1. Does `interrupt()` stop a thread forcefully?

> **No.** It only **sends an interruption request**. The thread decides how to handle it.

#### Q2. When is `InterruptedException` thrown?

> When the interrupted thread is inside `sleep()`, `join()`, or `wait()`.

#### Q3. What if the thread is not sleeping?

> No immediate exception. Only the **interrupt flag** is set to `true`.

#### Q4. Can `interrupt()` kill a thread?

> **No.** The thread may ignore the interruption and continue execution.

---

### Why Interrupt a Sleeping Thread?

> This is where many beginners misunderstand `interrupt()`.

The purpose of `interrupt()` is **NOT**: *"kill the thread"*

The purpose **IS**: *"request the thread to stop waiting/sleeping and respond immediately"*

**WRONG thinking:**
```
interrupt() = thread destroyed
```

**Correct understanding:**
```
interrupt() = "Hey thread, wake up and handle this situation"
```

---

### Example 1: Thread Continues After Interrupt

```java
class MyThread extends Thread {

    public void run() {
        try {
            System.out.println("Sleeping...");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }

        System.out.println("Still running..."); // executes after interrupt
    }
}

public class Test {

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();
        t.interrupt();
    }
}
```

#### Output

```
Sleeping...
Interrupted!
Still running...
```

> After the exception, **the thread continued execution** because the catch block handled it and `run()` continued normally.

---

### Real-Life Use Case: Server Shutdown

Suppose a server thread does this:

```java
while (true) {
    Thread.sleep(10000); // wait 10 seconds
    checkForRequests();
}
```

When the application is **shutting down**:

- **Without `interrupt()`** → thread sleeps the full 10 seconds
- **With `interrupt()`** → thread wakes immediately, cleans up, and stops gracefully

---

### Interrupt is Cooperative

Java thread interruption is **cooperative cancellation**:

> Java **politely asks** the thread to stop/wake. The thread decides what to do.

| Thread Behavior After Interrupt | Description                        |
|---------------------------------|------------------------------------|
| Stop execution                  | Graceful shutdown                  |
| Continue execution              | Ignore interruption                |
| Cleanup resources               | Close files, DB connections, etc.  |
| Retry operation                 | Continue work differently          |

---

### Example 2: Graceful Stop Using `interrupt()`

```java
class Worker extends Thread {

    public void run() {
        try {
            while (true) {
                System.out.println("Working...");
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.out.println("Cleaning resources...");
        }

        System.out.println("Thread stopped");
    }
}
```

#### Output After calling `t.interrupt()`

```
Working...
Working...
Cleaning resources...
Thread stopped
```

---

### Why Does `InterruptedException` Occur?

Because `sleep` was **forcefully broken**.

Normally `sleep(10000)` means: *"Do not wake me before 10 seconds."*

`interrupt()` says: *"No, wake up NOW."*

So Java throws the exception to **notify** the thread: *"Your sleep was interrupted unexpectedly."*

> **`interrupt()` is a communication mechanism — NOT a forceful kill mechanism.**

---

### Analogy

Imagine you are sleeping for 8 hours and someone wakes you after 2 hours.

You may:
- Wake up and work
- Go back to sleep
- Clean up and stop

Similarly, the **thread decides** what to do after interruption.

---

## 4. Final Comparison

| Method        | Purpose                        | Type     | Exception               | State                |
|---------------|--------------------------------|----------|-------------------------|----------------------|
| `yield()`     | Give chance to others          | `static` | ❌ No                    | Running → Runnable   |
| `join()`      | Wait for another thread        | Instance | ✅ `InterruptedException`| Running → Waiting    |
| `sleep()`     | Pause for a fixed time         | `static` | ✅ `InterruptedException`| Running → Timed Waiting |
| `interrupt()` | Wake up sleeping/waiting thread| Instance | May cause exception     | Depends              |

---

## 5. Easy Real-Life Analogy

| Method        | Analogy                                     |
|---------------|---------------------------------------------|
| `sleep()`     | *"I will rest for 2 minutes."*              |
| `join()`      | *"I will wait until you finish."*           |
| `interrupt()` | *"Wake up immediately!"*                    |

---

## 6. Final Revision Points

### `join()`
- Creates **dependency** between threads
- **Current thread** waits — not the target thread
- Instance method, `final`
- Throws `InterruptedException`
- Overloaded: `join()`, `join(ms)`, `join(ms, ns)`

### `sleep()`
- Pauses execution for a **fixed time**
- `static` method
- `Running → Timed Waiting`
- Does **NOT release locks** during sleep

### `interrupt()`
- Interrupts a **sleeping/waiting** thread
- Causes `InterruptedException` if thread is in `sleep()` / `join()` / `wait()`
- Sets **interrupt flag** if thread is running normally
- Does **NOT forcefully kill** the thread
- Used for **cooperative cancellation** and **graceful shutdown**

---

## Revision Checklist

- [ ] Do I know all three overloaded versions of `join()`?
- [ ] Can I explain which thread waits when `t1.join()` is called?
- [ ] Do I understand the deadlock scenario caused by `Thread.currentThread().join()`?
- [ ] Do I know that `sleep()` does NOT release locks?
- [ ] Can I explain the difference between `sleep()` state and `join()` state?
- [ ] Do I understand that `interrupt()` does not kill a thread?
- [ ] Can I explain what happens when you interrupt a sleeping vs a running thread?
- [ ] Do I understand cooperative cancellation using `interrupt()`?
