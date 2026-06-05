# Java Stream API (Java 8+) — Complete Guide for Junior Developers

The Java Stream API is one of the **MOST IMPORTANT** features introduced in Java 8.

If you work in:

- Spring Boot
- Backend Development
- Microservices
- Data Processing
- Collections Handling
- Modern Java Projects

you will use Streams almost **every day**.

A junior developer **MUST** understand:

- what streams are
- why they were introduced
- how they work internally
- intermediate operations
- terminal operations
- lazy evaluation
- map/filter/reduce
- stream pipelines
- functional programming concepts
- performance basics
- common mistakes

---

## 1. Why Stream API Was Introduced?

Before Java 8, collection processing was verbose.

**Example:**

Suppose you want:

- filter even numbers
- square them
- print result

### Before Java 8

```java
List<Integer> list = Arrays.asList(1,2,3,4,5,6);

List<Integer> result = new ArrayList<>();

for(Integer num : list) {
    if(num % 2 == 0) {
        result.add(num * num);
    }
}

for(Integer n : result) {
    System.out.println(n);
}
```

Too much boilerplate code.

### Java 8 Stream Version

```java
list.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .forEach(System.out::println);
```

> Cleaner. Readable. Declarative.

---

## 2. What is Stream?

> **VERY IMPORTANT.**

A stream is:

> a sequence of elements supporting functional-style operations

OR simpler:

> **Stream is a pipeline used to process data.**

### Important Point

> A stream **DOES NOT** store data.

- **Collections** store data.
- **Streams** process data.

### Example

```java
List<Integer> list = Arrays.asList(1,2,3,4);
```

`list` stores: `1, 2, 3, 4`

But:

```java
list.stream()
```

creates a **processing pipeline** over the data.

### Real Life Analogy

Imagine a water pipeline:

```
Water Source -> Filter -> Transform -> Output
```

Same in Stream:

```
Collection -> filter -> map -> collect
```

---

## 3. Stream Characteristics

### 1. Does Not Store Data

Works on source data.

### 2. Functional Style

Uses:

- lambda expressions
- functional interfaces

### 3. Lazy Processing

> Operations execute only when needed. **VERY IMPORTANT.**

### 4. Can Be Parallel

Supports multithreading easily.

### 5. Improves Readability

Less boilerplate code.

---

## 4. How to Create Streams

### From Collection

```java
List<Integer> list = Arrays.asList(1,2,3);

Stream<Integer> stream = list.stream();
```

### From Array

```java
int[] arr = {1,2,3};

IntStream stream = Arrays.stream(arr);
```

### Using `Stream.of()`

```java
Stream<String> stream =
        Stream.of("Java", "Spring", "React");
```

### Infinite Stream

```java
Stream<Integer> stream =
        Stream.iterate(1, n -> n + 1);
```

### Generate Stream

```java
Stream<Double> randoms =
        Stream.generate(Math::random);
```

---

## 5. Stream Pipeline

> **VERY IMPORTANT.**

A stream pipeline contains:

```
Source -> Intermediate Operations -> Terminal Operation
```

### Example

```java
list.stream()
    .filter(x -> x % 2 == 0)
    .map(x -> x * x)
    .forEach(System.out::println);
```

### Breakdown

| Part                    | Code                    |
|-------------------------|-------------------------|
| Source                  | `list.stream()`         |
| Intermediate Operations | `.filter()`, `.map()`   |
| Terminal Operation      | `.forEach()`            |

> Without terminal operation: **nothing executes.**

---

## 6. Intermediate Operations

Intermediate operations:

- return stream
- are **lazy**
- build pipeline

### Common Intermediate Operations

| Operation  | Purpose                     |
|------------|-----------------------------|
| `filter`   | condition                   |
| `map`      | transform                   |
| `sorted`   | sorting                     |
| `distinct` | remove duplicates           |
| `limit`    | restrict elements           |
| `skip`     | skip elements               |
| `peek`     | debugging                   |
| `flatMap`  | flatten nested structures   |

---

## 7. `filter()`

Used for **condition checking**.

Uses: `Predicate`

### Example

