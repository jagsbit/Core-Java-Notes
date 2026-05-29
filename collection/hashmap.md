# Java HashMap — Complete Interview Notes

---

## 1. What is HashMap?

Java `HashMap` is a class in the Java Collection Framework that stores data in **key-value pairs**.

**Example:**

```java
HashMap<Integer, String> map = new HashMap<>();

map.put(1, "Java");
map.put(2, "Spring");

System.out.println(map.get(1));
```

**Output:**
```
Java
```

---

## 2. Why HashMap is Fast?

HashMap provides near **O(1)** time complexity for `put()`, `get()`, and `remove()` because it uses:
- Hashing
- Buckets
- Array indexing

---

## 3. Internal Structure of HashMap

Internally HashMap uses:

> **Array + Linked List + Red Black Tree (Java 8+)**

```
Bucket Array
--------------------------------
0  -> null
1  -> Node
2  -> Node -> Node
3  -> null
4  -> TreeNode
--------------------------------
```

Each bucket stores nodes.

---

## 4. Node Structure Internally

Each entry is stored as a `Node` object.

```java
static class Node<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

| Field | Purpose |
|-------|---------|
| `hash` | hashcode of key |
| `key` | actual key |
| `value` | actual value |
| `next` | points to next node in collision chain |

---

## 5. Default Capacity and Load Factor

### Default Capacity

```
16
```

Meaning: 16 buckets initially.

### Default Load Factor

```
0.75
```

Resize happens when:

```
capacity × load factor = 16 × 0.75 = 12
```

After inserting **12 entries**, HashMap resizes to **32**.

---

## 6. How `put()` Works Internally

**Example:**

```java
map.put("JAVA", 100);
```

### Step 1: Calculate HashCode

```java
int hash = key.hashCode();
```

### Step 2: Calculate Bucket Index

$$index = hash \mathbin{\&} (n - 1)$$

where `n` = array size, `&` = bitwise AND.

**Example:**

```
hash = 18
n    = 16

18 & 15 = 2  →  stored at bucket index 2
```

### Step 3: Store Node

If bucket is empty:
```
bucket[2] -> Node
```

---

## 7. Collision in HashMap

### What is Collision?

When two keys generate the **same bucket index**.

**Example:**

```
"A" -> index 5
"B" -> index 5
```

Both go to the same bucket.

---

## 8. Collision Resolution — Separate Chaining

HashMap uses **Separate Chaining** — nodes are connected using a linked list.

```
bucket[5]
   |
   v
Node -> Node -> Node
```

### Case 1: Same Key

```java
map.put("A", 10);
map.put("A", 20);
```

HashMap checks `equals()`. If key already exists → **old value replaced**.

```
A -> 20
```

### Case 2: Different Key

```
"A" -> index 5
"B" -> index 5
```

Then:

```
Node(A) -> Node(B)  (linked list created)
```

---

## 9. How `get()` Works

**Example:**

```java
map.get("JAVA");
```

1. Calculate hash using `hashCode()`
2. Find bucket: $index = hash \mathbin{\&} (n - 1)$
3. Traverse bucket, check using `equals()` until matching key found

---

## 10. Java 8 Improvement — Treeification

### Problem Before Java 8

Too many collisions:

```
Node -> Node -> Node -> Node -> Node
```

Searching becomes **O(n)** — very slow.

### Treeify Threshold

```
TREEIFY_THRESHOLD = 8
```

If bucket size exceeds **8**:

```
Linked List  →  Red Black Tree
```

### Why Red Black Tree?

Searching becomes **O(log n)** instead of **O(n)**.

---

## 11. Important Interview Point ⭐

Treeification happens **ONLY IF**:

```
Bucket size > 8
AND
Array capacity >= 64
```

> Otherwise HashMap prefers **resizing** instead of tree conversion.

This is a very important **senior-level interview point**.

---

## 12. Resize / Rehashing

When threshold exceeded:

```
16 → 32 → 64 → 128
```

HashMap creates a new array and **rehashes** all entries.

> This operation is **expensive**.

---

## 13. Why Capacity is Always Power of 2?

Because the formula:

$$index = hash \mathbin{\&} (n - 1)$$

works efficiently **only with power of 2**.

**Benefits:**
- Faster calculation
- Better distribution
- Avoids modulo operator `%`

> Senior interview favourite question.

---

## 14. `hashCode()` and `equals()` Contract

### Rule 1

If two objects are equal:

```
a.equals(b) == true
```

then:

```
a.hashCode() == b.hashCode()   MUST be true
```

### Rule 2

If two objects have the same hashcode, they **may still be different objects** (collision is possible).

---

## 15. Custom Object as Key

**Always override** `equals()` and `hashCode()`.

**Example:**

```java
class Employee {
    int id;
    String name;

