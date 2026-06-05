# Core Java: `java.util.concurrent.locks` Package

This session focuses on the **limitations of the traditional `synchronized` keyword** and the introduction of the `java.util.concurrent.locks` package (introduced in **Java 1.5**) to provide more control and better performance in multithreading.

---

## 1. Limitations of the `synchronized` Keyword

The traditional `synchronized` keyword, while useful, has several significant drawbacks:

### 1. Lack of Flexibility — No "Try" Mechanism

Once a thread tries to acquire a lock, it must **wait indefinitely** until it gets it.

> There is **no way** to "try" to get a lock and move on if it is unavailable.

### 2. Deadlock Risk

Since there is no mechanism to:
- Time out while waiting for a lock
- Attempt alternative operations when a lock is busy

→ Threads can easily enter a **deadlock state**.

### 3. No Fairness Policy

You **cannot control** which thread gets the lock next.

> It is **not guaranteed** that the longest-waiting thread will be prioritized.

### 4. Lack of Monitoring

There is **no API** to check the status of a lock, such as:
- How many threads are waiting for it
- Whether the lock is currently held

### 5. Rigid Scope

`synchronized` must be used either:
- At the **method level**
- Within a **specific block**

It **cannot span across multiple methods** easily.

> Example: You **cannot** acquire a lock in **Method A** and release it in **Method B**.

---

### Summary of Limitations

| Limitation              | Problem                                                  |
|-------------------------|----------------------------------------------------------|
| No try mechanism        | Thread waits indefinitely — blocking                     |
| Deadlock risk           | No timeout or alternative path if lock is unavailable    |
| No fairness control     | Cannot prioritize longest-waiting thread                 |
| No monitoring API       | Cannot check lock status or waiting thread count         |
| Rigid scope             | Cannot acquire in one method and release in another      |

---

## 2. The `java.util.concurrent.locks` Package

> Introduced in **Java 1.5** as a modern, flexible alternative to `synchronized`.

### Key Components

| Component       | Type            | Full Name                                        |
|-----------------|-----------------|--------------------------------------------------|
| `Lock`          | Interface       | `java.util.concurrent.locks.Lock`                |
| `ReentrantLock` | Implementation  | `java.util.concurrent.locks.ReentrantLock`       |

> **`ReentrantLock`** is the **standard implementation** of the `Lock` interface.

---

## 3. Important Methods of the `Lock` Interface

| Method                                  | Description                                                                 |
|-----------------------------------------|-----------------------------------------------------------------------------|
| `void lock()`                           | Acquires the lock. If unavailable, thread **waits indefinitely**            |
| `boolean tryLock()`                     | Attempts to acquire the lock. Returns `true` if successful, `false` if not. Does **not** wait |
| `boolean tryLock(long time, TimeUnit u)`| Tries to acquire the lock within a **specified duration**                   |
| `void lockInterruptibly()`              | Acquires lock if available; waits if not — but can be **interrupted** while waiting |
| `void unlock()`                         | **Releases** the lock. Must be called by the thread that currently holds it |

---

## 4. Detailed Method Explanations

### `lock()`

```java
lock.lock();
```

- Acquires the lock
- If unavailable, thread **waits indefinitely** (similar to `synchronized`)
- Use when you must acquire the lock no matter what

---

### `tryLock()` — ⭐ Most Important

```java
if (lock.tryLock()) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
} else {
    // perform alternative operation
}
```

- Attempts to acquire the lock **immediately**
- Returns `true` if lock acquired, `false` if not
- **Does NOT wait** — non-blocking

> **Key Advantage:** Allows "alternative operations." If a thread fails to get the lock, it can **perform other tasks** instead of entering a waiting state.
>
> This significantly **improves system performance** and avoids deadlocks.

---

### `tryLock(long time, TimeUnit unit)` — Timed Version

```java
if (lock.tryLock(3, TimeUnit.SECONDS)) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
} else {
    // lock not acquired within 3 seconds
}
```

- Tries to acquire the lock within a **specified duration**
- Returns `true` if acquired within time, `false` if timeout expires

### `TimeUnit` Enum

`TimeUnit` is an enum found in `java.util.concurrent`.

| Constant                | Value         |
|-------------------------|---------------|
| `TimeUnit.NANOSECONDS`  | Nanoseconds   |
| `TimeUnit.MICROSECONDS` | Microseconds  |
| `TimeUnit.MILLISECONDS` | Milliseconds  |
| `TimeUnit.SECONDS`      | Seconds       |
| `TimeUnit.MINUTES`      | Minutes       |
| `TimeUnit.HOURS`        | Hours         |
| `TimeUnit.DAYS`         | Days          |

---

### `lockInterruptibly()`

```java
try {
    lock.lockInterruptibly();
    // critical section
} catch (InterruptedException e) {
    // handle interruption
} finally {
    lock.unlock();
}
```

- Acquires lock if available
- If not available, **waits** — but can be **interrupted** while waiting
- Throws `InterruptedException` if interrupted

> Useful when you want a thread to be **cancellable** while waiting for a lock.

---

### `unlock()` — ⚠️ Important Warning

```java
lock.unlock();
```

- **Releases** the lock
- **Must** be called by the thread that **currently holds** the lock

> ⚠️ If you call `unlock()` without currently holding the lock, JVM throws:
> **`IllegalMonitorStateException`**

### ✅ Best Practice — Always Use `finally`

```java
Lock lock = new ReentrantLock();

lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // always releases even if exception occurs
}
```

