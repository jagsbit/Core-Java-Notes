# Enum in Java

An **Enum (Enumeration)** in Java is a special data type used to store a **fixed set of constants**.

It is mainly used when we know all possible values in advance.

**Examples:**
- Days of the week
- Directions
- Traffic signals
- Months
- Status values

> Instead of using numbers or strings, enums provide **meaningful names**.

```java
enum TrafficLight {
    RED, GREEN, YELLOW
}
```

Here `RED`, `GREEN`, and `YELLOW` are **constants**.

---

## Why Use Enum?

**Without enum:**
```java
String light = "RED"; // Typing mistakes possible ("REDD"), no type safety
```

**With enum:**
```java
TrafficLight light = TrafficLight.RED; // Type safe, readable
```

| Without Enum | With Enum |
|-------------|----------|
| Typing mistakes possible | ✅ Type safety |
| No compile-time check | ✅ Better readability |
| Hard to maintain | ✅ Easy maintenance |
| Any value allowed | ✅ Fixed constants only |

---

## Syntax

```java
enum EnumName {
    CONSTANT1,
    CONSTANT2,
    CONSTANT3;
}
```

**Example:**
```java
enum Direction {
    EAST, WEST, NORTH, SOUTH;
}
```

---

## Basic Example

```java
enum TrafficLight {
    RED, GREEN, YELLOW;
}

public class Test {
    public static void main(String[] args) {

        TrafficLight t = TrafficLight.RED;
        System.out.println(t);
    }
}
```

**Output:**
```
RED
```

---

## Important Points About Enum

### 1. Enum is Internally a Class

Java internally treats enum as a class.

```java
enum Day {
    MONDAY, TUESDAY
}
```

Internally similar to:

```java
final class Day {
    public static final Day MONDAY = new Day();
    public static final Day TUESDAY = new Day();
}
```

### 2. Enum Constants are Objects

Each constant is an **object** of the enum type.

```java
Day d = Day.MONDAY; // MONDAY is an object
```

### 3. Enum Constants are Automatically

- `public`
- `static`
- `final`

So we can access them directly using the enum name:
```java
Day.MONDAY
```

### 4. Enum Cannot Extend Another Class

Enum already extends Java's built-in `Enum` class, so multiple inheritance is not possible.

```java
// ❌ Invalid
enum A extends Test { }

// ✅ Valid — enum CAN implement interfaces
enum A implements SomeInterface { }
```

---

## Declaration of Enum

### 1. Outside the Class ✅

```java
enum Color {
    RED, GREEN, BLUE;
}

public class Test {
    public static void main(String[] args) {
        Color c = Color.RED;
        System.out.println(c);
    }
}
```

### 2. Inside the Class ✅

```java
public class Test {

    enum Color {
        RED, GREEN, BLUE;
    }

    public static void main(String[] args) {
        Color c = Color.GREEN;
        System.out.println(c);
    }
}
```

### 3. Inside a Method ❌

```java
// ❌ Invalid — Enum cannot be declared inside a method
public class Test {
    public static void main(String[] args) {
        enum Color { RED }
    }
}
```

---

## Enum in Switch Statement

Enums work very well with `switch`.

```java
enum Day {
    MONDAY, TUESDAY, WEDNESDAY
}

public class Test {

    public static void main(String[] args) {

        Day d = Day.MONDAY;

        switch(d) {
            case MONDAY:
                System.out.println("Start of week");
                break;
            case TUESDAY:
                System.out.println("Second day");
                break;
            default:
                System.out.println("Another day");
        }
    }
}
```

**Output:**
```
Start of week
```

---

## Enum with Methods

Enums can contain methods just like classes.

```java
enum Laptop {

    HP, DELL, LENOVO;

    public void display() {
        System.out.println("Laptop Brand: " + this);
    }
}

public class Test {

    public static void main(String[] args) {
        Laptop l = Laptop.HP;
        l.display();
    }
}
```

**Output:**
```
Laptop Brand: HP
```

---

## Enum with Constructor

Enums can also have constructors.

```java
enum Mobile {

    APPLE,
    SAMSUNG,
    REALME;

    Mobile() {
        System.out.println("Constructor Called");
    }
}

public class Test {

    public static void main(String[] args) {
        Mobile m = Mobile.APPLE;
    }
}
```

**Output:**
```
Constructor Called
Constructor Called
Constructor Called
```

> 📝 The constructor is called **for all constants** when the enum is loaded. That is why the constructor executes **3 times**.

---

## Enum with Variables and Constructor

```java
enum Pizza {

    SMALL(100),
    MEDIUM(200),
    LARGE(300);

    int price;

    Pizza(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

public class Test {

    public static void main(String[] args) {
        Pizza p = Pizza.MEDIUM;
        System.out.println(p.getPrice());
    }
}
```

