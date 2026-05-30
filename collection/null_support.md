# Java Collections — Null Support Complete Reference

> ⭐ Very important interview topic. Knowing which collections allow null and **why** separates average from strong Java candidates.

---

## 1. Map Implementations

| Collection Class | Null Keys | Null Values | Important Notes |
|---|---|---|---|
| `HashMap` | ✅ One allowed | ✅ Allowed | Most commonly used map |
| `LinkedHashMap` | ✅ One allowed | ✅ Allowed | Same as HashMap + insertion order |
| `TreeMap` | ❌ Usually not allowed | ✅ Allowed | Null key causes comparison issue |
| `Hashtable` | ❌ Not allowed | ❌ Not allowed | Legacy synchronized class |
| `ConcurrentHashMap` | ❌ Not allowed | ❌ Not allowed | Avoids ambiguity in concurrency |
| `WeakHashMap` | ✅ One allowed | ✅ Allowed | Weak references on keys |
| `IdentityHashMap` | ✅ Allowed | ✅ Allowed | Uses `==` instead of `equals()` |
| `EnumMap` | ❌ Not allowed | ✅ Allowed | Keys must be enum constants |
| `ConcurrentSkipListMap` | ❌ Not allowed | ❌ Not allowed | Sorted concurrent map |
| `Map.of()` (Immutable) | ❌ Not allowed | ❌ Not allowed | Throws `NullPointerException` immediately |

### ⚠️ Important TreeMap Note

