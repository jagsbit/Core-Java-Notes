# Synchronization in Java Multi-Threading

Synchronization is one of the **MOST IMPORTANT** concepts in Java multithreading.

Almost every real-world backend system uses synchronization internally:

- Banking systems
- Booking systems
- Payment systems
- Ticket reservation
- Inventory systems

This topic is heavily asked in:
- Interviews
- OCJP/SCJP exams
- Backend development discussions

---

## 1. Why Synchronization is Needed

### The Core Problem

In multithreading, multiple threads execute simultaneously.

A problem occurs when **multiple threads access the same object simultaneously**, especially when they:
- Modify data
- Update values
- Perform write operations

This leads to: **Data Inconsistency**

---

### Real-Life Analogy

Imagine 5 dogs eating from the **same biryani plate** simultaneously.

Result:
- Chaos
- Inconsistency
- Collision

Similarly, multiple threads modifying the same object simultaneously causes **inconsistent data**.

---

### Example — Bank Balance Problem

Suppose bank balance = `1000`

Two threads execute simultaneously:

| Thread   | Operation  |
|----------|------------|
| Thread-1 | Withdraw 500 |
| Thread-2 | Withdraw 700 |

**Without synchronization:**
- Both may read the old value simultaneously
- Both may update incorrectly
- Final balance may become **wrong/inconsistent**

### This Problem is Called: **Race Condition**

> Multiple threads **race** to modify shared data.

### Solution

Use the `synchronized` keyword.

---

## 2. What is Synchronization?

### Definition

> Synchronization means **allowing only one thread at a time** to access critical/shared code.

OR

> A mechanism to **prevent simultaneous modification** of shared data.

### Main Goal

**Prevent data inconsistency.**

---

## 3. `synchronized` Keyword

### Important Rule

`synchronized` is a **modifier**.

It is applicable **only** to:
- Methods
- Blocks

### ✅ Valid Usage

```java
synchronized void m1() {
    // synchronized method
}

synchronized (this) {
    // synchronized block
}
```

### ❌ Invalid Usage

`synchronized` **cannot** be applied to:
- Classes
- Variables

```java
synchronized class Test { }   // ❌ Invalid
synchronized int x;           // ❌ Invalid
```

---

## 4. How Synchronization Works Internally

> ⭐ This is the **MOST IMPORTANT** part.

### Every Object Has a Lock

In Java, **every object** has one unique lock.

Also called:
- **Monitor lock**
- **Intrinsic lock**

```java
Display d = new Display();
```

Object `d` has **one unique lock**.

---

### Internal Mechanism

Suppose:

```java
synchronized void display() { }
```

When a thread wants to enter a synchronized method:

1. JVM checks lock availability
2. **If lock is free** → Thread acquires lock → enters method
3. **If lock is occupied** → Thread **waits**

### Important Point

Lock handling is **automatic**. The JVM automatically:
- Acquires the lock
- Releases the lock

The programmer does **NOT** manually handle it.

---

### Flow of Synchronization

```
Thread wants synchronized method
              |
              ↓
      Checks object lock
              |
      ──────────────────────
      |                    |
 Lock free           Lock occupied
      |                    |
 Acquire lock            WAIT
      |
 Execute method
      |
 Release lock
```

---

## 5. Object-Level Lock

> ⭐⭐ **VERY VERY IMPORTANT** — commonly misunderstood.

### Lock Belongs to the OBJECT — NOT to the Method

```java
class Display {

    synchronized void m1() { }

    synchronized void m2() { }

    void m3() { }
}
```

```java
Display d = new Display();
```

**Scenario:** Thread T1 executes `d.m1()`

Now the **object lock of `d`** is acquired.

| Method           | Can Another Thread Execute? | Reason                                  |
|------------------|-----------------------------|-----------------------------------------|
| `m1()` (sync)    | ❌ NO                        | Needs the same lock (already occupied)  |
| `m2()` (sync)    | ❌ NO                        | Also synchronized on same object        |
| `m3()` (normal)  | ✅ YES                       | Not synchronized — no lock required     |

### Important Understanding

> **One object = One lock**

All synchronized methods of the **same object** share the **same lock**.

### Visualization

```
Object d
   |
   |──── LOCK
   |
   |──── synchronized m1()  ──→ Needs lock
   |
   |──── synchronized m2()  ──→ Needs lock
   |
   |──── normal m3()        ──→ No lock needed
```

