# Java Memory Model (JMM)

The **Java Virtual Machine** uses a memory structure called the **Java Memory Model (JMM)** to manage:

- Memory allocation
- Object storage
- Class storage
- Method execution
- Thread communication
- Synchronization

### JMM Defines:

> How threads interact through memory

### JMM Guarantees:

| Guarantee | Description |
|-----------|-------------|
| **Visibility** | All threads see the latest value of shared variables |
| **Consistency** | Memory stays in a consistent state across threads |
| **Ordering** | Instructions execute in a predictable order |
| **Thread Safety** | Prevents data corruption in multithreaded programs |

---

## Why Java Memory Model is Needed?

In **multithreading**:

- Many threads may access the **same variable**
- CPU caches may store **old/stale values**
- Instructions may execute in **different order** (reordering)

**Without JMM:**

- Threads may see **inconsistent data**
- **Race conditions** may occur

> JMM provides rules to solve these problems.

---

## JVM Runtime Memory Areas

When a Java program runs, JVM creates different memory areas:

```
┌─────────────────────────────────────────────────────────┐
│  Method Area │  Heap  │  Stack  │  PC Register  │ Native Stack │
└─────────────────────────────────────────────────────────┘
```

### Classification of Memory Areas

| Shared Among Threads | Thread-Specific |
|----------------------|-----------------|
| Method Area | Stack |
| Heap | PC Register |
| | Native Method Stack |

---

## 1. Method Area

The **Method Area** stores **class-level information**.

> It is **shared among all threads**.

### What is Stored in Method Area?

- Class metadata
- Method definitions
- Static variables
- Runtime constant pool
- Method bytecode

### Example

```java
class Student {
    static int count = 10;
    int age;
    void show() {}
}
```

Stored in Method Area:
- Class name `Student`
- Method `show()`
- Static variable `count`
- Field information
- Bytecode

---

### Important Concept: Method Area vs Metaspace

> This is the most **confusing topic** — understand carefully.

#### Method Area

Method Area is a **logical memory area defined by JVM specification**.

> JVM specification says: *JVM must have an area to store class metadata.*
> That logical area is called the **Method Area**.

#### Metaspace

Metaspace is the **actual implementation** of Method Area in **Java 8+**.

#### Relationship

```
Method Area  =  JVM concept / specification
Metaspace    =  HotSpot JVM implementation of Method Area
```

#### Before vs After Java 8

| Java Version | Implementation |
|-------------|----------------|
| Before Java 8 | Method Area implemented using **PermGen** |
| Java 8 and after | Method Area implemented using **Metaspace** |

> **Metaspace is NOT inside Heap** — it uses **Native OS memory**.

#### ✅ Correct Understanding

```
❌ Wrong:   Method Area  AND  another Metaspace  (two separate storages)

✅ Correct: Method Area data IS stored USING Metaspace
```

---

### Runtime Constant Pool

Part of **Method Area**.

Stores:
- String literals
- Numeric constants
- Symbolic references

**Example:**

```java
String s = "Hello";
// "Hello" stored in constant pool
```

#### Symbolic Reference vs Direct Reference

| Type | Description |
|------|-------------|
| **Symbolic Reference** | Names stored in bytecode (class name, method name, field name) |
| **Direct Reference** | Actual memory address/reference found during runtime |

#### Resolution Process

```
Symbolic Reference
        ↓
   (Linking Phase)
        ↓
Direct Reference
```

> JVM searches Method Area metadata to find actual references.

---

## 2. Heap Memory

**Heap** stores:

- Objects
- Arrays
- Instance variables

### Example

```java
Student s = new Student();
// Object stored in Heap
// Reference 's' stored in Stack
```

### Heap Features

| Feature | Description |
|---------|-------------|
| **Largest** memory area | Biggest of all JVM memory areas |
| **Shared** | Shared among all threads |
| **GC Managed** | Managed by Garbage Collector |

---

### Heap Structure

```
Heap
│
├── Young Generation
│     ├── Eden Space
│     ├── Survivor S0
│     └── Survivor S1
│
└── Old Generation
```

