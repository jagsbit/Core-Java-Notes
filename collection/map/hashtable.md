# Java Hashtable — Complete Interview Notes

---

## 1. What is Hashtable?

`Hashtable` is a **legacy synchronized Map implementation** in Java.

It stores **key-value pairs** using a hashing mechanism, similar to `HashMap`.

### Declaration

```java
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, Serializable
```

> Very important interview declaration.

---

## 2. Important Historical Point

`Hashtable` was introduced in **JDK 1.0** — before the Java Collections Framework.

Later:
- `Map` interface was introduced
- `Hashtable` was retrofitted to implement `Map`

---

## 3. Hierarchy

> **Very important difference from HashMap.**

### Hashtable Hierarchy

```
Object
  ↑
Dictionary
  ↑
Hashtable
```

### HashMap Hierarchy

```
Object
  ↑
AbstractMap
  ↑
HashMap
```

---

## 4. Internal Working

`Hashtable` internally uses **Array + Linked List**, similar to old `HashMap`.

### Important Point

> `Hashtable` **NEVER uses Red-Black Tree** — only linked list chaining, even in modern Java versions.

### Collision Handling

If two keys map to the same bucket → **Linked List chaining** used.

```
bucket[5]
   ↓
Node → Node → Node
```

---

## 5. Thread Safety

> **MOST IMPORTANT Hashtable concept.**

`Hashtable` is **thread-safe** because **almost every method is synchronized**.

### Example Internal Methods

```java
public synchronized V put(K key, V value)

public synchronized V get(Object key)
```

### Meaning

At one time, **ONLY ONE thread** can access the `Hashtable` object.

### Example

| Thread | Operation |
|--------|-----------|
| Thread-1 | `map.put(1, "Java")` — acquires lock |
| Thread-2 | `map.get(1)` — **must WAIT** (even for reading) |

---

## 6. Why Hashtable is Slow?

Because the **entire table is locked for every operation**, including `put()`, `get()`, and `remove()`.

This creates **high contention** in multithreading.

> **Important Interview Line:**
> Hashtable uses **coarse-grained synchronization** — the whole object is locked.

---

## 7. HashMap vs Hashtable

| Feature | HashMap | Hashtable |
|---------|---------|-----------|
| Thread Safe | No | Yes |
| Synchronization | None | Entire methods synchronized |
| Performance | Faster | Slower |
| Null Key | Allowed | Not allowed |
| Null Values | Allowed | Not allowed |
| Introduced | JDK 1.2 | JDK 1.0 |
| Treeification | Yes | No |

---

## 8. Why Hashtable Does NOT Allow Null? ⭐

> **Very famous interview question.**

`Hashtable` forbids both **null keys** and **null values**.

```java
Hashtable<Integer,String> map = new Hashtable<>();

map.put(null, "Java"); // ❌ NullPointerException
```

### Why?

When `map.get(key)` returns `null`, it becomes ambiguous:

```
Does null mean:
  → key is absent?
  OR
  → value is null?
```

`Hashtable` avoids this confusion completely by requiring both **key and value to be non-null**.

---

## 9. Thread Safety Demonstration

### HashMap Problem

Two threads inserting simultaneously:

- Expected: **2000 entries**
- Actual: maybe **1800 or 1900** — race conditions occur

### Hashtable Result

Because methods are synchronized → all operations are serialized → final size reliably becomes **2000**.

---

## 10. Internal Synchronization Problem

Even `map.get(1)` acquires a lock.

So:
- Multiple readers **cannot** work simultaneously
- Huge **bottleneck** occurs

> `Hashtable` blocks **both readers and writers** — a major scalability problem.

---

## 11. Why Hashtable Became Legacy?

Because its synchronization strategy is **inefficient**.

Modern applications need:
- Parallel reads
- High concurrency
- Low locking

Hashtable fails here.

### Modern Replacement

Java introduced: **`ConcurrentHashMap`**

---

## 12. ConcurrentHashMap Improvement

`ConcurrentHashMap`:
- Allows **concurrent reads**
- Uses **fine-grained locking**
- Much **better scalability**

> **Important Interview Statement:**
> Hashtable is **synchronized but not scalable**.

---

## 13. Hashtable and Fail-Fast Behavior

`Hashtable` supports two traversal types:

