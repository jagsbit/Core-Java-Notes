# CompletableFuture in Java

`CompletableFuture` is one of the **MOST IMPORTANT** modern Java concurrency concepts.

It is heavily used in:
- Spring Boot
- Microservices
- REST APIs
- Async programming
- High-performance backend systems

**Introduced in:** Java 8

**Package:** `java.util.concurrent`

---

## 1. Problem with `Future` (Before Java 8)

Before Java 8, we had `Future` used with `Callable` and `ExecutorService`:

```java
Future<Integer> future = service.submit(() -> 100);
Integer result = future.get();
```

### Major Problems with `Future`

| Problem                      | Explanation                                               |
|------------------------------|-----------------------------------------------------------|
| Blocking                     | `get()` blocks the calling thread indefinitely           |
| Cannot chain tasks           | Hard to connect multiple async operations                 |
| No callback support          | Cannot automatically react when result is ready           |
| Difficult error handling     | Limited exception management                              |
| Hard async composition       | Combining multiple async tasks is messy                   |

### Example Problem

Suppose you need to:
1. **Download User**
2. **Fetch Orders**
3. **Send Email**

With old `Future` — chaining these steps becomes **messy and blocking**.

---

## 2. Solution → `CompletableFuture`

`CompletableFuture` is an **advanced asynchronous `Future`** that supports:
- Callbacks
- Chaining
- Async pipelines
- Composition
- Non-blocking programming

### Simple Definition

> `CompletableFuture` represents a future result that can be **manually completed** and **asynchronously processed**.

---

### Real-Life Analogy

**Ordering food online:**

| Approach              | Behavior                                                  |
|-----------------------|-----------------------------------------------------------|
| **`Future`**          | You repeatedly ask: *"Food ready? Food ready?"* — or block waiting |
| **`CompletableFuture`** | Restaurant prepares, delivers, sends notification, triggers next action — **automatically** |

### Main Superpower

Instead of:

```java
result = future.get(); // blocking wait
```

You can say:

> *"When result is ready, automatically do next work."*

This is: **Asynchronous Programming**

---

## 3. Creating `CompletableFuture`

```java
CompletableFuture<Integer> future =
    CompletableFuture.supplyAsync(() -> {
        return 100;
    });
```

**What happens?** The task runs **asynchronously** in a background thread.

---

## 4. `supplyAsync()` vs `runAsync()`

> ⭐ **VERY IMPORTANT interview question.**

| Method          | Similar To  | Returns Result? |
|-----------------|-------------|-----------------|
| `runAsync()`    | `Runnable`  | ❌ NO (`void`)   |
| `supplyAsync()` | `Callable`  | ✅ YES           |

### `runAsync()` — No Return Value

```java
CompletableFuture.runAsync(() -> {
    System.out.println("Task running in background");
});
```

### `supplyAsync()` — Returns a Value

```java
CompletableFuture<Integer> future =
    CompletableFuture.supplyAsync(() -> 100);
```

---

## 5. Why "Completable" Future?

Because the result can be **manually completed**:

```java
CompletableFuture<String> future = new CompletableFuture<>();

future.complete("Hello"); // manually complete it
```

Now the future's result becomes: `"Hello"`

---

## 6. Getting the Result

### Traditional (Blocking) Way

```java
Integer result = future.get(); // blocks thread
```

### Better Approach — Use Callbacks and Chaining

Instead of blocking, define **what to do when result is ready**.

---

## 7. `thenApply()` — Transform Result

> ⭐ **VERY IMPORTANT.**

Used to **transform** the result of the previous stage.

```java
CompletableFuture<Integer> future =
    CompletableFuture
        .supplyAsync(() -> 10)
        .thenApply(x -> x * 2);
```

### Flow

```
supplyAsync() → 10
     ↓
thenApply(x → x * 2)
     ↓
     20
```

> No manual waiting logic — the **pipeline executes automatically**.

---

## 8. `thenAccept()` — Consume Result

Used when you want the **final result** but don't need to return anything afterward.

