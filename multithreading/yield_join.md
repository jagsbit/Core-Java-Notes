# Preventing Thread Execution in Java — `yield()` and `join()`

Sometimes in multithreading, we do **NOT** want a thread to continue executing continuously.

We may want:
- Another thread to get CPU chance
- One thread to wait for another
- Temporary pause in execution

For this purpose, Java provides important thread control methods:

| Method    | Purpose                              |
|-----------|--------------------------------------|
| `yield()` | Give CPU chance to other threads     |
| `join()`  | Wait for another thread to finish    |
| `sleep()` | Pause for a specific amount of time  |

> This topic focuses mainly on `yield()` and `join()`.

---

## Why Do We Need These Methods?

Imagine multiple threads running simultaneously.

**Without control:**
- One thread may consume CPU continuously
- Dependent threads may execute before required data is ready
- Coordination becomes difficult

**These methods help:**
- Coordinate threads
- Manage execution order
- Improve cooperation between threads

---

## 1. `yield()` Method

### Definition

`yield()` is used when a running thread **voluntarily gives a chance** to other threads to execute.

It is like saying:
> *"I can pause temporarily. If another thread wants CPU, let it execute."*

### Syntax

```java
public static native void yield()
```

### Important Keywords

| Keyword  | Meaning                              |
|----------|--------------------------------------|
| `public` | Accessible everywhere                |
| `static` | Belongs to `Thread` class            |
| `native` | Implemented using OS/native code     |
| `void`   | Returns nothing                      |

### Important Point: Static Method

Because `yield()` is **static**:

```java
Thread.yield();
```

It always affects the **currently executing thread** — NOT some other thread.

---

### Example

```java
class MyThread extends Thread {

    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(Thread.currentThread().getName());
            Thread.yield();
        }
    }
}

public class Test {

    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();

        t1.start();
        t2.start();
    }
}
```

### Possible Output

```
Thread-0
Thread-1
Thread-0
Thread-1
```

OR any mixed order — output is **non-deterministic**.

---

### What Happens Internally?

Suppose `Thread-0` is running and `Thread.yield()` is called:

```
RUNNING  →  RUNNABLE
```

Thread goes back to the ready state. Now the scheduler may:
- Choose another thread to run
- OR continue the same thread again

### ⭐ Very Important Point

> `yield()` gives **NO guarantee**.

It is only a **suggestion/hint** to the scheduler. The scheduler may **ignore it completely**.

---

### Visual Flow

```
Thread Running
      |
      | yield()
      ↓
Runnable State
      |
Scheduler decides
      |
 ─────────────────────────
 |                       |
Other thread runs    Same thread runs again
```

---

### When is `yield()` Useful?

Useful in:
- Cooperative multitasking
- Long-running loops
- Reducing CPU domination by one thread

**Example Scenario:**

A thread printing `1` to `100000` without pause may starve other threads.
Adding `Thread.yield()` inside the loop gives equal-priority threads an opportunity.

---

### Important Characteristics of `yield()`

| Property           | Explanation             |
|--------------------|-------------------------|
| Method Type        | `static`                |
| Return Type        | `void`                  |
| Exception          | No checked exception    |
| State Change       | Running → Runnable      |
| Guarantee          | ❌ No                    |
| Scheduler Dependent| ✅ Yes                   |

---

### Interview Question

**Does `yield()` stop the thread?**

> No. It only pauses temporarily and gives the scheduler a chance. The thread may **resume immediately**.

---

## 2. `join()` Method

### Definition

`join()` is used when **one thread must wait until another thread finishes** execution.

This creates a **dependency** between threads.

### Real-Life Example

Suppose:
- `Thread-1` downloads a file
- `Thread-2` processes the file

`Thread-2` **cannot start** processing before the download completes.

```java
t1.join(); // Thread-2 waits for Thread-1 to finish
```

---

### Syntax

```java
public final void join() throws InterruptedException
```

### Important Keywords

| Keyword                   | Meaning                             |
|---------------------------|-------------------------------------|
| `final`                   | Cannot be overridden                |
| non-static (instance)     | Called on a thread object           |
| `throws InterruptedException` | Requires exception handling    |

### Important Point: Instance Method

Unlike `yield()`, `join()` is called on a thread object:

```java
t1.join();
```

This means: **the current thread waits for `t1` to complete.**

---

### Example

```java
class MyThread extends Thread {

    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Child Thread");
            try {
                Thread.sleep(500);
            } catch (Exception e) { }
        }
    }
}

public class Test {

    public static void main(String[] args) throws InterruptedException {

        MyThread t = new MyThread();

        t.start();

        t.join(); // main thread waits here

        System.out.println("Main Thread");
    }
}
```

### Output

```
Child Thread
Child Thread
Child Thread
Child Thread
Child Thread
Main Thread
```

