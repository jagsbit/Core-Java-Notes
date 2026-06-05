# Core Java: Thread Group in Multi-Threading

`ThreadGroup` is an advanced topic within Java multithreading that provides a structured way to organize and manage threads collectively.

---

## 1. Introduction to Thread Group

### Definition

> A `ThreadGroup` is a way to **group multiple threads into a single unit** based on their functionality.

**Examples:**
- All producer threads → one group
- All consumer threads → another group

### Structure

- A thread group contains **a group of threads**
- It can also contain **sub-thread groups** (nested groups)

### Advantage

Maintaining threads in groups allows developers to perform **common operations on all threads** in that group **simultaneously** with a single command.

**Examples of batch operations:**
- Suspend all consumer threads
- Set maximum priority for the group
- Interrupt or stop all threads in the group

---

### Real-World Analogy

Similar to managing contacts in a messenger app:

> Group contacts into **"Friends"** and **"Relatives"** → send a message to the **entire group at once**

Instead of messaging each person individually, you operate on the whole group.

---

## 2. Hierarchy and System Threads

### Root Hierarchy

> Every thread in Java **belongs to some thread group**.

The **System Group** acts as the **root** for all thread groups in Java.

### Default Group Hierarchy

```
System Group  (root)
    |
    └──── Main Thread Group
                |
                └──── Main Thread
                |
                └──── Custom Thread Groups (user-defined)
```

### System-Level Threads

The **System Group** manages critical background (daemon) threads:

| System Thread       | Purpose                         |
|---------------------|---------------------------------|
| Finalizer           | Garbage Collector               |
| Reference Handler   | Reference queue management      |
| Signal Dispatcher   | Handles OS signals              |
| Attach Listener     | JVM monitoring support          |

### Viewing System Group

You can get the system group reference via:

```java
Thread.currentThread().getThreadGroup().getParent()
```

This allows you to **monitor all active system-level threads** like the Garbage Collector.

---

## 3. Creating Thread Groups

Java provides **two primary constructors** for creating thread groups:

### Constructor 1: `ThreadGroup(String name)`

Creates a group with a specific name.

The **parent** of this new group is the thread group of the **currently executing thread**.

```java
ThreadGroup g = new ThreadGroup("FirstGroup");
```

### Constructor 2: `ThreadGroup(ThreadGroup parent, String name)`

Allows **explicit specification** of the parent group.

```java
ThreadGroup g1 = new ThreadGroup("ParentGroup");
ThreadGroup g2 = new ThreadGroup(g1, "ChildGroup");
```

### Adding a Thread to a Group

Pass the `ThreadGroup` to the `Thread` constructor:

```java
ThreadGroup g = new ThreadGroup("ProducerGroup");
Thread t = new Thread(g, "ProducerThread-1");
t.start();
```

---

## 4. Important Methods of `ThreadGroup` Class

### Information Methods

| Method                          | Description                                          |
|---------------------------------|------------------------------------------------------|
| `String getName()`              | Returns the name of the thread group                 |
| `ThreadGroup getParent()`       | Returns the parent thread group                      |
| `void list()`                   | Prints information about the group to the console    |

### Priority Methods

| Method                          | Description                                          |
|---------------------------------|------------------------------------------------------|
| `int getMaxPriority()`          | Returns the maximum priority allowed for the group   |
| `void setMaxPriority(int p)`    | Sets the maximum priority for the group              |

> ⚠️ **Important Note:** `setMaxPriority()` only affects **newly added threads**. Threads **already in the group** with a higher priority are **not affected**.

### Count Methods

| Method                          | Description                                          |
|---------------------------------|------------------------------------------------------|
| `int activeCount()`             | Returns the number of active threads in the group    |
| `int activeGroupCount()`        | Returns the number of active sub-groups              |

### Operation Methods

| Method                              | Description                                             |
|-------------------------------------|---------------------------------------------------------|
| `int enumerate(Thread[] list)`      | Copies references of all active threads into the array  |
| `void interrupt()`                  | Interrupts all waiting/sleeping threads in the group    |
| `void destroy()`                    | Destroys the group and all its sub-groups               |

---

## 5. Code Example

