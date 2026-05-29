# Java Vector Class — Complete Detailed Notes

---

## 1. What is Vector?

`Vector` is a class in Java that implements the:
- `List`
- `RandomAccess`
- `Cloneable`
- `Serializable`

interfaces.

**Package:**
```
java.util
```

### Definition

`Vector` is a **synchronized dynamic array**.

It grows automatically when elements are added.

### Important Point

`Vector` is a **Legacy Class** introduced in **JDK 1.0**, before the Collection Framework came in Java 1.2.

> Later, it was modified to support the Collection Framework.

---

## 2. Main Features of Vector

### 1. Dynamic Array

Like `ArrayList`, `Vector` internally uses `Object[]` and grows automatically.

### 2. Thread Safe

All methods are **synchronized**, meaning multiple threads can safely access it.

### 3. Ordered Collection

Maintains **insertion order**.

### 4. Allows Duplicates

```java
v.add(10);
v.add(10); // ✅ Allowed
```

### 5. Allows Null Values

```java
v.add(null); // ✅ Valid
```

### 6. Random Access

Fast retrieval using index. Complexity: **O(1)**

---

## 3. Why Vector Was Introduced?

Before Collection Framework:
- Java needed dynamic arrays
- Arrays had fixed size

Vector solved **dynamic resizing** and additionally provided **thread safety**.

---

## 4. Creating Vector

### Default Constructor

```java
Vector<Integer> v = new Vector<>();
```

Default capacity: **10**

### Custom Capacity

```java
Vector<Integer> v = new Vector<>(20);
```

Initial capacity: `20`

### Capacity Increment Constructor

```java
Vector<Integer> v = new Vector<>(5, 3);
```

Meaning:
- Initial capacity = `5`
- Grows by `3` every time full

### Collection Constructor

```java
List<Integer> list = Arrays.asList(1, 2, 3);

Vector<Integer> v = new Vector<>(list);
```

Creates vector from existing collection.

---

## 5. Internal Working of Vector

Internally uses:

```java
Object[] elementData
```

Similar to `ArrayList`.

### Size vs Capacity

**Size** — Actual number of elements.

**Capacity** — Total size of internal array.

### Example

```java
Vector<Integer> v = new Vector<>();
```

Initially:
```
Size     = 0
Capacity = 10
```

After adding 3 elements:
```
Size     = 3
Capacity = 10
```

---

## 6. Growth Mechanism

When `Vector` becomes full:
- New larger array created
- Elements copied

### Default Growth

Vector **doubles** capacity:

```
newCapacity = oldCapacity * 2
```

### Example

| Old Capacity | New Capacity |
|--------------|--------------|
| 10 | 20 |
| 20 | 40 |
| 40 | 80 |

### Capacity Increment Case

If increment provided:

```java
new Vector<>(5, 3)
```

| Old Capacity | New Capacity |
|--------------|--------------|
| 5 | 8 |
| 8 | 11 |
| 11 | 14 |

### Comparison with ArrayList

| Structure | Growth |
|-----------|--------|
| `ArrayList` | 1.5x |
| `Vector` | 2x |

---

## 7. Synchronization in Vector

> **Most important concept.**

### All Methods are Synchronized

Example internally:

```java
public synchronized boolean add(E e)
```

Meaning: Only **one thread at a time** can execute the method.

### What is Synchronization?

It prevents **multiple threads modifying data simultaneously**.

### Why Needed?

Without synchronization, **race conditions** occur leading to:
- Data corruption
- Inconsistent results

---

## 8. Race Condition Example

Suppose:
- Thread 1 adds element
- Thread 2 adds element simultaneously

Without synchronization: some writes may be lost.

### ArrayList Problem

```java
List<Integer> list = new ArrayList<>();
```

Multiple threads modifying it → **unsafe** → possible wrong size.

### Vector Solution

```java
Vector<Integer> v = new Vector<>();
```

**Safe** because methods are synchronized.

---

## 9. Why Vector is Slower?

Because synchronization introduces **locking overhead**.

Every operation:
1. Acquires lock
2. Releases lock

Extra work.

### In Single-Threaded Applications

Synchronization is unnecessary.

> So `ArrayList` is preferred.

---

## 10. Common Vector Methods

### `add()`

```java
v.add(10);
```

### `add(index, element)`

