# Daemon Threads in Java

Daemon threads are one of the **MOST IMPORTANT** concepts in Java multithreading interviews.

Students usually understand normal threads, synchronization, and wait/notify — but daemon threads explain **background JVM support behavior**.

---

## 1. What is a Daemon Thread?

### Definition

> A daemon thread is a **background support thread** that provides services to user (non-daemon) threads.

### Simple Meaning

Daemon threads work **behind the scenes**. They usually:
- Support application execution
- Perform maintenance tasks
- Help JVM operations

---

### Real-Life Analogy — Movie Shooting

> ⭐ This analogy is **VERY IMPORTANT**.

**Main Actors (User/Non-Daemon Threads):**
- Hero
- Heroine

These do the **visible main work**.

**Background Support Staff (Daemon Threads):**
- Makeup artists
- Light crew
- Camera operators
- Director assistants

These **support** the main actors.

### Important Understanding

> The movie exists mainly because of the **main actors**.
> Support staff exist **only to help** the actors.

Similarly:
> **Daemon threads exist only to support user threads.**

---

## 2. Main Objective of Daemon Threads

### Purpose

Daemon threads provide **background services** for user threads.

### Best Example → Garbage Collector

> ⭐ **Very important.**

**Scenario:**

Suppose the main application creates many objects. Memory becomes low.

JVM automatically runs the **Garbage Collector** — which is a **daemon thread**.

**GC Work:**
- Removes unused objects
- Frees memory
- Allows user thread to continue execution smoothly

> Without daemon threads, JVM maintenance becomes difficult.

---

### What Does "Support" Mean?

When daemon threads "support" user threads, they do **background/helper work** — NOT the primary business logic.

**Example — Garbage Collector:**

Your program creates objects:

```java
new Student();
new Employee();
new Order();
```

Some objects later become useless but still occupy memory.

The **GC thread** runs in the background and removes them.

GC is NOT:
- Processing user orders
- Handling banking logic
- Rendering UI

GC IS:
- Helping user threads continue working smoothly

That is why it is a **support/helper thread**.

---

### Real-Life Restaurant Analogy

| Role                   | Thread Type   |
|------------------------|---------------|
| Chefs, Waiters         | Non-Daemon    |
| Cleaners, Dishwashers  | Daemon        |

Cleaners support restaurant operations without doing the actual business logic.

### Another Example — Autosave in VS Code

| Action             | Thread Type   |
|--------------------|---------------|
| Typing, cursor, UI | Non-Daemon    |
| Autosave every few seconds | Daemon |

Autosave supports your work but is not the primary application purpose.

---

## 3. Examples of Daemon Threads

Common JVM daemon threads:

| Daemon Thread       | Work                         |
|---------------------|------------------------------|
| Garbage Collector   | Memory cleanup               |
| Signal Dispatcher   | Handles OS signals           |
| Attach Listener     | JVM monitoring support       |
| Finalizer           | Cleanup activities           |

---

## 4. Priority of Daemon Threads

Daemon threads usually have **low priority** because user threads are more important.

### JVM Can Increase Priority Temporarily

Suppose memory is critically low → JVM may temporarily increase GC priority → after cleanup, reduces priority again.

---

## 5. Methods Related to Daemon Threads

### `isDaemon()`

Checks whether a thread is a daemon thread.

```java
public boolean isDaemon()
```

```java
Thread t = new Thread();
System.out.println(t.isDaemon()); // false by default
```

---

### `setDaemon()`

Used to change the daemon nature of a thread.

```java
public void setDaemon(boolean b)
```

```java
Thread t = new Thread();
t.setDaemon(true); // make daemon
```

---

### ⭐ VERY IMPORTANT RULE

> You **MUST** call `setDaemon()` **BEFORE** calling `start()`.

#### ✅ Correct

```java
Thread t = new Thread();
t.setDaemon(true);  // set BEFORE start
t.start();
```

#### ❌ Wrong

```java
Thread t = new Thread();
t.start();
t.setDaemon(true); // ❌ too late
```

**Result:** `IllegalThreadStateException`

**Why?** Because the thread has already started execution — JVM does not allow changing thread nature afterward.

---

## 6. Default Nature of Threads

> ⭐ **VERY IMPORTANT interview topic.**

### Main Thread

The main thread is **ALWAYS non-daemon** by default.

```java
public class Test {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().isDaemon()); // false
    }
}
```

**Output:** `false`

---

### Child Threads

A child thread **inherits the daemon nature** of its parent thread.

| Parent Thread  | Child Thread    |
|----------------|-----------------|
| Daemon         | Daemon          |
| Non-Daemon     | Non-Daemon      |

> **Daemon nature is inherited: parent → child**

---

## 7. Can We Make the Main Thread Daemon?

> **No.**

**Why?**

- The main thread is **already started by JVM** before `main()` executes
- `setDaemon()` is only allowed **before** `start()`
- Since we cannot call `setDaemon()` before the main thread starts, we **cannot change its nature**

---

## 8. ⭐ MOST IMPORTANT RULE — JVM Termination Rule

> This is the **MOST IMPORTANT** daemon thread concept.

### Rule

> **When the last non-daemon thread terminates, JVM automatically terminates all daemon threads.**

### Important Meaning

