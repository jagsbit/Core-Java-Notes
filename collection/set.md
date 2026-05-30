# Set Interface in Java — Complete Interview Notes

---

## 1. What is Set?

`Set` is a collection that **does NOT allow duplicate elements**.

> ⭐ MOST IMPORTANT definition.

### Example

```java
Set<Integer> set = new HashSet<>();

set.add(10);
set.add(10); // duplicate — ignored
set.add(20);

System.out.println(set); // [10, 20]
```

### Why Duplicates Are Prevented

Before insertion, Set internally checks:
- **Equality** via `equals()`
- **Hash** via `hashCode()` (for hash-based sets)
- **Comparison** via `compareTo()` (for sorted sets)

---

## 2. Hierarchy

```
Iterable
   ↑
Collection
   ↑
Set
   ↑
HashSet / LinkedHashSet / TreeSet / EnumSet
```

### Declaration

```java
public interface Set<E> extends Collection<E>
```

> A `Set` models a **mathematical set** — all elements are unique.

---

## 3. Relationship Between Set and Map ⭐ MOST IMPORTANT

Java Sets **internally use Maps**.

### HashSet Internally Uses HashMap

In `HashMap`, **keys are already unique**. So `HashSet` simply stores:

```
elements as map keys → with a dummy constant value
```

### Internal Structure

```java
// Conceptually inside HashSet:
HashMap<E, Object> map;
static final Object PRESENT = new Object(); // dummy value
```

### Example

```java
set.add("Java");

// Internally becomes:
map.put("Java", PRESENT);
```

> ⭐ **Interview Statement**: *HashSet internally uses HashMap and stores elements as keys with a dummy constant value `PRESENT`.*

---

## 4. Core Set Implementations

| Set Type | Ordering | Internal Structure | Complexity |
|---|---|---|---|
| `HashSet` | No order | `HashMap` | O(1) avg |
| `LinkedHashSet` | Insertion order | `LinkedHashMap` | O(1) avg |
| `TreeSet` | Sorted (natural/comparator) | Red-Black Tree (`TreeMap`) | O(log n) |
| `EnumSet` | Enum declaration order | Bit vector / bitmask | O(1) |

---

## 5. HashSet

The most commonly used `Set` implementation.

### Features

- ✅ Unordered
- ✅ Unique elements
- ✅ Fastest general-purpose set

### Internal Structure

Uses `HashMap` internally — element stored as key, `PRESENT` as value.

### Complexity

| Operation | Average |
|---|---|
| `add()` | O(1) |
| `contains()` | O(1) |
| `remove()` | O(1) |

### Null Handling

`HashSet` allows **exactly one null element** — because `HashMap` allows one null key.

```java
Set<Integer> set = new HashSet<>();
set.add(null); // ✅ allowed
set.add(null); // ❌ duplicate — ignored
```

---

## 6. LinkedHashSet

Maintains **insertion order**.

```java
Set<Integer> set = new LinkedHashSet<>();

set.add(30);
set.add(10);
set.add(20);

System.out.println(set); // [30, 10, 20] — insertion order preserved
```

### Internal Structure

Uses `LinkedHashMap` internally — adds a **doubly linked list** for order tracking.

### Complexity

Still approximately O(1) but with slightly **more memory overhead** than `HashSet`.

---

## 7. TreeSet

Maintains elements in **sorted order** (natural ordering or custom `Comparator`).

```java
Set<Integer> set = new TreeSet<>();

set.add(30);
set.add(10);
set.add(20);

System.out.println(set); // [10, 20, 30] — automatically sorted
```

### Internal Structure

Uses `TreeMap` internally → `TreeMap` uses a **Red-Black Tree**.

### Complexity

| Operation | Time |
|---|---|
| `add()` | O(log n) |
| `contains()` | O(log n) |
| `remove()` | O(log n) |

### NavigableSet Support

`TreeSet` implements `NavigableSet` — supports powerful navigation methods:

```java
TreeSet<Integer> set = new TreeSet<>(Set.of(10, 20, 30));

set.higher(20);   // 30 — strictly greater than 20
set.lower(20);    // 10 — strictly less than 20
set.ceiling(20);  // 20 — >= 20
set.floor(20);    // 20 — <= 20
```

### Null Handling

`TreeSet` usually **does NOT allow null** — comparisons would throw `NullPointerException`.

---

## 8. EnumSet

Specialized set for **enum values only** — extremely optimized.

### Internal Structure

Uses **bit vectors / bitmasks** internally — one bit per enum constant.

Very memory efficient.

```java
enum Day { MONDAY, TUESDAY, WEDNESDAY }

EnumSet<Day> set = EnumSet.of(Day.MONDAY, Day.WEDNESDAY);
```

### Why EnumSet is Faster