### What Happened?

Normally, main thread and child thread run **simultaneously**.

But `t.join()` forces the **main thread to wait** until the child thread finishes.

---

### State Transition in `join()`

```
Main Thread Running
        |
        | t.join()
        ↓
     WAITING
        |
Child thread completes
        ↓
     RUNNABLE
        ↓
     RUNNING  (main thread resumes)
```

---

### Why `InterruptedException`?

While a thread is waiting, another thread may **interrupt** it.

Therefore Java forces you to handle it:

```java
// Option 1: try-catch
try {
    t.join();
} catch (InterruptedException e) {
    e.printStackTrace();
}

// Option 2: declare in method signature
public static void main(String[] args) throws InterruptedException {
    t.join();
}
```

---

### Overloaded Versions of `join()`

#### 1. `join()` — Wait forever

```java
t.join();
```

Waits until the thread **fully completes**.

#### 2. `join(milliseconds)` — Timed wait

```java
t.join(3000);
```

Waits **at most 3 seconds**. After timeout, current thread continues regardless.

#### 3. `join(milliseconds, nanoseconds)` — Precise wait

```java
t.join(3000, 500);
```

More precise waiting. Rarely used in real projects or interviews.

---

### Example of Timed Join

```java
t.join(2000); // wait at most 2 seconds
```

- If thread completes **before** 2 seconds → continues immediately
- If thread is **still running** after 2 seconds → continues anyway

---

### Important Characteristics of `join()`

| Property      | Explanation              |
|---------------|--------------------------|
| Method Type   | Instance method          |
| `final`       | ✅ Yes                    |
| Overloaded    | ✅ Yes (3 versions)       |
| Exception     | `InterruptedException`   |
| State Change  | Running → Waiting        |
| Guarantee     | ✅ Yes                    |

---

## 3. Key Difference Between `yield()` and `join()`

| Feature          | `yield()`              | `join()`                      |
|------------------|------------------------|-------------------------------|
| Purpose          | Give chance to others  | Wait for thread completion    |
| Method Type      | `static`               | Instance                      |
| State Change     | Running → Runnable     | Running → Waiting             |
| Exception        | ❌ None                 | ✅ `InterruptedException`      |
| Guarantee        | ❌ No                   | ✅ Yes                         |
| Creates Dependency | ❌ No                | ✅ Yes                         |

---

## 4. Difference Between `sleep()`, `yield()`, and `join()`

| Method    | Purpose                                     |
|-----------|---------------------------------------------|
| `sleep()` | Pause current thread for a **specific time** |
| `yield()` | Give chance to **other threads**             |
| `join()`  | Wait for **another thread to finish**        |

---

## 5. Simple Analogy

| Method    | Analogy                                         |
|-----------|-------------------------------------------------|
| `yield()` | *"I can pause. Someone else may work."*         |
| `join()`  | *"I will wait until you finish."*               |
| `sleep()` | *"I am sleeping for a fixed time."*             |

---

## 6. ⭐ Very Important Interview Questions

### Q1. Can `yield()` guarantee another thread executes?

> **No.** The scheduler may ignore it completely.

### Q2. Can `join()` guarantee waiting?

> **Yes.** The current thread waits until the target thread completes.

### Q3. Which thread waits when `t1.join()` is called?

> The thread that **calls** `t1.join()` waits — **NOT** `t1`.

```java
t1.join(); // Current thread (e.g., main) waits for t1 to finish
```

### Q4. Does `yield()` release locks?

> **No.** It only changes scheduling state. Locks remain **held**.

### Q5. Does `join()` release locks?

> `join()` uses an internal waiting mechanism. Its main purpose is waiting for thread completion — not lock management.

---

## 7. Final Understanding

### `yield()`
- Cooperative scheduling **hint**
- **No guarantee**
- `Running → Runnable`
- **Static** method
- No exception handling required

### `join()`
- Creates **dependency** between threads
- **Guaranteed** waiting
- `Running → Waiting`
- **Instance** method
- Throws `InterruptedException`

---

## One-Line Interview Definitions

**`yield()`**
> `yield()` temporarily pauses the currently executing thread and gives other threads an opportunity to execute.

**`join()`**
> `join()` makes the current thread wait until the target thread completes execution.

---

## Revision Checklist

- [ ] Do I know the syntax and return type of `yield()`?
- [ ] Do I understand why `yield()` is a static method?
- [ ] Can I explain that `yield()` gives no execution guarantee?
- [ ] Do I know the syntax and overloaded versions of `join()`?
- [ ] Can I explain which thread waits when `t1.join()` is called?
- [ ] Do I know why `join()` throws `InterruptedException`?
- [ ] Can I distinguish between `yield()`, `join()`, and `sleep()`?