| Traversal | Type |
|-----------|------|
| `Enumeration` | Legacy — **NOT fail-fast** |
| `Iterator` | **Fail-fast** |

**Iterator example:**

```java
Iterator<Integer> it = map.keySet().iterator();
```

Modifying the map during iteration may throw `ConcurrentModificationException`.

---

## 14. Default Capacity and Load Factor

| Property | Hashtable | HashMap |
|----------|-----------|---------|
| Default Capacity | **11** | 16 |
| Default Load Factor | 0.75 | 0.75 |

---

## 15. Rehashing in Hashtable

When threshold exceeded:

```
newCapacity = oldCapacity * 2 + 1
```

**Example:**
```
11 → 23 → 47
```

> Unlike `HashMap`'s power-of-2 resizing.

---

## 16. Hashtable Internal Node Structure

```java
Entry<K,V> {
    int hash;
    K key;
    V value;
    Entry<K,V> next;
}
```

---

## 17. Hashtable Uses Legacy `Dictionary`

`Dictionary` is an abstract class — old design before the Collections Framework.

> Modern code rarely uses it.

---

## 18. Hashtable vs ConcurrentHashMap

| Feature | Hashtable | ConcurrentHashMap |
|---------|-----------|-------------------|
| Locking | Entire table | Segment/Bucket level |
| Read Concurrency | Blocked | Mostly non-blocking |
| Performance | Slower | Faster |
| Nulls Allowed | No | No |
| Scalability | Poor | Excellent |

---

## 19. Why ConcurrentHashMap is Better?

Suppose **10 threads reading simultaneously**:

| | Hashtable | ConcurrentHashMap |
|--|-----------|-------------------|
| Threads allowed | Only **1** | **Many readers** simultaneously |

Huge performance improvement.

---

## 20. Important Interview Questions

### Q1. Why is Hashtable slower than HashMap?

Because **every method is synchronized**.

### Q2. Why is Hashtable considered legacy?

**Poor concurrency design** — whole object locked.

### Q3. Why is `null` not allowed?

To avoid **ambiguity** in `get()` return value.

### Q4. Does Hashtable use Red-Black Tree?

**No.** Only **linked-list chaining**.

### Q5. Difference between Hashtable and ConcurrentHashMap?

`ConcurrentHashMap` provides:
- Finer-grained locking
- Better scalability
- Non-blocking reads

---

## 21. Internal `put()` Flow

```
1. Acquire object lock
2. Calculate hash
3. Find bucket
4. Traverse linked list
5. Insert/update node
6. Release lock
```

---

## 22. Internal `get()` Flow

```
1. Acquire lock
2. Calculate hash
3. Find bucket
4. Traverse linked list
5. Return value
6. Release lock
```

---

## 23. Senior Java Developer Concepts

### 1. Coarse-Grained Locking

The **whole map is locked** per operation.

### 2. Poor Scalability

Performance **degrades** with many threads.

### 3. Legacy API Design

Uses old APIs:
- `Dictionary`
- `Enumeration`

### 4. Hashtable Never Modernized

Unlike `HashMap`:
- No treeification
- No modern concurrency improvements

---

## 24. Real Use Cases

`Hashtable` is mostly found in:
- Legacy systems
- Old enterprise applications

> Modern code should always prefer **`ConcurrentHashMap`**.

---

## 25. Final Revision Summary

### Key Points

- ✅ Legacy synchronized `Map`
- ✅ Thread-safe — entire methods synchronized
- ✅ No null keys or null values
- ✅ Linked-list chaining only (no Red-Black Tree)
- ✅ Slower due to coarse-grained synchronization
- ✅ Default capacity = **11** (not 16 like HashMap)
- ✅ Resize formula: `oldCapacity * 2 + 1`
- ✅ Extends `Dictionary` (not `AbstractMap`)
- ✅ Modern replacement: `ConcurrentHashMap`

### Quick Comparison

| | Hashtable | HashMap | ConcurrentHashMap |
|--|-----------|---------|-------------------|
| Thread Safe | Yes | No | Yes |
| Performance | Slow | Fast | Fast (concurrent) |
| Null Keys | No | Yes | No |
| Modern? | Legacy | Yes | Yes |

### Shortcut to Remember

```
Hashtable  =  Thread-safe HashMap (but slow, legacy)

ConcurrentHashMap  =  Modern, scalable, thread-safe HashMap
```
