# Java Stack — Complete Detailed Notes

---

## 1. What is a Stack?

A Stack is a **linear data structure** following the **LIFO principle**.

### LIFO Principle

> **Last In First Out** — the last inserted element is removed first.

### Real Life Examples

| Example | Explanation |
|---------|-------------|
| Stack of books | Remove top book first |
| Browser history | Last visited page comes first |
| Undo operation | Last action undone first |

### Visual Representation

```
Top
 ↓
[30]
[20]
[10]
```

If we remove → `30` removed first.

---

## 2. Stack in Java

Java provides a built-in `Stack` class.

**Package:**
```
java.util
```

### Declaration

```java
Stack<Integer> stack = new Stack<>();
```

### Important Hierarchy

```
Object
   ↓
Vector
   ↓
Stack
```

### Important Point

`Stack` extends `Vector`, therefore it is:
- Synchronized
- Thread-safe
- Dynamically resizing

---

## 3. Why Stack Extends Vector?

Because `Vector` already provides:
- Dynamic array
- Synchronization
- Resizing logic

`Stack` simply adds **LIFO-specific methods** like:
- `push()`
- `pop()`
- `peek()`

---

## 4. Internal Working of Stack

Internally `Stack` uses a **Dynamic Array (Vector)**, NOT a linked list.

### Stack Top

Top element stored at the **last index**.

**Example:**
```
Index: 0   1   2
       10  20  30
```

Top: `30`

---

## 5. Stack Operations

### 1. `push()`

Adds element to top.

**Syntax:**
```java
stack.push(10);
```

**Example:**
```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack);
```

**Output:**
```
[10, 20, 30]
```

Top: `30` | Complexity: **O(1)**

---

### 2. `pop()`

Removes and returns the top element.

```java
System.out.println(stack.pop());
```

**Output:**
```
30
```

Stack after pop: `[10, 20]`

Complexity: **O(1)**

> ⚠️ If stack is empty, `stack.pop()` throws `EmptyStackException`.

---

### 3. `peek()`

Returns top element **WITHOUT removing** it.

```java
System.out.println(stack.peek());
```

**Output:**
```
20
```

Stack remains the same. Complexity: **O(1)**

---

### 4. `isEmpty()`

Checks if the stack is empty.

```java
System.out.println(stack.isEmpty());
```

**Output:**
```
false
```

---

### 5. `size()`

Returns number of elements.

```java
System.out.println(stack.size());
```

---

### 6. `search()` ⭐

Searches element position from the top.

> **Very important interview point.**

**Important:** Uses **1-based indexing**, NOT zero-based.

**Example:**
```java
Stack<Integer> stack = new Stack<>();

stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack.search(30));
```

**Output:**
```
1
```

Because `30` is at the top.

```java
System.out.println(stack.search(10));
```

**Output:**
```
3
```

> If element not found, returns **-1**.

---

## 6. Complete Example

```java
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println(stack);

        System.out.println(stack.peek());

        System.out.println(stack.pop());

        System.out.println(stack);

        System.out.println(stack.search(10));

        System.out.println(stack.isEmpty());

        System.out.println(stack.size());
    }
}
```

**Output:**
```
[10, 20, 30]
30
30
[10, 20]
2
false
2
```

---

## 7. Why Stack is Thread-Safe?

Because it extends `Vector`, whose methods are synchronized.

**Internal Example:**
```java
public synchronized E push(E item)
```

Meaning: **one thread at a time**.

**Advantage:** Safe in multithreaded environments.

**Disadvantage:** Synchronization overhead → **slower performance**.

---

## 8. Stack vs ArrayList

| Feature | Stack | ArrayList |
|---------|-------|-----------|
| Thread Safe | Yes | No |
| LIFO Methods | Yes | No |
| Synchronization | Yes | No |
| Performance | Slower | Faster |

---

## 9. Using Vector Methods in Stack

Because `Stack` extends `Vector`:

```java
stack.add(100);
stack.remove(0);
```

