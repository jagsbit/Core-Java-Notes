# Core Java: Defining a Thread — Part 2 (Runnable Interface)

This session focuses on the **second way** to define a thread in Java: by implementing the `Runnable` interface. It also covers thread naming, common interview-focused nuances, and thread properties.

---

## 1. Defining a Thread: The Second Approach

While the first approach is extending the `Thread` class, the **recommended way** is to implement the `Runnable` interface.

### The Runnable Interface

| Property     | Detail                                        |
|--------------|-----------------------------------------------|
| Package      | `java.lang`                                   |
| Type         | Functional Interface                          |
| Abstract Method | `public void run()`                        |

### Code Structure

```java
class MyRunnable implements Runnable {
    public void run() {
        // Job of the thread
        for (int i = 0; i < 10; i++) {
            System.out.println("Child Thread");
        }
    }
}
```

### How to Start the Thread

**Step 1:** Create an object of the `Runnable` implementation class.
```java
MyRunnable R = new MyRunnable();
```

**Step 2:** Pass this object to the `Thread` class constructor.
```java
Thread T = new Thread(R);
```

**Step 3:** Call `T.start()` to begin execution.
```java
T.start();
```

### Complete Example

```java
class MyRunnable implements Runnable {
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Child Thread: " + i);
        }
    }
}

public class Test {
    public static void main(String[] args) {
        MyRunnable R = new MyRunnable();  // Runnable object (target)
        Thread T = new Thread(R);         // Thread object (driver)
        T.start();                        // starts new thread

        for (int i = 0; i < 5; i++) {
            System.out.println("Main Thread: " + i);
        }
    }
}
```

### Key Analogy

> - **Runnable object** → the **target** (contains the actual task)
> - **Thread object** → the **driver/starter** (executes the task)

---

## 2. `extends Thread` vs `implements Runnable`

| Feature             | `extends Thread`                          | `implements Runnable`              |
|---------------------|-------------------------------------------|------------------------------------|
| Inheritance         | Consumes your only extension opportunity  | You can still extend another class |
| Flexibility         | Less flexible                             | More flexible                      |
| Recommendation      | ❌ Not recommended                         | ✅ Highly Recommended               |

### Why `implements Runnable` is Better

Java supports **single inheritance** — a class can extend only one class.

If you extend `Thread`, you **lose the ability** to extend any other class.

**Problem with `extends Thread`:**
```java
class MyThread extends Thread {
    // Cannot extend any other class now
}
```

**Solution with `implements Runnable`:**
```java
class MyRunnable extends ParentClass implements Runnable {
    // Can still extend another class ✅
    public void run() {
        // thread job
    }
}
```

> ⭐ **Interview Tip:** Always prefer `implements Runnable` to maintain flexibility with class inheritance.

---

## 3. Case Studies: Thread & Runnable Interactions

> ⭐ Understanding these cases is **critical for interviews**.

### Case 1: `Thread T = new Thread();`

```java
Thread T = new Thread();
T.start();
```

- Starts an **empty** `run()` method
- **No output** — default `Thread.run()` has empty implementation

---

### Case 2: `Thread T = new Thread(R);`

```java
MyRunnable R = new MyRunnable();
Thread T = new Thread(R);
T.start();
```

- Starts the `run()` method **defined in R**
- ✅ Correct way to use `Runnable`

---

### Case 3: Directly Calling `t.run()`

```java
MyRunnable R = new MyRunnable();
Thread T = new Thread(R);
T.run(); // ❌ NOT multi-threading
```

- Does **NOT** create a new thread
- Simply executes `run()` as a **normal method call**
- Runs in the **current thread** (main thread)
- **No multi-threading** happens

---

### Case 4: Calling `r.start()`

```java
MyRunnable R = new MyRunnable();
R.start(); // ❌ Compile-time error
```

- Results in a **compile-time error**
- Because `Runnable` interface does **not contain** a `start()` method
- Only `Thread` class has `start()`

### Summary of Cases

