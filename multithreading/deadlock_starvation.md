# Deadlock and Starvation in Java Multi-Threading

These are two **VERY IMPORTANT** problems in multithreading.

They are heavily asked in:
- Java interviews
- Backend interviews
- OCJP/SCJP
- System design discussions

Many students confuse **Deadlock** and **Starvation** — this guide will make both crystal clear.

---

## 1. What is Deadlock?

### Definition

> Deadlock is a situation where **two or more threads wait for each other forever**, causing the program to hang permanently.

### Simple Meaning

- Thread-1 says: *"I will continue only if I get Lock-B"*
- Thread-2 says: *"I will continue only if I get Lock-A"*

But:
- Thread-1 **already holds** Lock-A
- Thread-2 **already holds** Lock-B

Now both wait forever.

---

### Real-Life Analogy

| Person   | Has        | Needs      |
|----------|------------|------------|
| Person-1 | Pen        | Notebook   |
| Person-2 | Notebook   | Pen        |

Neither releases their resource → **both stuck forever** → **Deadlock**

---

## 2. Most Important Understanding

Students often think:

> *"synchronized solves all multithreading problems."*

**Partially true. But the reality is:**

> Improper use of `synchronized` can **CREATE** deadlock.

### Important Statement

> **Synchronization prevents data inconsistency — BUT may create deadlock.**

---

## 3. Why Deadlock Happens

Main reason: **Circular Waiting**

```
Thread-1 waiting for Thread-2's lock
Thread-2 waiting for Thread-1's lock

→ No thread can proceed → DEADLOCK
```

---

## 4. Practical Deadlock Example

> ⭐ This is the **CLASSIC** interview example.

### Class A

```java
class A {

    synchronized void d1(B b) {
        System.out.println("Thread-1 starts execution of d1()");
        try {
            Thread.sleep(5000);
        } catch (Exception e) { }

        System.out.println("Thread-1 trying to call B.last()");
        b.last();
    }

    synchronized void last() {
        System.out.println("Inside A.last()");
    }
}
```

### Class B

```java
class B {

    synchronized void d2(A a) {
        System.out.println("Thread-2 starts execution of d2()");
        try {
            Thread.sleep(5000);
        } catch (Exception e) { }

        System.out.println("Thread-2 trying to call A.last()");
        a.last();
    }

    synchronized void last() {
        System.out.println("Inside B.last()");
    }
}
```

### Deadlock Thread Setup

```java
class DeadlockDemo extends Thread {

    A a = new A();
    B b = new B();

    public void m1() {
        this.start();  // starts child thread
        a.d1(b);       // main thread enters d1()
    }

    public void run() {
        b.d2(a);       // child thread enters d2()
    }

    public static void main(String[] args) {
        DeadlockDemo d = new DeadlockDemo();
        d.m1();
    }
}
```

---

### Step-by-Step Internal Flow

> ⭐ This is the **MOST IMPORTANT** part.

| Step | Thread       | Action                                          |
|------|--------------|-------------------------------------------------|
| 1    | Main Thread  | Calls `a.d1(b)` → acquires **lock of object A** |
| 2    | Child Thread | Calls `b.d2(a)` → acquires **lock of object B** |
| 3    | Main Thread  | Tries to call `b.last()` → **B lock held by Child** → waits |
| 4    | Child Thread | Tries to call `a.last()` → **A lock held by Main** → waits |
| 5    | Both         | **Waiting forever** → DEADLOCK                  |

### Final Situation

```
Main Thread  → waiting for B lock
Child Thread → waiting for A lock

→ Infinite waiting → Program hangs forever → DEADLOCK
```

---

## 5. Why Is `Thread.sleep()` Used in the Example?

> ⭐ **VERY IMPORTANT** to understand.

Without `Thread.sleep()`:
- One thread may complete **entire execution** before the second thread starts
- No deadlock occurs

`sleep()` **ensures** both threads:
1. Acquire their **first lock**
2. Then try to acquire the **second lock**

This **guarantees** the deadlock scenario occurs.

---

## 6. Important Deadlock Understanding

### Once Deadlock Happens

- Usually **no automatic recovery**
- Program remains stuck indefinitely

### Often the Only Solution

> **Terminate the application.**

### ⭐ Important Interview Point

> Java does **NOT** magically resolve deadlocks. The programmer must **prevent them through design**.

---

## 7. How to Prevent Deadlock

> ⭐ **VERY IMPORTANT interview topic.**

### Best Prevention Technique: Consistent Lock Ordering

#### ❌ Dangerous — Inconsistent Order

```
Thread-1:  Lock A  →  Lock B
Thread-2:  Lock B  →  Lock A    ← opposite order!
```

Circular dependency possible → **Deadlock risk**.

#### ✅ Correct — Same Order for All Threads

```
Thread-1:  Lock A  →  Lock B
Thread-2:  Lock A  →  Lock B    ← same order
```

