# Java LinkedHashMap — Complete Interview Notes

---

## 1. What is LinkedHashMap?

`LinkedHashMap` is a class that:
- Implements `Map`
- Extends `HashMap`
- **Maintains order of elements**

**Declaration:**

```java
public class LinkedHashMap<K,V>
    extends HashMap<K,V>
    implements Map<K,V>
```

### Main Feature of LinkedHashMap

Unlike `HashMap`, `LinkedHashMap` **maintains order**.

It preserves:
- **Insertion order** (default)
- **Access order** (optional, via constructor)

---

## 2. Internal Structure

`LinkedHashMap` internally uses:

> **Hash Table + Doubly Linked List**

Combining:
- Fast lookup from `HashMap`
- Ordering from linked list

### Internal Diagram

```
Bucket Array
     ↓
[Node] → [Node]

AND

Head ⇄ Node ⇄ Node ⇄ Tail
```

Every node is connected using `before` and `after` references.

---

## 3. LinkedHashMap Node Structure

**HashMap Node:**
```java
Node<K,V>
```

**LinkedHashMap Node:**
```java
Entry<K,V> extends HashMap.Node<K,V>
```

**Additional fields:**
- `before`
- `after`

These create the doubly linked list.

### Why Doubly Linked List?

**Benefits:**
- Preserve insertion order
- Move nodes easily
- Implement LRU cache efficiently

---

## 4. Hierarchy of LinkedHashMap

```
Map (interface)
    ↑
AbstractMap
    ↑
HashMap
    ↑
LinkedHashMap
```

> Very important interview hierarchy.

---

## 5. HashMap vs LinkedHashMap

| Feature | HashMap | LinkedHashMap |
|---------|---------|---------------|
| Ordering | No guarantee | Maintains order |
| Internal Structure | Array + List/Tree | HashMap + Doubly Linked List |
| Memory Usage | Less | More |
| Performance | Slightly faster | Slightly slower |
| Iteration Order | Random | Predictable |

---

## 6. Time Complexity

| Operation | Complexity |
|-----------|------------|
| `put()` | O(1) |
| `get()` | O(1) |
| `remove()` | O(1) |

> Even with linked list overhead, average complexity remains **O(1)**.

---

## 7. Insertion Order (Default Behavior)

Default `LinkedHashMap` preserves **Insertion Order**.

**Example:**

```java
LinkedHashMap<Integer,String> map =
        new LinkedHashMap<>();

map.put(3, "C");
map.put(1, "A");
map.put(2, "B");

System.out.println(map);
```

**Output:**
```
{3=C, 1=A, 2=B}
```

> Order preserved exactly.

### Important Point

If an existing key is updated:

```java
map.put(1, "NEW");
```

The **position does NOT change** in insertion-order mode.

---

## 8. Access Order Mode

### Special Constructor

```java
LinkedHashMap(
    int initialCapacity,
    float loadFactor,
    boolean accessOrder
)
```

| `accessOrder` | Behavior |
|---------------|----------|
| `false` | Default — Insertion order maintained |
| `true` | Access order — recently accessed items move to end |

### Example

```java
LinkedHashMap<Integer,String> map =
    new LinkedHashMap<>(16, 0.75f, true);

map.put(1, "A");
map.put(2, "B");
map.put(3, "C");

map.get(1);

System.out.println(map);
```

**Output:**
```
{2=B, 3=C, 1=A}
```

Key `1` was accessed recently, so it moved to the end.

### What Operations Count as Access? ⭐

In access-order mode, these **move the node to end**:

```
get()
put()
putIfAbsent()
compute()
replace()
```

> Very important senior interview point.

---

## 9. How Access Order Works Internally

**Initial state:**

```
Head ⇄ 1 ⇄ 2 ⇄ 3 ⇄ Tail
```

After `map.get(1)`:

```
Head ⇄ 2 ⇄ 3 ⇄ 1 ⇄ Tail
```

Node `1` moved to tail. **Most recently used stays near tail.**

---

## 10. LRU Cache Using LinkedHashMap

> **Very important interview topic.**

### What is LRU Cache?

**LRU = Least Recently Used**

When cache is full → remove the **least recently used** item.

### Why LinkedHashMap is Perfect for LRU?

Because:
- Maintains access order
- Can remove oldest entry automatically

### Core Design

1. Extend `LinkedHashMap`
2. Enable access order: `true`
3. Override `removeEldestEntry()`

### Complete LRU Cache Example

```java
class LRUCache<K,V> extends LinkedHashMap<K,V> {

    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(
            Map.Entry<K,V> eldest) {

        return size() > capacity;
    }
}
```

### How It Works