```java
List<Integer> list =
        Arrays.asList(1,2,3,4,5,6);

list.stream()
    .filter(n -> n % 2 == 0)
    .forEach(System.out::println);
```

Output:

```
2
4
6
```

### Internally

Each element checks condition.

- If **true** → passes forward.
- If **false** → discarded.

---

## 8. `map()`

Used for **transformation**.

Uses: `Function`

### Example

```java
list.stream()
    .map(n -> n * n)
    .forEach(System.out::println);
```

Output:

```
1
4
9
16
25
36
```

### Real Project Use

Used for:

- DTO conversion
- entity transformation
- formatting

Example:

```java
.map(emp -> emp.getName())
```

### `filter` + `map` Together

> **MOST COMMON INTERVIEW EXAMPLE.**

```java
list.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .forEach(System.out::println);
```

### Flow

```
1 -> rejected
2 -> accepted -> 4
3 -> rejected
4 -> accepted -> 16
```

---

## 9. `sorted()`

Used for **sorting**.

### Natural Sorting

```java
list.stream()
    .sorted()
    .forEach(System.out::println);
```

### Custom Sorting

```java
list.stream()
    .sorted((a,b) -> b - a)
    .forEach(System.out::println);
```

Descending order.

### String Sorting

```java
names.stream()
     .sorted()
     .forEach(System.out::println);
```

---

## 10. `distinct()`

Removes **duplicates**.

### Example

```java
List<Integer> list =
        Arrays.asList(1,2,2,3,3,4);

list.stream()
    .distinct()
    .forEach(System.out::println);
```

Output:

```
1
2
3
4
```

---

## 11. `limit()`

Restricts number of elements.

### Example

```java
Stream.iterate(1, n -> n + 1)
      .limit(5)
      .forEach(System.out::println);
```

Output:

```
1
2
3
4
5
```

---

## 12. `skip()`

Skips elements.

### Example

```java
list.stream()
    .skip(2)
    .forEach(System.out::println);
```

---

## 13. `peek()`

Used mainly for **debugging**.

### Example

```java
list.stream()
    .peek(x -> System.out.println("Before: " + x))
    .map(x -> x * x)
    .forEach(System.out::println);
```

---

## 14. `flatMap()`

> **VERY IMPORTANT.**

Used to **flatten nested collections**.

### Problem Without `flatMap`

```java
List<List<Integer>> numbers = Arrays.asList(
    Arrays.asList(1,2),
    Arrays.asList(3,4)
);
```

Need: `1 2 3 4`

### Using `flatMap`

```java
numbers.stream()
       .flatMap(list -> list.stream())
       .forEach(System.out::println);
```

### `map` vs `flatMap`

> **IMPORTANT INTERVIEW QUESTION.**

| `map`                    | `flatMap`                     |
|--------------------------|-------------------------------|
| One-to-one mapping       | One-to-many flattening        |
| Returns transformed object | Returns flattened stream    |

---

## 15. Terminal Operations

Terminal operations:

- produce **final result**
- **trigger** stream execution
- **close** stream

### Common Terminal Operations

| Operation      | Purpose              |
|----------------|----------------------|
| `forEach`      | iterate              |
| `collect`      | collect result       |
| `count`        | count elements       |
| `reduce`       | aggregation          |
| `findFirst`    | first element        |
| `anyMatch`     | condition            |
| `allMatch`     | condition            |
| `noneMatch`    | condition            |
| `min`/`max`    | min/max              |
| `toArray`      | array conversion     |

---

## 16. `forEach()`

Used to **consume** elements.

Uses: `Consumer`

### Example

```java
list.stream()
    .forEach(System.out::println);
```

### Method Reference

```java
System.out::println
```

Equivalent to:

```java
x -> System.out.println(x)
```

---

## 17. `collect()`

> **VERY IMPORTANT.**

Used to **collect** stream result.

### Example

```java
List<Integer> result =
        list.stream()
            .filter(x -> x % 2 == 0)
            .collect(Collectors.toList());
```

### Common Collectors

| Collector           | Purpose                   |
|---------------------|---------------------------|
| `toList`            | list                      |
| `toSet`             | set                       |
| `joining`           | join strings              |
| `counting`          | count                     |
| `groupingBy`        | grouping                  |
| `partitioningBy`    | true/false grouping       |
| `mapping`           | transformation            |

