# Reentrant Locks in Java (`ReentrantLock`)

This topic is **VERY IMPORTANT** in advanced Java multithreading.

Until now, we used `synchronized` for thread safety. But `synchronized` has limitations — so Java introduced the `java.util.concurrent.locks` package in **Java 1.5**.

The most important class in this package is: **`ReentrantLock`**

---

## 1. Why `ReentrantLock` Was Introduced

Traditional `synchronized` is simple but **lacks flexibility**.

### Problems with `synchronized`

| Problem                    | Explanation                                         |
|----------------------------|-----------------------------------------------------|
| No timeout support         | Thread waits forever for a lock                     |
| No fairness control        | JVM decides lock order                              |
| No try-lock mechanism      | Cannot attempt alternative work if lock unavailable |
| Hard deadlock management   | Limited control over lock acquisition               |
| No lock monitoring methods | Cannot inspect lock state or waiting threads        |

### Solution

Java introduced the **`Lock` Interface** inside the `java.util.concurrent.locks` package.

---

## 2. `Lock` Interface — Important Methods

| Method                            | Purpose                                          |
|-----------------------------------|--------------------------------------------------|
| `lock()`                          | Acquire the lock                                 |
| `unlock()`                        | Release the lock                                 |
| `tryLock()`                       | Try acquiring without waiting                    |
| `tryLock(long time, TimeUnit u)`  | Wait for a limited time                          |
| `lockInterruptibly()`             | Acquire unless interrupted while waiting         |

---

## 3. What is `ReentrantLock`?

### Definition

> `ReentrantLock` is an **implementation of the `Lock` interface** providing explicit locking operations.

### Simple Meaning

With `synchronized`, JVM handles locking automatically:

```java
synchronized void display() {
    // JVM acquires and releases lock automatically
}
```

With `ReentrantLock`, the **programmer manually controls** locking:

```java
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}
```

---

## 4. Why the Name "Reentrant"?

> ⭐ **VERY IMPORTANT.**

### Meaning of Reentrant

> The **same thread** can acquire the **same lock multiple times** without deadlocking itself.

### Why Is This Needed?

At first this seems strange — *"If a thread already has the lock, why would it need it again?"*

But this happens **very commonly** in real applications because:

> **Synchronized/locked methods often call other synchronized/locked methods** on the same object.

---

### The Problem Without Reentrancy

Suppose Java locks were **NOT** reentrant:

```
Thread-1 enters m1() → acquires lock
Thread-1 calls m2() → needs SAME lock again
Thread-1 waits for lock held by... itself
→ SELF DEADLOCK → Program hangs
```

This would make synchronized method calls **impossible** to chain.

---

### Real Example — Synchronized Method Calling Another

```java
class Test {

    synchronized void m1() {
        System.out.println("Inside m1");
        m2(); // calls another synchronized method
    }

    synchronized void m2() {
        System.out.println("Inside m2");
    }
}
```

**What Happens:**

1. Thread-1 enters `m1()` → acquires object lock
2. Inside `m1()`, calls `m2()`
3. `m2()` is also `synchronized` → needs same object lock
4. Since **same thread already owns lock** → Java allows re-entry
5. No deadlock

**Output:**

```
Inside m1
Inside m2
```

**If locks were NOT reentrant:**

```
Thread enters m1() → gets lock
Calls m2() → needs same lock
Waits forever for itself → SELF DEADLOCK
```

---

### Real-Life Analogy

Imagine you locked your house's main door and went inside.

Now you move from living room → bedroom → kitchen.

Should the system stop you each time saying:
> *"You already entered once!"*

**No** — because you are the **same owner**.

Similarly, the **same thread** can re-enter the same lock.

---

### Real-World Banking Example

```java
class BankAccount {

    synchronized void transfer() {
        validate();       // also synchronized
        updateBalance();  // also synchronized
    }

    synchronized void validate() {
        System.out.println("Validation");
    }

    synchronized void updateBalance() {
        System.out.println("Balance Updated");
    }
}
```