**Output:**
```
200
```

---

## Enum with Abstract Method

Each enum constant can provide its **own implementation**.

```java
enum Operation {

    ADD {
        public int calculate(int a, int b) { return a + b; }
    },
    SUBTRACT {
        public int calculate(int a, int b) { return a - b; }
    };

    public abstract int calculate(int a, int b);
}

public class Test {

    public static void main(String[] args) {
        System.out.println(Operation.ADD.calculate(10, 5));
        System.out.println(Operation.SUBTRACT.calculate(10, 5));
    }
}
```

**Output:**
```
15
5
```

---

## Iterating Enum using `values()`

`values()` returns all enum constants in array form.

```java
enum Day {
    MONDAY, TUESDAY, WEDNESDAY
}

public class Test {

    public static void main(String[] args) {
        for (Day d : Day.values()) {
            System.out.println(d);
        }
    }
}
```

**Output:**
```
MONDAY
TUESDAY
WEDNESDAY
```

---

## Important Built-in Methods of Enum

| Method | Description |
|--------|-------------|
| `values()` | Returns all constants as an array |
| `ordinal()` | Returns the index position (0-based) |
| `valueOf()` | Converts a String to enum constant |
| `name()` | Returns the constant name as a String |

### Example of `ordinal()`

```java
enum Day { MONDAY, TUESDAY, WEDNESDAY }

public class Test {
    public static void main(String[] args) {
        System.out.println(Day.MONDAY.ordinal());    // 0
        System.out.println(Day.WEDNESDAY.ordinal()); // 2
    }
}
```

### Example of `valueOf()`

```java
enum Day { MONDAY, TUESDAY }

public class Test {
    public static void main(String[] args) {
        Day d = Day.valueOf("MONDAY");
        System.out.println(d); // MONDAY
    }
}
```

---

## Enum Implementing Interface

```java
interface Print {
    void display();
}

enum Laptop implements Print {

    HP, DELL;

    public void display() {
        System.out.println("Display Method");
    }
}

public class Test {
    public static void main(String[] args) {
        Laptop.HP.display();
    }
}
```

---

## Real-Life Example: Order Status

```java
enum OrderStatus {
    PLACED, SHIPPED, DELIVERED, CANCELLED
}

public class Test {

    public static void main(String[] args) {

        OrderStatus status = OrderStatus.SHIPPED;

        if (status == OrderStatus.SHIPPED) {
            System.out.println("Order is shipped");
        }
    }
}
```

---

## Advantages & Limitations

### ✅ Advantages

| Advantage | Explanation |
|-----------|------------|
| **Type Safety** | Invalid values not allowed |
| **Readability** | Easy to understand |
| **Fixed Constants** | Prevents unwanted values |
| **Switch Support** | Works great in switch statements |
| **Maintainability** | Easy to update constants |

### ❌ Limitations

| Limitation | Explanation |
|-----------|------------|
| **Cannot extend a class** | Enum already extends `java.lang.Enum` |
| **Fixed constants only** | Cannot create new constants dynamically |

---

## Enum vs Class

| Enum | Class |
|------|-------|
| Fixed constants | Objects can be created dynamically |
| Used for constants | Used for general purpose |
| Cannot extend a class | Can extend a class |
| Constants are predefined | Objects created at runtime |

---

## Interview Questions

| Question | Answer |
|----------|--------|
| What is enum in Java? | A special data type for a fixed set of constants |
| Can enum have constructors? | ✅ Yes |
| Can enum extend a class? | ❌ No — already extends `java.lang.Enum` |
| Can enum implement interfaces? | ✅ Yes |
| Are enum constants objects? | ✅ Yes, each constant is an object |
| Can we use `new` to create enum? | ❌ No — `Day d = new Day();` is invalid |

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Enum** | Represents a fixed set of constants |
| **Type Safety** | Prevents invalid values at compile time |
| **Constants** | Automatically `public`, `static`, `final` |
| **Internals** | Treated as a class by JVM |
| **Features** | Can have variables, constructors, methods, abstract methods |
| **Restriction** | Cannot extend another class; cannot be declared inside a method |
| **Switch** | Works seamlessly with `switch` statements |

---

## 🎯 Key Interview Tips

- Enum constants are **objects**, not just names.
- Enum **implicitly extends** `java.lang.Enum` — so it cannot extend any other class.
- Enum **can implement** interfaces.
- The enum constructor is called **once per constant** when the class is loaded.
- Use `values()` for iteration, `ordinal()` for index, `valueOf()` for string-to-enum conversion.
- Enum **cannot** be declared inside a method.