```java
v.add(1, 20);
```

### `get()`

```java
v.get(0);
```

### `set()`

```java
v.set(0, 100);
```

### `remove()`

```java
v.remove(0);
```

### `size()`

```java
v.size();
```

### `clear()`

```java
v.clear();
```

### `capacity()` ⭐ Unique Method

Returns the internal capacity.

```java
v.capacity();
```

### Example

```java
Vector<Integer> v = new Vector<>();

System.out.println(v.capacity());

v.add(10);
v.add(20);

System.out.println(v.size());
```

**Output:**
```
10
2
```

---

## 11. Complete Example

```java
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Vector<Integer> v = new Vector<>();

        v.add(10);
        v.add(20);
        v.add(30);

        System.out.println(v);

        System.out.println(v.get(1));

        v.set(1, 100);

        System.out.println(v);

        System.out.println(v.capacity());

        v.remove(0);

        System.out.println(v);
    }
}
```

**Output:**
```
[10, 20, 30]
20
[10, 100, 30]
10
[100, 30]
```

---

## 12. Vector vs ArrayList

> **Very important interview question.**

| Feature | Vector | ArrayList |
|---------|--------|-----------|
| Thread Safe | Yes | No |
| Synchronization | Synchronized | Not synchronized |
| Speed | Slower | Faster |
| Legacy Class | Yes | No |
| Growth | 2x | 1.5x |

### Which is Better?

**Use `ArrayList` when:**
- Single-threaded
- Performance is important

**Use `Vector` when:**
- Thread safety required
- Working with legacy code

---

## 13. Vector vs LinkedList

| Feature | Vector | LinkedList |
|---------|--------|------------|
| Internal DS | Dynamic Array | Doubly Linked List |
| Access Speed | Fast | Slow |
| Insert/Delete Middle | Slow | Faster |
| Memory Usage | Less | More |

---

## 14. Enumeration in Vector

Old iteration mechanism.

```java
Enumeration<Integer> e = v.elements();

while (e.hasMoreElements()) {
    System.out.println(e.nextElement());
}
```

> Legacy feature.

**Modern Alternative:** Use `Iterator` or for-each loop.

---

## 15. Stack Class

`Stack` extends `Vector`.

Meaning: `Stack` inherits `Vector` methods.

```java
Stack<Integer> stack = new Stack<>();
```

Internally based on `Vector`.

---

## 16. Modern Alternatives to Vector

### 1. ArrayList + Synchronization

```java
Collections.synchronizedList(new ArrayList<>())
```

### 2. CopyOnWriteArrayList

**Package:**
```
java.util.concurrent
```

> Better for concurrent applications.

---

## 17. Important Interview Questions

### Q1. Is Vector synchronized?

**Yes.**

### Q2. Why is Vector slower than ArrayList?

Because of **synchronization overhead**.

### Q3. Default capacity of Vector?

```
10
```

### Q4. How does Vector grow?

By **doubling capacity**, unless an increment is specified.

### Q5. Is Vector part of Collection Framework?

**Yes** — although it existed before it.

### Q6. Which class extends Vector?

```
Stack
```

---

## 18. Thread Safety Example

```java
import java.util.*;

class MyThread extends Thread {

    Vector<Integer> v;

    MyThread(Vector<Integer> v) {
        this.v = v;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            v.add(i);
        }
    }
}

public class Main {

    public static void main(String[] args) throws Exception {

        Vector<Integer> v = new Vector<>();

        MyThread t1 = new MyThread(v);
        MyThread t2 = new MyThread(v);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(v.size());
    }
}
```

**Output:**
```
2000
```

> Safe because `Vector` is synchronized.

---

## 19. Final Revision Notes

### Key Points

- ✅ `Vector` is synchronized
- ✅ Thread-safe dynamic array
- ✅ Legacy class
- ✅ Implements `List`
- ✅ Allows duplicates
- ✅ Maintains insertion order
- ✅ Growth factor = 2x
- ✅ Slower than `ArrayList`
- ✅ `Stack` extends `Vector`

### Quick Comparison

| Feature | Vector | ArrayList |
|---------|--------|-----------|
| Thread Safe | Yes | No |
| Speed | Slow | Fast |
| Synchronized | Yes | No |

### Shortcut to Remember

```
Vector = Thread Safe ArrayList
```
