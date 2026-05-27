# Java: String vs StringBuffer vs StringBuilder

These three classes are used for handling character data in Java. Understanding their differences is very important for interviews, Java Certification, performance optimization, and multithreading concepts.

---

## 1. Introduction

Java provides three major classes for string manipulation:

| Class | Mutable? | Thread-Safe? |
|-------|---------|-------------|
| `String` | ❌ No | ✅ Yes |
| `StringBuffer` | ✅ Yes | ✅ Yes |
| `StringBuilder` | ✅ Yes | ❌ No |

---

## 2. String and Immutability

### What is Immutability?

> **Immutable** means the object **cannot be changed after creation**.

### String is Immutable

```java
String s = "Java";
s.concat(" Programming");
System.out.println(s); // Java
```

**Output:**
```
Java
```

Because `concat()` creates a **NEW object** — the original remains unchanged.

### Modifying String Correctly

```java
String s = "Java";
s = s.concat(" Programming");
System.out.println(s);
```

**Output:**
```
Java Programming
```

---

## 3. Why is String Immutable?

| Reason | Explanation |
|--------|------------|
| **Security** | Strings are used in DB URLs, file paths, passwords — immutability prevents modification |
| **Thread Safety** | Multiple threads can safely share the same string without synchronization |
| **SCP Optimization** | JVM can safely reuse strings from the String Constant Pool |
| **Hashcode Caching** | Hashcode never changes, making String safe for use in `HashMap`/`Hashtable` |

---

## 4. String Constant Pool (SCP)

SCP is a **special memory area inside the Heap** that stores string literals.

### Creating Strings — Using Literal

```java
String s1 = "Java";
String s2 = "Java";
```

Only **ONE object** created in SCP. Both references point to the same object.

```
s1 ----\
        ---> "Java" (SCP)
s2 ----/
```

### Creating Strings — Using `new` Keyword

```java
String s1 = new String("Java");
```

Creates **TWO objects**:
1. `"Java"` in SCP
2. A new object in Heap

`s1` points to the **Heap** object, not SCP.

---

## 5. `==` vs `equals()`

### `==` — Reference Comparison

```java
String s1 = "Java";
String s2 = "Java";
System.out.println(s1 == s2); // true — same SCP object

String s3 = new String("Java");
String s4 = new String("Java");
System.out.println(s3 == s4); // false — different Heap objects
```

### `equals()` — Content Comparison

```java
String s1 = new String("Java");
String s2 = new String("Java");
System.out.println(s1.equals(s2)); // true — same content
```

| Operator | Checks | Result for same content, different objects |
|----------|--------|------------------------------------------|
| `==` | Reference | `false` |
| `equals()` | Content | `true` |

---

## 6. `intern()` Method

### What Does `intern()` Do?

> `intern()` returns the **SCP reference** of a string.
> - If the string is already in SCP → returns that reference.
> - If not → adds it to SCP and returns the reference.

### Step-by-Step Example

```java
String s1 = new String("Java");
String s2 = s1.intern();
System.out.println(s2 == "Java"); // true
```

**Memory Diagram:**

```
After: String s1 = new String("Java");
SCP:  "Java"
Heap: "Java"  <--- s1

After: String s2 = s1.intern();
SCP:  "Java"  <--- s2
Heap: "Java"  <--- s1
```

`s2` and `"Java"` both point to the **same SCP object** → `true`.

> ⚠️ `intern()` does **NOT** move the heap object into SCP. It only returns the SCP reference.

### Another Example

```java
String s1 = new String("Python");
String s2 = s1.intern();
System.out.println(s1 == s2); // false — s1 is Heap, s2 is SCP
```

---

## 7. Compile-Time vs Runtime Concatenation

### Compile-Time Concatenation

```java
String s = "Java" + "Programming";
// Compiler converts to: String s = "JavaProgramming";
// Stored in SCP
```

### Runtime Concatenation

```java
String s1 = "Java";
String s2 = s1 + "Programming"; // Uses StringBuilder internally
// Result stored in Heap
```

---

## 8. Problem with String for Frequent Modifications

```java
String s = "";
for (int i = 0; i < 1000; i++) {
    s = s + i; // Creates a new object every iteration!
}
```

> ⚠️ Creates **many garbage objects** → slow performance and high memory usage.

**Solution:** Use `StringBuffer` or `StringBuilder`.

---

## 9. StringBuffer

`StringBuffer` is **mutable** — changes occur in the **same object**.

```java
StringBuffer sb = new StringBuffer("Java");
sb.append(" Programming");
System.out.println(sb); // Java Programming
```

**Output:**
```
Java Programming
```

> ✅ No new object created — the original object itself changes.

### StringBuffer is Thread-Safe

All methods are **synchronized** — only one thread can access them at a time.

```java
sb.append("A"); // internally synchronized
```

---

## 10. StringBuilder

Introduced in **Java 1.5**. Almost identical to `StringBuffer` but **not synchronized**.

```java
StringBuilder sb = new StringBuilder("Java");
sb.append(" Programming");
System.out.println(sb); // Java Programming
```

> ✅ **Faster** than `StringBuffer` because there is no synchronization overhead.
> ❌ **Not thread-safe** — suitable for single-threaded applications only.

