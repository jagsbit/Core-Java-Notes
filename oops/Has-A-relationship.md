# HAS-A Relationship and Composition in Java

These are very important Object-Oriented Programming (OOP) concepts.

---

## 1. HAS-A Relationship

A **HAS-A relationship** means:

> One class contains an object of another class.

**Example:**

> Car **HAS-A** Engine

because a car contains an engine.

### Example in Java

```java
class Engine {

    void start() {
        System.out.println("Engine Started");
    }
}

class Car {

    Engine engine = new Engine();

    void startCar() {
        engine.start();
        System.out.println("Car Started");
    }
}
```

### Explanation

Here:

```java
Engine engine = new Engine();
```

means:

> **Car HAS-A Engine**

because `Car` contains an `Engine` object.

---

### Types of HAS-A Relationship

There are mainly two types:

1. **Composition**
2. **Aggregation**

---

## 2. Composition

Composition is a **strong HAS-A relationship**.

### Definition

> Composition means one object **strongly owns** another object, and the contained object **cannot exist independently**.

### Example

> **House HAS-A Room**

- A room usually **cannot exist independently** without a house.
- If house is destroyed, rooms are also destroyed.
- This is **composition**.

### Java Example

```java
class Engine {

    Engine() {
        System.out.println("Engine Created");
    }
}

class Car {

    private Engine engine;

    Car() {
        engine = new Engine(); // Car creates the Engine
    }
}
```

### Why Is This Composition?

Because:

- `Car` **creates** the `Engine`
- `Engine` lifecycle **depends on** `Car`
- **Strong ownership**

### Key Characteristics of Composition

| Feature              | Composition     |
|----------------------|-----------------|
| Relationship         | Strong HAS-A    |
| Ownership            | Strong          |
| Lifecycle Dependency | Yes             |
| Reusability          | Less            |
| Example              | Car-Engine      |

---

## 3. Aggregation

Aggregation is a **weak HAS-A relationship**.

### Definition

> Aggregation means one object **uses** another object, but both can **exist independently**.

### Example

> **Student HAS-A Laptop**

- A laptop can exist even if student is deleted.
- So this is **aggregation**.

### Java Example

```java
class Laptop {

}

class Student {

    Laptop laptop;

    Student(Laptop laptop) {   // Laptop is passed from outside
        this.laptop = laptop;
    }
}
```

### Why Is This Aggregation?

Because:

- `Laptop` is **created outside**
- `Student` only **uses** it
- Both can **exist independently**

---

## Key Difference

| Composition                     | Aggregation                    |
|---------------------------------|--------------------------------|
| Strong relationship             | Weak relationship              |
| Object lifecycle dependent      | Independent lifecycle          |
| Object created **inside**       | Object **passed from outside** |
| Strong ownership                | Weak ownership                 |

---

## How Decorator Pattern Uses Composition

In the decorator code:

```java
public Coffee coffee;
```

Decorator contains another `Coffee` object.

So:

> **Decorator HAS-A Coffee**

**Example:**

```java
new SugarDecorator(
      new MilkDecorator(
             new SimpleCoffee()
      )
)
```

Internally:

- `SugarDecorator` **HAS-A** `MilkDecorator`
- `MilkDecorator` **HAS-A** `SimpleCoffee`

This chaining is possible because of **composition**.

---

## Why Composition Is Powerful

Composition gives:

1. **Flexibility** — Objects can be changed dynamically.
2. **Reusability** — Different objects can be combined.
3. **Runtime Behavior Changes** — Features can be added dynamically.

---

## Composition vs Inheritance

| Inheritance                | Composition              |
|----------------------------|--------------------------|
| IS-A relationship          | HAS-A relationship       |
| Tight coupling             | Loose coupling           |
| Less flexible              | More flexible            |
| Compile-time behavior      | Runtime behavior         |

### Example

**Inheritance**
> Dog **IS-A** Animal

**Composition**
> Car **HAS-A** Engine

---

## Important Interview Point

> Modern design patterns prefer:
>
> **Composition Over Inheritance**

because composition is **more flexible**.

> **Decorator pattern** is one of the best examples of this principle.

---

## Short Interview Definitions

### HAS-A Relationship

> HAS-A relationship means one class contains an object of another class.

### Composition

> Composition is a **strong HAS-A relationship** where one object strongly owns another object and **controls its lifecycle**.
