# ImmutableMap in Java — Complete Interview Notes

---

## 1. What is ImmutableMap?

An **immutable map** means the map content **cannot be changed after creation**.

| Operation | Allowed? |
|-----------|----------|
| `put()`    | ❌ No    |
| `remove()` | ❌ No    |
| `update()` | ❌ No    |
| `get()`    | ✅ Yes   |

### Example

```java
Map<String, Integer> map = Map.of("Java", 1);

map.put("Spring", 2); // ❌ Throws UnsupportedOperationException
```

### Why Immutability Matters

Immutability provides:

- ✅ **Safety** — no accidental modification
- ✅ **Predictability** — state is always known
- ✅ **Thread-safety benefits** — no race conditions on state changes
- ✅ **Functional programming** alignment

> ⭐ **Interview Statement**: *Immutable objects eliminate side effects because state cannot change after creation.*

Used heavily in:
- Concurrent programming
- Configuration data / constants
- Caching
- API response objects

---

## 2. Mutable vs Immutable Map

### Mutable Map

```java
HashMap<String, Integer> map = new HashMap<>();
map.put("Java", 1);
map.put("Spring", 2); // ✅ Allowed anytime
```

### Immutable Map

```java
Map<String, Integer> map = Map.of("Java", 1);
map.put("Spring", 2); // ❌ UnsupportedOperationException
```

---

## 3. Old Approach: `Collections.unmodifiableMap()`

Before Java 9, immutable-like behavior was achieved using:

```java
Map<String, Integer> original = new HashMap<>();
original.put("Java", 1);

Map<String, Integer> unmodifiable = Collections.unmodifiableMap(original);
```

### ⚠️ MOST IMPORTANT Concept

> `Collections.unmodifiableMap()` does **NOT** create a truly immutable map.
> It creates a **read-only VIEW** over the existing map.

### Visual Understanding

```
original HashMap  ←── actual data lives here
        ↑
unmodifiable view ←── only blocks direct writes through this reference
```

Both references point to the **SAME underlying data**.

### The Problem — Original is Still Mutable

```java
original.put("Spring", 2); // Modifying original

System.out.println(unmodifiable); // {Java=1, Spring=2}
// ⚠️ unmodifiable reflects the change!
```

> ⭐ **Interview Line**: *`Collections.unmodifiableMap()` creates an unmodifiable view, not a deeply immutable map.*

---

## 4. What Happens If You Modify the Wrapper?

```java
unmodifiable.put("Python", 3); // ❌ Throws UnsupportedOperationException
```

The **wrapper blocks** modifications through its reference.  
But the **original** backing map remains fully mutable — this is the **major weakness**.

---

## 5. Java 9 Solution: `Map.of()`

Java 9 introduced `Map.of()` which creates a **truly immutable map** with no exposed mutable backing map.

```java
Map<String, Integer> map = Map.of(
    "Java",   1,
    "Spring", 2
);
```

### Benefits

- ✅ Concise syntax
- ✅ Truly immutable (no backing mutable map)
- ✅ Memory efficient — compact internal structure
- ✅ No separate wrapper needed

### Any Modification Attempt Throws

```java
map.put("React", 3); // ❌ UnsupportedOperationException
```

---

## 6. Why `Map.of()` is Better Than `unmodifiableMap()`

| Reason | Explanation |
|--------|-------------|
| No mutable backing map | Data itself is immutable |
| Compact implementation | Optimized internal structure, no resize logic |
| No wrapper overhead | Single purpose object |
| Predictable state | Cannot change through any reference |

---

## 7. Important Limit of `Map.of()` ⭐ Famous Interview Question

> `Map.of()` supports a **maximum of 10 key-value pairs**.

### Valid

```java
Map.of("A", 1, "B", 2); // ✅
```

### Invalid (> 10 pairs)

```java
Map.of("A",1, "B",2, "C",3, "D",4, "E",5,
       "F",6, "G",7, "H",8, "I",9, "J",10,
       "K",11); // ❌ Compilation error — no such overload
```

### Why the Limit Exists?

Java provides **overloaded methods** internally:

```java
Map.of(K k1, V v1)
Map.of(K k1, V v1, K k2, V v2)
Map.of(K k1, V v1, K k2, V v2, K k3, V v3)
// ... up to 10 key-value pairs
```

This is a deliberate **optimization choice** — compact, type-safe overloads avoiding varargs overhead for common small-map use cases.

---

## 8. `Map.ofEntries()` — For Large Immutable Maps

For maps with **more than 10 entries**, use `Map.ofEntries()`:

```java
Map<String, Integer> map = Map.ofEntries(
    Map.entry("Java",   1),
    Map.entry("Spring", 2),
    Map.entry("React",  3),
    Map.entry("Python", 4)
    // ... unlimited entries
);
```

### Key Points

- ✅ Supports **unlimited entries**
- ✅ Still truly **immutable**
- ✅ Compact and safe
- Uses `Map.entry()` to build key-value pairs

---

## 9. `Map.entry()`

Creates an **immutable key-value pair**, primarily used inside `Map.ofEntries()`.

```java
Map.Entry<String, Integer> entry = Map.entry("Java", 1);
```

> Note: `Map.entry()` itself throws `NullPointerException` if key or value is null.

---

## 10. Null Handling ⭐ Important Interview Question

`Map.of()` and `Map.ofEntries()` do **NOT** allow null keys or null values.

```java
Map.of("Java", null); // ❌ NullPointerException
Map.of(null, 1);      // ❌ NullPointerException
```

### Why Nulls Are Not Allowed?

Immutable collections are **heavily optimized**. Null handling complicates:

- Internal implementation
- Lookup semantics (distinguish "key absent" vs "key maps to null")

So Java **forbids nulls** for simplicity and performance.

> Compare: `HashMap` allows one null key and multiple null values.

---

## 11. Duplicate Keys Not Allowed

```java
Map.of(
    "Java", 1,
    "Java", 2  // ❌ IllegalArgumentException
);
```

### Why?

Immutable maps require a **deterministic, fixed structure** at creation time. Duplicate keys are **ambiguous** — which value wins? Java throws early at creation rather than silently overwriting.

---

## 12. ImmutableMap vs UnmodifiableMap ⭐ MOST IMPORTANT Comparison

| Feature | `Collections.unmodifiableMap()` | `Map.of()` / `Map.ofEntries()` |
|---|---|---|
| Truly Immutable | ❌ No | ✅ Yes |
| Backing Mutable Map Exists | ✅ Yes | ❌ No |
| Reflects Original Changes | ✅ Yes | ❌ No |
| Null Keys/Values Allowed | Depends on backing map | ❌ No |
| Introduced In | Old Java (Java 1.2) | Java 9 |
| Memory Overhead | Higher (wrapper + backing map) | Lower (single compact object) |

---

## 13. Immutability and Thread Safety ⭐ Senior Concept

Immutable objects are **naturally safer for concurrency** because:

- State **never changes** after creation
- No race conditions on modification
- No synchronization needed for reads

### Important Clarification — Structural vs Deep Immutability

```java
Map<String, List<Integer>> map = Map.of(
    "scores", new ArrayList<>(List.of(1, 2, 3))
);
```

| Level | Status |
|-------|--------|
| Map structure | ✅ Immutable — cannot add/remove entries |
| List value inside | ⚠️ Still mutable — `map.get("scores").add(99)` works! |

> ⭐ **Advanced Interview Point**: Immutable collections provide **structural immutability**, not **deep immutability**. If contained values are mutable objects, those can still be mutated.

---

## 14. Defensive Copying

When returning internal maps from methods, protect against caller modification:

### ❌ Bad — Exposes Internal State

```java
public Map<String, Integer> getConfig() {
    return internalMap; // Caller can modify!
}
```

### ✅ Good — Return Immutable Copy

```java
public Map<String, Integer> getConfig() {
    return Map.copyOf(internalMap); // Safe
}
```

---

## 15. `Map.copyOf()` — Java 10

Java 10 introduced `Map.copyOf()` to create a **true immutable copy** of an existing map.