| Code                        | New Thread Created? | Output         | Error?               |
|-----------------------------|---------------------|----------------|----------------------|
| `new Thread(); T.start()`   | ✅ Yes               | No output      | No                   |
| `new Thread(R); T.start()`  | ✅ Yes               | Runnable's job | No                   |
| `T.run()`                   | ❌ No                | Runs in main   | No                   |
| `R.start()`                 | ❌ No                | —              | ✅ Compile-time error |

---

## 4. Thread Class Constructors

There are **8 common constructors** in the `Thread` class:

| #  | Constructor                                                              |
|----|--------------------------------------------------------------------------|
| 1  | `Thread()`                                                               |
| 2  | `Thread(Runnable target)`                                                |
| 3  | `Thread(String name)`                                                    |
| 4  | `Thread(Runnable target, String name)`                                   |
| 5  | `Thread(ThreadGroup group, String name)`                                 |
| 6  | `Thread(ThreadGroup group, Runnable target, String name)`                |
| 7  | `Thread(ThreadGroup group, Runnable target, String name, long stackSize)`|

### Examples

```java
Thread t1 = new Thread();                          // no target, no name
Thread t2 = new Thread(R);                         // with Runnable target
Thread t3 = new Thread("MyThread");                // with name
Thread t4 = new Thread(R, "MyThread");             // target + name
```

---

## 5. Thread Naming and Current Thread Information

### Thread Names

Every thread in Java has a **name**.

- If you don't provide one, the JVM assigns a **default name**:
  - Main thread → `"main"`
  - Child threads → `Thread-0`, `Thread-1`, `Thread-2` ...

### Methods to Manage Names

| Method                            | Description                    |
|-----------------------------------|--------------------------------|
| `public final String getName()`   | Returns the thread's name      |
| `public final void setName(String name)` | Sets the thread's name  |

```java
Thread t = new Thread(R, "DownloaderThread");
System.out.println(t.getName()); // DownloaderThread

t.setName("NewName");
System.out.println(t.getName()); // NewName
```

---

### Getting the Current Thread

Use the **static method**:

```java
Thread.currentThread()
```

To get the name of the currently executing thread:

```java
Thread.currentThread().getName()
```

### Key Insight

| Location              | `Thread.currentThread()` returns |
|-----------------------|----------------------------------|
| Inside `main()` method | `"main"` (Main Thread)          |
| Inside `run()` method  | `"Thread-0"` (Child Thread)     |

### Example

```java
class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Inside run(): " + Thread.currentThread().getName());
    }
}

public class Test {
    public static void main(String[] args) {
        System.out.println("Inside main(): " + Thread.currentThread().getName());

        MyRunnable R = new MyRunnable();
        Thread T = new Thread(R, "ChildThread");
        T.start();
    }
}
```

**Output:**

```
Inside main(): main
Inside run(): ChildThread
```

---

## 6. Summary & Key Takeaways

| Concept                          | Rule / Best Practice                                                       |
|----------------------------------|-----------------------------------------------------------------------------|
| Preferred approach               | Always use `implements Runnable` over `extends Thread`                      |
| Why Runnable is better           | Preserves ability to extend another class (avoids single inheritance limit) |
| `start()` vs `run()`             | `start()` → new thread; `run()` → normal method call in current thread      |
| Default thread name              | Main thread = `"main"`; child threads = `Thread-0`, `Thread-1`, ...        |
| Identify current thread          | Use `Thread.currentThread().getName()`                                      |
| `r.start()`                      | ❌ Compile-time error — `Runnable` has no `start()` method                  |

---

## 7. Interview Quick Revision

### Most Asked Questions from this Topic

1. What is the `Runnable` interface and where is it located?
2. Why is `implements Runnable` preferred over `extends Thread`?
3. What happens when you call `r.start()` on a `Runnable` object?
4. What is the difference between calling `T.run()` vs `T.start()`?
5. How do you get the name of the currently executing thread?
6. What is the default name given to threads by JVM?

---

## Revision Checklist

- [ ] Can I define a thread using `Runnable`?
- [ ] Do I understand why `run()` vs `start()` behaves differently?
- [ ] Can I identify the current executing thread using code?
- [ ] Do I know all 4 Thread & Runnable interaction cases?
- [ ] Can I explain why `implements Runnable` is better than `extends Thread`?
