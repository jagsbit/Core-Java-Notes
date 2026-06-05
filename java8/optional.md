# Optional in Java 8 — Complete Guide for Junior Developers

`Optional` is one of the **MOST IMPORTANT** Java 8 features.

It was introduced to solve one huge problem in Java:

> **NullPointerException (NPE)**

---

## 1. Why Optional Was Introduced?

Before Java 8, Java programs frequently crashed because of:

> `NullPointerException`

### Example

```java
String name = null;

System.out.println(name.length());
```

Output:

```
Exception in thread "main"
java.lang.NullPointerException
```

This is one of the **most common** exceptions in Java.

### Problem Before Optional

Suppose:

```java
User user = getUser();
```

What if `user == null`? Then:

```java
user.getName()
```

crashes the application.

So developers had to write:

```java
if(user != null) {
    System.out.println(user.getName());
}
```

everywhere.

- Too much null checking.
- Messy code.

### Java 8 Solution → Optional

Optional helps represent:

> **"Value may or may not be present"**

Instead of returning `null`, we return `Optional<T>`.

---

## 2. What is Optional?

`Optional` is a **container object**.

It may contain:

- a value
- OR **no value**

### Package

```
java.util.Optional
```

### Simple Definition

> Optional is a wrapper object used to avoid null handling issues.

---

## 3. Real Life Analogy

Suppose **food delivery**:

**Without Optional:**

> Delivery boy may come or may not come — Uncertain.

**With Optional:**

> Box exists. Inside box: food may exist OR food may not exist. → **Safer handling.**

---

## 4. How Optional Works

### Traditional Way

```java
String name = getName();

if(name != null) {
    System.out.println(name);
}
```

### Optional Way

```java
Optional<String> name = getName();

name.ifPresent(System.out::println);
```

> Cleaner. Readable. Safer.

---

## 5. Creating Optional Objects

> **MOST IMPORTANT.**

There are mainly 3 ways:

| Method                | Purpose              |
|-----------------------|----------------------|
| `Optional.of()`       | Non-null value       |
| `Optional.ofNullable()` | Null or non-null   |
| `Optional.empty()`    | Empty Optional       |

---

## 6. `Optional.of()`

Used when value is **definitely NOT null**.

### Syntax

```java
Optional.of(value)
```

### Example

```java
Optional<String> name =
        Optional.of("Java");

System.out.println(name);
```

Output:

```
Optional[Java]
```

### Important Rule

`Optional.of()` does **NOT** allow `null`.

```java
Optional<String> name =
        Optional.of(null);
```

Output:

```
NullPointerException
```

**Why?** Because `of()` assumes:

> "I guarantee value is not null"

### When to Use `Optional.of()`

Use when:

- value is guaranteed present
- you are 100% sure value is not null

Example:

```java
Optional.of("Spring Boot")
```

---

## 7. `Optional.ofNullable()`

> **MOST IMPORTANT METHOD.**

Used when value may be `null` OR non-null.

### Syntax

```java
Optional.ofNullable(value)
```

### Example 1

```java
String name = "Java";

Optional<String> optional =
        Optional.ofNullable(name);

System.out.println(optional);
```

Output:

```
Optional[Java]
```

### Example 2

```java
String name = null;

Optional<String> optional =
        Optional.ofNullable(name);

System.out.println(optional);
```

Output:

```
Optional.empty
```

### Important Difference

| Method          | Null Allowed? |
|-----------------|---------------|
| `of()`          | No            |
| `ofNullable()`  | Yes           |

### MOST IMPORTANT INTERVIEW QUESTION

**Difference Between `of()` and `ofNullable()`**

| `Optional.of()`             | `Optional.ofNullable()`         |
|-----------------------------|---------------------------------|
| Null NOT allowed            | Null allowed                    |
| Throws NPE if null          | Returns empty Optional          |
| Use when value guaranteed   | Use when uncertain              |

---

## 8. `Optional.empty()`

Creates an **empty** Optional.

### Example

```java
Optional<String> optional =
        Optional.empty();

System.out.println(optional);
```

Output:

```
Optional.empty
```

---

## 9. Checking Value Presence

### `isPresent()`

Checks if value exists.

```java
Optional<String> optional =
        Optional.of("Java");

System.out.println(optional.isPresent());
```

Output:

```
true
```

### Empty Example