    @Override
    public boolean equals(Object o) {
        Employee e = (Employee) o;
        return this.id == e.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
```

> Otherwise retrieval may fail.

---

## 16. Why Immutable Objects Preferred as Keys?

**Best keys:** `String`, `Integer`, `UUID`

Because immutable objects **don't change hashcode**.

If a mutable key changes:
- Object stored in one bucket
- But searched in another bucket
- **Retrieval fails**

---

## 17. HashMap Allows Null?

**Yes.**

HashMap allows:
- **One** null key
- **Multiple** null values

```java
map.put(null, "Java");
```

> Null key always stored in **bucket 0**.

---

## 18. Is HashMap Thread Safe?

**No.**

HashMap is **NOT thread-safe**. Concurrent modification can cause:
- Data inconsistency
- Infinite loop (old Java versions)
- Corruption

---

## 19. Thread-Safe Alternatives

### `Hashtable`

```java
Hashtable<K,V>
```
- Legacy class
- Synchronized
- Slower

### `ConcurrentHashMap` ⭐

```java
ConcurrentHashMap<K,V>
```

Best choice for multithreading. Uses:
- Segment locking (older)
- CAS + bucket locking (Java 8+)

Allows high concurrency.

> Senior interview important topic.

---

## 20. Fail-Fast Behavior

Iterator of HashMap is **fail-fast**.

**Example:**

```java
for (Integer i : map.keySet()) {
    map.put(4, "X");
}
```

Throws `ConcurrentModificationException` because internal modification count changes.

---

## 21. Time Complexity

| Operation | Average | Worst |
|-----------|---------|-------|
| `put()` | O(1) | O(log n) |
| `get()` | O(1) | O(log n) |
| `remove()` | O(1) | O(log n) |

> Before Java 8, worst case was **O(n)**.

---

## 22. HashMap vs Hashtable

| Feature | HashMap | Hashtable |
|---------|---------|-----------|
| Thread Safe | No | Yes |
| Performance | Faster | Slower |
| Null Key | Allowed | Not allowed |
| Null Values | Allowed | Not allowed |
| Introduced | Java 1.2 | Java 1.0 |

---

## 23. HashMap vs ConcurrentHashMap

| Feature | HashMap | ConcurrentHashMap |
|---------|---------|-------------------|
| Thread Safe | No | Yes |
| Performance | Faster single thread | Better multi-thread |
| Null Keys | Allowed | Not allowed |
| Iterator | Fail-fast | Fail-safe |

---

## 24. Internal Hash Optimization in Java 8

Java 8 improves hash distribution:

$$hash = h \oplus (h \ggg 16)$$

**Purpose:** Better bucket distribution, reduces collisions.

### Why Lower Bits Matter Most

When capacity `n = 16`:

```
n - 1 = 15
binary = 00001111
```

During `hash & 00001111`, only the **last 4 bits** affect the bucket index.

If lower bits are poor → many keys → same bucket → more collisions.

### What XOR Shift Does

Takes higher 16 bits and **mixes them into lower 16 bits**, so lower bits become influenced by upper bits too.

**Simplified Example:**

```
h        = 10101010 11110000
h >>> 8  = 00000000 10101010

XOR result:
  10101010 11110000
  00000000 10101010
  -----------------
  10101010 01011010
```

Lower bits changed significantly → better bucket spread.

### Why Power of 2 Capacity Matters

```
n-1 values:
16  → 00001111
32  → 00011111
64  → 00111111
```

Efficiently extracts lower bits using bitwise AND — much faster than modulo `%`.

> **Senior Interview One-Line Answer:**
> HashMap bucket index depends on lower bits because `hash & (n-1)` uses only lower bits when capacity is a power of 2. Java 8 improves distribution by mixing higher bits into lower bits using XOR shifting: $h \oplus (h \ggg 16)$.

---

## 25. Complete HashMap Hierarchy

### Declaration

```java
public class HashMap<K,V>
    extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable
```

### Full Hierarchy

```
        Map (interface)
              ↑
       AbstractMap (abstract class)
              ↑
          HashMap
              ↑
        LinkedHashMap
```

HashMap also implements: `Cloneable`, `Serializable`

---

## 26. Map Interface

`Map` is an **interface** defining the contract for key-value storage.

**Important methods declared in Map:**

```
put()         get()          remove()
containsKey() containsValue()
keySet()      values()       entrySet()
```

### Why Map is NOT Part of Collection Interface?

> **Very famous interview question.**

Because:
- `Collection` stores **individual objects**
- `Map` stores **key-value pairs**

So Map has a **separate hierarchy**.

### Important Map Implementations

| Class | Ordering | Thread Safe |
|-------|----------|-------------|
| `HashMap` | No order | No |
| `LinkedHashMap` | Insertion order | No |
| `TreeMap` | Sorted order | No |
| `Hashtable` | No order | Yes |
| `ConcurrentHashMap` | No order | Yes |

---

## 27. AbstractMap Class

`AbstractMap` is an **abstract class** that provides partial implementation of the `Map` interface, reducing coding effort for subclasses.

**What AbstractMap provides:**

```
isEmpty()       containsKey()
containsValue() toString()
equals()
```

> HashMap mainly focuses on core hashing logic.

---

## 28. Cloneable Interface

Marker interface — allows object to be cloned using `clone()`.

```java
HashMap<Integer,String> map2 =
    (HashMap<Integer,String>) map1.clone();
```

> Clone is a **shallow copy** — structure copied, object references shared.

---

## 29. Serializable Interface

Marker interface — allows object conversion into byte stream.

Used for: file storage, network transfer, caching.

---

## 30. `Map.Entry<K,V>`

Nested interface inside `Map` representing a **single key-value pair**.

**Methods:** `getKey()`, `getValue()`, `setValue()`

**Example:**

```java
for (Map.Entry<Integer,String> e : map.entrySet()) {
    System.out.println(e.getKey());
    System.out.println(e.getValue());
}
```

---

## 31. Internal Classes Inside HashMap

| Internal Class | Purpose |
|----------------|---------|
| `Node` | Linked list node |
| `TreeNode` | Red-Black tree node |
| `KeySet` | `keySet()` view |
| `Values` | `values()` view |
| `EntrySet` | `entrySet()` view |

---

## 32. Internal `put()` Flow (Complete)

```
put(key, value)

1. Calculate hash
2. Calculate index
3. Check bucket empty?
4. If yes  →  insert
5. If collision:
      a. equals() true  →  replace value
      b. else add node to chain
6. If bucket size > 8  →  treeify
7. If size > threshold →  resize
```

---

## 33. Internal `get()` Flow

```
get(key)

1. Calculate hash
2. Find bucket
3. Traverse list/tree
4. Compare using hash + equals()
5. Return value
```

---

## 34. Important Constants

| Constant | Value |
|----------|-------|
| Default Capacity | 16 |
| Load Factor | 0.75 |
| Treeify Threshold | 8 |
| Untreeify Threshold | 6 |
| Minimum Capacity for Treeify | 64 |

---

## 35. Untreeification in HashMap — Deep Dive

### What is Untreeify?

**Untreeify** means converting a **Red-Black Tree back into a Linked List** inside a HashMap bucket.

### Why Does This Happen?

In Java 8, when collisions become too many:

```
Linked List  →  Red-Black Tree   (Treeification)
```

But if many elements are later **removed**, the tree may contain very few nodes (e.g., only 3 or 4).

Maintaining a Red-Black Tree then becomes **unnecessary overhead** because:
- Tree structure is more complex
- More memory is used
- Balancing operations are needed

For a small number of nodes, a **Linked List is cheaper and simpler**.

So Java converts the tree back to a linked list — this is called **Untreeification**.

---

### Important Thresholds

| Threshold | Value | Meaning |
|-----------|-------|---------|
| `TREEIFY_THRESHOLD` | 8 | Linked List → Tree when bucket size > 8 |
| `UNTREEIFY_THRESHOLD` | 6 | Tree → Linked List when bucket size < 6 |

### Why Are 8 and 6 Different? ⭐

> **Very important interview point.**

If both thresholds were the same (e.g., both 8):

```
8 nodes → Tree
7 nodes → List
8 nodes → Tree
7 nodes → List
```

Continuous conversion would happen — causing **performance overhead**.

So Java uses **Hysteresis** — different upper/lower limits to avoid frequent switching.

---

### Visual Understanding

**Initially:**

```
Bucket
  ↓
Node → Node → Node
(Linked List)
```

**Too many collisions (> 8):**

```
Bucket
  ↓
       50
      /  \
    30    70
(Red-Black Tree)
```

**Elements removed (< 6):**

```
Bucket
  ↓
Node → Node → Node
(Back to Linked List)
```

---

### Internal Java Logic

Internally HashMap checks:

```java
if (number_of_nodes <= UNTREEIFY_THRESHOLD) {
    untreeify();
}
```

### Why Linked List is Better for Small Data?

Suppose only 2 nodes exist.

**Linked list search:** 1 or 2 comparisons — very cheap.

**Red-Black Tree requires:**
- Tree node structure
- Rotations
- Balancing logic

Which is **unnecessary** for small buckets.

---

### Complete Flow

```
Small collisions
    ↓
Linked List

Bucket size > 8
    ↓
Treeify

Bucket size < 6
    ↓
Untreeify
```

---

### Final One-Line Definition

> Untreeification is the process in Java 8+ HashMap where a bucket's Red-Black Tree is converted back into a Linked List when the number of nodes becomes small (less than 6) to reduce overhead and improve efficiency.

---

## 36. Senior Java Developer Level Concepts

### 1. Tree Bins

After treeification, `TreeNode` replaces `Node`. Internally a **Red-Black tree** is maintained.

### 2. Untreeification

If entries reduce below `UNTREEIFY_THRESHOLD = 6`, the tree converts back to a linked list.

### 3. ModCount

HashMap maintains `modCount` — used for fail-fast iterators.

### 4. Comparable Keys Optimization

If keys implement `Comparable`, tree operations become more efficient.

### 5. Hash Flooding Attack

Attackers intentionally generate same hashcodes.
- Before Java 8: O(n)
- After treeification: O(log n)

> Java 8 improved security too.

---

## 36. Common Interview Questions

### Q1. Why are both `equals()` and `hashCode()` needed?

```
hashCode()  →  finds bucket
equals()    →  finds exact object inside bucket
```

### Q2. Can two unequal objects have the same hashcode?

**Yes.** Collision is possible.

### Q3. Why is `String` the best key?

Because `String` is **immutable** and properly overrides `equals()`/`hashCode()`.

### Q4. Why is load factor 0.75?

Good balance between memory and performance.

| Load Factor | Effect |
|-------------|--------|
| Lower | More memory, less collision |
| Higher | Less memory, more collision |

### Q5. Why does `ConcurrentHashMap` not allow null?

Because ambiguity occurs:

```java
map.get(key); // returns null
```

Does null mean **key absent** OR **value is null**?

---

## 37. Best Practices

### Always Override Together

```java
equals()
hashCode()
```

### Avoid Mutable Keys

**Bad:**
- `ArrayList`
- `Date`
- Custom mutable object

### Set Initial Capacity Properly

If large data expected:

```java
new HashMap<>(1000);
```

Avoids frequent resizing.

### Use `ConcurrentHashMap` in Multi-threading

Never use plain `HashMap` in concurrent systems.

---

## 38. Final Revision Summary

### Core Concepts

- HashMap uses hashing for fast access
- Internally uses **Array + LinkedList + Red-Black Tree**
- Collision handled using **separate chaining**
- Java 8 introduced **treeification**
- `equals()` and `hashCode()` are extremely important
- Capacity doubles during resize
- Default capacity = **16**
- Default load factor = **0.75**

### Important Hierarchy

```
Map (interface)
   ↑
AbstractMap
   ↑
HashMap
   ↑
LinkedHashMap
```

### Key Interfaces

| Interface | Purpose |
|-----------|---------|
| `Map` | Key-value contract |
| `Cloneable` | Supports cloning |
| `Serializable` | Supports serialization |

### Key Parent Class

| Class | Purpose |
|-------|---------|
| `AbstractMap` | Partial Map implementation |

---

## 39. Most Important Interview Topics ⭐

Focus heavily on:

1. `equals()` vs `hashCode()`
2. Collision handling
3. Treeification in Java 8
4. Why power of 2?
5. `ConcurrentHashMap` vs `HashMap`
6. Fail-fast iterator
7. Mutable keys problem
8. Internal working of `put()` and `get()`
9. Rehashing
10. Why load factor = 0.75

> These are **very frequently asked** in senior Java interviews.