These are **valid**, but **NOT recommended**.

### Why?

Because it breaks **LIFO behavior**.

**Example:**
```java
stack.remove(0); // removes bottom element ❌
```

Not proper stack behavior.

### Best Practice

Use only:
```java
push()
pop()
peek()
```

for stack operations.

---

## 10. Time Complexity

| Operation | Complexity |
|-----------|------------|
| `push()` | O(1) |
| `pop()` | O(1) |
| `peek()` | O(1) |
| `search()` | O(n) |

### Why `search()` is O(n)?

Needs linear traversal from top.

---

## 11. Stack Underflow

Occurs when `pop()` or `peek()` is called on an empty stack.

**Exception:**
```
EmptyStackException
```

**Safe Check:**
```java
if (!stack.isEmpty()) {
    stack.pop();
}
```

---

## 12. Common Applications of Stack

### 1. Function Call Stack
Java internally uses stack memory.

### 2. Undo/Redo
Text editors.

### 3. Browser History
Back button.

### 4. Expression Evaluation
- Postfix
- Infix
- Prefix

### 5. Parentheses Matching
```
{[()]}
```

### 6. DFS (Depth First Search)
Graphs and trees.

---

## 13. Alternative Implementations of Stack

### 1. LinkedList as Stack

```java
LinkedList<Integer> stack = new LinkedList<>();

stack.push(10);
stack.push(20);

System.out.println(stack.pop());
```

**Why Better?**
- No synchronization overhead
- Faster in single-threaded programs

### 2. ArrayDeque (Recommended) ⭐

Modern preferred stack implementation.

```java
Deque<Integer> stack = new ArrayDeque<>();

stack.push(10);
stack.push(20);

System.out.println(stack.pop());
```

**Why Recommended?**
- Better performance
- Better memory efficiency than `Stack` class

> **Official Recommendation:** Java documentation recommends `ArrayDeque` instead of `Stack` for most use cases.

---

## 14. Stack vs Queue

| Feature | Stack | Queue |
|---------|-------|-------|
| Principle | LIFO | FIFO |
| Insertion | Top | Rear |
| Removal | Top | Front |

### Example

**Stack:**
```
Push: 10 20 30
Pop:  30 first
```

**Queue:**
```
Insert: 10 20 30
Remove: 10 first
```

---

## 15. Important Interview Questions

### Q1. Which principle does Stack follow?

```
LIFO
```

### Q2. Which class does Stack extend?

```
Vector
```

### Q3. Is Stack synchronized?

**Yes.**

### Q4. Why is Stack slower?

Because it is **synchronized**.

### Q5. Which modern class is preferred over Stack?

```
ArrayDeque
```

### Q6. Difference between `peek()` and `pop()`?

| Method | Removes Element |
|--------|----------------|
| `peek()` | No |
| `pop()` | Yes |

### Q7. What does `search()` return?

Position from top using **1-based indexing**.

---

## 16. Complete Real Example

```java
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Stack<String> stack = new Stack<>();

        stack.push("Java");
        stack.push("Spring");
        stack.push("React");

        System.out.println(stack);

        System.out.println(stack.peek());

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
```

**Output:**
```
[Java, Spring, React]
React
React
Spring
Java
```

---

## 17. Final Revision Notes

### Key Points

- ✅ Stack follows LIFO
- ✅ Stack extends `Vector`
- ✅ Thread-safe
- ✅ `push()` adds to top
- ✅ `pop()` removes from top
- ✅ `peek()` views top
- ✅ `search()` uses 1-based indexing
- ✅ `pop()`/`peek()` on empty stack throws `EmptyStackException`
- ✅ `ArrayDeque` preferred in modern Java

### Quick Complexity Table

| Operation | Complexity |
|-----------|------------|
| `push` | O(1) |
| `pop` | O(1) |
| `peek` | O(1) |
| `search` | O(n) |

### Shortcut to Remember

```
Stack = LIFO

Last In → First Out
```
