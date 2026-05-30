# EnumMap in Java — Complete Interview Notes

---

## 1. What is EnumMap?

`EnumMap` is a **specialized Map implementation** in Java designed **exclusively for enum keys**.

### Declaration

```java
public class EnumMap<K extends Enum<K>, V>
    extends AbstractMap<K, V>
    implements Serializable, Cloneable
```

> ⭐ Very important interview declaration.

### MOST IMPORTANT Point

EnumMap works **ONLY** when keys are:
- enum constants
- from the **SAME enum type**

### Example Enum

```java
enum Day {
    MONDAY,
    TUESDAY,
    WEDNESDAY
}
```

### Example EnumMap

```java
EnumMap<Day, String> map = new EnumMap<>(Day.class);
```

---

## 2. Why EnumMap?

You *can* use `HashMap<Day, String>`, but Java provides `EnumMap` because:

- Enums have **fixed, finite values**
- All possible keys are **known beforehand**
- This makes the map **highly optimizable**

### The Key Difference

| HashMap Needs | EnumMap Avoids |
|---|---|
| Hashing | ✅ No hashing |
| Collision handling | ✅ No collisions |
| Resizing | ✅ No resizing |
| Nodes / linked lists / trees | ✅ No complex structures |

> ⭐ **Interview Statement**: *EnumMap is a specialized high-performance map implementation for enum keys.*

---

## 3. Internal Working of EnumMap ⭐ MOST IMPORTANT

### EnumMap Internally Uses an ARRAY

NOT a hash table, tree, or linked list — just a plain **Object array**.

### Core Idea — `ordinal()` as Array Index

Every enum constant has an `ordinal()` method that returns its position (0-based).

```java
enum Day {
    MONDAY,    // ordinal() = 0
    TUESDAY,   // ordinal() = 1
    WEDNESDAY  // ordinal() = 2
}
```

| Enum Constant | `ordinal()` | Array Index |
|---|---|---|
| `MONDAY` | 0 | `array[0]` |
| `TUESDAY` | 1 | `array[1]` |
| `WEDNESDAY` | 2 | `array[2]` |

### Example — `put()` Operation

```java
map.put(Day.TUESDAY, "Gym");
```

**Internally:**
```
array[1] = "Gym";  // Day.TUESDAY.ordinal() == 1
```

### Example — `get()` Operation

```java
map.get(Day.TUESDAY);
```

**Internally:**
```
return array[1];   // Direct array access — extremely fast!
```

---

## 4. Internal Array Size ⭐ Important Interview Question

The internal array size in `EnumMap` is directly based on the **number of enum constants**, NOT a default capacity like `HashMap`.

### Example

```java
enum Day {
    MONDAY, TUESDAY, WEDNESDAY  // 3 constants
}

EnumMap<Day, String> map = new EnumMap<>(Day.class);
```

**Internally creates:**
```java
Object[] vals = new Object[3];  // Exactly 3 — one slot per enum constant
```

### Another Example

```java
enum Status {
    NEW, PROCESSING, COMPLETED, FAILED  // 4 constants
}
// Internally: Object[] vals = new Object[4];
```

### Internal Implementation Detail

```java
private transient Object[] vals;
// Size = keyUniverse.length
// where keyUniverse = all enum constants via Day.values()
```

### HashMap vs EnumMap Capacity

| Feature | HashMap | EnumMap |
|---|---|---|
| Default Capacity | 16 | **Enum constant count** |
| Dynamic Resize | ✅ Yes | ❌ No |
| Collision Handling | ✅ Yes | ❌ No |
| Internal Structure | Buckets (Array + LL + Tree) | Plain Array |

### Why No Resizing Needed?

Because `EnumMap` already knows the **maximum possible keys** at initialization:

```
enum Day { MONDAY, TUESDAY, WEDNESDAY }
→ Maximum possible keys = 3
→ No other key can ever exist
→ Fixed-size array sufficient forever
```

> ⭐ **Senior Insight**: EnumMap achieves memory efficiency, cache locality, and zero resizing overhead because the enum key-space is **finite and fully known at initialization time**.

---

## 5. Why EnumMap is Faster Than HashMap?

| Reason | Detail |
|---|---|
| No hashing | Direct `ordinal()` index lookup |
| No collision resolution | Each enum has a unique ordinal |
| No resizing | Fixed array size forever |
| No node/pointer overhead | Just an `Object[]` array |
| CPU cache-friendly | Contiguous array memory layout |

### Complexity

All core operations (`put`, `get`, `remove`, `containsKey`) are:

$$O(1)$$

with **very low constant overhead** — faster than HashMap's O(1) due to eliminated hashing costs.

---

## 6. No Collisions