When Thread-1 enters `transfer()`:
- It acquires the object lock
- Calls `validate()` → needs same lock → reentrant → allowed
- Calls `updateBalance()` → needs same lock → reentrant → allowed

Without reentrant locking → **self-deadlock** every time `transfer()` is called.

---

### JVM Maintains a Hold Count

Each time a thread acquires the same lock:

```
lock.lock();  // hold count = 1
lock.lock();  // hold count = 2
```

Each time `unlock()` is called:

```
lock.unlock(); // hold count = 1
lock.unlock(); // hold count = 0  → lock fully released
```

> ⭐ **Lock is released ONLY when hold count becomes 0.**

---

### ⚠️ Important Clarification

> Reentrant does **NOT** mean multiple threads can enter simultaneously.

| Meaning              | Correct? |
|----------------------|----------|
| Same thread can re-enter repeatedly | ✅ YES |
| Multiple threads can enter together | ❌ NO  |

Other threads **still must wait** — reentrancy applies **only to the same thread**.

---

### Visualization

```
Thread-1
   |
   |──── acquires Lock (hold count = 1)
   |
   |──── enters m1()
              |
              |──── enters m2() (hold count = 2)
                         |
                         |──── enters m3() (hold count = 3)

All possible because same thread owns the lock
```

---

## 5. Are `synchronized` Methods and Blocks Also Reentrant?

> **YES — BOTH are reentrant in Java.**

Java's **intrinsic locks (monitor locks)** are **reentrant by default**.

This applies to:
- `synchronized` methods
- `synchronized` blocks
- `ReentrantLock`

---

### Reentrant `synchronized` Method Example

```java
class Test {

    synchronized void m1() {
        System.out.println("Inside m1");
        m2(); // reentrant call
    }

    synchronized void m2() {
        System.out.println("Inside m2");
    }
}

public class Demo {
    public static void main(String[] args) {
        Test t = new Test();
        t.m1();
    }
}
```

**Output:**

```
Inside m1
Inside m2
```

**Internal Hold Count:**

```
Enter m1() → hold count = 1
Enter m2() → hold count = 2
Exit  m2() → hold count = 1
Exit  m1() → hold count = 0  → lock fully released
```

---

### Reentrant `synchronized` Block Example

```java
class Test {

    void m1() {
        synchronized (this) {
            System.out.println("Inside Block-1");

            synchronized (this) { // same lock — reentrant
                System.out.println("Inside Block-2");
            }
        }
    }
}
```

**Output:**

```
Inside Block-1
Inside Block-2
```

The second `synchronized(this)` requires the **same lock** — since the same thread already owns it, re-entry is allowed. **No deadlock.**

---

### Reentrancy Summary Table

| Feature                  | Reentrant? |
|--------------------------|------------|
| `synchronized` method    | ✅ YES      |
| `synchronized` block     | ✅ YES      |
| `ReentrantLock`          | ✅ YES      |

---

## 6. Creating `ReentrantLock`

### Default Constructor — Non-Fair Lock

```java
ReentrantLock lock = new ReentrantLock();
```

Creates a **non-fair lock** (default behavior).

### Fairness Constructor — Fair Lock

```java
ReentrantLock lock = new ReentrantLock(true);
```

Creates a **fair lock**.

---

## 7. Fair Lock vs Non-Fair Lock

> ⭐ **VERY IMPORTANT interview topic.**

### Fair Lock

Threads acquire the lock in **first-come, first-served** order.

> Like a bank queue — the longest-waiting thread gets the lock first.

### Non-Fair Lock

JVM may give the lock to **any thread** — even a newly arrived one.

- Better **performance** and **throughput**
- Less fairness

### Default

> `new ReentrantLock()` creates a **non-fair** lock for better throughput.

