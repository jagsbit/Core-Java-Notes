# Java SortedMap, NavigableMap & TreeMap — Complete Notes

> These are heavily asked in Java interviews, system design interviews, and backend development interviews.

---

## 1. Why Do We Need TreeMap?

### Problem with HashMap

```java
HashMap<Integer,String> map = new HashMap<>();

map.put(3, "C");
map.put(1, "A");
map.put(2, "B");

System.out.println(map);
```

**Possible Output:**
```
{2=B, 1=A, 3=C}
```

Order is **unpredictable**.

### Requirement

Sometimes we need **keys automatically sorted**.

**Examples:**
- Ranking systems
- Leaderboard
- Range queries
- Nearest value search
- Scheduling systems

This is where `SortedMap`, `NavigableMap`, and `TreeMap` come in.

---

## 2. Hierarchy

> **Very important interview hierarchy.**

```
Map
 ↑
SortedMap
 ↑
NavigableMap
 ↑
TreeMap
```

---

## 3. SortedMap Interface

`SortedMap` is an **interface**.

**Declaration:**

```java
public interface SortedMap<K,V>
    extends Map<K,V>
```

### Main Purpose

Provides **sorted ordering of keys**.

### Sorting Options

**Natural Ordering** — uses `Comparable`

| Type | Natural Order |
|------|--------------|
| `Integer` | Numerical |
| `String` | Alphabetical |

**Custom Ordering** — uses `Comparator` provided during map creation.

### Example

```java
TreeMap<Integer,String> map = new TreeMap<>();

map.put(3, "C");
map.put(1, "A");
map.put(2, "B");

System.out.println(map);
```

**Output:**
```
{1=A, 2=B, 3=C}
```

---

## 4. Important SortedMap Methods

### `firstKey()`

Returns the **smallest** key.

```java
map.firstKey();
```

### `lastKey()`

Returns the **largest** key.

```java
map.lastKey();
```

### `headMap(toKey)`

Returns keys **strictly less than** `toKey`.

```java
map.headMap(5); // returns keys < 5
```

### `tailMap(fromKey)`

Returns keys **>= fromKey**.

```java
map.tailMap(5); // returns keys >= 5
```

### `subMap(from, to)`

Returns range: **fromKey <= key < toKey**

```java
map.subMap(2, 5); // returns keys from 2 (inclusive) to 5 (exclusive)
```

> Very important for **range queries**.

---

## 5. TreeMap Class

`TreeMap` is the implementation class.

**Declaration:**

```java
public class TreeMap<K,V>
    extends AbstractMap<K,V>
    implements NavigableMap<K,V>
```

### Internal Data Structure

> **MOST important TreeMap interview point.**

`TreeMap` internally uses a **Red-Black Tree**, NOT a hash table.

---

## 6. What is Red-Black Tree?

A Red-Black Tree is a **Self-balancing Binary Search Tree**.

Meaning: the tree automatically stays balanced → height remains small.

### Why Balancing is Needed?

A normal BST can become **skewed**:

```
1
 \
  2
   \
    3
```

Search becomes **O(n)** — very slow.

### Red-Black Tree Prevents This

Keeps the tree balanced → operations remain **O(log n)**.

---

## 7. TreeMap Time Complexity

| Operation | Complexity |
|-----------|------------|
| `put()` | O(log n) |
| `get()` | O(log n) |
| `remove()` | O(log n) |
| `containsKey()` | O(log n) |
| `containsValue()` | O(n) |

### Why `containsValue()` is O(n)?

Because **values are NOT sorted** — the entire tree must be scanned.

---

## 8. TreeMap Ordering

TreeMap sorts **ONLY by KEYS**, not values.

### Example

```java
TreeMap<Integer,String> map = new TreeMap<>();

map.put(30, "C");
map.put(10, "A");
map.put(20, "B");

System.out.println(map);
```

**Output:**
```
{10=A, 20=B, 30=C}
```

---

## 9. Comparable Requirement

Keys must either:
- Implement `Comparable`
- OR have a `Comparator` supplied

Otherwise: `ClassCastException` occurs.

**Example — Fails:**

```java
class Employee {
    int id;
}

TreeMap<Employee, String> map = new TreeMap<>(); // ❌ ClassCastException
```

---

## 10. Custom Comparator

```java
TreeMap<Integer,String> map =
    new TreeMap<>((a, b) -> b - a); // descending order
```

**Output:**
```
{30=C, 20=B, 10=A}
```

---

## 11. Why TreeMap Does NOT Allow Null Keys? ⭐

> **Very famous interview question.**

TreeMap needs key comparisons:

```
compareTo()
compare()
```

But `null` **cannot be compared**.

```java
map.put(null, "A"); // ❌ NullPointerException
```

### Important Point

TreeMap does **NOT** allow null keys.
TreeMap **DOES** allow multiple null values.

---

## 12. NavigableMap Interface

`NavigableMap` extends `SortedMap`.

**Declaration:**

```java
public interface NavigableMap<K,V>
    extends SortedMap<K,V>
```

### Main Purpose

Provides **navigation methods** for nearest matching keys.

---

## 13. Important NavigableMap Methods

