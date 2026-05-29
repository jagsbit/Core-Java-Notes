# Queue Interface in Java — Complete Detailed Notes

The `Queue` interface is part of the `java.util` package. It is used to store elements in a specific processing order.

---

## 1. What is Queue?

A Queue is a data structure that usually follows the **FIFO Principle**.

### FIFO Principle

> **First In First Out** — the first inserted element is removed first.

### Real Life Example

**Ticket counter line** — Person entering first leaves first.

### Visual Representation

```
Front                    Rear
 ↓                         ↓
[10] [20] [30] [40]
```

- Removal happens from: **Front**
- Insertion happens at: **Rear**

---

## 2. Queue Hierarchy

```
Iterable
   ↓
Collection
   ↓
Queue
```

`Queue` extends `Collection`.

### Important Queue Implementations

| Class | Description |
|-------|-------------|
| `LinkedList` | Queue + List implementation |
| `PriorityQueue` | Priority-based queue |
| `ArrayDeque` | Double-ended queue |
| `ConcurrentLinkedQueue` | Thread-safe non-blocking queue |
| `ArrayBlockingQueue` | Fixed-size blocking queue |
| `LinkedBlockingQueue` | Blocking queue using linked nodes |
| `PriorityBlockingQueue` | Thread-safe priority queue |
| `SynchronousQueue` | Direct thread handoff |
| `DelayQueue` | Delayed element processing |

---

## 3. Main Queue Operations

| Method | Purpose |
|--------|---------|
| `add(e)` | Insert element |
| `offer(e)` | Insert safely |
| `remove()` | Remove front |
| `poll()` | Remove safely |
| `element()` | View front |
| `peek()` | View safely |

---

## 4. Difference Between `add()` and `offer()`

### `add()`

```java
queue.add(10);
```

If insertion fails → **throws exception**

### `offer()`

```java
queue.offer(10);
```

If insertion fails → **returns `false`**

> Safer method.

---

## 5. Difference Between `remove()` and `poll()`

### `remove()`

```java
queue.remove();
```

If queue empty → **throws exception**

### `poll()`

```java
queue.poll();
```

If queue empty → **returns `null`**

---

## 6. Difference Between `element()` and `peek()`

### `element()`

```java
queue.element();
```

Empty queue → **throws exception**

### `peek()`

```java
queue.peek();
```

Empty queue → **returns `null`**

---

## 7. LinkedList as Queue

```java
Queue<Integer> q = new LinkedList<>();

q.offer(10);
q.offer(20);
q.offer(30);

System.out.println(q.poll());
```

**Output:**
```
10
```

> FIFO maintained.

---

## 8. PriorityQueue

> **Very important interview topic.**

### What is PriorityQueue?

Elements processed based on **priority**, NOT insertion order.

### Default Behavior

**Min-heap** — smallest element removed first.

### Example

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();

pq.offer(30);
pq.offer(10);
pq.offer(20);

System.out.println(pq.poll());
```

**Output:**
```
10
```

### Internal Structure

Uses a **Heap** internally.

### Complexity

| Operation | Complexity |
|-----------|------------|
| `add` | O(log n) |
| `poll` | O(log n) |
| `peek` | O(1) |

---

## 9. Deque Interface

**Deque** means **Double Ended Queue**.

Allows insertion/removal from **both front and rear**.

### Hierarchy

```
Queue
   ↓
Deque
```

### Common Implementations

| Class | Description |
|-------|-------------|
| `ArrayDeque` | Array-based deque |
| `LinkedList` | Linked deque |

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

## 10. BlockingQueue

Used in **multithreading**.

### Special Feature

- If queue **empty** → consumer **waits**
- If queue **full** → producer **waits**

### Common Blocking Queues

| Queue | Description |
|-------|-------------|
| `ArrayBlockingQueue` | Fixed-size |
| `LinkedBlockingQueue` | Linked nodes |
| `PriorityBlockingQueue` | Priority-based |
| `SynchronousQueue` | No internal storage |
| `DelayQueue` | Delayed processing |

---

## 11. Null Elements in Queue

> **Very important interview question.**

### Which Queues Allow `null`?

| Queue Type | Allows null? |
|------------|-------------|
| `LinkedList` | ✅ YES |
| `ArrayDeque` | ❌ NO |
| `PriorityQueue` | ❌ NO |
| `ConcurrentLinkedQueue` | ❌ NO |
| `BlockingQueue` implementations | ❌ NO |

### Why Most Queues Do NOT Allow `null`?

Because `null` is used as a **special return value** for methods like `poll()` and `peek()`.

**Example:**

```java
Queue<Integer> q = new PriorityQueue<>();