`HashMap` problem: multiple keys can hash to the same bucket.

`EnumMap` solution: every enum constant has a **unique ordinal** → **zero collisions, ever**.

> ⭐ **Interview Point**: EnumMap is collision-free by design.

---

## 7. Memory Efficiency

| HashMap Stores | EnumMap Stores |
|---|---|
| `Node` objects with key, value, hash, next pointer | Just `array[index] = value` |
| Hash values per entry | Nothing extra |
| Linked list / tree nodes on collision | N/A — no collisions |

EnumMap has a **much lighter memory footprint**.

---

## 8. Constructor Requirement ⭐ Important

You **MUST** pass the enum class to the constructor:

```java
new EnumMap<>(Day.class)
```

### Why?

EnumMap needs to know:
- Total number of enum constants → to allocate array size
- Ordinal mapping → for index-based access

**Internally:**
```java
// EnumMap constructor does approximately:
K[] keyUniverse = Day.values();
Object[] vals = new Object[keyUniverse.length];
```

---

## 9. Ordering in EnumMap

`EnumMap` maintains **natural enum declaration order**, NOT insertion order.

```java
map.put(Day.WEDNESDAY, "Code");
map.put(Day.MONDAY, "Walk");

System.out.println(map);
// Output: {MONDAY=Walk, WEDNESDAY=Code}
// ✅ MONDAY comes first — enum declaration order preserved
```

---

## 10. Null Handling

| | Allowed? | Detail |
|---|---|---|
| **Null Keys** | ❌ No | `NullPointerException` — `ordinal()` cannot be called on `null` |
| **Null Values** | ✅ Yes | `map.put(Day.MONDAY, null)` is valid |

```java
map.put(null, "Test");        // ❌ NullPointerException
map.put(Day.MONDAY, null);    // ✅ Allowed
```

---

## 11. EnumMap vs HashMap ⭐ MOST IMPORTANT Comparison

| Feature | HashMap | EnumMap |
|---|---|---|
| Key Type | Any object | **Enum only** |
| Internal Structure | Hash Table (Array + LL + Tree) | **Plain Array** |
| Default Capacity | 16 | **Enum constant count** |
| Dynamic Resize | ✅ Yes | ❌ No |
| Collision Handling | ✅ Yes | ❌ No |
| Ordering | No guarantee | **Enum declaration order** |
| Memory Usage | Higher | **Lower** |
| Performance | Fast O(1) | **Faster O(1) for enums** |
| Null Keys | ✅ One allowed | ❌ Not allowed |

---

## 12. Why SonarLint Recommends EnumMap

Using `HashMap<EnumType, ...>` when keys are enums:

- Wastes memory on `Node` objects and hash values
- Wastes CPU on hashing and collision resolution
- Loses enum declaration ordering

`EnumMap` is **semantically correct** and **optimized** — SonarLint flags `HashMap` with enum keys as a code smell.

---

## 13. Internal Put and Get Flow

### `put()` Flow

```
map.put(Day.TUESDAY, "Gym")
        ↓
1. Get ordinal: Day.TUESDAY.ordinal() → 1
2. Store: array[1] = "Gym"
```

### `get()` Flow

```
map.get(Day.TUESDAY)
        ↓
1. Get ordinal: Day.TUESDAY.ordinal() → 1
2. Return: array[1]  →  "Gym"
```

### Conceptual Internal State

```
Object[] vals = [Walk, Gym, null]
                  ↑     ↑     ↑
               MONDAY TUESDAY WEDNESDAY
```

---

## 14. Thread Safety

`EnumMap` is **NOT thread-safe**.

For concurrent access:

```java
// Option 1: Synchronized wrapper
Map<Day, String> syncMap = Collections.synchronizedMap(new EnumMap<>(Day.class));

// Option 2: External synchronization with locks
```

---

## 15. EnumSet vs EnumMap

| | EnumSet | EnumMap |
|---|---|---|
| Stores | Enum values only | Enum → value mappings |
| Purpose | Set of enum constants | Map with enum keys |
| Internal Structure | BitVector / long bitmask | Object array |
| Example | `EnumSet<Day>` | `EnumMap<Day, String>` |

```java
// EnumSet — just a collection of enum values
EnumSet<Day> workdays = EnumSet.of(Day.MONDAY, Day.TUESDAY);

// EnumMap — enum keys mapped to values
EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
schedule.put(Day.MONDAY, "Walk");
```

---

## 16. Real Use Cases

`EnumMap` is perfect for:

| Use Case | Example |
|---|---|
| State machines | `EnumMap<State, Handler>` |
| Configuration mappings | `EnumMap<Feature, Boolean>` |
| Workflow status | `EnumMap<Status, String>` |
| Game states | `EnumMap<GameState, Action>` |
| Switch-like behavior | Replace `switch` with map lookup |
| Day/month scheduling | `EnumMap<Day, List<Task>>` |

```java
enum Status { NEW, PROCESSING, COMPLETED, FAILED }

EnumMap<Status, String> messages = new EnumMap<>(Status.class);
messages.put(Status.NEW,        "Order received");
messages.put(Status.PROCESSING, "Order in progress");
messages.put(Status.COMPLETED,  "Order delivered");
messages.put(Status.FAILED,     "Order failed");
```

---

## 17. Important Interview Questions

**Q1: Why is EnumMap faster than HashMap?**

> It uses **ordinal-based direct array indexing** instead of hashing. No hash computation, no collision resolution, no resizing — just `array[ordinal]`.

---

**Q2: Does EnumMap use hashing?**

> ❌ No. It uses `ordinal()` as a direct array index.

---

**Q3: Why are there no collisions in EnumMap?**

> Each enum constant has a **unique ordinal** value. Unique indexes mean zero collisions by design.

---

**Q4: Does EnumMap maintain insertion order?**

> ❌ No. It maintains **enum declaration order** (based on ordinal values).

---

**Q5: Why does the constructor need the Enum class?**

> To determine the number of enum constants and allocate the internal array of the correct fixed size.

---

**Q6: What is the default array size of EnumMap?**

> Exactly equal to the **number of enum constants** in the enum type. Unlike HashMap's default capacity of 16, EnumMap never resizes because all possible keys are known upfront.

---

**Q7: Why doesn't EnumMap resize?**

> Because all possible keys (enum constants) are **finite and fully known at creation time**. The maximum number of entries can never exceed the number of enum constants.

---

## 18. Advanced Senior-Level Concepts

### 1. Ordinals Must Remain Stable

Changing enum **declaration order** changes ordinals:

```java
// Before:
enum Day { MONDAY, TUESDAY }  // MONDAY=0, TUESDAY=1

// After (dangerous!):
enum Day { TUESDAY, MONDAY }  // TUESDAY=0, MONDAY=1
```

> ⚠️ Changing enum order can break **serialization** and **persistence logic** that relies on ordinals.

---

### 2. Array-Based Optimization

EnumMap essentially behaves like:

```java
Object[] values = new Object[enumConstantCount];
// Put:  values[key.ordinal()] = value;
// Get:  return values[key.ordinal()];
```

Maximum simplicity, maximum performance.

---

### 3. CPU Cache-Friendly

Arrays store data in **contiguous memory** → better CPU cache locality → fewer cache misses → measurable performance improvement for sequential iteration compared to HashMap's scattered nodes.

---

### 4. Structural vs Deep Key Immutability

Enum constants are **immutable singletons** — the best possible map keys:
- `equals()` and `hashCode()` are identity-based
- No risk of key mutation changing lookup behavior (a common `HashMap` pitfall with mutable keys)

---

## 19. Final Revision Summary

```
EnumMap
├── Purpose: Specialized map for enum keys only
├── Internal Structure: Object[] array
├── Key Optimization: ordinal() used as direct array index
├── Array Size: Exactly = number of enum constants (no default 16, no resizing)
├── Ordering: Enum declaration order
├── Null Keys: ❌ Not allowed (NullPointerException)
├── Null Values: ✅ Allowed
├── Thread Safe: ❌ No
└── Performance: Faster than HashMap for enum keys
```

| Property | Value |
|---|---|
| Internal Structure | `Object[]` array |
| Array Size | Number of enum constants |
| Resizes? | ❌ Never |
| Collision? | ❌ Never |
| Ordering | Enum declaration order |
| Null Keys | ❌ Throws `NullPointerException` |
| Null Values | ✅ Allowed |
| Thread Safe | ❌ No |

---

## 20. Most Important Interview Topics — Focus List

- ✅ `ordinal()` usage as array index
- ✅ Internal fixed-size array structure
- ✅ Array size = enum constant count (NOT 16 like HashMap)
- ✅ Why no resizing ever needed
- ✅ Why faster than HashMap
- ✅ No collision concept
- ✅ Enum declaration ordering (not insertion order)
- ✅ Constructor must receive `Enum.class`
- ✅ HashMap vs EnumMap full comparison
- ✅ Null key restriction
- ✅ Memory optimization over HashMap
- ✅ Real-world use cases (state machines, config, workflows)

---

> ⭐ **Shortcut to Remember**:
> - `HashMap` = **post office** — sort by hash, handle collisions, resize when full
> - `EnumMap` = **numbered pigeonholes** — each enum has its fixed slot, just drop the value in
