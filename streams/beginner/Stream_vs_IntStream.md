# Stream\<Integer\> vs IntStream in Java

## The Problem

`Arrays.stream(nums)` works because it returns an `IntStream`, and `IntStream` has an `average()` method:

```java
int[] nums = {1, 2, 3, 4, 5, 6, 7};

Arrays.stream(nums)   // IntStream
      .average();     // ✅ available in IntStream
```

But with a `List<Integer>`:

```java
List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7);

list.stream()  // returns Stream<Integer>, NOT IntStream
```

`Stream<Integer>` does **not** have an `average()` method. That is why this **fails**:

```java
list.stream().average()  // ❌ Compile error
```

---

## Why Java Separates Them

### 1. Object Streams
```
Stream<Integer>
Stream<String>
Stream<Employee>
```
These work with **objects**.

### 2. Primitive Streams
```
IntStream
LongStream
DoubleStream
```
These are **optimized for primitive numbers** and contain extra methods like:
- `sum()`
- `average()`
- `max()`
- `min()`

---

## Correct Way for `List<Integer>`

Convert `Stream<Integer>` into `IntStream` using `mapToInt()`:

```java
List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7);

double average = list.stream()
                     .mapToInt(Integer::intValue)
                     .average()
                     .orElse(-1);

System.out.println(average);  // Output: 4.0
```

---

## Flow of Conversion

```
List<Integer>
      ↓  stream()
Stream<Integer>
      ↓  mapToInt()
IntStream
      ↓  average()
OptionalDouble
```

---

## Why `Arrays.stream(int[])` Directly Gives `IntStream`

Because the array is of **primitive type** `int[]`, Java knows it should create an `IntStream`:

```java
Arrays.stream(nums)
// internally becomes something like:
IntStream.of(nums)
```

---

## Interview Concept: `Stream<Integer>` vs `IntStream`

| Feature              | `Stream<Integer>`   | `IntStream`       |
|----------------------|---------------------|-------------------|
| Stores               | `Integer` objects   | primitive `int`   |
| `average()`          | ❌ No               | ✅ Yes            |
| `sum()`              | ❌ No               | ✅ Yes            |
| Boxing / Unboxing    | Required            | Not required      |
| Performance          | Slower              | Faster            |

---

## Alternative Way

```java
double avg = list.stream()
                 .collect(Collectors.averagingInt(Integer::intValue));
```

> But `mapToInt().average()` is more common and efficient.

---

## Method Reference vs Lambda in `mapToInt()`

The method reference:
```java
.mapToInt(Integer::intValue)
```
can be replaced with a lambda:
```java
.mapToInt(x -> x.intValue())
```

### Why Both Are the Same

| Style | Code | Meaning |
|-------|------|---------|
| Method Reference | `Integer::intValue` | "For every `Integer` object, call `intValue()`" |
| Lambda | `x -> x.intValue()` | Exactly the same |

### Even Shorter — Auto-Unboxing

Java automatically unboxes `Integer` to `int`, so this also works:

```java
.mapToInt(x -> x)
```

```java
double average = list.stream()
                     .mapToInt(x -> x)
                     .average()
                     .orElse(-1);
```

Java internally converts `Integer → int` using **auto-unboxing**.

---

## General Rule

> `ClassName::methodName` is shorthand for `x -> x.methodName()`

| Method Reference      | Lambda Equivalent             |
|-----------------------|-------------------------------|
| `String::length`      | `s -> s.length()`             |
| `Integer::intValue`   | `x -> x.intValue()`           |
| `System.out::println` | `x -> System.out.println(x)`  |
