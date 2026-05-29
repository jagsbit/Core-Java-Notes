# Java WeakHashMap & WeakReference — Complete Notes

---

## 1. The Key Confusion — Why Entry Survives After `key = null`?

### Case 1: HashMap

```java
HashMap<String,String> map = new HashMap<>();

String key = new String("Java");

map.put(key, "Spring");

key = null;
```

You may think: local variable is gone → object eligible for GC.

**BUT** internally, HashMap itself stores a **STRONG reference** to the key.

**Memory view:**

```
map ───────► key object ("Java")
```

GC says: **Object is still alive** → entry remains.

---

### Case 2: WeakHashMap

```java
WeakHashMap<String,String> map = new WeakHashMap<>();

String key = new String("Java");

map.put(key, "Spring");

key = null;
```

Internally, `WeakHashMap` stores `WeakReference(key)`.

**Memory view:**

```
map ──weak──► key object
```

After `key = null` → **no strong references remain**, only weak reference.

GC sees: object is weakly reachable only → **JVM may destroy it**.

Then `WeakHashMap` removes the entry automatically.

---

## 2. Most Important Difference

### HashMap

```
Strong Reference
map ─────────► key

GC cannot remove key.
```

### WeakHashMap

```
Weak Reference
map ──weak──► key

GC CAN remove key.
```

> **In HashMap**, the map itself keeps keys alive because keys are strongly referenced.
> **In WeakHashMap**, keys are wrapped inside `WeakReference` objects, so the map does not prevent garbage collection.

---

## 3. Step-by-Step Memory Understanding

### Step 1 — Create key variable

```java
String key = new String("Java");
```

```
key ─────► "Java" object
```

One reference exists: `key` variable.

---

### Step 2 — Put into HashMap

```java
map.put(key, "Spring");
```

HashMap internally stores another reference.

```
key ─────► "Java" object
               ▲
               │
             map
```

**Two references exist:**
- Variable `key`
- Internal `HashMap` node

---

### Step 3 — Set key to null

```java
key = null;
```

Only the **variable reference** is removed.

```
key ─────► null

map ─────► "Java" object    (still alive!)
```

The object **STILL exists** because the map still points to it.

> GC cannot remove it — `key = null` only removes YOUR variable's connection.

---

### Actual Internal HashMap Storage

When you do `map.put(key, "Spring")`, HashMap creates:

```java
Node {
    key   = reference to "Java"
    value = "Spring"
}
```

That internal node stays alive — so the object stays alive.

---

## 4. Key vs Object — Critical Distinction

> `key` variable is **NOT** the object. It is only a **reference** (pointer-like variable).

**Analogy:**

```
Object    = House
Reference = Address written on paper
```

- You throw away your paper (`key = null`)
- But HashMap still has **its own paper** (internal reference)
- So the house **still exists**

**HashMap** = friend keeps a strong copy.
**WeakHashMap** = friend keeps a temporary, erasable copy.

---

## 5. Small Demo

### HashMap — Entry Survives

```java
HashMap<String,String> map = new HashMap<>();

String key = new String("A");

map.put(key, "Java");

key = null;

System.gc();

System.out.println(map);
```

**Output (usually):**
```
{A=Java}
```

Entry survives.

---

### WeakHashMap — Entry May Disappear

```java
WeakHashMap<String,String> map = new WeakHashMap<>();

String key = new String("A");

map.put(key, "Java");

key = null;

System.gc();

Thread.sleep(1000);

System.out.println(map);
```

**Possible Output:**
```
{}
```

> Entry disappeared. *(Not guaranteed immediately — GC timing is unpredictable.)*

---

## 6. Understanding WeakReference in Java

### Object vs Reference

```java
Phone p = new Phone();
```

| Part | Meaning |
|------|---------|
| `new Phone()` | Actual object in heap memory |
| `p` | Reference variable pointing to object |

```
p ─────► Phone Object
```

---

## 7. What is a Strong Reference?

Normal Java references are **Strong References**.

```java
Phone p = new Phone();
```

As long as a strong reference exists → **GC CANNOT destroy the object**.

```
p ─────strong────► Phone Object    (Object is safe)
```

---

## 8. What is a WeakReference?

`WeakReference` means: **a reference that does NOT protect the object from GC**.

**Java class:**
```
java.lang.ref.WeakReference
```

### Creating WeakReference

```java
WeakReference<Phone> ref =
    new WeakReference<>(new Phone());
```

```
ref ─────weak────► Phone Object
```

> Weak reference does **NOT** keep the object alive.

### Core Idea

If **only** weak references remain → GC may destroy object anytime.

---

## 9. Strong vs Weak Reference

| Type | Declaration | GC Behavior |
|------|-------------|-------------|
| Strong | `Phone p = new Phone()` | Object survives |
| Weak | `new WeakReference<>(new Phone())` | Object may disappear after GC |

---

## 10. Step-by-Step WeakReference Flow

### Step 1

```java
Phone p = new Phone();
```

```
p ─────► Phone Object
```

### Step 2