### `joining()`

```java
List<String> names =
        Arrays.asList("Java", "Spring");

String result =
        names.stream()
             .collect(Collectors.joining(", "));
```

Output:

```
Java, Spring
```

---

## 18. `reduce()`

> **MOST IMPORTANT.**

Used for **aggregation**.

### Example

```java
List<Integer> list =
        Arrays.asList(1,2,3,4);

int sum = list.stream()
              .reduce(0, (a,b) -> a+b);

System.out.println(sum);
```

### Internally

```
0+1=1
1+2=3
3+3=6
6+4=10
```

### `reduce` for Maximum

```java
int max = list.stream()
              .reduce(Integer.MIN_VALUE,
                      (a,b) -> a > b ? a : b);
```

---

## 19. `count()`

```java
long count =
        list.stream()
            .filter(x -> x > 2)
            .count();
```

---

## 20. `findFirst()`

```java
Optional<Integer> result =
        list.stream()
            .filter(x -> x > 2)
            .findFirst();
```

---

## 21. Optional

Java 8 introduced `Optional` to avoid:

> `NullPointerException`

### Example

```java
Optional<String> name =
        Optional.of("Java");
```

### Common Methods

| Method       | Purpose                     |
|--------------|-----------------------------|
| `isPresent`  | check value                 |
| `get`        | get value                   |
| `orElse`     | default value               |
| `ifPresent`  | execute if value exists     |

### Example

```java
Optional<String> name =
        Optional.ofNullable(null);

System.out.println(
        name.orElse("Default")
);
```

Output:

```
Default
```

---

## 22. `anyMatch()`, `allMatch()`, `noneMatch()`

### `anyMatch`

At least one matches.

```java
boolean result =
        list.stream()
            .anyMatch(x -> x > 5);
```

### `allMatch`

All must match.

```java
list.stream()
    .allMatch(x -> x > 0);
```

### `noneMatch`

None should match.

```java
list.stream()
    .noneMatch(x -> x < 0);
```

---

## 23. Lazy Evaluation

> **MOST IMPORTANT CONCEPT.**

Intermediate operations are **lazy**.

They execute only when **terminal operation** comes.

### Example

```java
list.stream()
    .filter(x -> {
        System.out.println(x);
        return x % 2 == 0;
    });
```

> Nothing prints.

**Why?** No terminal operation.

### Now

```java
list.stream()
    .filter(x -> {
        System.out.println(x);
        return x % 2 == 0;
    })
    .count();
```

> Now executes.

---

## 24. Stream Internal Processing

> **VERY IMPORTANT.**

Streams use: **internal iteration**

instead of external iteration.

### External Iteration

```java
for(Integer x : list)
```

Developer controls iteration.

### Internal Iteration

```java
list.stream().forEach(...)
```

Stream API controls iteration.

---

## 25. Single Use Streams

> **IMPORTANT.**

A stream can be consumed **only once**.

### Wrong

```java
Stream<Integer> stream = list.stream();

stream.forEach(System.out::println);

stream.forEach(System.out::println);  // ❌
```

Exception:

```
stream has already been operated upon or closed
```

---

## 26. Parallel Streams

> **VERY IMPORTANT.**

Allows **parallel processing**.

### Example

```java
list.parallelStream()
    .forEach(System.out::println);
```

Uses multiple threads internally.

### When Useful?

Useful for:

- large datasets
- CPU intensive tasks

### When NOT Useful?

Not good for:

- small collections
- database calls
- shared mutable state

### Sequential vs Parallel

| Sequential         | Parallel                    |
|--------------------|-----------------------------|
| Single thread      | Multiple threads            |
| Predictable        | Faster for huge data        |
| Easier debugging   | Complex behavior            |

---

## 27. Method References

Java 8 feature closely related to streams.

### Example

Instead of:

```java
x -> System.out.println(x)
```

Use:

```java
System.out::println
```

### Types of Method References

| Type              | Example              |
|-------------------|----------------------|
| Static            | `Math::abs`          |
| Instance          | `obj::method`        |
| Class Instance    | `String::length`     |
| Constructor       | `ArrayList::new`     |