```java
Optional<String> optional =
        Optional.empty();

System.out.println(optional.isPresent());
```

Output:

```
false
```

### Problem with `isPresent()`

This becomes similar to old null checks.

**Bad style:**

```java
if(optional.isPresent()) {
    System.out.println(optional.get());
}
```

Equivalent to:

```java
if(obj != null)
```

So modern Java prefers:

- `ifPresent()`
- `orElse()`
- `map()`
- `orElseGet()`

---

## 10. `get()`

Returns the actual value.

### Example

```java
Optional<String> optional =
        Optional.of("Java");

System.out.println(optional.get());
```

Output:

```
Java
```

### Danger of `get()`

If Optional is empty:

```java
Optional.empty().get();
```

Output:

```
NoSuchElementException
```

### BEST PRACTICE

> Avoid direct use of `get()` unless you are absolutely sure value exists.

---

## 11. `ifPresent()`

**Modern safe approach.**

Executes only if value exists.

### Example

```java
Optional<String> optional =
        Optional.of("Java");

optional.ifPresent(System.out::println);
```

Output:

```
Java
```

### Internally

Equivalent to:

```java
if(value != null) {
    System.out.println(value);
}
```

> `ifPresent` uses `Consumer<T>` — a functional interface.

---

## 12. `orElse()`

Provides **default value**.

> **MOST IMPORTANT.**

### Example

```java
Optional<String> optional =
        Optional.ofNullable(null);

String result =
        optional.orElse("Default Value");

System.out.println(result);
```

Output:

```
Default Value
```

### If Value Exists

```java
Optional<String> optional =
        Optional.of("Java");

System.out.println(
        optional.orElse("Default")
);
```

Output:

```
Java
```

### Real World Use

```java
String city =
        user.getCity().orElse("Unknown");
```

---

## 13. `orElseGet()`

Similar to `orElse()` but **lazy**.

> **VERY IMPORTANT INTERVIEW QUESTION.**

### Example

```java
String result =
        optional.orElseGet(() -> "Generated");
```

### Difference Between `orElse` and `orElseGet`

> **VERY IMPORTANT.**

#### `orElse()`

Always executes default value creation.

```java
optional.orElse(expensiveMethod());
```

Even if value exists → `expensiveMethod()` still executes.

#### `orElseGet()` — Better

```java
optional.orElseGet(() -> expensiveMethod());
```

Only executes if needed.

### Interview Summary

| `orElse`           | `orElseGet`              |
|--------------------|--------------------------|
| Eager              | Lazy                     |
| Always evaluates   | Evaluates when needed    |
| May waste performance | Better performance    |

---

## 14. `orElseThrow()`

Throws exception if value absent.

### Example

```java
String value =
        optional.orElseThrow(
            () -> new RuntimeException("Value missing")
        );
```

### Real Project Usage

Very common in Spring Boot:

```java
User user = repository.findById(id)
        .orElseThrow(() ->
            new UserNotFoundException());
```

---

## 15. `map()` in Optional

> **VERY IMPORTANT.**

Transforms the contained value.

### Example

```java
Optional<String> optional =
        Optional.of("java");

Optional<String> upper =
        optional.map(String::toUpperCase);

System.out.println(upper.get());
```

Output:

```
JAVA
```

### Why Useful?

Avoids nested null checks.

**Without Optional:**

```java
if(user != null) {
    Address address = user.getAddress();

    if(address != null) {
        return address.getCity();
    }
}
```

Messy.

**Using Optional:**

```java
Optional<String> city =
        Optional.ofNullable(user)
                .map(User::getAddress)
                .map(Address::getCity);
```

Very clean.

---

## 16. `flatMap()`

Used when the method already returns `Optional`.

### Problem Without `flatMap`

`map()` returns `Optional<Optional<String>>` — nested Optional.

### `flatMap` Solves It

```java
Optional<User> user = Optional.of(new User());

Optional<String> name =
        user.flatMap(User::getName);
```

### `map` vs `flatMap`

| `map`                           | `flatMap`                    |
|---------------------------------|------------------------------|
| Wraps result                    | Flattens Optional            |
| `Optional<Optional<T>>` possible | Avoids nesting              |

---

## 17. `filter()`

Works like Stream `filter`.

### Example