```java
CompletableFuture
    .supplyAsync(() -> 10)
    .thenApply(x -> x * 2)
    .thenAccept(result -> {
        System.out.println(result);
    });
```

**Output:**

```
20
```

---

## 9. `thenRun()` — Run Next Task

Used when:
- No input needed from previous stage
- No return value needed

Just execute the next task.

```java
CompletableFuture
    .runAsync(() -> {
        System.out.println("Task-1");
    })
    .thenRun(() -> {
        System.out.println("Task-2");
    });
```

**Output:**

```
Task-1
Task-2
```

---

## 10. Callback Methods Summary

| Method          | Input from Previous? | Returns Value? | Use Case                          |
|-----------------|----------------------|----------------|-----------------------------------|
| `thenApply()`   | ✅ Yes                | ✅ Yes          | Transform result                  |
| `thenAccept()`  | ✅ Yes                | ❌ No           | Consume/use result                |
| `thenRun()`     | ❌ No                 | ❌ No           | Run a task after completion       |

---

## 11. Combining Multiple Futures

> This is where `CompletableFuture` becomes **VERY powerful**.

### Example — Fetch Multiple Resources in Parallel

```java
CompletableFuture<String> f1 =
    CompletableFuture.supplyAsync(() -> "User");

CompletableFuture<String> f2 =
    CompletableFuture.supplyAsync(() -> "Orders");
```

### Combine Using `thenCombine()`

```java
CompletableFuture<String> combined =
    f1.thenCombine(f2, (a, b) -> a + " " + b);

System.out.println(combined.get()); // User Orders
```

**Result:** `User Orders`

Both futures run **concurrently** and their results are combined when both complete.

---

## 12. Exception Handling with `exceptionally()`

> ⭐ **VERY IMPORTANT** — elegant async error handling.

```java
CompletableFuture
    .supplyAsync(() -> {
        int x = 10 / 0; // throws ArithmeticException
        return x;
    })
    .exceptionally(ex -> {
        System.out.println("Error: " + ex.getMessage());
        return -1; // fallback value
    });
```

**Output:**

```
Error: / by zero
```

**Result:** `-1` (fallback value)

---

## 13. Async Pipeline Example

### Traditional `Future` Style (Blocking)

```java
Future<Integer> f = ...;
Integer result = f.get(); // thread blocked here
// nothing else happens until result is ready
```

### `CompletableFuture` Style (Non-Blocking)

```java
CompletableFuture
    .supplyAsync(() -> fetchUser())
    .thenApply(user -> fetchOrders(user))
    .thenApply(orders -> generateReport(orders))
    .thenAccept(report -> sendEmail(report))
    .exceptionally(ex -> {
        System.out.println("Error: " + ex);
        return null;
    });

// Thread continues doing other work — not blocked!
```

> **Thread continues** doing other work while the pipeline executes asynchronously. Much more **scalable**.

---

## 14. Real Enterprise Usage

### Example — E-Commerce Product Page

A product page requires:
- Product details
- Reviews
- Recommendations
- Stock status

**Without `CompletableFuture`** → one-by-one sequential calls → slow.

**With `CompletableFuture`** → all APIs called **concurrently** → faster response.

```java
CompletableFuture<String> details     = CompletableFuture.supplyAsync(() -> getDetails());
CompletableFuture<String> reviews     = CompletableFuture.supplyAsync(() -> getReviews());
CompletableFuture<String> stock       = CompletableFuture.supplyAsync(() -> getStock());

CompletableFuture.allOf(details, reviews, stock).join(); // wait for all
```

Used in:
- Spring Boot async APIs
- Microservices
- REST aggregation
- Parallel DB calls
- Async email sending
- Parallel API requests

---

## 15. `CompletableFuture` vs `Future`

> ⭐ **VERY IMPORTANT interview question.**

| Feature                    | `Future`       | `CompletableFuture`    |
|----------------------------|----------------|------------------------|
| Blocking `get()`           | ✅ Always       | Optional               |
| Callback Support           | ❌ No           | ✅ Yes                  |
| Task Chaining              | ❌ No           | ✅ Yes                  |
| Combine Multiple Tasks     | ❌ Difficult    | ✅ Easy (`thenCombine`) |
| Exception Handling         | ❌ Limited      | ✅ Powerful             |
| Async Pipelines            | ❌ No           | ✅ Yes                  |
| Manual Completion          | ❌ No           | ✅ Yes (`complete()`)   |