Suppose capacity = `3`.

Insert: `1, 2, 3`

Access: `get(1)` → order becomes `2, 3, 1`

Insert `4` → **`2` removed automatically** because `2` = least recently used.

### Important Internal Point ⭐

`removeEldestEntry()` is automatically called after:
- `put()`
- `putAll()`

**NOT** after `get()`.

> Interview favourite question.

---

## 11. `getOrDefault()`

```java
map.getOrDefault(key, defaultValue);
```

**Example:**

```java
map.getOrDefault(10, "NOT FOUND");
```

If key absent → returns **default value** instead of `null`.

---

## 12. `putIfAbsent()`

```java
map.putIfAbsent(key, value);
```

Adds entry **only if key is absent**.

**Example:**

```java
map.putIfAbsent(1, "Java");
```

Safer than:

```java
if (!map.containsKey(key)) { ... }
```

---

## 13. Thread Safety

`LinkedHashMap` is **NOT thread-safe** — same as `HashMap`.

For multithreading:
- Use synchronization
- OR use `ConcurrentHashMap`

---

## 14. Fail-Fast Iterator

Iterator is **fail-fast**.

**Example:**

```java
for (Integer k : map.keySet()) {
    map.put(5, "X");
}
```

Throws `ConcurrentModificationException` because `modCount` changes.

---

## 15. LinkedHashMap vs TreeMap

| Feature | LinkedHashMap | TreeMap |
|---------|---------------|---------|
| Ordering | Insertion/Access | Sorted |
| Structure | Hash Table + DLL | Red-Black Tree |
| Complexity | O(1) | O(log n) |
| Null Key | Allowed | Not allowed |

---

## 16. Internal Hooks Used by LinkedHashMap

`LinkedHashMap` overrides HashMap hooks:

```
afterNodeAccess()
afterNodeInsertion()
afterNodeRemoval()
```

These maintain the linked list order.

> Senior-level concept.

---

## 17. Memory Overhead

`LinkedHashMap` nodes contain **extra pointers**:
- `before`
- `after`

Memory usage is **higher** than `HashMap`.

---

## 18. When to Use LinkedHashMap?

### Use When:
- Order matters
- LRU cache needed
- Predictable iteration needed

### Do NOT Use When:
- Ordering is unnecessary
- Memory optimization is critical

---

## 19. Important Interview Questions

### Q1. Difference between HashMap and LinkedHashMap?

- **HashMap:** No order
- **LinkedHashMap:** Maintains insertion/access order using doubly linked list

### Q2. How does LinkedHashMap maintain order?

Using a **doubly linked list** connecting all entries.

### Q3. Does LinkedHashMap allow null?

**Yes.**
- One null key
- Multiple null values

Same as `HashMap`.

### Q4. Why is LinkedHashMap slower than HashMap?

Because it maintains a **doubly linked list** — extra pointer updates required on every operation.

### Q5. What is access-order mode?

Recently accessed entries **move to the end**. Used for **LRU cache** implementation.

---

## 20. Internal Flow of `put()`

```
1. Calculate hash
2. Find bucket
3. Insert node
4. Link node at end of doubly linked list
5. Maintain ordering
```

---

## 21. Internal Flow of `get()`

```
1. Find bucket
2. Locate node
3. If accessOrder = true:
       move node to tail
4. Return value
```

---

## 22. Senior Java Developer Concepts

### 1. LinkedHashMap Preserves Iteration Stability

Even after resize, **order is preserved** — unlike `HashMap`.

### 2. Useful in JSON Parsing Libraries

Many libraries use `LinkedHashMap` because **insertion order matters** in JSON fields.

### 3. Widely Used in Caching Systems

Foundation of:
- LRU caches
- Eviction systems

### 4. Access Order ≠ Sorted Order

> Very common confusion.

**Example:**

```
2 1 3
```

can exist. Access order only tracks **usage sequence**, not natural order.

---

## 23. Important Revision Constants

| Feature | Value |
|---------|-------|
| Default Capacity | 16 |
| Default Load Factor | 0.75 |
| Default Ordering | Insertion order |
| Access Order Possible | Yes |

---

## 24. Final Revision Summary

### Core Concepts

- ✅ `LinkedHashMap` extends `HashMap`
- ✅ Maintains insertion/access order
- ✅ Uses doubly linked list internally
- ✅ Slightly slower than `HashMap`
- ✅ Supports LRU cache implementation
- ✅ Access-order mode moves recently used items to tail
- ✅ Allows one null key and multiple null values
- ✅ Not thread-safe

### Shortcut to Remember

```
LinkedHashMap = HashMap + Order

Insertion order  →  default
Access order     →  LRU cache
```