| Feature           | Fair Lock          | Non-Fair Lock          |
|-------------------|--------------------|------------------------|
| Lock order        | FIFO (queue)       | Any thread             |
| Fairness          | ✅ Guaranteed       | ❌ Not guaranteed       |
| Performance       | Lower throughput   | Higher throughput      |
| Default?          | ❌ No               | ✅ Yes                  |

---

## 8. Basic Example Using `ReentrantLock`

```java
import java.util.concurrent.locks.ReentrantLock;

class Display {

    ReentrantLock lock = new ReentrantLock();

    void wish(String name) {
        lock.lock();
        try {
            for (int i = 0; i < 5; i++) {
                System.out.print("Good Morning : ");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) { }
                System.out.println(name);
            }
        } finally {
            lock.unlock(); // always in finally
        }
    }
}
```

### ⭐ VERY IMPORTANT — Always `unlock()` in `finally`

```java
lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // guaranteed release even if exception occurs
}
```

**Why?**

If an exception occurs without `finally`:
- Lock may **never be released**
- Other threads **wait forever** → dangerous situation

---

## 9. `tryLock()` Method

> ⭐ One of the **BIGGEST advantages** over `synchronized`.

### Problem with `synchronized`

If lock unavailable → thread **MUST wait**. No choice.

### `tryLock()` Solution

```java
if (lock.tryLock()) {
    try {
        // got the lock — do work
    } finally {
        lock.unlock();
    }
} else {
    // lock not available — do alternative work
}
```

### Behavior

| Situation          | Returns | Thread Waits? |
|--------------------|---------|---------------|
| Lock available     | `true`  | No            |
| Lock not available | `false` | No            |

> **Huge Advantage:** Thread can skip, retry later, or perform alternative operations.

### Real-Life Analogy

| Approach       | Behavior                                   |
|----------------|--------------------------------------------|
| `synchronized` | Stand in ATM queue **forever**             |
| `tryLock()`    | *"ATM busy? I'll go to another ATM."*      |

---

## 10. `tryLock(timeout)` — Timed Version

```java
if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // lock acquired within 5 seconds
    } finally {
        lock.unlock();
    }
} else {
    // could not get lock within 5 seconds
}
```

**Advantage:** Prevents **infinite waiting**, avoids some deadlock situations.

### `TimeUnit` Constants

| Constant                | Value        |
|-------------------------|--------------|
| `TimeUnit.NANOSECONDS`  | Nanoseconds  |
| `TimeUnit.MICROSECONDS` | Microseconds |
| `TimeUnit.MILLISECONDS` | Milliseconds |
| `TimeUnit.SECONDS`      | Seconds      |
| `TimeUnit.MINUTES`      | Minutes      |
| `TimeUnit.HOURS`        | Hours        |
| `TimeUnit.DAYS`         | Days         |

---

## 11. `lockInterruptibly()`

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

- Waits for lock — but can be **interrupted** while waiting
- Throws `InterruptedException` if interrupted

**Useful in:**
- Responsive applications
- Cancellation systems
- Task management systems

---

## 12. Important Monitoring Methods

> `ReentrantLock` provides monitoring features **unavailable** in `synchronized`.

| Method                        | Purpose                                          |
|-------------------------------|--------------------------------------------------|
| `lock.isLocked()`             | Is lock currently occupied?                      |
| `lock.getHoldCount()`         | How many times current thread acquired lock      |
| `lock.isHeldByCurrentThread()`| Does current thread own this lock?               |
| `lock.getQueueLength()`       | Number of threads waiting for lock               |
| `lock.isFair()`               | Is fairness policy enabled?                      |
| `lock.getOwner()`             | Thread currently holding the lock                |

---

## 13. `ReentrantLock` vs `synchronized`

> ⭐ **VERY IMPORTANT interview question.**