### `lowerKey(key)`

Returns **greatest key strictly smaller** than given key.

```java
map.lowerKey(20); // returns 10
```

### `floorKey(key)`

Returns **greatest key <= given key**.

```java
map.floorKey(20); // returns 20
```

### `higherKey(key)`

Returns **smallest key strictly greater** than given key.

```java
map.higherKey(20); // returns 30
```

### `ceilingKey(key)`

Returns **smallest key >= given key**.

```java
map.ceilingKey(20); // returns 20
```

### Visual Example

Suppose map contains keys: `10, 20, 30`

| Method | Input | Output |
|--------|-------|--------|
| `lowerKey()` | 20 | 10 |
| `floorKey()` | 20 | 20 |
| `higherKey()` | 20 | 30 |
| `ceilingKey()` | 20 | 20 |

---

## 14. `descendingMap()`

Returns a **reverse-order view** of the map.

```java
map.descendingMap();
```

**Output:**
```
{30=C, 20=B, 10=A}
```

---

## 15. TreeMap Internal Working

Suppose insertions: `50, 20, 70, 10, 30`

Internally a BST-like structure is created:

```
       50
      /  \
    20    70
   /  \
 10   30
```

Balanced using **Red-Black rules**.

---

## 16. HashMap vs TreeMap

| Feature | HashMap | TreeMap |
|---------|---------|---------|
| Ordering | No | Sorted |
| Structure | Hash Table | Red-Black Tree |
| Complexity | O(1) | O(log n) |
| Null Key | Allowed | Not allowed |
| Range Queries | Difficult | Easy |

---

## 17. LinkedHashMap vs TreeMap

| Feature | LinkedHashMap | TreeMap |
|---------|---------------|---------|
| Order Type | Insertion order | Sorted order |
| Complexity | O(1) | O(log n) |
| Internal Structure | DLL + Hash Table | Red-Black Tree |

---

## 18. Important TreeMap Interview Questions

### Q1. Why is TreeMap slower than HashMap?

`TreeMap` uses tree traversal → **O(log n)**.
`HashMap` uses hashing → **O(1)**.

### Q2. Why does TreeMap use a Red-Black Tree?

To guarantee **balanced height** and **logarithmic complexity**.

### Q3. Why are null keys not allowed?

Because **comparison is impossible** with `null`.

### Q4. How does TreeMap sort objects?

Using `Comparable` or a custom `Comparator`.

### Q5. Does TreeMap maintain insertion order?

**No.** It maintains **sorted order** only.

---

## 19. Senior Java Developer Concepts

### 1. TreeMap Entries Are Nodes

Internally each entry is:

```java
Entry<K,V> {
    K key
    V value
    Entry<K,V> left
    Entry<K,V> right
    Entry<K,V> parent
    boolean color  // RED or BLACK
}
```

### 2. Red-Black Tree Rules

Every node is either **RED** or **BLACK**. These rules maintain balance.

### 3. TreeMap Iteration is Always Sorted

Iteration order is always in **sorted key order**.

### 4. NavigableMap Supports Efficient Range Queries

Very useful in:
- Databases
- Schedulers
- Search systems

---

## 20. Real Use Cases

### TreeMap

Use when:
- Sorted reports
- Ranking systems
- Leaderboard
- Range filtering

### NavigableMap

Use when:
- Nearest timestamp lookup
- Closest price search
- Floor/ceiling operations
- Interval searching

---

## 21. Internal Flow of `put()`

```
1. Compare keys
2. Traverse tree
3. Insert node
4. Rebalance tree
5. Maintain Red-Black properties
```

---

## 22. Internal Flow of `get()`

```
1. Start from root
2. Compare key
3. Move left (if smaller) or right (if larger)
4. Return matching node
```

---

## 23. Thread Safety

`TreeMap` is **NOT thread-safe**.

**Alternatives:**
- Use synchronization
- Or use `ConcurrentSkipListMap`

---

## 24. `ConcurrentSkipListMap` ⭐

> Advanced interview topic.

A **thread-safe, sorted map** — concurrent alternative to `TreeMap`.

---

## 25. Final Revision Summary

### SortedMap

```
Provides sorted ordering of keys
firstKey() / lastKey()
headMap() / tailMap() / subMap()
```

### NavigableMap

```
Provides navigation operations
lowerKey() / floorKey()
higherKey() / ceilingKey()
descendingMap()
```

### TreeMap

```
Red-Black Tree implementation
O(log n) for all key operations
No null keys allowed
Supports Comparable + Comparator
```

### Key Points

- ✅ TreeMap sorts by **keys** only
- ✅ Internally uses **Red-Black Tree**
- ✅ `null` keys **not allowed**
- ✅ `null` values allowed
- ✅ Supports natural and custom ordering
- ✅ O(log n) time complexity
- ✅ Not thread-safe → use `ConcurrentSkipListMap`
- ✅ `NavigableMap` provides floor/ceiling/lower/higher methods

### Shortcut to Remember

```
HashMap      →  Fast, No Order
LinkedHashMap →  Fast, Insertion Order
TreeMap      →  Slower, Sorted Order
```