`EnumSet` operations reduce to **bitwise operations** on a `long` — O(1) with extremely low overhead.

> Much faster than `HashSet<EnumType>` for enum keys.

---

## 9. Common Set Methods

Since `Set` extends `Collection`:

### `add()`

```java
boolean add(E e);
// returns true  → element inserted
// returns false → duplicate, ignored
```

### `contains()`

```java
boolean contains(Object o);
```

### `remove()`

```java
boolean remove(Object o);
```

### `clear()`, `isEmpty()`, `size()`

```java
set.clear();        // remove all elements
set.isEmpty();      // true if empty
set.size();         // number of elements
```

---

## 10. Set Equality Rules ⭐ Important

Set uniqueness depends on **correct implementation of `equals()` and `hashCode()`** (for hash-based sets).

### Example — Custom Object Without `equals()`/`hashCode()`

```java
class Student {
    int id;
    // No equals() or hashCode() overridden
}

Set<Student> set = new HashSet<>();
set.add(new Student(1));
set.add(new Student(1)); // ❌ Both added! — different object references
```

> Without proper `equals()` and `hashCode()`, duplicates can appear unexpectedly in `HashSet`.

---

## 11. Thread Safety in Sets

`HashSet`, `LinkedHashSet`, `TreeSet` are all **NOT thread-safe**.

### Old Approach — Synchronized Wrapper

```java
Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());
```

**Problem:** Uses coarse-grained locking — entire set locked per operation. Slower under contention.

---

## 12. ConcurrentSkipListSet

Recommended **concurrent sorted set**.

### Features

- ✅ Thread-safe
- ✅ Sorted (natural/comparator order)
- ✅ Scalable concurrency — no single lock

### Internal Structure

Uses `ConcurrentSkipListMap` internally.

### Complexity

| Operation | Time |
|---|---|
| `add()` | O(log n) |
| `contains()` | O(log n) |
| `remove()` | O(log n) |

### Iterator

**Weakly consistent** — modifications during iteration may or may not be visible. Never throws `ConcurrentModificationException`.

---

## 13. CopyOnWriteArraySet ⭐ Important Concurrency Topic

### Internal Structure

Uses `CopyOnWriteArrayList` internally.

### Main Idea

On every modification:

```
Entire internal array is COPIED
→ New copy modified
→ Reference atomically swapped
```

### Best Use Case

- ✅ **Read-heavy, write-rare** systems
- ✅ Readers never blocked
- ✅ Stable snapshot available during reads

### Example Scenarios

- Configuration listeners
- Subscriber / event handler lists
- Plugin registries

### Trade-off

| Operation | Performance |
|---|---|
| Read / iterate | ✅ Very fast — no locking |
| Write (`add`, `remove`) | ❌ Expensive — full array copy |

---

## 14. Stable Snapshot vs Weak Consistency ⭐ Senior-Level Topic

| Feature | `CopyOnWriteArraySet` | `ConcurrentSkipListSet` |
|---|---|---|
| Iterator type | **Stable snapshot** | **Weakly consistent** |
| Modifications visible during iteration | ❌ No | May or may not |
| `ConcurrentModificationException` | ❌ Never | ❌ Never |
| Best for | Read-heavy | Read-write balanced + sorted |

### Example Scenario

Thread modifies set while another thread iterates:

```
CopyOnWriteArraySet:
  Iterating thread sees OLD snapshot — unaffected by write.

ConcurrentSkipListSet:
  Iterating thread may partially reflect the new state.
```

---

## 15. Immutable Sets — Java 9+

```java
Set<Integer> set = Set.of(1, 2, 3);

set.add(4); // ❌ UnsupportedOperationException
```

### Key Points

- ✅ Truly immutable — no backing mutable set exposed
- ❌ Null elements not allowed
- ❌ Duplicate elements throw `IllegalArgumentException`

---

## 16. `Collections.unmodifiableSet()`

Creates a **read-only wrapper** — NOT truly immutable.

```java
Set<Integer> unmodifiable = Collections.unmodifiableSet(original);
```

> Same concept as `Collections.unmodifiableMap()` — if original set changes, wrapper reflects it.

---

## 17. HashSet vs TreeSet ⭐ MOST IMPORTANT Comparison

| Feature | `HashSet` | `TreeSet` |
|---|---|---|
| Ordering | ❌ No order | ✅ Sorted |
| Complexity | O(1) average | O(log n) |
| Internal Structure | `HashMap` | Red-Black Tree (`TreeMap`) |
| Null Allowed | ✅ One null | ❌ Usually no |
| `NavigableSet` support | ❌ No | ✅ Yes |
| Use When | Fast lookup, no order needed | Sorted access needed |

---

## 18. HashSet vs LinkedHashSet