---

## 16. Core Internal Understanding

`CompletableFuture` enables **event-driven async programming**.

Instead of:
> *"Wait for result"*

You define:
> *"What should happen **AFTER** result arrives"*

This is a **very important mindset shift** from traditional blocking code.

---

## 17. ⭐ Important Interview Questions

### Q1. Why was `CompletableFuture` introduced?

> To overcome the **limitations of `Future`** — specifically blocking `get()`, no callback support, and no chaining.

### Q2. Difference between `runAsync()` and `supplyAsync()`?

| | `runAsync()` | `supplyAsync()` |
|-|--------------|-----------------|
| Similar to | `Runnable` | `Callable` |
| Returns value | ❌ No | ✅ Yes |

### Q3. Is `CompletableFuture` blocking?

> **No by design** — unless `get()` or `join()` is explicitly used.

### Q4. What is `thenApply()` used for?

> To **transform** the result of the previous async stage.

### Q5. What is `exceptionally()` used for?

> To handle **exceptions** in the async pipeline and provide a **fallback value**.

### Q6. Difference between `thenApply()`, `thenAccept()`, and `thenRun()`?

| Method         | Takes Input? | Returns Value? |
|----------------|--------------|----------------|
| `thenApply()`  | ✅ Yes        | ✅ Yes          |
| `thenAccept()` | ✅ Yes        | ❌ No           |
| `thenRun()`    | ❌ No         | ❌ No           |

### Q7. What does `thenCombine()` do?

> Combines the results of **two independent** `CompletableFuture` instances when both complete.

---

## Final Revision Table

| Concept              | Meaning                                               |
|----------------------|-------------------------------------------------------|
| `Future`             | Placeholder for async result — blocking               |
| `CompletableFuture`  | Advanced async pipeline — non-blocking                |
| `supplyAsync()`      | Runs task async and **returns** a value               |
| `runAsync()`         | Runs task async with **no return** value              |
| `thenApply()`        | Transform result                                      |
| `thenAccept()`       | Consume result (no return)                            |
| `thenRun()`          | Run next task (no input, no return)                   |
| `thenCombine()`      | Combine results of two futures                        |
| `exceptionally()`    | Handle errors and provide fallback                    |
| `complete()`         | Manually complete a future with a value               |

---

## One-Line Interview Definitions

**`CompletableFuture`**
> `CompletableFuture` is a modern asynchronous programming tool in Java 8+ that enables non-blocking task execution, result chaining, callback handling, and efficient composition of multiple async operations.

**`supplyAsync()`**
> `supplyAsync()` runs a `Callable`-like task asynchronously in a background thread and returns a `CompletableFuture` holding the result.

**`thenApply()`**
> `thenApply()` transforms the result of a `CompletableFuture` stage and passes it to the next stage in the async pipeline.

**`exceptionally()`**
> `exceptionally()` handles any exception thrown in the async pipeline and returns a fallback value to allow graceful recovery.

---

## Revision Checklist

- [ ] Can I explain the limitations of `Future` that led to `CompletableFuture`?
- [ ] Do I know the difference between `supplyAsync()` and `runAsync()`?
- [ ] Can I explain what "Completable" means — manual completion via `complete()`?
- [ ] Do I understand `thenApply()`, `thenAccept()`, and `thenRun()` differences?
- [ ] Can I use `thenCombine()` to combine two parallel futures?
- [ ] Do I know how to handle exceptions with `exceptionally()`?
- [ ] Can I explain why `CompletableFuture` is non-blocking by design?
- [ ] Can I compare `Future` vs `CompletableFuture` in a table?
- [ ] Do I know where `CompletableFuture` is used in real enterprise applications?
- [ ] Can I build a complete async pipeline using `supplyAsync()` → `thenApply()` → `thenAccept()`?
