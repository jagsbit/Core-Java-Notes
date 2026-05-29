# Java LinkedList — Complete Detailed Notes

---

## 1. What is LinkedList?

`LinkedList` is a class in Java that implements:
- `List`
- `Deque`
- `Queue`

interfaces.

**Package:**
```
java.util
```

### Definition

A `LinkedList` is a **linear data structure** where elements are connected using links/references.

> Unlike `ArrayList`, elements are **NOT** stored in contiguous memory.

### Important Difference

**ArrayList**
- Uses dynamic array
- Elements stored continuously in memory

**LinkedList**
- Uses nodes connected through references
- Elements can exist anywhere in memory

---

## 2. Internal Structure of LinkedList

Each element is called a **Node**.

### Structure of a Node

A node contains:
1. Data
2. Reference to next node
3. Reference to previous node (in doubly linked list)

### Visual Representation

```
[Prev | Data | Next]
```

**Example:**
```
null <- [10] <-> [20] <-> [30] -> null
```

### Java LinkedList Type

Java's `LinkedList` is a **Doubly Linked List**, meaning:
- Each node knows the **next** node
- Each node knows the **previous** node

---

## 3. Why Use LinkedList?

Main advantage: **Fast insertion and deletion** because no shifting is required.

### ArrayList Problem

Suppose:
```
[10, 20, 30, 40]
```

Insert `15` at index 1 → Need shifting:
```
[10, 15, 20, 30, 40]
```

Elements moved. **Costly operation.**

### LinkedList Advantage

Just update links:

```
10 -> 20
```

becomes

```
10 -> 15 -> 20
```

**No shifting.**

---

## 4. Types of Linked Lists

### 1. Singly Linked List

Each node stores:
- Data
- Next reference

**Structure:**
```
[Data | Next]
```

**Example:**
```
10 -> 20 -> 30 -> null
```

> Can move only **forward**.

### 2. Doubly Linked List

Each node stores:
- Previous reference
- Data
- Next reference

**Structure:**
```
[Prev | Data | Next]
```

**Example:**
```
null <- 10 <-> 20 <-> 30 -> null
```

> Can move **forward** and **backward**.

### 3. Circular Linked List

Last node points back to first node.

**Example:**
```
10 -> 20 -> 30
^             |
|_____________|
```

Creates a cycle.

> Java `LinkedList` uses **Doubly Linked List**.

---

## 5. Basic Node Implementation

To understand `LinkedList` internally.

### Simple Node Class

```java
class Node {

    int value;
    Node next;

    Node(int value) {
        this.value = value;
        this.next = null;
    }
}
```

### Creating Nodes

```java
Node n1 = new Node(10);
Node n2 = new Node(20);
```

### Linking Nodes

```java
n1.next = n2;
```

Now:
```
10 -> 20
```

### Traversal

```java
Node temp = n1;

while (temp != null) {
    System.out.println(temp.value);
    temp = temp.next;
}
```

---

## 6. Creating LinkedList in Java

### Syntax

```java
LinkedList<Integer> list = new LinkedList<>();
```

**OR**

```java
List<Integer> list = new LinkedList<>();
```

**Preferred:**

```java
List<Integer> list = new LinkedList<>();
```

Because: Programming to interface → loose coupling.

---

## 7. Common LinkedList Methods

### `add()`

```java
list.add(10);
```

Adds at end.

### `addFirst()`

```java
list.addFirst(5);
```

Adds at beginning.

### `addLast()`

```java
list.addLast(20);
```

Adds at end.

### `getFirst()`

```java
System.out.println(list.getFirst());
```

### `getLast()`

```java
System.out.println(list.getLast());
```

### `removeFirst()`

```java
list.removeFirst();
```

### `removeLast()`

```java
list.removeLast();
```

### Example

```java
import java.util.*;

public class Main {

    public static void main(String[] args) {

        LinkedList<Integer> list = new LinkedList<>();

        list.add(10);
        list.add(20);

        list.addFirst(5);
        list.addLast(30);

        System.out.println(list);

        System.out.println(list.getFirst());

        System.out.println(list.getLast());

        list.removeFirst();

        System.out.println(list);
    }
}
```

**Output:**
```
[5, 10, 20, 30]
5
30
[10, 20, 30]
```

---

## 8. Time Complexity

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| `get(index)` | O(1) | O(n) |
| `add` at end | O(1) amortized | O(1) |
| `add`/`remove` middle | O(n) | O(1)* |
| search | O(n) | O(n) |

> \* After reaching the node position.

### Why `get(index)` is Slow?

Suppose:
```
10 -> 20 -> 30 -> 40
```

Want `list.get(3)` → Need traversal:
```
10 → 20 → 30 → 40
```

Cannot directly jump. Complexity: **O(n)**

### Why Insertion is Fast?

Only references change.

**Before:**
```
10 -> 30
```

**After inserting 20:**
```
10 -> 20 -> 30
```

No shifting.

---

## 9. Memory Overhead

`LinkedList` uses **more memory** because each node stores:
1. Data
2. Next reference
3. Previous reference

