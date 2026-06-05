# Java 8+ Functional Programming Concepts

Before Java 8, Java was mostly:

- Object-Oriented Programming (OOP)
- Class-based programming
- Verbose code

Java 8 introduced Functional Programming features to make code:

- shorter
- cleaner
- more readable
- more expressive
- easier for collections processing
- better for multithreading

The **MOST IMPORTANT** Java 8 features for interviews and real projects are:

- Lambda Expressions
- Functional Interfaces
- Predefined Functional Interfaces
  - Predicate
  - Function
  - Consumer
  - Supplier
  - BiFunction
  - UnaryOperator
  - BinaryOperator

---

## 1. Lambda Expressions

### What is Lambda Expression?

A lambda expression is a short way of writing **anonymous functions**.

Anonymous means:

- no class
- no method name
- directly pass behavior as data

### Why Lambda Was Introduced?

Before Java 8, if you wanted to pass functionality:

You had to:

- create class
- implement interface
- override method

Very lengthy.

#### Before Java 8

Example using Runnable:

```java
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};
```

Huge code.

#### Java 8 Lambda Version

```java
Runnable r = () -> {
    System.out.println("Running");
};
```

Much shorter.

### Syntax of Lambda

```
(parameters) -> { body }
```

### Examples

#### Example 1

```java
() -> System.out.println("Hello");
```

No parameters.

#### Example 2

```java
(a, b) -> a + b
```

Two parameters returning sum.

#### Example 3

```java
name -> System.out.println(name)
```

Single parameter.

### Important Rules

#### Rule 1: Parameter Types Optional

```java
(int a, int b) -> a + b
```

OR

```java
(a, b) -> a + b
```

Compiler can infer type.

This is called: **Type Inference**

#### Rule 2: Curly Braces Optional for Single Statement

```java
x -> System.out.println(x)
```

#### Rule 3: `return` Keyword Optional

```java
(a, b) -> a + b
```

instead of:

```java
(a, b) -> {
   return a + b;
}
```

### Internally How Lambda Works?

> Important interview topic.

Lambda does **NOT** create anonymous inner class exactly.

Internally JVM uses:

- `invokedynamic`
- functional interface implementation dynamically

This makes lambdas more **memory efficient** than anonymous classes.

### Where Lambda Is Used?

Mostly with:

- Functional Interfaces
- Streams API
- Collections
- Multithreading
- Event handling

#### Example with Comparator

Before Java 8:

```java
Comparator<Integer> c = new Comparator<Integer>() {
    @Override
    public int compare(Integer a, Integer b) {
        return a - b;
    }
};
```

Java 8:

```java
Comparator<Integer> c = (a, b) -> a - b;
```

### Real Life Thinking

Suppose method needs some behavior:

Example:

- sorting logic
- filtering logic
- printing logic

Instead of creating classes repeatedly:

> we pass function directly using lambda.

---

## 2. Functional Interface

### What is Functional Interface?

A functional interface is an interface containing:

> **ONLY ONE ABSTRACT METHOD**

It may contain:

- multiple default methods
- multiple static methods

but only:

- **one abstract method**

### Syntax

```java
@FunctionalInterface
interface MyInterface {
    void show();
}
```

### Why Functional Interface Important?

Because:

> Lambda expressions work **ONLY** with functional interfaces.

### Example

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}
```

Lambda:

```java
Calculator c = (a, b) -> a + b;

System.out.println(c.add(10, 20));
```

Output:

```
30
```

### Why `@FunctionalInterface` Annotation?

Not mandatory but recommended.

Compiler checks:

> exactly one abstract method exists

### Invalid Functional Interface

```java
@FunctionalInterface
interface Test {
    void m1();
    void m2();
}
```

**Compilation error.**

Because: 2 abstract methods.

### Functional Interface Can Have Default Methods

```java
@FunctionalInterface
interface Demo {

    void show();

    default void display() {
        System.out.println("Default");
    }
}
```

**Valid.**

Because only one abstract method.

### Examples of Built-in Functional Interfaces

Java already provides many:

- Runnable
- Callable
- Comparator
- Predicate
- Function
- Consumer
- Supplier

### Why Functional Interfaces Are Powerful?

They enable:

- functional programming
- loose coupling
- reusable behavior
- stream processing

---

## 3. Predefined Functional Interfaces

Java 8 introduced many ready-made functional interfaces in:

```
java.util.function
```

Most important ones:

| Interface        | Purpose                    |
|------------------|----------------------------|
| `Predicate`      | Testing                    |
| `Function`       | Transformation             |
| `Consumer`       | Consuming                  |
| `Supplier`       | Supplying                  |
| `BiFunction`     | Two inputs                 |
| `UnaryOperator`  | Same type input/output     |
| `BinaryOperator` | Two same type inputs       |

> These are **EXTREMELY IMPORTANT**.

---

## 4. Predicate

### What is Predicate?

Predicate is used for:

> **condition checking / filtering**

It takes:

- one input
- returns **boolean**

### Definition

```java
Predicate<T>
```

Method:

```java
boolean test(T t)
```

### Example 1

```java
Predicate<Integer> p = x -> x > 10;