If one synchronized method is running → **all other synchronized methods are blocked** for the same object.

---

## 6. Non-Synchronized Methods and the Object Lock

> ⚠️ This confuses many students.

**Common misconception:**
> *"If the object lock is acquired, the entire object becomes inaccessible."*

**This is NOT true.**

### The Real Rule

The lock is associated with the object, **BUT**:

- Only **synchronized code** checks/acquires that lock
- **Non-synchronized methods** do NOT check the lock
- Non-synchronized methods **bypass the lock completely**

So they can execute freely — even while another thread holds the object's lock.

---

### Example

```java
class Display {

    synchronized void m1() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Synchronized Method");
            try {
                Thread.sleep(1000);
            } catch (Exception e) { }
        }
    }

    void m2() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Normal Method");
        }
    }
}
```

```java
Display d = new Display();
```

| Thread   | Calls      | Can Execute? | Reason                       |
|----------|------------|--------------|------------------------------|
| Thread-1 | `d.m1()`   | ✅ Yes        | Acquires object lock         |
| Thread-2 | `d.m2()`   | ✅ Yes        | No lock check needed         |
| Thread-2 | `d.m1()`   | ❌ No         | Lock already held by Thread-1|

---

### Internal Understanding

When a thread enters a **synchronized method**:

```
JVM: "Acquire object lock first"
```

When a thread enters a **non-synchronized method**:

```
JVM: "No lock checking — enter directly"
```

> **Object lock only restricts synchronized code — NOT normal code.**

---

### Why Java Designed It This Way

Locking everything would **destroy performance**.

Many operations are safe without synchronization:
- Reading data
- Calculations
- Printing
- Utility methods

So Java only blocks **synchronized regions**.

---

### Real-World Example

```java
class BankAccount {

    synchronized void withdraw() {
        // modifies balance — needs lock
    }

    double getBalance() {
        return balance; // just reading — often safe
    }
}
```

**Scenario:** Thread-1 is executing `withdraw()`.

Can Thread-2 call `getBalance()`?

> ✅ **YES** — because `getBalance()` is non-synchronized.

### ⚠️ Important Danger

Even though non-synchronized methods can execute freely, they may read **partially updated / inconsistent data** if synchronization is not designed properly.

> That is why sometimes even **getter methods are synchronized**.

---

### Easy Analogy

Imagine a room with:
- One **VIP locker** (protected)
- Normal chairs (unprotected)

The lock protects **only** the VIP locker area.

People can still:
- Walk in the room
- Sit on chairs
- Use non-protected areas

Similarly:
- **Synchronized methods** require the lock
- **Normal methods** don't care about the lock

---

### Final Key Takeaway on Object Lock

> Even though the lock belongs to the object:
> - Only synchronized methods/blocks **use** that lock
> - Non-synchronized methods **bypass** the lock completely
> - Therefore, multiple threads **can** execute non-synchronized methods simultaneously on the same object

---

## 7. Synchronized Area vs Non-Synchronized Area

### Synchronized Area — Used For

- Updating data
- Deleting records
- Inserting values
- Modifying shared state

> Critical operations that must be protected.

### Non-Synchronized Area — Used For

- Reading data
- Display operations
- Calculations without shared modification

> Many threads can execute simultaneously here.

### Example

```java
// Unsafe — needs synchronization
balance = balance - 500;

// Usually safe — no synchronization needed
System.out.println(balance);
```

---

## 8. Practical Example — `wish()` Method

### Without Synchronization

```java
class Display {

    public void wish(String name) {
        for (int i = 0; i < 5; i++) {
            System.out.print("Good Morning : ");
            try {
                Thread.sleep(1000);
            } catch (Exception e) { }
            System.out.println(name);
        }
    }
}
```

**Suppose:**
- Thread-1 passes `"Dhoni"`
- Thread-2 passes `"Kohli"`

**Possible Output (scrambled):**

```
Good Morning : Good Morning : Dhoni
Kohli
Good Morning : Good Morning : Dhoni
Kohli
```

**Why?** While one thread sleeps, another enters the method — both overlap.

---

### With Synchronization

```java
class Display {

    synchronized public void wish(String name) {
        for (int i = 0; i < 5; i++) {
            System.out.print("Good Morning : ");
            try {
                Thread.sleep(1000);
            } catch (Exception e) { }
            System.out.println(name);
        }
    }
}
```