---

## 11. Why Immutable Objects (String) are Thread-Safe

### The Problem with Mutable Objects in Threads

```java
class Counter {
    int count = 0;
    void increment() { count++; }
}
```

If **Thread 1** and **Thread 2** both call `increment()` simultaneously:

- Both may read/update `count` at the same time
- Causes **Race Condition** → inconsistent data

### Why String is Safe

```java
String s = "Java";

// Thread 1
s.concat(" Programming"); // Creates NEW object, does NOT modify "Java"

// Thread 2
System.out.println(s);    // Safely reads "Java" — unchanged
```

> Since `concat()` creates a new object and never touches the original, **Thread 2 always reads clean data**.

### Key Idea

> **Immutable objects are thread-safe because no thread can modify the shared data.**

| Concept | Result |
|---------|--------|
| No modification possible | No race condition |
| State never changes | No synchronization needed |
| Shared safely | No inconsistent state |

### Real-Life Analogy

| Type | Analogy | Thread Safety |
|------|---------|--------------|
| **String (Immutable)** | Printed book — 100 people can read simultaneously, nobody can change it | ✅ Safe |
| **StringBuffer (Mutable + Sync)** | Whiteboard with one-person-at-a-time rule | ✅ Safe |
| **StringBuilder (Mutable, No Sync)** | Whiteboard with no rules — chaos if many write | ❌ Unsafe |

---

## 12. Capacity Concept

| Creation | Default Capacity |
|----------|-----------------|
| `new StringBuffer()` | **16** |
| `new StringBuffer("Java")` | **16 + length** = 16 + 4 = **20** |

```java
StringBuffer sb = new StringBuffer();
System.out.println(sb.capacity()); // 16

StringBuffer sb2 = new StringBuffer("Java");
System.out.println(sb2.capacity()); // 20
```

---

## 13. Important Methods

| Method | Purpose |
|--------|---------|
| `append()` | Add text at the end |
| `insert()` | Insert text at a position |
| `replace()` | Replace a portion of text |
| `delete()` | Delete characters |
| `reverse()` | Reverse the content |
| `charAt()` | Get character at index |
| `capacity()` | Get current capacity |
| `length()` | Get current length |

### Example

```java
StringBuilder sb = new StringBuilder("Java");
sb.append(" Programming");
sb.reverse();
System.out.println(sb);
```

**Output:**
```
gnimmargorP avaJ
```

---

## 14. StringBuffer vs StringBuilder

| Feature | StringBuffer | StringBuilder |
|---------|-------------|--------------|
| Mutable | ✅ Yes | ✅ Yes |
| Thread-Safe | ✅ Yes | ❌ No |
| Synchronized | ✅ Yes | ❌ No |
| Performance | Slower | Faster |
| Introduced In | Java 1.0 | Java 1.5 |

---

## 15. When to Use What?

| Situation | Recommended Class |
|-----------|-----------------|
| Data **never changes** | `String` |
| Frequent modifications in **multithreading** | `StringBuffer` |
| Frequent modifications in **single thread** | `StringBuilder` |

---

## 16. Final Comparison Table

| Feature | `String` | `StringBuffer` | `StringBuilder` |
|---------|---------|---------------|----------------|
| Mutable | ❌ No | ✅ Yes | ✅ Yes |
| Thread-Safe | ✅ Yes | ✅ Yes | ❌ No |
| Synchronized | Not needed | ✅ Yes | ❌ No |
| Performance | Slow | Medium | Fast |
| Memory Efficient | ❌ No | ✅ Yes | ✅ Yes |
| Introduced In | Java 1.0 | Java 1.0 | Java 1.5 |

---

## 17. Interview Questions

### Q1. Difference between String and StringBuffer?

| String | StringBuffer |
|--------|-------------|
| Immutable | Mutable |
| Slow modifications | Fast modifications |
| More memory usage | Less memory usage |
| Thread-safe (immutable) | Thread-safe (synchronized) |

### Q2. Difference between StringBuffer and StringBuilder?

> `StringBuffer` is **synchronized** and thread-safe. `StringBuilder` is **not synchronized** and faster.

### Q3. Why is String immutable?

> Security, Thread Safety, SCP optimization, and Hashcode caching.

### Q4. Which is the fastest?

> **`StringBuilder`** — no synchronization overhead.

### Q5. What does `intern()` do?

> It returns the **canonical (SCP) reference** of a string. If the string is not in SCP, JVM adds it. Helps save memory by reusing strings.

### Q6. Which comparison should be used for Strings?

> Use **`equals()`** for content comparison. Use `==` only for reference comparison.

---

## 🎯 Key Interview Tips

- `String` is immutable → **inherently thread-safe** without any synchronization.
- `StringBuffer` is mutable but uses **synchronized methods** → thread-safe but slower.
- `StringBuilder` is mutable with **no synchronization** → fastest but not thread-safe.
- `intern()` returns the **SCP reference**, it does NOT move objects from Heap to SCP.
- Always use **`equals()`** for string content comparison, never `==`.
- Immutability **removes the need for synchronization** because shared data can never change.