#### 🔹 Young Generation

Stores **newly created objects**.

| Space | Description |
|-------|-------------|
| **Eden Space** | Objects are **first created** here (`new Student()`) |
| **Survivor S0 / S1** | Objects surviving **Minor GC** move here |

> **Minor GC** — Garbage collection in Young Generation. Fast and frequent.

#### 🔹 Old Generation

- Stores **long-living objects**.
- Objects surviving many GC cycles are **promoted** here.

> **Major GC** — Garbage collection in Old Generation. Slower than Minor GC.

### Heap Example

```java
Student s1 = new Student();
Student s2 = new Student();
// Both objects stored in Heap
```

### OutOfMemoryError

Occurs when **heap becomes full**.

```java
List<String> list = new ArrayList<>();
while (true) {
    list.add("Java"); // Heap fills up → OutOfMemoryError
}
```

---

## 3. Stack Memory

Each **thread** gets its own **separate Stack memory**.

Stores:
- Method calls
- Local variables
- Intermediate calculations

> Stack works on **LIFO (Last In First Out)**.

### Stack Frame

Every method call creates a **stack frame**.

```java
void add(int a, int b) { ... }
// Creates a new stack frame when called
```

### Stack Frame Components

#### A) Local Variable Array

Stores method parameters and local variables.

```java
int x = 10; // Stored in Local Variable Array
```

#### B) Operand Stack

Temporary workspace for calculations.

```java
int c = a + b; // Intermediate values stored in Operand Stack
```

#### C) Frame Data

Stores:
- Exception information
- Method references

---

### Stack Operations

| Operation | Description |
|-----------|-------------|
| **Push** | Method call added to stack |
| **Pop** | Method removed after execution completes |

### Stack Example

```
main()   ← frame 1
  ↓
show()   ← frame 2
  ↓
add()    ← frame 3 (top of stack)
```

Each method creates a **separate frame**.

### Stack Features

- **Thread-specific** — Each thread has its own stack
- **Fast** memory access
- **Automatically managed**

### StackOverflowError

Occurs due to **deep or infinite recursion**.

```java
void fun() {
    fun(); // Infinite recursion → StackOverflowError
}
```

---

## 4. PC Register (Program Counter)

Each thread has its own **separate PC Register**.

Stores:
- Address of the **currently executing instruction**

### Example

```
Instruction 1  ← PC points here
Instruction 2
Instruction 3
```

> PC tracks which instruction is currently being executed.

---

## 5. Native Method Stack

Stores **native method execution details**.

Native methods are written in **C** or **C++**.

```java
System.loadLibrary(); // Uses Native Method Stack
```

---

## Thread Memory Structure

### Each Thread Has (Thread-Specific):

- Own **Stack**
- Own **PC Register**
- Own **Native Method Stack**

### Shared Memory:

- **Heap**
- **Method Area**

---

## Object Creation Process

```java
Student s = new Student();
```

| Step | Action | Memory Area |
|------|---------|-------------|
| **Step 1** | Class Loader loads `Student.class` | Method Area / Metaspace |
| **Step 2** | Object instance is created | Heap |
| **Step 3** | Reference variable `s` is stored | Stack |
| **Step 4** | Execution Engine executes bytecode | — |

---

## Garbage Collection (GC)

GC **automatically removes unused objects** from Heap.

### Eligible for GC

```java
Student s = new Student();
s = null; // Object becomes unreachable → eligible for GC
```

### GC Phases

```
1. Mark  →  2. Sweep  →  3. Compact
```

| Phase | Description |
|-------|-------------|
| **Mark** | Reachable objects are identified |
| **Sweep** | Unused objects are removed |
| **Compact** | Memory is rearranged to reduce fragmentation |

### GC Roots

Objects reachable from the following are **preserved**:
- Local variables
- Static variables
- Active threads
- JNI references

### Types of Garbage Collectors