```java
WeakReference<Phone> weak = new WeakReference<>(p);
```

```
p    ─────strong────► Phone Object
weak ──────weak──────►
```

Object still safe because strong reference exists.

### Step 3

```java
p = null;
```

**Only weak reference remains.**

```
weak ──weak────► Phone Object
```

### Step 4 — GC Runs

```java
System.gc();
```

JVM sees: **no strong references exist** → object becomes collectible → GC destroys object.

### Step 5 — After GC

```java
System.out.println(weak.get());
```

**Output may be:**
```
null
```

> `WeakReference` object itself still exists, but the **referenced object was removed**.

---

## 11. `weak.get()` Method

| Condition | Returns |
|-----------|---------|
| Object still alive | Actual object |
| Object GC'd | `null` |

**Example:**

```java
WeakReference<String> ref =
    new WeakReference<>(new String("Java"));

System.gc();

System.out.println(ref.get()); // possibly null
```

> **"Possibly"** because GC timing is unpredictable — `System.gc()` is only a suggestion.

---

## 12. Very Important Confusion

> **WeakReference is NOT a weak object.**

The object is the same normal object. Only the **reference strength** changes.

**Example:**

```java
Phone p    = new Phone();
WeakReference<Phone> ref = new WeakReference<>(p);
```

Both point to the **same object**. Difference: one strong, one weak.

---

## 13. Why WeakReference is Needed?

Main purpose: **Allow GC to reclaim memory automatically**.

Used for:
- Caches
- Metadata
- Temporary mappings
- Avoiding memory leaks

---

## 14. Real-Life Analogy

| Type | Analogy |
|------|---------|
| Strong Reference | Permanent house ownership — house cannot be demolished |
| Weak Reference | Temporary note with address — house may be demolished if nobody permanently owns it |

---

## 15. Important GC Rule

```
Object survives  →  if at least ONE strong reference exists

Object may die   →  if ONLY weak references exist
```

> GC removes an object only when **no strong references exist anywhere** — not just because a variable becomes `null`.

---

## 16. WeakReference vs Strong Reference Diagram

```
Strong Reference
strong ─────► Object    GC cannot remove object.

Weak Reference
weak   ─────► Object    GC allowed to remove object.
```

---

## 17. WeakReference and WeakHashMap Connection

`WeakHashMap` internally stores:

```
WeakReference(keys)
```

That is why keys can **disappear automatically**.

> `WeakHashMap` is built using the `WeakReference` concept.

---

## 18. Reference Types — Senior Level

| Reference Type | GC Behavior |
|----------------|-------------|
| `Strong` | Never collected |
| `Weak` | Collected eagerly |
| `Soft` | Collected when memory is low |
| `Phantom` | Post-mortem cleanup |

### WeakReference vs SoftReference

| Type | GC Behavior | Best For |
|------|-------------|----------|
| `WeakReference` | Removed aggressively | Temporary mappings |
| `SoftReference` | Removed only when memory needed | Caches |

---

## 19. Important Interview Questions

### Q1. Does WeakReference prevent GC?

**No.** It allows the object to be garbage collected.

### Q2. What happens when the object is removed?

```java
weak.get(); // returns null
```

### Q3. Can an object survive with only a weak reference?

**Yes**, until GC runs. Weak reference does NOT mean immediate destruction.

### Q4. Why is WeakReference useful?

To avoid **memory leaks** and permanently retained objects.

### Q5. Is WeakReference deterministic?

**No.** GC timing is unpredictable.

### Q6. Why does `WeakHashMap` automatically remove entries?

Because it stores keys as `WeakReference` — when no strong references to the key exist, GC collects it and `WeakHashMap` removes the entry.

### Q7. What is the difference between HashMap and WeakHashMap regarding key lifetime?

| | HashMap | WeakHashMap |
|--|---------|-------------|
| Key reference | Strong | Weak |
| Key survives `key = null`? | Yes | No (after GC) |
| Prevents GC of key? | Yes | No |

---

## 20. Common Mistake

Developer thinks:

> "WeakReference automatically deletes the object immediately."

**Wrong.**

Actual behavior: Object removed **only during GC**, when no strong references exist.

---

## 21. Final Revision Summary

### Reference Strength

```
Strong Reference  →  Keeps object alive
Weak Reference    →  Does NOT keep object alive
```

### Object Removed When

```
1. Only weak references remain
   AND
2. GC runs
```

### Key Takeaways

- ✅ `key = null` removes only the **variable's** reference, not all references
- ✅ HashMap stores **strong references** to keys — map keeps keys alive
- ✅ WeakHashMap stores **weak references** to keys — GC can collect them
- ✅ `WeakReference.get()` returns `null` after object is collected
- ✅ GC timing is unpredictable
- ✅ `WeakReference` is the foundation of `WeakHashMap`

### One-Line Definitions

```
WeakReference  →  "I reference this object but won't stop GC from collecting it."

WeakHashMap    →  HashMap where keys are weakly referenced
                  and entries auto-remove when keys are GC'd.
```