---

## 28. Common Real Project Examples

### Example 1: Get Employee Names

```java
employees.stream()
         .map(Employee::getName)
         .forEach(System.out::println);
```

### Example 2: Filter High Salary Employees

```java
employees.stream()
         .filter(emp -> emp.getSalary() > 50000)
         .forEach(System.out::println);
```

### Example 3: Convert List to Map

```java
Map<Integer, String> map =
        employees.stream()
                 .collect(Collectors.toMap(
                     Employee::getId,
                     Employee::getName
                 ));
```

### Example 4: Group Employees by Department

```java
Map<String, List<Employee>> result =
        employees.stream()
                 .collect(Collectors.groupingBy(
                     Employee::getDepartment
                 ));
```

---

## 29. Stream vs Collection

> **IMPORTANT INTERVIEW QUESTION.**

| Collection          | Stream               |
|---------------------|----------------------|
| Stores data         | Processes data       |
| Eager               | Lazy                 |
| Reusable            | Single-use           |
| External iteration  | Internal iteration   |

---

## 30. Common Interview Questions

### Q1: Difference Between `map` and `flatMap`?

#### `map`

One object → one object

```java
.map(String::toUpperCase)
```

#### `flatMap`

Nested objects → flattened stream

```java
.flatMap(List::stream)
```

### Q2: Why Streams Are Lazy?

Improves **performance**.

Only needed operations execute.

### Q3: Can Streams Modify Original Collection?

**No.**

Streams are non-mutating unless explicitly modifying objects.

### Q4: Difference Between `forEach` and `peek`?

| `forEach`        | `peek`               |
|------------------|----------------------|
| Terminal         | Intermediate         |
| Final processing | Debugging            |

### Q5: Why Streams Cannot Be Reused?

Because once terminal operation executes:

> stream closes.

---

## 31. Important Collectors

### `groupingBy()`

```java
Collectors.groupingBy(Employee::getDepartment)
```

Groups data.

### `partitioningBy()`

Splits into: `true` / `false`

```java
Collectors.partitioningBy(x -> x % 2 == 0)
```

### `counting()`

```java
Collectors.counting()
```

### `averagingInt()`

```java
Collectors.averagingInt(Employee::getSalary)
```

### `summarizingInt()`

Provides:

- min
- max
- avg
- sum
- count

---

## 32. Best Practices

### 1. Keep Streams Readable

**Bad:** huge complex pipeline.

Break into **smaller methods**.

### 2. Avoid Side Effects

Avoid modifying shared variables.

**Bad:**

```java
int sum = 0;

list.stream().forEach(x -> sum += x);  // ❌
```

### 3. Prefer Method References

Readable.

### 4. Use Parallel Carefully

Not always faster.

---

## 33. Most Important Mental Model

### Stream Pipeline

```
Source
   ↓
Intermediate Operations
   ↓
Terminal Operation
```

### Example

```java
employees.stream()
         .filter(emp -> emp.getSalary() > 50000)
         .map(Employee::getName)
         .sorted()
         .collect(Collectors.toList());
```

### Flow

```
employees
   ↓
filter salary
   ↓
extract names
   ↓
sort names
   ↓
collect into list
```

---

## 34. Final Summary Every Junior Must Remember

### Streams Process Data

- **Collections** store data.
- **Streams** process data.

### Intermediate Operations Are Lazy

> Nothing executes until terminal operation.

### Most Used Operations

| Operation  | Purpose         |
|------------|-----------------|
| `filter`   | condition       |
| `map`      | transform       |
| `collect`  | collect result  |
| `forEach`  | iterate         |
| `reduce`   | aggregate       |
| `sorted`   | sorting         |

### Most Used Functional Interfaces

| Interface   | Used In   |
|-------------|-----------|
| `Predicate` | `filter`  |
| `Function`  | `map`     |
| `Consumer`  | `forEach` |

### Most Common Stream Pipeline

```java
list.stream()
    .filter(...)
    .map(...)
    .collect(...);
```

---

## One-Line Summary

> **Stream API allows functional-style, declarative processing of collections.**