> `ArrayList` stores only data.

---

## 10. `removeIf()`

Removes elements using a condition.

```java
list.removeIf(x -> x % 2 == 0);
```

Removes all even numbers.

### Example

```java
LinkedList<Integer> list = new LinkedList<>();

list.add(10);
list.add(15);
list.add(20);
list.add(25);

list.removeIf(x -> x % 2 == 0);

System.out.println(list);
```

**Output:**
```
[15, 25]
```

---

## 11. `removeAll()`

Removes all matching elements from another collection.

```java
list.removeAll(Arrays.asList(10, 20));
```

---

## 12. LinkedList as Queue

Because `LinkedList` implements the `Queue` interface.

```java
Queue<Integer> q = new LinkedList<>();

q.offer(10);
q.offer(20);

System.out.println(q.poll());
```

**Output:**
```
10
```

> FIFO behavior.

---

## 13. LinkedList as Stack

```java
LinkedList<Integer> stack = new LinkedList<>();

stack.push(10);
stack.push(20);

System.out.println(stack.pop());
```

**Output:**
```
20
```

> LIFO behavior.

---

## 14. Reference Type Important Concept ⚠️

Suppose:

```java
List<Integer> list = new LinkedList<>();
```

Now only `List` methods are accessible.

**Cannot use:**

```java
list.addFirst(10); // ❌ Error
```

Because `addFirst()` belongs to the `LinkedList` class, **not** the `List` interface.

**Solution:**

```java
LinkedList<Integer> list = new LinkedList<>();
```

### Interview Question — Why Use Interface Reference?

```java
List<Integer> list = new LinkedList<>();
```

**Benefits:**
- Loose coupling
- Flexibility
- Interchangeable implementations

---

## 15. LinkedList vs ArrayList

| Feature | ArrayList | LinkedList |
|---------|-----------|------------|
| Internal Structure | Dynamic Array | Doubly Linked List |
| Memory | Less | More |
| Random Access | Fast | Slow |
| Insert/Delete | Slow | Fast |
| Cache Friendly | Yes | No |

### When to Use ArrayList?

Use when:
- Frequent access needed
- More read operations
- Less insert/delete

### When to Use LinkedList?

Use when:
- Frequent insertions/deletions
- Queue operations
- Stack operations

---

## 16. Internal Working of `add()`

Suppose:
```
10 <-> 20
```

Add `30`: Java creates a new node, updates old tail's `next`, updates new node's `prev`, and updates the `tail` reference.

---

## 17. Important Internal Fields

Internally `LinkedList` maintains:

| Field | Description |
|-------|-------------|
| `head` | Points to first node |
| `tail` | Points to last node |
| `size` | Stores number of nodes |

---

## 18. Null Elements

`LinkedList` allows **null values**.

```java
list.add(null); // ✅ Valid
```

---

## 19. Thread Safety

`LinkedList` is **NOT synchronized** — not thread-safe.

**Thread-Safe Alternative:**

```java
Collections.synchronizedList(new LinkedList<>());
```

---

## 20. Common Exceptions

### `NoSuchElementException`

Occurs with `getFirst()` or `removeFirst()` on an empty list.

### Safer Alternatives

```java
peekFirst()  // returns null instead of exception
pollFirst()  // returns null instead of exception
```

---

## 21. Complete Example

```java
import java.util.*;

public class Main {

    public static void main(String[] args) {

        LinkedList<String> list = new LinkedList<>();

        list.add("Java");
        list.add("Python");

        list.addFirst("C");
        list.addLast("Spring");

        System.out.println(list);

        System.out.println(list.getFirst());

        list.removeLast();

        System.out.println(list);

        list.removeIf(x -> x.startsWith("P"));

        System.out.println(list);
    }
}
```

**Output:**
```
[C, Java, Python, Spring]
C
[C, Java, Python]
[C, Java]
```

---

## 22. Common Interview Questions

### Q1. Why is `get(index)` slow in LinkedList?

Because traversal is required node-by-node. Complexity: **O(n)**.

### Q2. Why is insertion fast?

Because only references change.

### Q3. Does LinkedList use contiguous memory?

**No.**

### Q4. Which Linked List does Java use?

**Doubly Linked List.**

### Q5. Which is better: ArrayList or LinkedList?

**Depends on use case.**

---

## 23. Final Revision Notes

### Key Points

- ✅ `LinkedList` uses nodes
- ✅ Java uses Doubly Linked List
- ✅ Fast insertion/deletion
- ✅ Slow random access
- ✅ More memory usage
- ✅ Implements `List` + `Queue` + `Deque`
- ✅ Allows null values
- ✅ Not synchronized

### Quick Complexity Revision

| Operation | Complexity |
|-----------|------------|
| `get()` | O(n) |
| `addFirst()` | O(1) |
| `addLast()` | O(1) |
| `removeFirst()` | O(1) |
| `removeLast()` | O(1) |

### Shortcut to Remember

```
ArrayList   → Fast Access

LinkedList  → Fast Modification
```