`TreeMap` keys must be **compared and sorted**, so `null` is not valid:

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(null, "Java"); // ❌ NullPointerException
```

`null` values are fine — only keys require comparison.

---

## 2. Set Implementations

> Sets internally use Map keys — so null rules mirror their backing map's key rules.

| Collection Class | Null Elements Allowed? | Important Notes |
|---|---|---|
| `HashSet` | ✅ One null | Uses `HashMap` internally |
| `LinkedHashSet` | ✅ One null | Maintains insertion order |
| `TreeSet` | ❌ Usually not allowed | Sorting/comparison required |
| `EnumSet` | ❌ Not allowed | Enum constants only |
| `CopyOnWriteArraySet` | ✅ Allowed | Thread-safe snapshot set |
| `ConcurrentSkipListSet` | ❌ Not allowed | Sorted concurrent set |
| `Set.of()` (Immutable) | ❌ Not allowed | Throws `NullPointerException` |

### ⚠️ Important TreeSet Note

`TreeSet` internally uses `TreeMap` → null elements trigger comparison → `NullPointerException`.

---

## 3. List Implementations

Lists are the **most flexible** regarding nulls.

| Collection Class | Null Elements Allowed? | Important Notes |
|---|---|---|
| `ArrayList` | ✅ Multiple nulls | Index-based, dynamic array |
| `LinkedList` | ✅ Multiple nulls | Also implements `Queue`/`Deque` |
| `Vector` | ✅ Allowed | Legacy synchronized list |
| `Stack` | ✅ Allowed | Extends `Vector` |
| `CopyOnWriteArrayList` | ✅ Allowed | Thread-safe snapshot list |
| `List.of()` (Immutable) | ❌ Not allowed | Throws `NullPointerException` |

---

## 4. Queue Implementations ⭐ Important Design Decision

**Most queues do NOT allow null.**

### Why Queues Reject Null

Queue methods return `null` to signal an **empty queue**:

```java
queue.poll() → null  means "queue is empty"
queue.peek() → null  means "queue is empty"
```

If null elements were allowed:

```
poll() returns null
→ Is queue empty?
→ Or was null the actual element?
→ Ambiguous — impossible to tell
```

| Collection Class | Null Allowed? | Important Notes |
|---|---|---|
| `PriorityQueue` | ❌ Not allowed | Heap-based queue |
| `ArrayDeque` | ❌ Not allowed | Faster than `Stack` for most cases |
| `ConcurrentLinkedQueue` | ❌ Not allowed | Lock-free queue |
| `ArrayBlockingQueue` | ❌ Not allowed | Bounded blocking queue |
| `LinkedBlockingQueue` | ❌ Not allowed | Optionally bounded blocking queue |
| `PriorityBlockingQueue` | ❌ Not allowed | Concurrent priority queue |
| `DelayQueue` | ❌ Not allowed | Elements with delay expiry |
| `SynchronousQueue` | ❌ Not allowed | Zero-capacity handoff queue |
| `LinkedList` (as Queue) | ✅ Allowed technically | `LinkedList` permits null elements |

---

## 5. Deque Implementations

| Collection Class | Null Allowed? |
|---|---|
| `ArrayDeque` | ❌ Not allowed |
| `LinkedList` | ✅ Allowed |
| `ConcurrentLinkedDeque` | ❌ Not allowed |
| `LinkedBlockingDeque` | ❌ Not allowed |

---

## 6. Immutable Collections — Java 9+ ⭐

**All immutable factory methods reject null — always.**

| Collection Factory | Null Allowed? |
|---|---|
| `List.of()` | ❌ `NullPointerException` |
| `Set.of()` | ❌ `NullPointerException` |
| `Map.of()` | ❌ Both key and value |
| `Map.ofEntries()` | ❌ `NullPointerException` |

```java
List.of(1, null, 3);     // ❌ NullPointerException
Set.of("A", null);       // ❌ NullPointerException
Map.of("key", null);     // ❌ NullPointerException
```

---

## 7. Most Important Exceptions to Know

| Code | Exception Thrown |
|---|---|
| `new TreeMap<>().put(null, "val")` | `NullPointerException` |
| `new TreeSet<>().add(null)` | `NullPointerException` |
| `new ConcurrentHashMap<>().put(null, "val")` | `NullPointerException` |
| `new PriorityQueue<>().add(null)` | `NullPointerException` |
| `List.of(null)` | `NullPointerException` |
| `new EnumMap<>(Day.class).put(null, "val")` | `NullPointerException` |

---

## 8. Quick Memory Tricks

### ✅ Hash-Based Collections — Usually Allow Null

```
HashMap, HashSet, LinkedHashMap, LinkedHashSet, WeakHashMap
→ null keys/elements generally allowed (one null key)
```

### ❌ Sorted Collections — No Null Keys/Elements

```
TreeMap, TreeSet, ConcurrentSkipListMap, ConcurrentSkipListSet
→ null not allowed — comparison/sorting would throw NPE
```

### ❌ Concurrent Collections — No Null

```
ConcurrentHashMap, ConcurrentSkipListMap,
ConcurrentLinkedQueue, ConcurrentLinkedDeque
→ null not allowed — avoids ambiguity between "absent" and "null value"
```

### ❌ Immutable Collections — Never Allow Null

```
List.of(), Set.of(), Map.of(), Map.ofEntries()
→ null never allowed — throws NPE at construction
```

### ❌ Most Queues — No Null

```
PriorityQueue, ArrayDeque, ArrayBlockingQueue, LinkedBlockingQueue
→ null not allowed — poll()/peek() return null to mean "empty"
```

---

## 9. MOST IMPORTANT Interview Concepts

| Topic | Key Point |
|---|---|
| `HashMap` null handling | One null key allowed, multiple null values allowed |
| `ConcurrentHashMap` | No nulls — avoids ambiguity in concurrent `get()` |
| `TreeMap` / `TreeSet` | No null keys/elements — sorting requires `compareTo()` |
| Queue null restriction | `poll()`/`peek()` return null for empty — null elements cause ambiguity |
| Immutable collections | No nulls ever — `NullPointerException` at creation |
| `Hashtable` | No nulls at all — legacy strict behavior |

### Deep Insight — Why ConcurrentHashMap Forbids Null

In `HashMap`, if `get(key)` returns `null`:

```
→ Key was explicitly mapped to null
OR
→ Key does not exist in map
```

In single-threaded code this is resolvable with `containsKey()`.

In **concurrent** code:

```java
if (!map.containsKey(key)) {
    map.put(key, compute());
}
```

Between `containsKey()` and `put()`, another thread may have inserted the key.

Allowing null would make this race condition **undetectable** — so `ConcurrentHashMap` forbids null entirely.

---

## 10. Final Super-Short Revision Table

| Collection | Null Keys | Null Values / Elements |
|---|---|---|
| `HashMap` | ✅ One | ✅ Multiple |
| `LinkedHashMap` | ✅ One | ✅ Multiple |
| `TreeMap` | ❌ | ✅ Values ok |
| `Hashtable` | ❌ | ❌ |
| `ConcurrentHashMap` | ❌ | ❌ |
| `WeakHashMap` | ✅ One | ✅ |
| `EnumMap` | ❌ | ✅ |
| `HashSet` | — | ✅ One null element |
| `LinkedHashSet` | — | ✅ One null element |
| `TreeSet` | — | ❌ |
| `ArrayList` | — | ✅ Multiple |
| `LinkedList` | — | ✅ Multiple |
| `PriorityQueue` | — | ❌ |
| `ArrayDeque` | — | ❌ |
| `List.of()` | — | ❌ |
| `Set.of()` | — | ❌ |
| `Map.of()` | ❌ | ❌ |

---

> ⭐ **Shortcut to Remember:**
> - **Hash collections** → null friendly
> - **Sorted collections** → null hostile (can't compare null)
> - **Concurrent collections** → null hostile (ambiguity in concurrent access)
> - **Immutable collections** → null hostile (strict by design)
> - **Queues** → null hostile (null = empty signal)