```java
Map<String, Integer> mutable = new HashMap<>();
mutable.put("Java", 1);

Map<String, Integer> immutable = Map.copyOf(mutable); // ✅ Truly immutable copy
```

### `Map.copyOf()` vs `Collections.unmodifiableMap()`

| Feature | `Map.copyOf()` | `unmodifiableMap()` |
|---|---|---|
| Creates separate copy | ✅ Yes | ❌ No (same data) |
| Reflects original changes | ❌ No | ✅ Yes |
| Truly immutable | ✅ Yes | ❌ No |

---

## 16. Internal Optimization

Immutable maps benefit from:

| Optimization | Detail |
|---|---|
| Smaller memory footprint | No load factor, no resize array |
| No resize logic | Fixed size forever |
| No synchronization overhead | Immutable = inherently safe |
| Optimized lookup | Can use specialized compact structures |

> Immutable maps can be **faster for reads** than mutable maps due to these optimizations.

---

## 17. Real Use Cases

| Use Case | Example |
|---|---|
| Configuration constants | App settings, feature flags |
| API responses | Return fixed metadata |
| Lookup tables | Country codes, error messages |
| Enum-like mappings | Status code → description |
| Caching metadata | Pre-computed, never-changing data |
| Functional pipelines | Passing state without mutation risk |

---

## 18. Important Interview Questions

**Q1: Difference between immutable and unmodifiable map?**

> Unmodifiable = **wrapper only** — original backing data can still change.  
> Immutable = **underlying data cannot change** through any reference.

---

**Q2: Why are immutable collections useful?**

> They prevent side effects, improve safety, simplify reasoning, and are naturally thread-safe for structural access.

---

**Q3: Why is `Map.of()` limited to 10 entries?**

> Java provides **overloaded methods** up to 10 key-value pairs for type-safety and performance optimization, avoiding varargs overhead.

---

**Q4: Why are nulls not allowed in `Map.of()`?**

> Simpler, optimized implementation. Nulls complicate lookup semantics and internal structure.

---

**Q5: Is an immutable map fully thread-safe?**

> **Structure: Yes.** No thread can add/remove/update entries.  
> **Contained mutable objects: Maybe not.** If values are mutable (e.g., `List`), those can still be mutated by multiple threads.

---

## 19. Senior Java Developer Concepts

### 1. Structural vs Deep Immutability

| Type | Meaning |
|---|---|
| **Structural Immutability** | Map structure (entries) cannot change |
| **Deep Immutability** | Both map AND all contained objects are immutable |

> For true deep immutability, all values must also be immutable types (e.g., `String`, `Integer`, other immutable records).

---

### 2. Safe Publication

Immutable objects can be safely **published** (shared) between threads without synchronization, because:

- No need to guard against writes
- Any thread sees consistent, complete state

---

### 3. Functional Programming Style

Immutability is a **core principle** in:

- Reactive programming (Project Reactor, RxJava)
- Functional design patterns
- Stream pipelines
- Value objects / DTOs

---

## 20. Final Revision Summary

```
Collections.unmodifiableMap()
├── Read-only WRAPPER
├── Backing map still mutable
└── NOT truly immutable ⚠️

Map.of()
├── Truly immutable
├── Max 10 entries
├── No nulls, no duplicate keys
└── Java 9+

Map.ofEntries()
├── Truly immutable
├── Unlimited entries
├── Uses Map.entry() pairs
└── Java 9+

Map.copyOf()
├── Truly immutable COPY
├── Independent of original
└── Java 10+
```

| Method | Truly Immutable | Max Entries | Nulls | Java Version |
|---|---|---|---|---|
| `Collections.unmodifiableMap()` | ❌ | Unlimited | Depends | Java 1.2 |
| `Map.of()` | ✅ | **10** | ❌ | Java 9 |
| `Map.ofEntries()` | ✅ | Unlimited | ❌ | Java 9 |
| `Map.copyOf()` | ✅ | Unlimited | ❌ | Java 10 |

---

> ⭐ **Shortcut to Remember**:
> - `unmodifiableMap` = **locked door, but someone else has a key** (original map owner)
> - `Map.of()` = **sealed vault — nobody has a key, not even the creator**