| Feature | `HashSet` | `LinkedHashSet` |
|---|---|---|
| Ordering | ❌ No | ✅ Insertion order |
| Speed | Slightly faster | Slightly slower |
| Memory | Lower | Higher (linked list overhead) |
| Internal Structure | `HashMap` | `LinkedHashMap` |

---

## 19. TreeSet vs ConcurrentSkipListSet

| Feature | `TreeSet` | `ConcurrentSkipListSet` |
|---|---|---|
| Thread-Safe | ❌ No | ✅ Yes |
| Internal Structure | Red-Black Tree | Skip List |
| Complexity | O(log n) | O(log n) |
| Iterator | Fail-Fast | Weakly Consistent |
| Use When | Single-threaded sorted | Concurrent sorted access |

---

## 20. Important Interview Questions

**Q1: How does HashSet prevent duplicates?**

> Uses `hashCode()` and `equals()` through the underlying `HashMap`. If `hashCode()` matches and `equals()` returns `true`, the element is considered a duplicate and insertion is rejected.

---

**Q2: Why does HashSet allow one null?**

> Because `HashMap` (which backs `HashSet`) allows one null key. `null.hashCode()` is treated as 0 and stored at bucket 0.

---

**Q3: Why does TreeSet usually disallow null?**

> `TreeSet` uses `compareTo()` or `Comparator` to maintain sorted order. Calling `compareTo()` on null throws `NullPointerException`.

---

**Q4: Difference between HashSet and TreeSet?**

> `HashSet` uses hashing → O(1) average, unordered.
> `TreeSet` uses Red-Black Tree → O(log n), sorted.

---

**Q5: Best concurrent set choice?**

> - Sorted needed → **`ConcurrentSkipListSet`**
> - Read-heavy, rarely modified → **`CopyOnWriteArraySet`**
> - Unordered, high contention → **`Collections.synchronizedSet(new HashSet<>())`** (or `ConcurrentHashMap.newKeySet()`)

---

## 21. Senior Java Developer Concepts

### 1. Set Backed by Map

Almost all major `Set` implementations internally use their corresponding `Map`:

```
HashSet        → HashMap
LinkedHashSet  → LinkedHashMap
TreeSet        → TreeMap
ConcurrentSkipListSet → ConcurrentSkipListMap
```

### 2. Copy-On-Write Trade-off

```
Writes: expensive (full array copy)
Reads:  extremely fast (no lock, stable snapshot)
```

### 3. Weakly Consistent Iterators

Concurrent collections (`ConcurrentSkipListSet`) avoid fail-fast exceptions by using weakly consistent iterators that tolerate concurrent modifications.

### 4. NavigableSet

`TreeSet` and `ConcurrentSkipListSet` both implement `NavigableSet`:

```java
floor(e)    // greatest element ≤ e
ceiling(e)  // smallest element ≥ e
lower(e)    // greatest element < e
higher(e)   // smallest element > e
headSet(e)  // elements < e
tailSet(e)  // elements ≥ e
```

---

## 22. Final Revision Summary

```
HashSet
├── Unordered, unique
├── Backed by HashMap
├── O(1) average
└── One null allowed

LinkedHashSet
├── Insertion-ordered, unique
├── Backed by LinkedHashMap
└── O(1) average

TreeSet
├── Sorted (Red-Black Tree)
├── Backed by TreeMap
├── O(log n)
├── No null
└── NavigableSet support

EnumSet
├── Enum values only
├── Bit vector internals
└── Fastest set for enums

ConcurrentSkipListSet
├── Thread-safe sorted set
├── Backed by ConcurrentSkipListMap
├── O(log n)
└── Weakly consistent iterator

CopyOnWriteArraySet
├── Thread-safe
├── Backed by CopyOnWriteArrayList
├── Read-heavy use case
└── Stable snapshot iterator
```

| Set | Order | Thread-Safe | Null | Complexity |
|---|---|---|---|---|
| `HashSet` | None | ❌ | ✅ One | O(1) |
| `LinkedHashSet` | Insertion | ❌ | ✅ One | O(1) |
| `TreeSet` | Sorted | ❌ | ❌ | O(log n) |
| `EnumSet` | Enum order | ❌ | ❌ | O(1) |
| `ConcurrentSkipListSet` | Sorted | ✅ | ❌ | O(log n) |
| `CopyOnWriteArraySet` | Insertion | ✅ | ✅ | O(n) write |

---

> ⭐ **Shortcut to Remember:**
> - Need speed, no order → **`HashSet`**
> - Need insertion order → **`LinkedHashSet`**
> - Need sorted order → **`TreeSet`**
> - Need sorted + thread-safe → **`ConcurrentSkipListSet`**
> - Read-heavy + thread-safe → **`CopyOnWriteArraySet`**
> - Enum keys → **`EnumSet`**