```java
Optional<String> optional =
        Optional.of("Java");

optional.filter(s -> s.length() > 3)
        .ifPresent(System.out::println);
```

### If Condition Fails

Returns: `Optional.empty`

---

## 18. Complete Real Project Example

### Traditional Way

```java
User user = repository.findById(id);

if(user != null) {
    Address address = user.getAddress();

    if(address != null) {
        System.out.println(address.getCity());
    }
}
```

### Modern Optional Way

```java
repository.findById(id)
          .map(User::getAddress)
          .map(Address::getCity)
          .ifPresent(System.out::println);
```

> Cleaner. Readable. Null-safe.

---

## 19. Optional in Streams

### Example

```java
List<String> names =
        Arrays.asList("Java", null, "Spring");
```

### Safe Processing

```java
names.stream()
     .filter(Objects::nonNull)
     .forEach(System.out::println);
```

---

## 20. Optional Best Practices

> **MOST IMPORTANT SECTION.**

### ✅ Best Practice 1 — Use Optional as Return Type

**GOOD:**

```java
Optional<User> findById(int id)
```

**BAD:**

```java
User findById(int id)
```

Because it may return `null`.

### ❌ Best Practice 2 — DO NOT Use Optional for Fields

**BAD:**

```java
class User {
    Optional<String> name;
}
```

Not recommended.

### ❌ Best Practice 3 — DO NOT Use Optional in Setters

**BAD:**

```java
setName(Optional<String> name)
```

### ✅ Best Practice 4 — Avoid `get()`

Prefer:

- `orElse`
- `ifPresent`
- `orElseThrow`

### ✅ Best Practice 5 — Use `ofNullable` When Uncertain

### ❌ Best Practice 6 — Do NOT Overuse Optional

**Bad:**

```java
Optional<Integer> a = Optional.of(10);
Optional<Integer> b = Optional.of(20);
```

For simple primitive values — unnecessary.

---

## 21. Optional in Spring Boot

> **VERY IMPORTANT.**

Spring Data JPA commonly uses `Optional`.

### Example

```java
Optional<User> user =
        repository.findById(1);
```

### Common Pattern

```java
User user =
        repository.findById(id)
                  .orElseThrow(
                      () -> new RuntimeException("User not found")
                  );
```

---

## 22. Common Interview Questions

### Q1: Why Optional Introduced?

To reduce `NullPointerException` and improve null handling.

### Q2: Difference Between `of()` and `ofNullable()`

| `of`               | `ofNullable`            |
|--------------------|-------------------------|
| null not allowed   | null allowed            |
| throws NPE         | returns empty           |

### Q3: Difference Between `orElse` and `orElseGet`

| `orElse`           | `orElseGet`              |
|--------------------|--------------------------|
| eager              | lazy                     |
| always executes    | executes only if needed  |

### Q4: Is Optional Serializable?

**No.**

### Q5: Can Optional Be Null?

Technically yes, but should **NEVER** be.

**Bad:**

```java
Optional<String> optional = null;
```

> Destroys the purpose of Optional.

---

## 23. Internal Mental Model

Think of `Optional` as:

> **A box that may contain a value.**

### Example

```java
Optional<String> name =
        Optional.of("Java");
```

Box contains: `Java`

### Empty Optional

```java
Optional.empty()
```

Empty box.

---

## 24. Most Important Methods Summary

| Method          | Purpose                   |
|-----------------|---------------------------|
| `of`            | non-null value            |
| `ofNullable`    | null/non-null             |
| `empty`         | empty optional            |
| `isPresent`     | check value               |
| `ifPresent`     | execute if exists         |
| `get`           | get value                 |
| `orElse`        | default value             |
| `orElseGet`     | lazy default              |
| `orElseThrow`   | throw exception           |
| `map`           | transform                 |
| `flatMap`       | flatten                   |
| `filter`        | condition                 |

---

## 25. Final Recommended Usage Pattern

> **MOST COMMON REAL PROJECT STYLE.**

```java
User user = repository.findById(id)
        .orElseThrow(() ->
            new RuntimeException("User not found"));
```

### Another Common Pattern

```java
repository.findById(id)
          .map(User::getName)
          .ifPresent(System.out::println);
```

---

## Final Mental Model

> `Optional` helps avoid direct null handling.

---

## One-Line Summary

> **Optional is a container object used to represent presence or absence of value safely.**