**Output (clean and consistent):**

```
Good Morning : Dhoni
Good Morning : Dhoni
Good Morning : Dhoni
Good Morning : Kohli
Good Morning : Kohli
```

### What Happened Internally?

- Thread-1 enters `wish()` → **acquires object lock**
- Even during `sleep()` → **lock remains held**
- Thread-2 **cannot enter** until Thread-1 fully exits

### ⭐ Very Important Interview Point

> **`sleep()` does NOT release the synchronized lock.**
>
> Even if the thread sleeps, the lock remains held by that thread.

---

## 9. Advantages of Synchronization

| Advantage                  | Description                                    |
|----------------------------|------------------------------------------------|
| Prevents data inconsistency | Most important — protects shared resources    |
| Thread safety               | Multiple threads can work without corruption  |
| Predictable output          | No irregular/scrambled behavior               |

---

## 10. Disadvantages of Synchronization

| Disadvantage              | Description                                           |
|---------------------------|-------------------------------------------------------|
| Increased waiting         | Threads wait for lock to become available             |
| Performance reduction     | Only one thread executes synchronized code at a time  |
| Deadlock possibility      | Improper synchronization can cause infinite waiting   |

### Important Recommendation

> Use synchronization **only when necessary**. Avoid excessive synchronization.

---

## 11. ⭐ Important Interview Questions

### Q1. Can `synchronized` be applied to a class?

> **No.** Only to **methods** and **blocks**.

### Q2. Can multiple threads execute synchronized methods simultaneously?

**Depends on the object:**

| Scenario                          | Answer |
|-----------------------------------|--------|
| Same object, same method          | ❌ NO  |
| Same object, different sync method| ❌ NO  |
| Different objects, same method    | ✅ YES |

```java
Display d1 = new Display();
Display d2 = new Display();

// Thread-1: d1.m1()
// Thread-2: d2.m1()
// ✅ Can execute simultaneously — different locks
```

### Q3. Who manages locks?

> **JVM manages locks automatically.**

### Q4. Is the lock method-level or object-level?

> **Object-level.** ⭐ Most important.

### Q5. Can a non-synchronized method execute while a synchronized method is running?

> **YES.** Non-synchronized methods bypass the lock entirely.

---

## 12. Final Revision Table

| Concept                          | Key Point                                          |
|----------------------------------|----------------------------------------------------|
| `synchronized` modifier          | Applicable to methods and blocks only              |
| Lock ownership                   | Every object has one unique lock                   |
| Lock scope                       | Object-level (not method-level)                    |
| Synchronized methods             | Require the object's lock before execution         |
| Non-synchronized methods         | No lock needed — execute freely                    |
| `sleep()` inside `synchronized`  | Lock is **NOT** released during sleep              |
| Advantage                        | Data consistency and thread safety                 |
| Disadvantage                     | Performance overhead and possible deadlock         |

---

## One-Line Interview Definitions

**Synchronization**
> Synchronization is the process of allowing only one thread at a time to access shared resources to prevent data inconsistency.

**Object Lock**
> Every Java object has a unique intrinsic lock used to control access to its synchronized methods and blocks.

**`synchronized` Method**
> A synchronized method requires a thread to acquire the object's lock before execution, ensuring only one thread runs it at a time.

**Non-Synchronized Method and the Lock**
> Non-synchronized methods do not check or require the object lock, so they can be executed by multiple threads simultaneously even when the object is locked.

---

## Revision Checklist

- [ ] Do I know where `synchronized` can and cannot be applied?
- [ ] Do I understand that the lock is object-level, not method-level?
- [ ] Can I explain what happens when two threads call synchronized methods on the same object?
- [ ] Can I explain what happens when two threads call synchronized methods on different objects?
- [ ] Do I know that `sleep()` does NOT release the lock?
- [ ] Can I explain why non-synchronized methods can run freely even when the lock is held?
- [ ] Do I understand the trade-off between data safety and performance in synchronization?
- [ ] Can I explain Race Condition and how synchronization solves it?

---

---

# Synchronization Part 2 — Object-Level Lock vs Class-Level Lock

This is one of the **MOST IMPORTANT** synchronization topics for:
- Interviews
- OCJP/SCJP
- Backend development

