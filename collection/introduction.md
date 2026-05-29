# Java Collection Framework — Complete Explanation

The Java Collection Framework (JCF) is one of the most important parts of Core Java.
It provides a standard way to store, manage, and manipulate groups of objects.

Think of it as a ready-made toolkit containing:

- Interfaces
- Classes
- Algorithms

for handling data efficiently.

---

## 1. What is a Collection?

A **collection** means a group of objects.

**Example:**
- Group of students
- List of names
- Set of unique IDs
- Queue of requests

> In Java, collections store **objects only** (not primitive data types directly).

**Example:**

```java
ArrayList<String> names = new ArrayList<>();
```

Here:
- `ArrayList` = collection class
- `String` objects are stored inside it

---

## 2. Why Collection Framework Was Introduced?

Before Java 1.2, Java had classes like:
- `Vector`
- `Stack`
- `Hashtable`

**Problems:**
- No standard structure
- Different methods
- Difficult to learn
- Difficult to write reusable code

> If one class used different methods than another, generic programming became difficult.

So Java introduced the **Collection Framework in Java 1.2**.

**Goals:**
- Standard interfaces
- Reusable code
- Better performance
- Easy data manipulation
- Consistency

---

## 3. Collection Framework Hierarchy

```
             Iterable
                 |
            Collection
      /         |        \
   List        Set      Queue
                               \
                               Deque

Map (Separate hierarchy)
```

---

## 4. Iterable Interface

### Definition

`Iterable` is the **root interface** of the collection hierarchy.

**Package:**
```
java.lang
```

**Purpose:** It allows objects to be used in the **for-each loop**.

**Example:**

```java
ArrayList<String> list = new ArrayList<>();

for (String s : list) {
    System.out.println(s);
}
```

This works because:

```
ArrayList
→ implements List
→ extends Collection
→ extends Iterable
```

---

## 5. Collection Interface

### Definition

`Collection` is the **main/root interface** of the Collection Framework.

**Package:**
```
java.util
```

It provides common operations for all collections.

### Important Methods of Collection

| Method | Purpose |
|--------|---------|
| `add()` | Add element |
| `remove()` | Remove element |
| `contains()` | Check element |
| `size()` | Number of elements |
| `isEmpty()` | Check empty |
| `clear()` | Remove all elements |

### Example

```java
Collection<Integer> nums = new ArrayList<>();

nums.add(10);
nums.add(20);

System.out.println(nums.size());
```

**Output:**
```
2
```

---

## 6. List Interface

### Characteristics

A `List`:
- Maintains **insertion order**
- Allows **duplicates**
- Allows **indexing**

**Example:**
```
[10, 20, 10]
```
Duplicates are allowed.

### Common List Classes

| Class | Description |
|-------|-------------|
| `ArrayList` | Dynamic array |
| `LinkedList` | Doubly linked list |
| `Vector` | Synchronized dynamic array |
| `Stack` | LIFO stack |

### ArrayList Example

```java
List<String> list = new ArrayList<>();

list.add("Java");
list.add("Python");
list.add("Java");

System.out.println(list);
```

**Output:**
```
[Java, Python, Java]
```

---

## 7. Set Interface

### Characteristics

A `Set`:
- Does **NOT** allow duplicates
- Usually **unordered**
- Stores **unique** elements

**Example:**
```
[10, 20]
```
If you insert duplicate `10`, it will be ignored.

### Common Set Classes

| Class | Description |
|-------|-------------|
| `HashSet` | Fast, unordered |
| `LinkedHashSet` | Maintains insertion order |
| `TreeSet` | Sorted order |

### Example

```java
Set<Integer> set = new HashSet<>();

set.add(10);
set.add(20);
set.add(10);

System.out.println(set);
```

**Output:**
```
[10, 20]
```

---

## 8. Queue Interface

### Definition

Queue follows:

> **FIFO (First In First Out)**

**Example:**
- Ticket line
- Printer queue

First inserted element gets removed first.

### Common Queue Classes

| Class | Description |
|-------|-------------|
| `PriorityQueue` | Priority-based queue |
| `LinkedList` | Can act as queue |

### Example

```java
Queue<Integer> q = new LinkedList<>();

q.add(10);
q.add(20);

System.out.println(q.poll());
```

**Output:**
```
10
```

> `poll()` removes the first element.

---

## 9. Deque Interface