System.out.println(q.poll());
```

**Output:**
```
null
```

Meaning: queue is empty.

### Problem If `null` Were Allowed

Suppose `null` inserted:

```java
q.offer(null);
```

Now:

```java
q.poll(); // returns null
```

**Question:** Was the queue empty? OR was an actual `null` stored?

> Ambiguity occurs — therefore most queue implementations **disallow null elements**.

---

## 12. Why LinkedList Allows `null`?

Because `LinkedList` is primarily a **List implementation**, and lists allow null values.

```java
LinkedList<Integer> list = new LinkedList<>();

list.add(null);

System.out.println(list);
```

**Output:**
```
[null]
```

### But Using `null` in Queue Logic is Dangerous

Because queue methods use `null` as a failure indicator.

> Generally: **avoid inserting null** even in `LinkedList` queues.

---

## 13. ArrayDeque and `null`

```java
Deque<Integer> dq = new ArrayDeque<>();

dq.add(null); // ❌ Throws NullPointerException
```

**Why?** Because methods like `pollFirst()` and `peekFirst()` return `null` for an empty deque.

---

## 14. PriorityQueue and `null`

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();

pq.add(null); // ❌ Exception
```

**Why?** Because `PriorityQueue` must:
- Compare elements
- Maintain heap order

`null` cannot be compared.

---

## 15. Concurrent Queues and `null`

Examples: `ConcurrentLinkedQueue`, `BlockingQueue` — do **NOT** allow `null`.

**Why?** In concurrent programming, `null` commonly signals a failure/empty state. Allowing `null` would break thread communication logic.

---

## 16. Queue vs Stack

| Feature | Queue | Stack |
|---------|-------|-------|
| Principle | FIFO | LIFO |
| Insertion | Rear | Top |
| Removal | Front | Top |

---

## 17. Queue vs Deque

| Queue | Deque |
|-------|-------|
| One-end operations | Both-end operations |
| FIFO only | FIFO + LIFO |

---

## 18. ArrayDeque — Modern Recommendation ⭐

For stack/queue operations, `ArrayDeque` is **preferred** over `Stack` and `LinkedList` because:
- Faster
- Memory efficient
- No synchronization overhead

### Example as Stack

```java
Deque<Integer> stack = new ArrayDeque<>();

stack.push(10);
stack.push(20);

System.out.println(stack.pop());
```

### Example as Queue

```java
Deque<Integer> queue = new ArrayDeque<>();

queue.offer(10);
queue.offer(20);

System.out.println(queue.poll());
```

---

## 19. Important Interview Questions

### Q1. Why do Queue implementations usually disallow `null`?

Because `null` is used as a **special return value**.

### Q2. Which Queue implementation allows `null`?

```
LinkedList
```

### Q3. Why does PriorityQueue disallow `null`?

Because **comparisons are required** — heap ordering is impossible with `null`.

### Q4. Which queue is thread-safe?

Examples:
- `ConcurrentLinkedQueue`
- `BlockingQueue` implementations

### Q5. Best modern stack/queue implementation?

```
ArrayDeque
```

---

## 20. Final Revision Notes

### Key Points

- ✅ Queue follows FIFO
- ✅ Queue extends `Collection`
- ✅ `offer`/`poll` preferred over `add`/`remove`
- ✅ Most queues do **NOT** allow `null`
- ✅ `null` used as empty indicator
- ✅ `LinkedList` allows `null`
- ✅ `PriorityQueue` uses heap internally
- ✅ `Deque` supports both ends
- ✅ `ArrayDeque` is the modern preferred implementation

### Quick Null Support Table

| Queue Type | Null Allowed? | Reason |
|------------|--------------|--------|
| `LinkedList` | Yes | List behavior |
| `ArrayDeque` | No | `null` used for empty |
| `PriorityQueue` | No | Comparison issue |
| `ConcurrentLinkedQueue` | No | Concurrency semantics |
| `BlockingQueue` | No | Thread signaling |

### Shortcut to Remember

```
Queue → FIFO

Most Queues:
NO null
because null = empty signal
```