Students usually understand `synchronized void m()` but get confused about:
- Multiple objects
- Static synchronized methods
- Class-level locks

This topic clears that confusion.

---

## 1. Synchronization Works Only on the SAME Object

> ⭐ This is the **MOST IMPORTANT RULE**.

### Rule

```
Same Object      →  Same Lock      →  Synchronization WORKS
Different Objects →  Different Locks →  No Synchronization Effect
```

### Why?

Because every object has its **own unique lock**.

---

### Example Class

```java
class Display {

    synchronized void wish(String name) {
        for (int i = 0; i < 3; i++) {
            System.out.print("Good Morning : ");
            try {
                Thread.sleep(1000);
            } catch (Exception e) { }
            System.out.println(name);
        }
    }
}
```

---

### Case 1: One Object + Multiple Threads

```java
Display d = new Display();

MyThread t1 = new MyThread(d, "Dhoni");
MyThread t2 = new MyThread(d, "Kohli");
```

Both threads use the **same object** `d`.

**What Happens?**

- Thread-1 enters `wish()` → acquires **lock of object `d`**
- Thread-2 **cannot enter** → must wait

**Output (regular and consistent):**

```
Good Morning : Dhoni
Good Morning : Dhoni
Good Morning : Dhoni
Good Morning : Kohli
Good Morning : Kohli
Good Morning : Kohli
```

> Synchronization worked because both threads competed for the **same object lock**.

---

### Case 2: Two Objects + Two Threads

```java
Display d1 = new Display();
Display d2 = new Display();

MyThread t1 = new MyThread(d1, "Dhoni");
MyThread t2 = new MyThread(d2, "Kohli");
```

Now:
- Thread-1 uses `d1`
- Thread-2 uses `d2`

**There are now TWO different locks:**
- `d1` lock
- `d2` lock

**What Happens?**

- Thread-1 acquires `d1` lock
- Thread-2 acquires `d2` lock
- **No conflict** — both execute simultaneously

**Output (mixed/irregular):**

```
Good Morning : Good Morning : Dhoni
Kohli
Good Morning : Good Morning : Dhoni
Kohli
```

---

### ⭐ Very Important Understanding

**Wrong thinking:**
> *"A synchronized method means only one thread in the entire JVM."*

**Correct understanding:**
> *"Only one thread per object lock."*

### Golden Rule

> **Different Objects = Different Locks → Synchronization effect disappears**

---

### Visualization

**One Object:**

```
Object d
   |
   |──── ONE LOCK
   |
Thread-1 and Thread-2 compete for same lock → Synchronization works
```

**Two Objects:**

```
Object d1 ──── Lock-1    ← Thread-1 uses this

Object d2 ──── Lock-2    ← Thread-2 uses this

Both threads get separate locks → No waiting
```

---

### Real-Life Analogy

| Scenario                      | Result              |
|-------------------------------|---------------------|
| 2 dogs eating from 1 plate    | Need coordination   |
| 2 dogs eating from 2 plates   | No issue at all     |

---

## 2. Static Synchronization

> ⭐ Another **VERY IMPORTANT** concept.

### Instance Synchronized Method

```java
synchronized void m() { }
```

Uses → **object lock**

### Static Synchronized Method

```java
static synchronized void m() { }
```

Uses → **class-level lock**

### Why?

Because static members belong to the **class itself** — NOT to individual objects.

---

## 3. Class-Level Lock

> ⭐ **VERY IMPORTANT**

### Every Class Also Has One Lock

Just like every object has a lock, every **class** in Java also has one **class-level lock**.

```
Display.class  →  represents the Class object  →  holds the class-level lock
```

### Example

```java
class Display {

    static synchronized void show() {
        // requires Display.class lock
    }
}
```

When a thread calls `Display.show()`, it must acquire the **`Display.class` lock**.

### ⭐ Very Important

> There is **ONLY ONE** class-level lock per class — regardless of the number of objects.

### Consequence

No two threads can execute **any static synchronized method** of the same class simultaneously.

```java
class Display {

    static synchronized void m1() { }

    static synchronized void m2() { }
}
```

If Thread-1 is executing `m1()`:

- Thread-2 **cannot execute** `m2()`
- Because both require the **same class-level lock**

---

### Important Difference

