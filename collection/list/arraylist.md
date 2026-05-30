# Java ArrayList — Complete Detailed Notes

---

## 1. Introduction to List Interface

`List` is an **interface** in the Java Collection Framework.

**Hierarchy:**

```
Iterable
   ↓
Collection
   ↓
List
```

**Package:**
```
java.util
```

### Characteristics of List

#### 1. Maintains Insertion Order

Elements remain in the order they were inserted.

**Example:**

```java
List<String> list = new ArrayList<>();

list.add("A");
list.add("B");
list.add("C");

System.out.println(list);
```

**Output:**
```
[A, B, C]
```

> Order is preserved.

#### 2. Allows Duplicates

```java
list.add("A");
list.add("A");
```

Valid output:
```
[A, A]
```

#### 3. Index-Based Access

Each element has an index.

```
0  1  2
A  B  C
```

Access using:

```java
list.get(1);
```

**Output:**
```
B
```

### Classes Implementing List

| Class | Description |
|-------|-------------|
| `ArrayList` | Dynamic array |
| `LinkedList` | Doubly linked list |
| `Vector` | Synchronized dynamic array |
| `Stack` | LIFO stack |

---

## 2. Why Use ArrayList?

### Problem with Arrays

Normal arrays have **fixed size**.

**Example:**

```java
int[] arr = new int[5];
```

Size cannot grow dynamically.

If array becomes full:
- Need to create new array
- Copy elements manually

This is inconvenient.

### Solution: ArrayList

`ArrayList` automatically resizes itself.

**Example:**

```java
ArrayList<Integer> list = new ArrayList<>();
```

**Benefits:**
- Dynamic size
- Easy insertion/removal
- Fast access
- No manual resizing

---

## 3. Creating ArrayList

### Basic Syntax

```java
ArrayList<Integer> list = new ArrayList<>();
```

**OR**

```java
List<Integer> list = new ArrayList<>();
```

**Preferred:**

```java
List<Integer> list = new ArrayList<>();
```

Because:
- Programming to interface is better
- Loose coupling

---

## 4. Generics in ArrayList

### Without Generics

```java
ArrayList list = new ArrayList();

list.add(10);
list.add("Java");
```

**Problem:**
- No type safety
- Runtime errors possible

### With Generics

```java
ArrayList<Integer> list = new ArrayList<>();
```

Now only integers allowed.

> Safer and recommended.

---

## 5. Important ArrayList Methods

### `add()`

Adds element at end.

```java
list.add(10);
list.add(20);
```

### `add(index, element)`

Insert at specific position.

```java
list.add(1, 50);
```

### `get(index)`

Retrieve element.

```java
System.out.println(list.get(0));
```

### `set(index, element)`

Replace element.

```java
list.set(0, 100);
```

### `remove(index)`

Remove using index.

```java
list.remove(0);
```

### `contains(element)`

Checks existence.

```java
list.contains(10);
```

### `size()`

Returns number of elements.

```java
System.out.println(list.size());
```

### `clear()`

Removes all elements.

```java
list.clear();
```

### Complete Example

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        list.add("Java");
        list.add("Python");
        list.add("C++");

        System.out.println(list);

        System.out.println(list.get(1));

        list.set(1, "Spring");

        System.out.println(list);

        list.remove(0);

        System.out.println(list);

        System.out.println(list.contains("C++"));

        System.out.println(list.size());
    }
}
```

**Output:**
```
[Java, Python, C++]
Python
[Java, Spring, C++]
[Spring, C++]
true
2
```

---

## 6. Iterating ArrayList

### 1. Traditional for loop

```java
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}
```

### 2. Enhanced for-each loop

```java
for (String s : list) {
    System.out.println(s);
}
```

### 3. Iterator

```java
Iterator<String> it = list.iterator();

while (it.hasNext()) {
    System.out.println(it.next());
}
```

---

## 7. Internal Working of ArrayList

> **Very important interview topic.**

### Internal Data Structure

`ArrayList` internally uses:

```java
Object[] elementData;
```

Meaning: Elements stored inside an array internally.

### Size vs Capacity

#### Size

Number of **actual elements**.

**Example:**
```
[10, 20, 30]
```
Size = 3

#### Capacity

Size of the **internal array**.

Default capacity:
```
10
```
Even if only 2 elements exist.

### Example

```java
ArrayList<Integer> list = new ArrayList<>();
```

Initially:
```
Capacity = 10
Size = 0
```

After adding 3 elements:
```
Capacity = 10
Size = 3
```

---

## 8. Resizing Mechanism

When capacity becomes full:
- Java creates new bigger array
- Copies old elements

### Growth Formula

**New Capacity:**

```
oldCapacity + (oldCapacity / 2)
```

OR **1.5x growth**

### Example

| Old Capacity | New Capacity |
|--------------|--------------|
| 10 | 15 |
| 15 | 22 |
| 22 | 33 |

### Why Resizing is Costly?

Because:
- New array created
- Old elements copied

Copying takes time.

---

## 9. Time Complexity

| Operation | Complexity |
|-----------|------------|
| `get()` | O(1) |
| `set()` | O(1) |
| `add()` at end | O(1) average |
| `add()` middle | O(n) |
| `remove()` | O(n) |
| `contains()` | O(n) |

### Why `get()` is Fast?

Because arrays support **direct indexing**.

```java
list.get(5);
```

Direct memory access.

### Why `remove()` is Slow?

Elements **shift** after removal.

**Example:**

Before:
```
[10, 20, 30, 40]
```

Remove index 1:
```
[10, 30, 40]
```

Shifting required.

---

## 10. `trimToSize()`

Reduces unused memory.

**Example:**

```java
ArrayList<Integer> list = new ArrayList<>();