System.out.println(p.test(20));
System.out.println(p.test(5));
```

Output:

```
true
false
```

### Real World Use

Used in:

- filtering employees
- filtering products
- validation
- checking conditions

### Example 2

```java
Predicate<String> p = str -> str.length() > 5;

System.out.println(p.test("Java"));
System.out.println(p.test("SpringBoot"));
```

### Predicate with Collections

```java
List<Integer> list = Arrays.asList(10, 15, 20, 25);

Predicate<Integer> even = x -> x % 2 == 0;

for(Integer i : list) {
    if(even.test(i)) {
        System.out.println(i);
    }
}
```

### Predicate Chaining

> Very important.

#### `and()`

```java
Predicate<Integer> p1 = x -> x > 10;
Predicate<Integer> p2 = x -> x % 2 == 0;

Predicate<Integer> result = p1.and(p2);
```

Both conditions true.

#### `or()`

```java
p1.or(p2)
```

Either condition true.

#### `negate()`

Opposite condition.

```java
p1.negate()
```

### Real Example

```java
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> lengthGreaterThan3 = s -> s.length() > 3;

Predicate<String> finalPredicate =
        startsWithA.and(lengthGreaterThan3);
```

---

## 5. Function

### What is Function?

Function is used for:

> **transformation**

It:

- takes input
- returns output

### Definition

```java
Function<T, R>
```

- `T` → input
- `R` → return type

Method:

```java
R apply(T t)
```

### Example

```java
Function<Integer, Integer> square = x -> x * x;

System.out.println(square.apply(5));
```

Output:

```
25
```

### String Example

```java
Function<String, Integer> length = s -> s.length();

System.out.println(length.apply("Java"));
```

### Real World Use

Used for:

- DTO conversion
- mapping
- formatting
- object transformation

### Function Chaining

#### `andThen()`

First execute current function then next.

```java
Function<Integer, Integer> multiply = x -> x * 2;
Function<Integer, Integer> square = x -> x * x;

System.out.println(
    multiply.andThen(square).apply(5)
);
```

Step:

```
5 * 2 = 10
10 * 10 = 100
```

#### `compose()`

Reverse order.

```java
multiply.compose(square)
```

First square then multiply.

#### Identity Function

```java
Function.identity()
```

Returns same value.

---

## 6. Consumer

### What is Consumer?

Consumer is used when:

> you want input but **no return value**

Consumes data.

### Definition

```java
Consumer<T>
```

Method:

```java
void accept(T t)
```

### Example

```java
Consumer<String> c =
        name -> System.out.println(name);

c.accept("Rahul");
```

### Real World Usage

Used for:

- printing
- logging
- saving data
- sending notifications

### Example with List

```java
List<String> list =
        Arrays.asList("Java", "Spring", "React");

Consumer<String> print = item ->
        System.out.println(item);

list.forEach(print);
```

### Consumer Chaining

```java
Consumer<String> c1 = x -> System.out.println(x);
Consumer<String> c2 = x -> System.out.println(x.toUpperCase());

c1.andThen(c2).accept("java");
```

---

## 7. Supplier

### What is Supplier?

Supplier **supplies** data.

It:

- takes **NO input**
- returns output

### Definition

```java
Supplier<T>
```

Method:

```java
T get()
```

### Example

```java
Supplier<String> s =
        () -> "Hello";

System.out.println(s.get());
```

### Random Number Example

```java
Supplier<Integer> random =
        () -> (int)(Math.random() * 100);

System.out.println(random.get());
```

### Real World Use

Used for:

- lazy loading
- object creation
- random generation
- configuration values

---

## 8. BiFunction

### What is BiFunction?

Like Function but takes:

> **TWO inputs**

### Definition

```java
BiFunction<T, U, R>
```

- `T` → first input
- `U` → second input
- `R` → return type

Method:

```java
R apply(T t, U u)
```

### Example

```java
BiFunction<Integer, Integer, Integer> add =
        (a, b) -> a + b;

System.out.println(add.apply(10, 20));
```

### Real World Use

Used when operation needs:

- two values
- combining objects
- calculations

### Example

```java
BiFunction<String, String, String> fullName =
        (f, l) -> f + " " + l;

System.out.println(
        fullName.apply("John", "Doe"));