| GC | Description |
|----|-------------|
| **Serial GC** | Single-threaded |
| **Parallel GC** | Multi-threaded |
| **CMS GC** | Low pause time |
| **G1 GC** | Balanced performance |
| **ZGC** | Very low latency |

### `finalize()` Method

Called **before object destruction**.

```java
@Override
protected void finalize() {
    System.out.println("Finalize called");
}
```

| Issue | Description |
|-------|-------------|
| **Slow** | Unpredictable execution time |
| **Unpredictable** | No guarantee when it runs |
| **Deprecated** | After Java 9 |

> ✅ Preferred alternative: **try-with-resources**

---

## JMM and Multithreading

JMM mainly handles:

- **Visibility**
- **Ordering**
- **Atomicity**

---

### Visibility Problem

```java
boolean flag = false;

// Thread 1:
flag = true;

// Thread 2 may still see: false
// Because CPU caches old value
```

### `volatile` Keyword

Ensures **visibility** between threads — forces read/write from main memory.

```java
volatile boolean flag;
```

---

### `synchronized` Keyword

Provides:
- **Mutual exclusion**
- **Visibility**
- **Ordering**

```java
synchronized void show() {
    // Only one thread can enter at a time
}
```

---

### Atomicity

An operation that completes **fully without interruption**.

**Non-Atomic Example:**

```java
count++;
// Internally: read → increment → write
// Multiple threads can cause inconsistency
```

**Solution — Atomic Classes:**

```java
AtomicInteger count = new AtomicInteger();
count.incrementAndGet(); // Atomic operation
```

---

### Happens-Before Relationship

JMM defines **execution ordering rules**.

| Rule | Description |
|------|-------------|
| **unlock happens-before lock** | Unlock of a monitor happens before any subsequent lock |
| **write happens-before read** | Write to a `volatile` variable happens before any subsequent read |

> Ensures **memory consistency** across threads.

---

## JVM Tuning Parameters

| Parameter | Description | Example |
|-----------|-------------|---------|
| `-Xms` | Initial Heap Size | `-Xms512m` |
| `-Xmx` | Maximum Heap Size | `-Xmx2048m` |
| `-Xss` | Stack Size per Thread | `-Xss512k` |
| `-XX:+UseG1GC` | Enable G1 Garbage Collector | `-XX:+UseG1GC` |

---

## Complete Memory Flow Example

```java
class Test {
    static int count = 10;

    public static void main(String[] args) {
        Student s = new Student();
        s.show();
    }
}
```

### Memory Allocation

| Item | Stored In |
|------|-----------|
| Class metadata | Method Area / Metaspace |
| Static variable `count` | Method Area |
| Object instance | Heap |
| Reference variable `s` | Stack |
| Current instruction | PC Register |

---

## Comparisons

### Heap vs Stack

| Feature | Stack Memory | Heap Memory |
|---------|-------------|-------------|
| **Storage** | Method-specific local variables, function call data | Objects, class variables |
| **Size** | Smaller | Larger |
| **Management** | Automatically managed by JVM | Managed by Garbage Collector (GC) |
| **Access Speed** | Faster | Slower |
| **Scope** | Limited to method execution | Available globally as long as references exist |
| **Thread Safety** | Each thread has its own stack (thread-safe) | Shared across threads (requires synchronization) |

### Method Area vs Heap

| Feature | Method Area | Heap |
|---------|-------------|------|
| **Stores** | Class-level data | Objects |
| **Variables** | Static variables | Instance variables |
| **Sharing** | Shared | Shared |

---

## Final Summary

```
Method Area / Metaspace
    → Class metadata, static variables, method bytecode

Heap
    → Objects, arrays, instance variables

Stack
    → Method calls & local variables (per thread)

PC Register
    → Currently executing instruction (per thread)

Native Method Stack
    → Native (C/C++) method execution (per thread)
```

---

## ⭐ Most Important Concept

```
Method Area  =  Logical JVM specification concept

Metaspace    =  Actual implementation of Method Area in Java 8+

They are NOT two duplicate/separate memory areas.
```

---