- Daemon threads **cannot keep JVM alive**
- Only **user (non-daemon) threads** keep JVM running

### Visualization

```
Is any non-daemon thread alive?
          |
   YES    |    NO
          |
JVM continues    JVM shuts down
                       ↓
          All daemon threads killed immediately
```

### Why Does JVM Do This?

Daemon threads exist **only to support user threads**.

If no user thread remains → daemon threads are **useless** → JVM shuts down.

---

### JVM's Internal Thinking

Suppose only daemon threads remain.

JVM thinks:

> *"No real application work remains."*

So JVM shuts down. Because daemon threads alone are **not considered meaningful application execution**.

---

## 9. Practical Example

### Code

```java
class MyThread extends Thread {

    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println("Child Thread");
            try {
                Thread.sleep(1000);
            } catch (Exception e) { }
        }
    }
}

public class Test {

    public static void main(String[] args) {

        MyThread t = new MyThread();
        t.setDaemon(true); // make daemon
        t.start();

        System.out.println("End of Main Thread");
    }
}
```

### Possible Output

```
End of Main Thread
```

OR maybe:

```
Child Thread
End of Main Thread
```

### Why Does the Child Thread Stop Suddenly?

- Main thread finishes
- No non-daemon threads remain
- JVM **immediately terminates** the daemon child thread — even if it hasn't completed

> **Daemon threads may terminate abruptly. JVM does NOT wait for them.**

---

### If We Remove `setDaemon(true)`

Then child becomes a **normal (non-daemon) thread**.

Now JVM **waits** until the child thread completes.

**Output:**

```
End of Main Thread
Child Thread
Child Thread
Child Thread
... (all 10 iterations complete)
```

---

## 10. Real-Life Office Analogy

| Role                  | Thread Type   |
|-----------------------|---------------|
| Main employees        | Non-daemon    |
| Cleaning/support staff| Daemon        |

When all main employees leave → cleaning staff also leaves → office closes.

Similarly:
- No user threads → JVM shuts down → daemon threads terminated

---

## 11. Daemon vs User Thread Comparison

| Feature              | Daemon Thread             | User Thread               |
|----------------------|---------------------------|---------------------------|
| Purpose              | Background support work   | Main application logic    |
| JVM waits?           | ❌ No                      | ✅ Yes                     |
| Keeps JVM alive?     | ❌ No                      | ✅ Yes                     |
| Example              | Garbage Collector         | Main thread               |
| Termination          | Automatic (when no user thread) | Completes normally   |
| Default nature       | Inherited from parent     | Main = always non-daemon  |

---

## 12. ⭐ Important Interview Questions

### Q1. What is a daemon thread?

> A background support thread that provides services to user threads.

### Q2. Example of a daemon thread?

> **Garbage Collector**

### Q3. Can a daemon thread keep the JVM alive?

> **No.**

### Q4. What happens when the last non-daemon thread ends?

> **JVM automatically terminates all daemon threads.**

### Q5. Can we change daemon nature after `start()`?

> **No.** Results in `IllegalThreadStateException`.

### Q6. Is the main thread a daemon thread?

> **No.** Main thread is always non-daemon.

### Q7. Do child threads inherit daemon nature?

> **Yes.** Child inherits daemon nature from parent.

### Q8. Why do daemon threads "support" user threads?

> They perform **background helper work** (like memory cleanup, monitoring, autosave) that assists user threads — but do NOT perform the primary application logic.

---

## 13. Core Internal Understanding

JVM treats daemon threads as **background infrastructure work** — not **main application work**.

That is why JVM does NOT wait for daemon threads when all user threads finish.

**One-Line Core Concept:**

> Daemon threads are the "backstage crew" — essential support, but the show ends when the main actors leave.

---

## Final Revision Points

### Daemon Thread

- Background helper thread
- Supports user threads
- JVM does **NOT** wait for completion
- May terminate **abruptly**

### Key Rules

| Rule | Detail |
|------|--------|
| `setDaemon()` timing | Must call **before** `start()` |
| Main thread | Always **non-daemon** |
| Daemon inheritance | Child inherits from parent |
| JVM termination | Last non-daemon ends → all daemons killed |

---

## One-Line Interview Definitions

**Daemon Thread**
> A daemon thread is a background support thread that provides services to user threads and terminates automatically when all user threads finish execution.

**User (Non-Daemon) Thread**
> A non-daemon thread responsible for the main execution logic of the application that keeps the JVM alive until it completes.

**JVM Termination Rule**
> JVM terminates automatically when all non-daemon (user) threads finish, killing all remaining daemon threads immediately.

---

## Revision Checklist

- [ ] Can I define a daemon thread in one sentence?
- [ ] Can I give a real example of a daemon thread (Garbage Collector)?
- [ ] Do I know that daemon threads cannot keep JVM alive?
- [ ] Can I explain what happens when the last non-daemon thread finishes?
- [ ] Do I know that `setDaemon()` must be called before `start()`?
- [ ] Can I explain why calling `setDaemon()` after `start()` throws `IllegalThreadStateException`?
- [ ] Do I know that the main thread is always non-daemon?
- [ ] Can I explain daemon nature inheritance from parent to child?
- [ ] Do I understand what "support" means in the context of daemon threads?