**Deque = Double Ended Queue**

Insertion/removal possible from:
- Front
- Rear

### Example

```java
Deque<Integer> dq = new ArrayDeque<>();

dq.addFirst(10);
dq.addLast(20);

System.out.println(dq);
```

**Output:**
```
[10, 20]
```

---

## 10. Map Interface

### Important Point

> `Map` is **NOT** part of the Collection interface hierarchy.  
> It is a **separate hierarchy**.

**Why?**  
Because `Map` stores **Key → Value**, while `Collection` stores only individual objects.

### Characteristics
- Key-value pairs
- Keys must be **unique**
- Values can **duplicate**

### Common Map Classes

| Class | Description |
|-------|-------------|
| `HashMap` | Fast, unordered |
| `LinkedHashMap` | Insertion order |
| `TreeMap` | Sorted keys |
| `Hashtable` | Synchronized |

### Example

```java
Map<Integer, String> map = new HashMap<>();

map.put(1, "Java");
map.put(2, "Python");

System.out.println(map);
```

**Output:**
```
{1=Java, 2=Python}
```

---

## 11. Difference Between List and Set

| Feature | List | Set |
|---------|------|-----|
| Duplicates | Allowed | Not allowed |
| Order | Maintains order | Usually unordered |
| Indexing | Yes | No |
| Example | `ArrayList` | `HashSet` |

---

## 12. Difference Between Queue and Deque

| Queue | Deque |
|-------|-------|
| FIFO | Both-end operations |
| Insert/remove from one side | Insert/remove from both sides |

---

## 13. Why Interfaces Are Important?

**Example:**

```java
List<Integer> list = new ArrayList<>();
```

We use:
- **Interface reference** (`List`)
- **Implementation object** (`ArrayList`)

**Benefits:**
- Flexibility
- Loose coupling
- Easy replacement

**Example:**

```java
List<Integer> list = new LinkedList<>();
```

No major code change needed.

---

## 14. Why Map is Separate from Collection?

Because:
- `Collection` stores **single objects**
- `Map` stores **key-value pairs**

**Example:**

Collection:
```
[10, 20, 30]
```

Map:
```
{101=Rahul, 102=Amit}
```

Structure is completely different.

---

## 15. Legacy Classes

Before Collection Framework:

| Legacy Class | Modern Alternative |
|--------------|--------------------|
| `Vector` | `ArrayList` |
| `Hashtable` | `HashMap` |
| `Stack` | `Deque` |

> These older classes are **synchronized** and **slower**.

---

## 16. Important Interview Points

### Q1. Difference between `Collection` and `Collections`?

| `Collection` | `Collections` |
|--------------|---------------|
| Interface | Utility class |
| Represents data structure | Contains helper methods |

**Example:**
```java
Collections.sort(list);
```

### Q2. Why `Collection` interface cannot be instantiated?

Because it is an **interface**.

**Wrong:**
```java
Collection c = new Collection(); // ❌
```

**Correct:**
```java
Collection c = new ArrayList(); // ✅
```

### Q3. Why `Iterable` is important?

Because it enables:
- `for-each` loop
- `iterator` support

---

## 17. Real Life Analogy

| Java Structure | Real Life Example |
|----------------|-------------------|
| `List` | Playlist |
| `Set` | Unique Aadhaar numbers |
| `Queue` | Waiting line |
| `Deque` | Train coach entry/exit |
| `Map` | Dictionary |

---

## 18. Summary

The Java Collection Framework:

- Standardizes data handling
- Provides reusable interfaces/classes
- Makes programming easier
- Supports generic programming
- Improves maintainability

---

## Final Revision Notes

### Hierarchy

```
Iterable
   ↓
Collection
 ┌───────┬────────┬───────┐
List    Set     Queue
                   ↓
                 Deque

Map (Separate)
```

### Main Interfaces

| Interface | Feature |
|-----------|---------|
| `List` | Ordered + duplicates |
| `Set` | Unique elements |
| `Queue` | FIFO |
| `Deque` | Double-ended queue |
| `Map` | Key-value pairs |

### Most Used Classes

| Interface | Common Class |
|-----------|--------------|
| `List` | `ArrayList` |
| `Set` | `HashSet` |
| `Queue` | `PriorityQueue` |
| `Deque` | `ArrayDeque` |
| `Map` | `HashMap` |