| Method Type                | Lock Used                   |
|----------------------------|-----------------------------|
| `synchronized` instance method | `this` → object-level lock |
| `static synchronized` method   | `ClassName.class` → class-level lock |

---

## 4. Interaction Between Object Lock and Class Lock

> ⭐ **VERY IMPORTANT INTERVIEW QUESTION**

```java
class Display {

    synchronized void m1() { }           // object lock

    static synchronized void m2() { }    // class-level lock
}
```

**Question:** If Thread-1 is executing `m1()`, can Thread-2 execute `m2()`?

> ✅ **YES**

**Why?**

- `m1()` requires the **object lock**
- `m2()` requires the **class-level lock**
- These are **different locks** → no conflict

### Lock Summary Table

| Method Type                     | Lock Used          |
|---------------------------------|--------------------|
| `synchronized` instance method  | Object lock        |
| `static synchronized` method    | Class-level lock   |

---

## 5. Practical Example — Numbers and Characters

### Without Synchronization

```java
class Display {

    void displayNumbers() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(i);
            Thread.sleep(1000);
        }
    }

    void displayCharacters() {
        for (char c = 'A'; c <= 'E'; c++) {
            System.out.println(c);
            Thread.sleep(1000);
        }
    }
}
```

**Output (mixed):**

```
1
A
2
B
3
C
```

Both threads execute simultaneously → mixed output.

---

### With Synchronization

```java
synchronized void displayNumbers() { ... }

synchronized void displayCharacters() { ... }
```

**Output (clean):**

```
1
2
3
4
5
A
B
C
D
E
```

OR reverse order — but **NOT mixed**.

**Why?** Because only one thread can hold the **same object lock** at a time.

---

## 6. Performance Understanding

Synchronization improves:
- ✅ **Data safety**

But reduces:
- ❌ **Parallel execution**

> Excessive synchronization **hurts performance**.

### Real Enterprise Practice

Backend systems usually:
- Synchronize **only critical sections**
- Avoid full-method synchronization unnecessarily

---

## 7. ⭐ Most Important Interview Questions

### Q1. Does synchronization work across different objects?

> **No.** Different objects have different locks — no synchronization effect.

### Q2. What lock does a `synchronized` instance method use?

> **Object lock** (`this`)

### Q3. What lock does a `static synchronized` method use?

> **Class-level lock** (`ClassName.class`)

### Q4. Can a static synchronized and an instance synchronized method run simultaneously?

> ✅ **Yes** — they use different locks.

### Q5. How many class-level locks exist per class?

> **One** — regardless of how many objects are created.

---

## 8. Final Revision Table (Part 2)

| Concept                                      | Lock Used / Behavior                        |
|----------------------------------------------|---------------------------------------------|
| `synchronized` instance method               | Object lock (`this`)                        |
| `static synchronized` method                 | Class-level lock (`ClassName.class`)        |
| Same object, multiple threads                | Synchronization works                       |
| Different objects, multiple threads          | Synchronization has no effect               |
| Two static synchronized methods (same class) | Block each other (same class lock)          |
| Instance sync + static sync simultaneously   | ✅ Allowed (different locks)                |
| Normal (non-synchronized) methods            | Unaffected by any lock                      |

---

## Core Understanding

Synchronization is fundamentally **lock management**.

Locks exist at **two levels**:

| Level         | Scope          | Used By                          |
|---------------|----------------|----------------------------------|
| Object Level  | One per object | `synchronized` instance methods  |
| Class Level   | One per class  | `static synchronized` methods    |

---

## One-Line Interview Definitions (Part 2)

**Object-Level Lock**
> Lock associated with a specific object instance, used by synchronized instance methods.

**Class-Level Lock**
> Lock associated with the `Class` object (`ClassName.class`), used by static synchronized methods.

**Static Synchronization**
> When a static method is synchronized, it acquires the class-level lock, ensuring only one thread executes any static synchronized method of that class at a time.

---

## Updated Revision Checklist

- [ ] Do I understand why synchronization only works on the same object?
- [ ] Can I explain what happens with two threads and two different objects?
- [ ] Do I know the difference between object-level and class-level locks?
- [ ] Can I explain what lock a `static synchronized` method uses?
- [ ] Do I know that instance sync and static sync can run simultaneously?
- [ ] Can I explain how many class-level locks exist per class?
- [ ] Do I understand the performance trade-off of excessive synchronization?