No circular waiting possible → **No deadlock**.

### Other Prevention Strategies

| Strategy                        | Description                                      |
|---------------------------------|--------------------------------------------------|
| Consistent lock ordering        | All threads acquire locks in the same sequence   |
| Avoid nested synchronization    | Minimize acquiring multiple locks simultaneously |
| Use timeout locks               | `tryLock(timeout)` from `ReentrantLock`          |
| Lock ordering by object identity| Use `System.identityHashCode()` to order locks   |

---

## 8. What is Starvation?

### Definition

> Starvation means a thread **waits for a very long time** because other threads continuously get resources first.

### Important Difference from Deadlock

In starvation:
- Waiting is **long** but **NOT necessarily infinite**
- Eventually the thread **may** execute

---

### Real-Life Analogy

Imagine a restaurant where:
- VIP customers are **always served first**
- Normal customers **keep waiting**

The normal customer is:
- Not permanently blocked
- But **heavily delayed**

This is **Starvation**.

---

### Example in Threads

Suppose:
- Multiple `Priority-10` threads running continuously
- One `Priority-1` thread waiting

The low-priority thread may wait **very, very long** because the scheduler always favors higher-priority threads.

**But eventually:** if high-priority threads stop, the low-priority thread gets CPU → **not infinite waiting**.

---

## 9. Deadlock vs Starvation

> ⭐ **VERY IMPORTANT comparison table.**

| Feature          | Deadlock                       | Starvation                        |
|------------------|--------------------------------|-----------------------------------|
| Waiting Time     | **Infinite**                   | Long but **finite**               |
| Cause            | Circular lock dependency       | Unfair CPU/resource scheduling    |
| Program State    | Completely stuck               | Slow progress                     |
| Recovery         | Usually terminate process      | Eventually executes               |
| Main Problem     | Lock management                | CPU/resource allocation           |

### Visualization

**Deadlock:**

```
T1 waiting for T2's lock
T2 waiting for T1's lock
→ FOREVER — no progress
```

**Starvation:**

```
Low priority thread: waiting...
Low priority thread: waiting...
Low priority thread: ... eventually gets a chance
```

---

## 10. ⭐ Important Interview Questions

### Q1. Does `synchronized` prevent deadlock?

> **No.** Improper synchronization **causes** deadlock.

### Q2. What is the main reason for deadlock?

> **Circular waiting for locks.**

### Q3. Can Java automatically fix deadlock?

> **No.** The programmer must prevent it through proper design.

### Q4. Main difference between deadlock and starvation?

| | Answer |
|-|--------|
| Deadlock | **Infinite** waiting — threads stuck forever |
| Starvation | **Very long** waiting — thread eventually executes |

### Q5. Best deadlock prevention technique?

> **Consistent lock ordering** — all threads acquire locks in the same sequence.

---

## 11. Real Enterprise Understanding

Deadlocks are dangerous in:
- Banking systems
- Databases
- Transaction systems
- Distributed systems

That is why enterprise systems:
- Minimize nested locks
- Use **timeout locks** (`tryLock()` from `ReentrantLock`)
- Apply **lock ordering strategies**
- Use deadlock detection tools and monitoring

---

## 12. Core Internal Understanding

Deadlock fundamentally occurs because:

> Threads **hold partial resources** while **waiting for others**.

This creates a **circular dependency graph**:

```
T1 → holds R1, wants R2
T2 → holds R2, wants R1

Circular dependency → no thread can proceed
```

---

## Final Revision Points

### Deadlock

- Infinite waiting
- Circular lock dependency
- Caused by **improper synchronization**
- **No automatic recovery**

### Starvation

- Unfair waiting
- Often caused by **thread priorities**
- Eventually thread **may execute**

### Golden Rule

> **Multiple locks + inconsistent acquisition order = Deadlock risk**

---

## One-Line Interview Definitions

**Deadlock**
> Deadlock is a condition where two or more threads wait **indefinitely** for each other's locks, causing the program to hang permanently.

**Starvation**
> Starvation is a condition where a thread is denied CPU/resources for a **very long time** due to unfair scheduling favoring other threads.

**Deadlock Prevention**
> Acquire multiple locks always in the **same consistent order** across all threads to eliminate circular waiting.

---

## Revision Checklist

- [ ] Can I define deadlock in one sentence?
- [ ] Can I trace through the classic A/B deadlock example step by step?
- [ ] Do I understand why `Thread.sleep()` is used in the deadlock demo?
- [ ] Can I explain why `synchronized` can cause deadlock?
- [ ] Do I know the best technique to prevent deadlock (consistent lock ordering)?
- [ ] Can I define starvation and distinguish it from deadlock?
- [ ] Do I know that deadlock = infinite wait, starvation = long but finite wait?
- [ ] Can I explain what causes starvation (thread priorities / unfair scheduling)?
- [ ] Do I know that Java does NOT automatically resolve deadlocks?
