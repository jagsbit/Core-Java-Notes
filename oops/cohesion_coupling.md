# Cohesion vs Coupling in Java / OOP

These are important **software design concepts** used to measure the quality of a system.

| Concept | Describes |
|---------|-----------|
| **Cohesion** | Relationship **within** a class/module |
| **Coupling** | Relationship **between** classes/modules |

> ✅ Good software design aims for: **High Cohesion + Low Coupling**

---

## 1. Cohesion

### Definition

> Cohesion refers to **how closely related the functionalities inside a class/module are**.

A class is **highly cohesive** if all its methods and variables work toward **one specific purpose**.

---

### High Cohesion ✅

A class should do **only one job**.

```java
class Calculator {

    int add(int a, int b) {
        return a + b;
    }

    int subtract(int a, int b) {
        return a - b;
    }
}
```

This class only performs **calculation-related work** → **High Cohesion**

---

### Low Cohesion ❌

When a class performs **unrelated tasks**.

```java
class Employee {

    void calculateSalary() { }

    void printReport() { }

    void connectDatabase() { }

    void sendEmail() { }
}
```

This class does many unrelated things → **Low Cohesion**

---

### Why High Cohesion is Good

| Benefit | Description |
|---------|-------------|
| ✅ Easier maintenance | Changes affect only one area |
| ✅ Better readability | Clear purpose of the class |
| ✅ Easier testing | Focused, isolated logic |
| ✅ Reusable code | Single-purpose classes are easier to reuse |
| ✅ Fewer bugs | Less complexity, less chance of errors |

---

## 2. Coupling

### Definition

> Coupling refers to **how much one class depends on another class**.

---

### Tight Coupling ❌

When classes are **highly dependent** on each other.

```java
class Engine {
    void start() {
        System.out.println("Engine Started");
    }
}

class Car {
    Engine engine = new Engine(); // Car directly creates Engine

    void drive() {
        engine.start();
    }
}
```

- `Car` directly creates `Engine`
- If `Engine` changes, `Car` may also need changes
- This is **tight coupling**

---

### Loose Coupling ✅

Classes depend **less** on each other. Usually achieved using:

- **Interfaces**
- **Abstraction**
- **Dependency Injection**

```java
interface Engine {
    void start();
}

class PetrolEngine implements Engine {
    public void start() {
        System.out.println("Petrol Engine");
    }
}

class DieselEngine implements Engine {
    public void start() {
        System.out.println("Diesel Engine");
    }
}

class Car {

    Engine engine;

    Car(Engine engine) {       // Engine injected from outside
        this.engine = engine;
    }

    void drive() {
        engine.start();
    }
}
```

- `Car` depends on the **abstraction** (`Engine` interface), not a concrete class
- You can swap `PetrolEngine` for `DieselEngine` without changing `Car`
- This is **loose coupling**

---

### Why Loose Coupling is Good

| Benefit | Description |
|---------|-------------|
| ✅ Easier modification | Change one class without affecting others |
| ✅ Better flexibility | Swap implementations easily |
| ✅ Easier testing | Mock dependencies during unit tests |
| ✅ Easier maintenance | Less ripple effect from changes |
| ✅ Reusable code | Classes work independently |

---

## 3. Real-Life Analogy

### Cohesion

A **TV remote** that only controls TV functions:
- Volume
- Channel
- Power

→ **High Cohesion** ✅

If the same remote also cooks food, prints paper, and drives a car → **Low Cohesion** ❌

---

### Coupling

A TV remote that works **only with one specific TV model** → **Tight Coupling** ❌

A TV remote that works with **many TVs using standard signals** → **Loose Coupling** ✅

---

## 4. Cohesion vs Coupling — Comparison

| | Cohesion | Coupling |
|--|---------|---------|
| **Describes** | Relationship **within** a class | Relationship **between** classes |
| **Measures** | Focus/relatedness of a class | Dependency between classes |
| **Goal** | **High** cohesion is good | **Low** coupling is good |
| **Scope** | Internal quality | External dependency |
| **Related To** | Single Responsibility Principle | Dependency management |

---

## 5. Ideal OOP Design

```
High Cohesion  →  Each class has one clear responsibility
Low Coupling   →  Classes depend minimally on each other
```

> ⚠️ **High Cohesion + Tight Coupling** — possible but not ideal.
> ⚠️ **Low Cohesion + Loose Coupling** — also poor design; classes are poorly organized.

The sweet spot is always: **High Cohesion AND Low Coupling**.

---

## Summary

| Concept | Scope | Prefer | Meaning |
|---------|-------|--------|---------|
| **Cohesion** | Inside a class | **HIGH** | All methods/fields serve one purpose |
| **Coupling** | Between classes | **LOW** | Classes depend minimally on each other |

---

## 🎯 Key Interview Tips

- **Cohesion** = degree to which elements **inside a class** belong together.
- **Coupling** = degree of **dependency between classes/modules**.
- Use **interfaces and dependency injection** to achieve loose coupling.
- **Single Responsibility Principle (SRP)** directly promotes high cohesion.
- Loose coupling makes code easier to **unit test** (mock dependencies easily).
- Both concepts together form the foundation of **clean, maintainable OOP design**.
