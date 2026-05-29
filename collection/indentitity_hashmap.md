# Java IdentityHashMap — Complete Interview Notes

---

## 1. What is IdentityHashMap?

`IdentityHashMap` is a special implementation of the `Map` interface where:

> **Keys are compared using `==` instead of `equals()`**

This is the **MOST important concept**.

### Normal HashMap Behavior

`HashMap` checks:
- `equals()`
- `hashCode()`

Meaning: **content equality**

### IdentityHashMap Behavior

`IdentityHashMap` checks:
- `==`
- `System.identityHashCode()`

Meaning: **reference equality** (memory identity)

### Core Difference

```
HashMap          →  "Do these objects contain same data?"

IdentityHashMap  →  "Are these EXACT SAME objects in memory?"
```

---

## 2. Declaration

```java
public class IdentityHashMap<K,V>
    extends AbstractMap<K,V>
    implements Map<K,V>, Serializable, Cloneable
```

---

## 3. HashMap Recap First

`HashMap` internally uses `hashCode()` and `equals()`.

**Flow:**
```
1. Calculate hashCode
2. Find bucket
3. Use equals() to compare keys
```

### Example Setup

```java
String s1 = new String("Java");
String s2 = new String("Java");
```

These are: **different objects**, **same content**.

### HashMap Example

```java
HashMap<String,Integer> map = new HashMap<>();

map.put(s1, 1);
map.put(s2, 2);

System.out.println(map.size());
```

**Output:**
```
1
```

### Why Size = 1?

Because `String` overrides `equals()` and `hashCode()`.

Both strings have the same content → same hashcode → `HashMap` treats them as the **same key** → second value replaces first.

**Internal check:**

```
s1.equals(s2)  →  true  →  replace old value
```

---

## 4. IdentityHashMap Working

```java
IdentityHashMap<String,Integer> map =
    new IdentityHashMap<>();

map.put(s1, 1);
map.put(s2, 2);

System.out.println(map.size());
```

**Output:**
```
2
```

### Why Size = 2?

Because `IdentityHashMap` checks:

```
s1 == s2  →  false
```

Because they are **different objects** at different memory locations → both entries stored separately.

### Most Important Concept

| Collection | Equality Check | Meaning |
|------------|---------------|---------|
| `HashMap` | `equals()` | Logical/content equality |
| `IdentityHashMap` | `==` | Reference equality |

---

## 5. Visual Memory Understanding

```java
String s1 = new String("Java");
String s2 = new String("Java");
```

**Memory:**

```
s1 ─────► Object A ("Java")

s2 ─────► Object B ("Java")
```

Contents same. Objects different.

### HashMap View

```
A and B are EQUAL  (because equals() == true)
```

### IdentityHashMap View

```
A and B are DIFFERENT  (because A == B is false)
```

---

## 6. `System.identityHashCode()`

`IdentityHashMap` uses:

```java
System.identityHashCode(object)
```

instead of:

```java
object.hashCode()
```

### Difference

| Method | Based On |
|--------|----------|
| `object.hashCode()` | Can be overridden — content-based |
| `System.identityHashCode()` | Object identity — JVM memory identity, ignores overrides |

### Example

```java
String s1 = new String("Java");
String s2 = new String("Java");

System.out.println(s1.hashCode());           // same
System.out.println(s2.hashCode());           // same (content same)

System.out.println(System.identityHashCode(s1)); // different
System.out.println(System.identityHashCode(s2)); // different (objects differ)
```

---

## 7. Internal Structure

> **Very important advanced interview point.**

Unlike `HashMap`, `IdentityHashMap` does **NOT** use chaining, trees, or linked lists.

Instead it uses: **Linear Probing**

### What is Linear Probing?

If a collision occurs → search the **next empty slot sequentially** instead of creating a linked list/tree.

### Simplified Structure

```
[key1, value1, key2, value2, ...]
```

Stored in a **single flat array**.

---

## 8. Why IdentityHashMap Exists?

