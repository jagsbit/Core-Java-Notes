# Garbage Collection (GC) in Java

Garbage Collection (GC) is one of the most important features of Java.
It automatically manages memory by removing unused objects from memory, so developers do not need to manually free memory like in C or C++.

---

## 1. What is Garbage Collection?

Garbage Collection is the process of automatically identifying and deleting objects that are no longer used by the program.

### Definition

> Garbage Collection in Java is the **automatic process of reclaiming memory** occupied by unreachable objects.

### Why GC is Needed?

Without GC, programs can face:

- Memory leaks
- Dangling pointers
- Manual memory management complexity

Java solves this by allowing the **JVM to automatically clean unused objects**.

---

## 2. JVM Memory Structure

Before understanding GC, we must understand JVM memory areas.

### JVM Memory Areas

---

### 1. Stack Memory

Stores:

- Method calls
- Local variables
- Primitive data types

> Each thread has its own stack.

---

### 2. Heap Memory

All objects are stored in **Heap Memory**.

Heap is divided into:

#### 🔹 Young Generation

Used for **newly created objects**.

Contains:
- Eden Space
- Survivor Space S0
- Survivor Space S1

##### Eden Space
New objects are created here.

##### Survivor Spaces
Objects that survive GC move between S0 and S1.

---

#### 🔹 Old Generation (Tenured)

Objects that survive **many GC cycles** are moved here.

> These are long-living objects.

---

#### 🔹 Metaspace / Method Area

Stores:
- Class metadata
- Static variables
- Method information

---

## 3. Working of Garbage Collection

GC mainly works in **three phases**:

### Step 1: Mark Phase

- JVM identifies reachable objects.
- Objects still referenced are **marked as alive**.

### Step 2: Sweep Phase

- Unused objects are **removed from memory**.

### Step 3: Compact Phase

- Remaining objects are **rearranged** to remove memory fragmentation.
- This improves memory allocation efficiency.

---

## 4. Reachability

An object becomes eligible for GC when it is **no longer reachable**.

### Reachable Object
An object that can still be accessed through references.

### Unreachable Object
An object with **no active references**.

### Example

```java
public class GCDemo {
    public static void main(String[] args) {

        String s1 = new String("Hello");
        String s2 = new String("World");

        s1 = s2;

        // "Hello" object becomes unreachable

        System.gc();
    }
}
```

After:
```java
s1 = s2;
```

The `"Hello"` object has no reference, so it becomes **eligible for garbage collection**.

---

## 5. Types of Garbage Collection

### 1. Minor GC

- Cleans **Young Generation**.
- Triggered **frequently** because most objects die quickly.

#### Process
- Cleans Eden space
- Moves surviving objects to Survivor spaces

---

### 2. Major GC / Full GC

- Cleans **Old Generation**.
- **Slower** because old objects are large and long-living.

#### Characteristics
- Takes more time
- Causes **application pause**

---

## 6. Types of Garbage Collectors in Java

### 1. Serial Garbage Collector

- Uses **single thread**
- Suitable for **small applications**

#### JVM Option:
```
-XX:+UseSerialGC
```

| ✅ Advantages | ❌ Disadvantages |
|--------------|-----------------|
| Simple | Long pause times |
| Low overhead | |

---

### 2. Parallel Garbage Collector

- Uses **multiple threads**
- Improves **throughput**

#### JVM Option:
```
-XX:+UseParallelGC
```

#### Best For
- Multi-threaded applications

---

### 3. CMS (Concurrent Mark Sweep)

- Reduces **pause time**
- Performs GC **concurrently** with application

#### JVM Option:
```
-XX:+UseConcMarkSweepGC
```

| ✅ Advantage | ❌ Disadvantage |
|-------------|----------------|
| Low latency | Memory fragmentation |

---

### 4. G1 Garbage Collector (Garbage First)

Modern **default collector**.

- Divides heap into **regions**
- Cleans regions with **most garbage first**

#### JVM Option:
```
-XX:+UseG1GC
```

| ✅ Advantages |
|--------------|
| Predictable pause times |
| Good for large applications |

---

### 5. ZGC and Shenandoah

**Low latency** collectors.

#### JVM Options:
```
-XX:+UseZGC
-XX:+UseShenandoahGC
```

#### Features
- Very small pause times
- Suitable for **real-time systems**

---

## 7. What is a Memory Leak?

A **memory leak** occurs when the application keeps holding onto objects that are **no longer needed**, preventing the GC from reclaiming that memory.

> Even though Java has automatic GC, memory leaks can still occur if references to unused objects are never released.

### How a Memory Leak Happens

- An object is created and stored in memory.
- The object is **no longer needed** by the program.
- But a **reference still exists** (e.g., in a collection or static variable), so GC **cannot remove it**.
- Over time, memory fills up and leads to **`OutOfMemoryError`**.

### Common Causes of Memory Leaks

| Cause | Description |
|-------|-------------|
| **Holding unnecessary references** | Keeping objects in collections or static variables that are no longer used |
| **Creating excessive objects** | Too many short-lived objects created without being garbage collected |
| **Unclosed external resources** | Not closing database connections, network sockets, or file streams |

### Example of a Memory Leak

```java
import java.util.ArrayList;
import java.util.List;

public class MemoryLeakDemo {
    static List<String> list = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            list.add(new String("LeakingData"));
            // Objects are added but never removed
            // GC cannot collect them because 'list' still holds references
        }
    }
}
```

> The `list` keeps growing indefinitely → **Memory Leak**.

---

## 8. Prevention of Memory Leaks

To maintain an efficient application, developers should follow these best practices:

---

### 1. Nullify References

Explicitly set object references to `null` when the object is no longer needed.

```java
String s = new String("Hello");

// After use, nullify the reference
s = null;

// Now the "Hello" object is eligible for GC
System.gc();
```

---

### 2. Use Try-with-Resources

Use the **try-with-resources** statement (introduced in Java 7) to automatically close external resources like database connections or IO streams.

```java
// Without try-with-resources (Risk of resource leak)
BufferedReader br = new BufferedReader(new FileReader("file.txt"));
String line = br.readLine();
br.close(); // Must be manually closed — easy to forget!

// With try-with-resources (Recommended)
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line = br.readLine();
    // 'br' is automatically closed after the block
}
```

> Resources are **automatically closed**, preventing resource and memory leaks.

---

### 3. Optimize Object Creation

Avoid creating redundant or excessive objects.

```java
// Bad Practice — Creates multiple String objects on Heap
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i; // Each '+' creates a new String object
}

// Good Practice — Use StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i); // Reuses the same object
}
String result = sb.toString();
```

---

### 4. Remove Objects from Collections When No Longer Needed

```java
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");

// Remove when no longer needed
list.remove("A");
list.clear(); // Or clear entire list
```

---

### 5. Use Memory Profiling Tools

Use specialized tools to **monitor memory consumption** and identify which objects are causing leaks during debugging.

| Tool | Description |
|------|-------------|
| **VisualVM** | Visual memory and CPU profiler for JVM |
| **Eclipse MAT** | Memory Analyzer Tool for heap dump analysis |
| **JProfiler** | Advanced Java profiling tool |
| **YourKit** | Java & Kotlin profiler |

---

### Summary: Memory Leak Prevention Checklist

| Practice | Why It Helps |
|----------|-------------|
| Nullify unused references | Allows GC to reclaim memory |
| Use try-with-resources | Automatically closes resources |
| Use `StringBuilder` over `String` | Avoids creating excessive objects |
| Clear collections when done | Removes stale references |
| Use memory profiling tools | Detects leaks early |

---
