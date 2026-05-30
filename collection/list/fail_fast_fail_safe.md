# Fail-Fast vs Fail-Safe Iterators in Java

In Java, collections like `ArrayList`, `HashSet`, and `HashMap` are traversed using an **Iterator**.

While iterating through a collection, modifying it can cause special behavior. This leads to two concepts:

1. **Fail-Fast Iterator**
2. **Fail-Safe Iterator**

---

## 1. What is an Iterator?

An **Iterator** is an interface used to traverse collection elements one by one.

### Example

```java
ArrayList<String> list = new ArrayList<>();

list.add("Apple");
list.add("Banana");

Iterator<String> it = list.iterator();

while (it.hasNext()) {
    System.out.println(it.next());
}
```

### Why Iterator is Used?

Iterator provides:
- **Uniform traversal**
- **Safer removal**
- **Dynamic collection traversal**

### Internal Working

Even the enhanced `for-each` loop internally uses an Iterator.

```java
// This:
for (String s : list) { }

// Internally behaves like:
Iterator<String> it = list.iterator();
```

---

## 2. ConcurrentModificationException

This exception occurs when a collection is **structurally modified while iterating** over it.

### Structural Modification Means

Operations that change collection size/content:
- `add()`
- `remove()`
- `clear()`

### Example

```java
ArrayList<String> list = new ArrayList<>();

list.add("Apple");
list.add("Banana");

for (String s : list) {
    list.remove(s); // ❌ ConcurrentModificationException
}
```

### Why Does This Happen?

Because:
- Iterator expects collection structure to **remain stable**
- Modifying collection during traversal creates **inconsistency**

---

## 3. Fail-Fast Iterator

### Definition

A **Fail-Fast** iterator **immediately throws** `ConcurrentModificationException` when it detects structural modification during iteration.

> **Main Idea:** Detect modification quickly and fail immediately.

---

### How Fail-Fast Works Internally?

Uses two variables:

| Variable | Description |
|----------|-------------|
| `modCount` | Stored inside collection — tracks number of structural modifications |
| `expectedModCount` | Stored inside iterator — copy of `modCount` at the time iteration starts |

#### `modCount`

Whenever `add()`, `remove()`, or `clear()` is called:

```
modCount++
```

#### During Iteration

Iterator continuously checks:

```
modCount == expectedModCount ?
```

If they are **different** → Iterator throws `ConcurrentModificationException`.

---

### Visual Flow

```
Iterator Created
       ↓
expectedModCount = modCount
       ↓
Collection Modified (add/remove/clear)
       ↓
modCount changes
       ↓
Iterator checks mismatch
       ↓
ConcurrentModificationException thrown
```

---

### Fail-Fast Example

```java
import java.util.*;

public class Test {

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");

        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            String item = it.next();

            if (item.equals("Apple")) {
                list.remove(item); // ❌ Structural modification
            }
        }
    }
}
```

### What Happens?

| State | `modCount` | `expectedModCount` |
|-------|-----------|-------------------|
| Initial | 2 | 2 |
| After `remove()` | 3 | 2 |

> Mismatch detected → **`ConcurrentModificationException`**

---

### Collections Using Fail-Fast Iterators

From `java.util` package:

- `ArrayList`
- `HashMap`
- `HashSet`
- `Vector`

> **Important:** Fail-Fast works on the **original collection**, so modifications are immediately visible.

---

## 4. Fail-Safe Iterator

### Definition

A **Fail-Safe** iterator does **NOT** throw `ConcurrentModificationException` during iteration.

> **Main Idea:** Iterator works on a **COPY** of the collection instead of the original.

---

### Internal Working

When the iterator starts:
1. A **snapshot/copy** of the collection is created.
2. Iterator **traverses the copied version**, not the original.

Therefore, even if the original collection changes — **iterator remains unaffected**.

---

### Visual Flow

```
Original Collection
       ↓
Copy / Snapshot Created
       ↓
Iterator Traverses Copy
       ↓
Original Collection Changes
       ↓
No Exception ✅
```

---

### Fail-Safe Example

```java
import java.util.concurrent.CopyOnWriteArrayList;

public class Test {

    public static void main(String[] args) {

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Apple");
        list.add("Banana");

        for (String item : list) {
            System.out.println(item);
            list.add("Pineapple"); // ✅ No exception
        }
    }
}
```