list.trimToSize();
```

Capacity becomes equal to current size.

> Useful for memory optimization.

---

## 11. Different Ways to Initialize Lists

### 1. Standard Constructor

```java
List<Integer> list = new ArrayList<>();
```

Fully modifiable.

### 2. `Arrays.asList()`

```java
List<String> list = Arrays.asList("A", "B", "C");
```

**Characteristics:**
- Fixed size
- Cannot add/remove
- Can replace using `set()`

**Example:**

```java
list.set(0, "Java"); // ✅ Allowed
```

But:

```java
list.add("Python"); // ❌ Throws UnsupportedOperationException
```

### 3. `List.of()` (Java 9+)

```java
List<Integer> list = List.of(1, 2, 3);
```

Completely **immutable**.

Cannot:
- `add`
- `remove`
- `set`

Everything throws exception.

### Difference

| Method | Add/Remove | `set()` |
|--------|-----------|---------|
| `ArrayList` | Yes | Yes |
| `Arrays.asList` | No | Yes |
| `List.of` | No | No |

---

## 12. Copy Constructor

Creates new modifiable list.

```java
List<String> oldList = List.of("A", "B");

List<String> newList = new ArrayList<>(oldList);

newList.add("C"); // ✅ Valid
```

---

## 13. `remove()` Confusion ⚠️ (Important Interview Point)

### Problem

```java
List<Integer> list = new ArrayList<>();

list.add(10);
list.add(20);

list.remove(1);
```

What happens?

It removes **index 1**, NOT value `1`.

### To Remove by Object Value

Use:

```java
list.remove(Integer.valueOf(10));
```

Now it removes **value** `10`.

---

## 14. Capacity Tuning

If you know approximate size beforehand:

```java
ArrayList<Integer> list = new ArrayList<>(1000);
```

**Benefits:**
- Fewer resizing operations
- Better performance

Useful in:
- Big applications
- Large datasets

---

## 15. Sorting ArrayList

Using `Collections` class:

```java
Collections.sort(list);
```

**Example:**

```java
List<Integer> list = new ArrayList<>();

list.add(30);
list.add(10);
list.add(20);

Collections.sort(list);

System.out.println(list);
```

**Output:**
```
[10, 20, 30]
```

### Alternative

```java
list.sort(null);
```

---

## 16. Conversions

### Convert ArrayList to Array

```java
String[] arr = list.toArray(new String[0]);
```

### Convert Array to List

```java
List<String> list = Arrays.asList(arr);
```

---

## 17. ArrayList vs LinkedList

| Feature | ArrayList | LinkedList |
|---------|-----------|------------|
| Internal Structure | Dynamic Array | Doubly Linked List |
| Access Speed | Fast | Slow |
| Insertion/Deletion | Slow | Faster |
| Memory Usage | Less | More |

### When to Use ArrayList?

Use when:
- Frequent access required
- More reading operations
- Less insertion/deletion in middle

---

## 18. Common Exceptions

### `IndexOutOfBoundsException`

```java
list.get(100); // ❌ Invalid index
```

### `UnsupportedOperationException`

Occurs with `Arrays.asList()` or `List.of()` when modifying structure.

---

## 19. Interview Questions

### Q1. Why ArrayList is faster than LinkedList for retrieval?

Because `ArrayList` uses arrays with **direct indexing**.

### Q2. Default capacity of ArrayList?

```
10
```

### Q3. Growth rate of ArrayList?

```
1.5 times
```

### Q4. Is ArrayList synchronized?

**No.**

For thread-safe version use:

```java
Vector
```

or

```java
Collections.synchronizedList()
```

### Q5. Can ArrayList store primitive types?

**No** directly.

Uses wrapper classes:
- `Integer`
- `Double`
- `Character`

> Autoboxing handles conversion automatically.

---

## 20. Final Revision Notes

### Key Points

- ✅ Ordered
- ✅ Allows duplicates
- ✅ Dynamic resizing
- ✅ Fast random access
- ✅ Uses internal array
- ✅ `get()` → O(1)
- ✅ `remove()` → O(n)
- ✅ Default capacity = 10
- ✅ Growth factor = 1.5x

### Quick Comparison

| Feature | ArrayList |
|---------|-----------|
| Ordered | Yes |
| Duplicates | Yes |
| Indexed | Yes |
| Thread Safe | No |
| Internal DS | Dynamic Array |