```java
public class ThreadGroupDemo {

    public static void main(String[] args) throws Exception {

        // Create a thread group
        ThreadGroup g = new ThreadGroup("MyGroup");

        // Create threads in the group
        Thread t1 = new Thread(g, () -> {
            System.out.println("Thread-1 running in: "
                + Thread.currentThread().getThreadGroup().getName());
        }, "Thread-1");

        Thread t2 = new Thread(g, () -> {
            System.out.println("Thread-2 running in: "
                + Thread.currentThread().getThreadGroup().getName());
        }, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Group name     : " + g.getName());
        System.out.println("Active count   : " + g.activeCount());
        System.out.println("Max priority   : " + g.getMaxPriority());
        System.out.println("Parent group   : " + g.getParent().getName());

        g.list(); // prints group info
    }
}
```

---

## 6. `enumerate()` — Important Usage Note

> ⭐ **Always use `activeCount()` before `enumerate()`** to correctly size the array.

```java
ThreadGroup g = new ThreadGroup("MyGroup");

// ... add threads to g ...

int count = g.activeCount();
Thread[] threads = new Thread[count];
g.enumerate(threads);

for (Thread t : threads) {
    System.out.println(t.getName());
}
```

> ⚠️ If array size is too small → `ArrayIndexOutOfBoundsException`

---

## 7. Thread Group Hierarchy — Full Picture

```
System Group  (root — managed by JVM)
    |
    |──── Finalizer (GC)
    |──── Reference Handler
    |──── Signal Dispatcher
    |──── Attach Listener
    |
    └──── Main Group
                |
                |──── Main Thread
                |
                └──── Custom Groups (user-defined)
                            |
                            └──── Sub-Groups
```

---

## 8. Key Takeaways

| Concept                   | Detail                                                               |
|---------------------------|----------------------------------------------------------------------|
| Purpose                   | Group related threads for batch management                           |
| Root group                | System Group                                                         |
| Main thread's group       | Main Group (child of System Group)                                   |
| Default max priority      | `10`                                                                 |
| `setMaxPriority()` scope  | Affects only **new** threads added after the call                    |
| `enumerate()` best practice | Always use `activeCount()` first to size the array correctly       |
| System threads visibility | Access via `getThreadGroup().getParent()`                           |

---

## 9. ⭐ Important Interview Questions

### Q1. What is a `ThreadGroup`?

> A `ThreadGroup` is a Java mechanism to group multiple related threads together so that operations can be performed on all of them simultaneously.

### Q2. What is the root thread group in Java?

> The **System Group** — it is the root of all thread groups.

### Q3. Which group does the main thread belong to?

> The **Main Thread Group**, which is a direct child of the System Group.

### Q4. Does `setMaxPriority()` affect existing threads in the group?

> **No.** It only affects threads **added to the group after** the call.

### Q5. What is the default maximum priority of a thread group?

> **10** (`Thread.MAX_PRIORITY`)

### Q6. Why should `activeCount()` be called before `enumerate()`?

> To correctly size the array passed to `enumerate()` and avoid `ArrayIndexOutOfBoundsException`.

---

## Final Revision Points

### Thread Group Hierarchy

```
System → Main Group → Custom Groups
```

### Default Values

- Default max priority = `10`
- Main thread's group = **Main Group**
- Parent of any new group (by default) = group of currently executing thread

### `setMaxPriority()` Behavior

- Only applies to **newly added** threads
- Does **NOT** retroactively change priority of existing threads

### Monitoring System Threads

```java
Thread.currentThread().getThreadGroup().getParent()
// → System Group → contains GC, Signal Dispatcher, etc.
```

---

## One-Line Interview Definitions

**ThreadGroup**
> A `ThreadGroup` is a Java class that allows grouping of multiple threads for collective management and batch operations.

**System Group**
> The root thread group in Java that manages all JVM-level daemon threads like the Garbage Collector and Signal Dispatcher.

---

## Revision Checklist

- [ ] Can I define `ThreadGroup` and explain its purpose?
- [ ] Do I know the full hierarchy: System → Main → Custom Groups?
- [ ] Can I create a thread group using both constructors?
- [ ] Do I know that `setMaxPriority()` only affects new threads?
- [ ] Can I explain how to add a thread to a specific group?
- [ ] Do I know the default max priority of a thread group (`10`)?
- [ ] Can I explain why `activeCount()` must be used before `enumerate()`?
- [ ] Do I know how to access the system group and view system-level threads?
- [ ] Can I list at least 4 important methods of `ThreadGroup`?