**Output:**
```
Apple
Banana
```

> `Pineapple` is added to the **original list**, but the iterator does **NOT** see it — because iteration is happening on the **copied snapshot**.

---

### Collections Using Fail-Safe Iterators

From `java.util.concurrent` package:

- `CopyOnWriteArrayList`
- `ConcurrentHashMap`

### Why Called CopyOnWrite?

Whenever a modification happens:
1. A **new copy** is created
2. Modifications are applied on the **copy**

### Thread Safety

Fail-Safe collections are generally **thread-safe** because concurrent modification is safely handled.

### Major Disadvantage

**Extra memory usage** — because a copy of the collection is created.

| Collection Size | Impact |
|----------------|--------|
| Large collections | High memory consumption + slower writes |

---

## 5. Fail-Fast vs Fail-Safe — Comparison

| Feature | Fail-Fast | Fail-Safe |
|---------|-----------|-----------|
| **Exception** | Throws `ConcurrentModificationException` | No exception |
| **Works On** | Original collection | Copy / Snapshot |
| **Memory Usage** | Low | High |
| **Thread Safety** | Not thread-safe | Usually thread-safe |
| **Modification During Iteration** | ❌ Not allowed | ✅ Allowed |
| **Package** | `java.util` | `java.util.concurrent` |

### Key Internal Difference

```
Fail-Fast
    ↓
Checks modCount vs expectedModCount


Fail-Safe
    ↓
Uses a collection snapshot/copy
```

> **Important Interview Point:** There is **no official "Fail-Safe Iterator" interface** in Java. It is just a common term. The actual Java terminology is **Non-Fail-Fast Iterator**.

---

## 6. Safe Removal During Iteration

Using the iterator's **own `remove()` method** is allowed and safe.

```java
Iterator<String> it = list.iterator();

while (it.hasNext()) {
    String item = it.next();

    if (item.equals("Apple")) {
        it.remove(); // ✅ Safe — uses iterator's remove
    }
}
```

### Why This Works?

Because the iterator internally updates `expectedModCount` properly — so **no mismatch** occurs.

### Important Rule

| Approach | Result |
|----------|--------|
| `list.remove()` during iteration | ❌ `ConcurrentModificationException` |
| `iterator.remove()` during iteration | ✅ Safe |

---

## 7. Real-Life Analogy

### Fail-Fast

> Like a teacher checking attendance from the **live classroom**.
> If students suddenly move/change — teacher **stops the process immediately**.

### Fail-Safe

> Like a teacher using a **photocopy of the attendance sheet**.
> Even if the classroom changes — **attendance continues safely** on the copy.

---

## 8. Common Interview Questions

### Why does `ConcurrentModificationException` happen?
> Because the collection structure **changes during iteration**, causing a mismatch between `modCount` and `expectedModCount`.

### Which collections are Fail-Fast?
> `ArrayList`, `HashMap`, `HashSet`

### Which collections are Fail-Safe?
> `CopyOnWriteArrayList`, `ConcurrentHashMap`

### Is Fail-Fast guaranteed?
> ❌ **No** — it is a **best-effort mechanism** only. It cannot be guaranteed in all cases.

### Why does Fail-Safe use more memory?
> Because a **copy/snapshot** of the collection is created for iteration.

### Which is faster?

| Operation | Better Choice |
|-----------|--------------|
| **Frequent Reads** | Fail-Safe |
| **Frequent Writes** | Fail-Fast |

---

## Final Summary

```
Fail-Fast
    ↓
Works on original collection
Detects modification via modCount
Throws ConcurrentModificationException


Fail-Safe
    ↓
Works on copied collection
Allows modification during iteration
No exception
```

### Memory Summary

| Feature | Fail-Fast | Fail-Safe |
|---------|-----------|-----------|
| **Collection Used** | Original | Copy/Snapshot |
| **Memory** | Low | High |
| **Speed** | Faster | Slower (copy creation) |
| **Safety** | Fails immediately on change | Safe, no interruption |

---

## ⭐ Most Important Concept

```
Fail-Fast  =  Safety by STOPPING execution on modification

Fail-Safe  =  Safety by using a COPY of the collection
```

---
