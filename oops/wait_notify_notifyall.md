# Core Java: Inter-Thread Communication — `wait()`, `notify()`, `notifyAll()`

---

## 1. Introduction to Inter-Thread Communication

**Inter-thread communication** is the mechanism by which two or more threads exchange information or coordinate tasks.

### The Problem

Without communication, if one thread (**T1**) is waiting for data from another thread (**T2**), it might repeatedly check for an update (e.g., polling every 10 minutes). This is known as **"busy waiting"** — a waste of CPU time and system resources.

### The Solution

- The thread **expecting** the update → calls `wait()`
- The thread **providing** the update → calls `notify()`

This way, T1 sleeps efficiently instead of constantly polling.

---

## 2. Key Methods for Communication

Two threads can communicate using these methods present in the **`Object` class**:

| Method | Description |
|--------|-------------|
| `wait()` | Causes the current thread to wait until notified |
| `notify()` | Wakes up a single waiting thread |
| `notifyAll()` | Wakes up all threads that are waiting on the object |

---

## 3. Why Are These Methods in `Object`, Not `Thread`?

> 🎯 **Classic Interview Question:** *"Why are `wait()`, `notify()`, and `notifyAll()` in the `Object` class and not in the `Thread` class?"*

**Answer:**

- Any thread can call these methods on **any Java object**.
- Since they are applicable to **all objects**, they must be defined in the parent `Object` class.
- In contrast, `start()` and `join()` are only relevant to `Thread` objects, so they belong in the `Thread` class.

---

## 4. Rules for Using `wait()`, `notify()`, `notifyAll()`

> ⚠️ **Synchronized Area Requirement:** These methods **must** be called from a **synchronized method or block**.

- If called outside a synchronized context → Java throws **`IllegalMonitorStateException`**.
- A thread must **own the lock** of the object to call these methods on it.
- Owning the lock = being inside a `synchronized` block for that specific object.

---

## 5. Method Prototypes

### `wait()` — Overloaded

```java
public final void wait() throws InterruptedException           // Wait indefinitely
public final native void wait(long ms) throws InterruptedException  // Wait for specific ms
public final void wait(long ms, int ns) throws InterruptedException // Wait for ms + ns
```

### `notify()` and `notifyAll()`

```java
public final native void notify()
public final native void notifyAll()
```

> 📝 **Note:** All `wait()` variants throw `InterruptedException` — a **checked exception** that must be handled with `try-catch` or `throws`.

---

## 6. How Thread States Change

### `wait()`

```
Thread calls wait()
      ↓
Immediately releases the object lock
      ↓
Thread enters → Waiting State
      ↓
Stays there until notify() / notifyAll() is called
```

### `notify()`

```
Thread calls notify()
      ↓
Signals ONE waiting thread
      ↓
Notifying thread may finish remaining tasks in synchronized block
      ↓
Lock is released after the synchronized block ends
```

> 💡 **Only `wait()`, `notify()`, and `notifyAll()` release the object lock.**
> Methods like `sleep()`, `yield()`, and `join()` do **NOT** release locks.

---

## 7. Thread Life Cycle in Inter-Thread Context

```
Waiting State (after wait())
        ↓  notify() received
Blocked State (waiting to re-acquire lock)
        ↓  lock re-acquired
Ready / Runnable State
        ↓  thread scheduler allocates CPU
Running State
```

> ⚠️ A notified thread does **NOT** jump directly to Running — it must first **re-acquire the object lock**.

---

## 8. Example: Producer-Consumer Pattern

```java
class SharedResource {
    int data;
    boolean available = false;

    synchronized void produce(int value) throws InterruptedException {
        while (available) {
            wait(); // Wait until consumer reads the data
        }
        this.data = value;
        available = true;
        System.out.println("Produced: " + value);
        notify(); // Notify the consumer
    }

    synchronized void consume() throws InterruptedException {
        while (!available) {
            wait(); // Wait until producer adds data
        }
        System.out.println("Consumed: " + data);
        available = false;
        notify(); // Notify the producer
    }
}
```

---

## 9. Lock Release Comparison

| Method | Releases Object Lock? |
|--------|-----------------------|
| `wait()` | ✅ Yes — immediately |
| `notify()` | ✅ Yes — after synchronized block ends |
| `notifyAll()` | ✅ Yes — after synchronized block ends |
| `sleep()` | ❌ No |
| `yield()` | ❌ No |
| `join()` | ❌ No |

---

## Summary

| Concept | Key Point |
|---------|-----------|
| `wait()` | Releases lock, thread enters Waiting state |
| `notify()` | Wakes one waiting thread; lock released after block ends |
| `notifyAll()` | Wakes all waiting threads |
| **Location** | Defined in `Object` class (applicable to all objects) |
| **Requirement** | Must be used inside `synchronized` context |
| **Exception** | `wait()` throws `InterruptedException` (checked) |
| **Wrong context** | Throws `IllegalMonitorStateException` |

---

## 🎯 Key Interview Tips

- `wait()`, `notify()`, `notifyAll()` are in **`Object`** because they work on object locks, not threads.
- These are the **only methods** that cause a thread to release an object lock.
- Always handle **`InterruptedException`** when calling `wait()`.
- A notified thread must **re-acquire the lock** before moving to Runnable state.
- `IllegalMonitorStateException` = called outside of `synchronized` block.
