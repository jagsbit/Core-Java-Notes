# Core Java: Thread Priorities

Thread priorities in Java are a significant topic for **OCJP/SCJP examinations** and general multi-threading applications.

---

## 1. Introduction to Thread Priorities

Every thread in Java is assigned a **priority**. This is a property of the thread, much like its name.

Priorities are used by the **Thread Scheduler** to decide which thread should be allocated processor time first.

| Type             | Description                                      |
|------------------|--------------------------------------------------|
| Default Priority | Generated automatically by the JVM              |
| Custom Priority  | Explicitly assigned by the programmer            |

---

## 2. Valid Range of Priorities

> ⚠️ A common point of confusion is the valid range for thread priorities.

- **Range:** `1` to `10`
- It is **NOT** `0 to 10` — do not confuse it with an array index

### Priority Constants

| Constant                  | Value | Meaning          |
|---------------------------|-------|------------------|
| `Thread.MIN_PRIORITY`     | `1`   | Minimum Priority |
| `Thread.NORM_PRIORITY`    | `5`   | Normal Priority  |
| `Thread.MAX_PRIORITY`     | `10`  | Maximum Priority |

> **Note:** A higher numerical value simply means **higher priority** to the scheduler. It is not a competitive rank — it is just a hint to the JVM.

---

## 3. How the Thread Scheduler Uses Priorities

When multiple threads are waiting for the processor:

### If priorities differ

The scheduler gives execution preference to the thread with the **higher numerical value**.

```
Thread with priority 8  →  executes before  →  Thread with priority 3
```

### If priorities are the same

The execution order is **unpredictable**. It depends on the underlying scheduler algorithm:

- Round Robin
- First-Come-First-Served
- etc.

> ⚠️ You **cannot rely** on a specific order when priorities are equal.

---

## 4. Methods to Manage Priorities

The `Thread` class provides two `final` methods:

| Method                           | Description                            |
|----------------------------------|----------------------------------------|
| `public final int getPriority()` | Returns the current priority of the thread |
| `public final void setPriority(int p)` | Sets the priority of the thread  |

### Example

```java
Thread t = new Thread();

// Get priority
System.out.println(t.getPriority()); // 5 (default)

// Set priority
t.setPriority(8);
System.out.println(t.getPriority()); // 8
```

### ⚠️ Warning: Invalid Priority Value

If you attempt to set a priority **outside the 1–10 range**, Java throws:

```
java.lang.IllegalArgumentException  (Runtime Exception)
```

```java
t.setPriority(15); // ❌ throws IllegalArgumentException
t.setPriority(0);  // ❌ throws IllegalArgumentException
```

---

## 5. Default Priority Inheritance

> ⭐ A crucial concept — how threads inherit priorities.

### Main Thread

- The **main thread** always has a default priority of **5**.

### Child Threads

- A child thread **inherits** the priority of its **parent thread**.
- If you create a new thread from within the main thread, the child thread defaults to the main thread's priority (`5`), unless explicitly changed.

```java
public class Test {
    public static void main(String[] args) {

        System.out.println("Main thread priority: " + Thread.currentThread().getPriority()); // 5

        Thread child = new Thread(() -> {
            System.out.println("Child thread priority: " + Thread.currentThread().getPriority()); // 5 (inherited)
        });

        child.start();
    }
}
```

### Changing Parent Priority Before Creating Child

```java
public class Test {
    public static void main(String[] args) {

        Thread.currentThread().setPriority(8); // change main thread priority

        Thread child = new Thread(() -> {
            System.out.println("Child priority: " + Thread.currentThread().getPriority()); // 8 (inherited)
        });

        child.start();
    }
}
```

### Important Distinction

> **Parent class** ≠ **Parent thread**

- **Parent class** → the class a thread's class inherits from (OOP concept)
- **Parent thread** → the thread that **invokes `start()`** on the child thread (threading concept)

---

## 6. Real-World Caveat: Platform Dependency

> ⚠️ Thread priorities are **platform-dependent**.

While Java provides the priority mechanism, not all operating systems fully support it.

| Platform               | Behavior                                        |
|------------------------|-------------------------------------------------|
| Some Windows versions  | May **ignore** priority settings entirely       |
| Linux / Unix           | Partially supports priorities                   |
| JVM (abstract level)   | Provides the API, but delegates to OS           |

### Takeaway

> **Never rely entirely on thread priorities** for critical application logic, as behavior is **platform-dependent** and **not guaranteed**.

---

## 7. Summary

Thread priorities in Java are a mechanism to **influence** the thread scheduler. While the developer can set priorities between `1` and `10`, the actual execution order is ultimately at the discretion of the **underlying OS** and the **JVM's scheduler**.

---

## Key Takeaways

| Concept               | Detail                                                                   |
|-----------------------|--------------------------------------------------------------------------|
| Valid range           | `1` (MIN) to `10` (MAX)                                                  |
| Default priority      | Main thread = `5`; child threads inherit from parent                     |
| Invalid value         | Throws `IllegalArgumentException` if value is outside `1–10`            |
| Equal priorities      | Results in **non-deterministic** execution order                         |
| Platform dependency   | Priorities may **not behave identically** across all operating systems   |

---

## 8. Interview Quick Revision

### Priority Constants to Remember

```java
Thread.MIN_PRIORITY  = 1
Thread.NORM_PRIORITY = 5
Thread.MAX_PRIORITY  = 10
```

### Most Asked Questions from this Topic

1. What is the valid range for thread priorities in Java?
2. What is the default priority of the main thread?
3. What priority does a child thread get by default?
4. What exception is thrown when an invalid priority is set?
5. Does higher priority guarantee earlier execution?
6. Are thread priorities guaranteed to work across all platforms?
7. What is the difference between a parent class and a parent thread?

### Quick Answers

| Question                                           | Answer                                      |
|----------------------------------------------------|---------------------------------------------|
| Valid priority range                               | `1` to `10`                                 |
| Default main thread priority                       | `5`                                         |
| Child thread default priority                      | Inherits from parent thread                 |
| Exception on invalid priority                      | `IllegalArgumentException`                  |
| Does high priority guarantee first execution?      | ❌ No — it's just a hint to the scheduler   |
| Platform-dependent?                                | ✅ Yes                                       |

---

## Revision Checklist

- [ ] Do I know the valid range for thread priorities (`1–10`)?
- [ ] Can I recall the three priority constants and their values?
- [ ] Do I understand how child threads inherit priorities from parent threads?
- [ ] Do I know what exception is thrown for an invalid priority value?
- [ ] Do I understand why thread priorities are platform-dependent?
- [ ] Can I distinguish between a **parent class** and a **parent thread**?