```

---

## 9. UnaryOperator

### What is UnaryOperator?

Special type of Function.

> Input and output type are **SAME**.

### Definition

```java
UnaryOperator<T>
```

Equivalent to:

```java
Function<T, T>
```

### Example

```java
UnaryOperator<Integer> square =
        x -> x * x;

System.out.println(square.apply(5));
```

### String Example

```java
UnaryOperator<String> upper =
        s -> s.toUpperCase();
```

### Why Use UnaryOperator?

Improves readability.

Clearly tells:

> same type input/output

---

## 10. BinaryOperator

### What is BinaryOperator?

Special type of BiFunction.

- takes **two same-type inputs**
- returns **same type**

### Definition

```java
BinaryOperator<T>
```

Equivalent to:

```java
BiFunction<T, T, T>
```

### Example

```java
BinaryOperator<Integer> add =
        (a, b) -> a + b;

System.out.println(add.apply(10, 20));
```

### Real World Usage

Used for:

- reduction
- aggregation
- combining values

### Example with Stream Reduce

```java
List<Integer> list =
        Arrays.asList(1,2,3,4);

BinaryOperator<Integer> sum =
        (a,b) -> a+b;

int result =
        list.stream()
            .reduce(sum)
            .get();

System.out.println(result);
```

---

## Complete Relationship Between Interfaces

```
            Function<T,R>
                 |
         -----------------
         |               |
  UnaryOperator<T>   BiFunction<T,U,R>
                           |
                    BinaryOperator<T>
```

### Most Important Difference

| Interface        | Input | Output      |
|------------------|-------|-------------|
| `Predicate`      | 1     | boolean     |
| `Function`       | 1     | value       |
| `Consumer`       | 1     | no output   |
| `Supplier`       | 0     | value       |
| `BiFunction`     | 2     | value       |
| `UnaryOperator`  | 1     | same type   |
| `BinaryOperator` | 2     | same type   |

---

## How They Are Used Together in Real Projects

Suppose you have `Employee` objects.

#### Predicate — Filter employees

```java
emp -> emp.getSalary() > 50000
```

#### Function — Convert employee to DTO

```java
emp -> new EmployeeDTO(emp.getName())
```

#### Consumer — Print employee

```java
emp -> System.out.println(emp)
```

#### Supplier — Create employee

```java
() -> new Employee()
```

---

## Very Important Interview Concepts

### 1. Can Lambda Work Without Functional Interface?

**No.**

Lambda requires functional interface target type.

### 2. Difference Between Anonymous Class and Lambda

| Anonymous Class                       | Lambda                        |
|---------------------------------------|-------------------------------|
| Creates separate class                | No separate class             |
| Uses `this` for anonymous object      | Uses `this` for outer class   |
| More memory                           | Less memory                   |
| Verbose                               | Short                         |

### 3. Why Functional Programming Useful?

Because it enables:

- declarative coding
- parallel processing
- cleaner code
- stream operations

### 4. Which Package Contains Functional Interfaces?

```
java.util.function
```

### 5. Functional Interface vs Normal Interface

| Functional Interface      | Normal Interface              |
|---------------------------|-------------------------------|
| One abstract method       | Multiple abstract methods     |
| Used for lambda           | General purpose               |

### 6. Why Predicate Returns Boolean?

Because it represents: **condition testing**.

### 7. Why Consumer Has Void Return Type?

Because its purpose is: **performing action**.

### 8. Why Supplier Has No Input?

Because it only **supplies/generates** data.

---

## Real Project Example

```java
List<String> names =
        Arrays.asList("Ram", "Shyam", "Aman", "Ajay");

Predicate<String> startsWithA =
        s -> s.startsWith("A");

Function<String, String> upper =
        s -> s.toUpperCase();

Consumer<String> print =
        s -> System.out.println(s);

names.stream()
     .filter(startsWithA)
     .map(upper)
     .forEach(print);
```

### Flow

```
filter  →  Predicate
map     →  Function
forEach →  Consumer
```

### Output

```
AMAN
AJAY
```

---

## What Junior Developers MUST Remember

### MOST IMPORTANT

> **Lambda = implementation of functional interface**

### Functional Interface

> Exactly **ONE** abstract method.

### Remember These 4 Core Interfaces

| Interface   | Purpose   |
|-------------|-----------|
| `Predicate` | test      |
| `Function`  | transform |
| `Consumer`  | consume   |
| `Supplier`  | supply    |

> These 4 are used **EVERYWHERE** in Java 8+.

### Stream API Relation

These interfaces are heavily used in:

- Stream API
- Spring Boot
- Collections
- Reactive programming
- CompletableFuture

---

## Final Mental Model

```
Predicate  -> checks condition
Function   -> transforms data
Consumer   -> uses data
Supplier   -> creates/provides data
```

---

## One-Line Summary

> **Lambda expression provides implementation of functional interface methods in a concise way.**