| Feature                    | `synchronized`      | `ReentrantLock`         |
|----------------------------|---------------------|-------------------------|
| Simplicity                 | Simple              | More code required      |
| Lock handling              | Automatic           | Manual                  |
| `tryLock` support          | ❌ No                | ✅ Yes                   |
| Timeout support            | ❌ No                | ✅ Yes                   |
| Fairness policy            | ❌ No                | ✅ Yes (`new ReentrantLock(true)`) |
| Interruptible waiting      | ❌ No                | ✅ `lockInterruptibly()` |
| Monitoring APIs            | ❌ No                | ✅ Yes                   |
| Reentrant                  | ✅ Yes               | ✅ Yes                   |
| Cross-method locking       | ❌ No                | ✅ Yes                   |

---

## 14. Real Enterprise Usage

`ReentrantLock` is heavily used in:
- Thread pools
- Concurrent caches
- High-performance servers
- Schedulers
- Banking systems
- Distributed systems

---

## 15. ⭐ Important Interview Questions

### Q1. Why is it called `ReentrantLock`?

> Because the **same thread** can acquire the same lock **multiple times** without deadlocking itself.

### Q2. What happens if `lock()` is called twice by the same thread?

> The **hold count increases**. The thread must call `unlock()` the same number of times to fully release the lock.

### Q3. What if `unlock()` is called fewer times than `lock()`?

> The **lock is not fully released** — other threads continue to wait.

### Q4. Main advantage over `synchronized`?

> `tryLock()`, timeout support, fairness policy, interruptible locking, and monitoring APIs.

### Q5. What is the default lock type?

> **Non-fair** — for better throughput.

### Q6. Why must `unlock()` be in a `finally` block?

> To **guarantee lock release** even if an exception occurs — prevents potential deadlocks.

### Q7. Are `synchronized` methods and blocks also reentrant?

> **Yes.** All Java intrinsic locks (monitor locks) are reentrant by default.

### Q8. What is the internal mechanism that enables reentrancy?

> Java internally maintains an **owner thread** and **hold count** for each lock. The lock is only released when hold count reaches `0`.

---

## Final Revision Table

| Concept             | Key Point                                              |
|---------------------|--------------------------------------------------------|
| Reentrant           | Same thread can reacquire the same lock               |
| Hold Count          | Tracks nested lock acquisitions                        |
| Fair Lock           | FIFO ordering — `new ReentrantLock(true)`             |
| Non-Fair Lock       | Higher performance — default                           |
| `tryLock()`         | No waiting — returns `true`/`false` immediately        |
| `tryLock(timeout)`  | Waits for limited duration                             |
| `unlock()`          | Must release manually — always in `finally`            |
| `synchronized`      | Also reentrant — intrinsic lock                        |

---

## One-Line Interview Definitions

**`ReentrantLock`**
> `ReentrantLock` is an implementation of the `Lock` interface that provides advanced, flexible thread synchronization with support for `tryLock`, timeouts, fairness, and monitoring.

**Reentrant Behavior**
> Reentrant behavior allows the **same thread** to acquire the same lock multiple times without deadlocking itself, tracked internally via a hold count.

**Hold Count**
> An internal counter maintained by the JVM that tracks how many times the current thread has acquired a lock — the lock is released only when hold count reaches `0`.

---

## Revision Checklist

- [ ] Can I explain what "reentrant" means with a code example?
- [ ] Can I demonstrate a real scenario where reentrancy prevents self-deadlock?
- [ ] Do I know that `synchronized` methods and blocks are also reentrant?
- [ ] Do I understand the hold count mechanism?
- [ ] Can I create a `ReentrantLock` with and without fairness?
- [ ] Do I know the difference between fair and non-fair locks?
- [ ] Can I use `tryLock()` with alternative operation logic?
- [ ] Do I know all `TimeUnit` constants?
- [ ] Can I always place `unlock()` in a `finally` block correctly?
- [ ] Can I list at least 5 monitoring methods of `ReentrantLock`?
- [ ] Can I compare `ReentrantLock` vs `synchronized` in a table?