> **Always release the lock in a `finally` block** to avoid potential deadlocks.

---

## 5. Complete Example — `ReentrantLock`

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Counter {

    private int count = 0;
    private Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
            System.out.println(Thread.currentThread().getName()
                + " -> count = " + count);
        } finally {
            lock.unlock();
        }
    }
}

public class LockDemo {

    public static void main(String[] args) {

        Counter c = new Counter();

        Thread t1 = new Thread(c::increment, "Thread-1");
        Thread t2 = new Thread(c::increment, "Thread-2");
        Thread t3 = new Thread(c::increment, "Thread-3");

        t1.start();
        t2.start();
        t3.start();
    }
}
```

---

## 6. `tryLock()` vs `lock()` — Key Difference

| Feature               | `lock()`                       | `tryLock()`                       |
|-----------------------|--------------------------------|-----------------------------------|
| Waiting               | Waits **indefinitely**         | Does **not** wait                 |
| Return value          | `void`                         | `boolean`                         |
| Blocking              | ✅ Blocking                     | ❌ Non-blocking                    |
| Alternative operations| ❌ Not possible while waiting   | ✅ Possible if lock not acquired   |
| Deadlock risk         | Higher                         | Lower                             |

---

## 7. Why Use `java.util.concurrent.locks`?

| Benefit                | Description                                                                 |
|------------------------|-----------------------------------------------------------------------------|
| Better performance     | Avoid indefinite waiting → reduce bottlenecks and deadlocks                 |
| Fine-grained control   | Check lock availability, specify timeouts, implement fairness policies      |
| Flexibility            | Lock and unlock across different methods — not restricted to one block      |
| Monitoring capability  | APIs to inspect lock state and waiting threads                              |

---

## 8. `synchronized` vs `Lock` Interface

| Feature                      | `synchronized`         | `Lock` (ReentrantLock)         |
|------------------------------|------------------------|--------------------------------|
| Lock acquisition             | Implicit               | Explicit (`lock()`)            |
| Lock release                 | Implicit (auto)        | Explicit (`unlock()`)          |
| Try without waiting          | ❌ Not possible         | ✅ `tryLock()`                  |
| Timeout support              | ❌ No                   | ✅ `tryLock(time, unit)`        |
| Interruptible waiting        | ❌ No                   | ✅ `lockInterruptibly()`        |
| Fairness policy              | ❌ No                   | ✅ `new ReentrantLock(true)`    |
| Lock scope                   | Method/block only      | Can span across methods        |
| Monitoring                   | ❌ No API               | ✅ `getQueueLength()`, etc.     |

---

## 9. ⭐ Important Interview Questions

### Q1. What are the limitations of `synchronized`?

> No try mechanism, deadlock risk due to indefinite waiting, no fairness, no monitoring API, rigid scope.

### Q2. What is `ReentrantLock`?

> The standard implementation of the `Lock` interface in `java.util.concurrent.locks`, introduced in Java 1.5.

### Q3. Difference between `lock()` and `tryLock()`?

| | `lock()` | `tryLock()` |
|-|----------|-------------|
| Blocking | Yes — waits indefinitely | No — returns immediately |
| Return | `void` | `boolean` |

### Q4. What is `TimeUnit`?

> An enum in `java.util.concurrent` used with `tryLock(time, unit)` to specify the waiting duration. Constants: `SECONDS`, `MILLISECONDS`, `MINUTES`, etc.

### Q5. What happens if `unlock()` is called without holding the lock?

> JVM throws **`IllegalMonitorStateException`**.

### Q6. Why should `unlock()` always be in a `finally` block?

> To ensure the lock is always released even if an exception occurs — preventing potential deadlock.

---

## Final Revision Points

### Key Rule

> **`synchronized` is implicit; `Lock` is explicit.**

### `tryLock()` — Most Important Advantage

> Allows threads to perform **alternative operations** instead of blocking indefinitely.

### Always Release in `finally`

```java
lock.lock();
try {
    // work
} finally {
    lock.unlock(); // never skip this
}
```

### Lock vs `synchronized` — One Line

> Use `Lock` when you need **flexibility** (`tryLock`, timeout, interruptibility, cross-method locking) beyond what `synchronized` offers.

---

## One-Line Interview Definitions

**`Lock` Interface**
> A Java interface in `java.util.concurrent.locks` that provides explicit, flexible locking mechanisms as an alternative to the `synchronized` keyword.

**`ReentrantLock`**
> The standard implementation of the `Lock` interface that allows a thread to re-acquire a lock it already holds, with support for `tryLock`, timeouts, and fairness policies.

**`tryLock()`**
> A non-blocking method that attempts to acquire the lock and returns `true` if successful or `false` if not, allowing alternative operations instead of indefinite waiting.

---

## Revision Checklist

- [ ] Can I list all 5 limitations of the `synchronized` keyword?
- [ ] Do I know the package name: `java.util.concurrent.locks`?
- [ ] Can I distinguish between `lock()` (blocking) and `tryLock()` (non-blocking)?
- [ ] Do I know the timed version: `tryLock(long time, TimeUnit unit)`?
- [ ] Can I list all `TimeUnit` constants?
- [ ] Do I know what `lockInterruptibly()` does and when to use it?
- [ ] Do I know that `unlock()` without holding the lock throws `IllegalMonitorStateException`?
- [ ] Can I explain why `unlock()` must always be in a `finally` block?
- [ ] Can I compare `synchronized` vs `Lock` in a table?
- [ ] Do I know that `ReentrantLock` is the standard implementation of `Lock`?