Sometimes **object identity** matters more than content.

### Example Scenarios

| Scenario | Why Identity Matters |
|----------|---------------------|
| Graph Processing | Two logically equal nodes may be different actual nodes |
| Object Serialization | Need to track exact object instances |
| Proxy Frameworks | Object identity is critical |
| Caching Object Instances | Need separate tracking of each object |

---

## 9. Important Comparison Table

| Feature | HashMap | IdentityHashMap |
|---------|---------|-----------------|
| Equality Check | `equals()` | `==` |
| Hash Function | `hashCode()` | `identityHashCode()` |
| Focus | Content | Object identity |
| Collision Handling | Chaining/Tree | Linear probing |
| Duplicate Content Keys | Replaced | Stored separately |

---

## 10. Important Interview Example

```java
Integer a = new Integer(10);
Integer b = new Integer(10);
```

### HashMap

```
a.equals(b)  →  true  →  One key
```

### IdentityHashMap

```
a == b  →  false  →  Two keys
```

---

## 11. ⚠️ Very Important Warning

**Never use `IdentityHashMap` when logical equality is expected.**

Because behavior may look "wrong".

**Example:**

```java
map.get(new String("Java")); // may return null!
```

Even though the same content exists in the map — because it's a **different object reference**.

---

## 12. IdentityHashMap with String Pool ⭐

```java
String s1 = "Java";
String s2 = "Java";
```

Now:

```
s1 == s2  →  true
```

Because String Pool **reuses the same object**.

So `IdentityHashMap` treats them as the **same key**.

> **Very important tricky interview point.**

---

## 13. Performance

| Operation | Complexity |
|-----------|------------|
| `put()` | O(1) |
| `get()` | O(1) |
| `remove()` | O(1) |

---

## 14. Thread Safety

`IdentityHashMap` is **NOT synchronized** — same as `HashMap`.

---

## 15. Important Interview Questions

### Q1. Main difference between HashMap and IdentityHashMap?

- **HashMap:** uses `equals()` / `hashCode()`
- **IdentityHashMap:** uses `==` / `identityHashCode()`

### Q2. Why does IdentityHashMap ignore `equals()`?

Because it is designed for **object identity semantics**.

### Q3. Can logically equal objects coexist as keys?

**Yes.** If references differ, `IdentityHashMap` treats them as separate keys.

### Q4. Does IdentityHashMap use Red-Black Trees?

**No.** It uses **linear probing**.

### Q5. Why is `System.identityHashCode()` needed?

To get an identity-based hash **independent of overridden `hashCode()`**.

---

## 16. Internal Flow

### `put()`

```
1. Calculate identityHashCode
2. Find slot (linear probing)
3. Compare using ==
4. Insert or replace
```

---

## 17. Senior Java Developer Concepts

### 1. IdentityHashMap Violates General Map Contract

The `Map` contract assumes `equals()`-based comparison. `IdentityHashMap` **intentionally breaks this**.

> Very important senior-level point.

### 2. Used Heavily in JVM/Internal Frameworks

Examples:
- Serialization
- Reflection
- Proxy tracking

### 3. Not Suitable for Business Logic Maps

Because users expect **logical equality**.

---

## 18. Final Revision Summary

### HashMap

```
Content equality
equals()
hashCode()
```

### IdentityHashMap

```
Reference equality
==
identityHashCode()
```

### Key Points

- ✅ Keys compared using `==`, not `equals()`
- ✅ Uses `System.identityHashCode()` instead of `hashCode()`
- ✅ Logically equal but distinct objects can both be keys
- ✅ Uses linear probing (no chaining/trees)
- ✅ String literals may behave unexpectedly due to String Pool
- ✅ Not thread-safe
- ✅ Intentionally violates general Map contract

---

## 19. Final One-Line Definition

> `IdentityHashMap` compares keys using **reference equality (`==`)** instead of logical equality (`equals()`), and uses `System.identityHashCode()` instead of the object's overridden `hashCode()` method.
