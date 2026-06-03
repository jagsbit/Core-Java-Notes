# Interface vs Abstract Class in Java

---

## The Classic Analogy

> **Interface** = A contract (*"what you must do"*)
> **Abstract Class** = A partial blueprint (*"here's some base behavior, you finish the rest"*)

---

## 1. Core Differences

| Feature | Interface | Abstract Class |
|---------|-----------|---------------|
| Keyword | `implements` | `extends` |
| Multiple inheritance | ✅ A class can implement many | ❌ Only one abstract class |
| Constructor | ❌ Not allowed | ✅ Allowed |
| Instance variables | ❌ Only `public static final` (constants) | ✅ Any type of fields |
| Access modifiers on methods | Only `public` (implicitly) | Any (`private`, `protected`, `public`) |
| State (fields) | ❌ No state | ✅ Can hold state |

---

## 2. Before Java 8 — The Clear-Cut World

**Interface** — 100% abstract, no implementation at all:

```java
interface Flyable {
    void fly(); // purely a contract, no body
}
```

**Abstract Class** — Mix of abstract + concrete methods:

```java
abstract class Animal {
    String name; // can have state

    Animal(String name) { // can have constructor
        this.name = name;
    }

    abstract void makeSound(); // subclass MUST implement this

    void breathe() { // concrete method — shared behavior
        System.out.println("Inhale... Exhale...");
    }
}
```

---

## 3. Java 8 Changed the Game — `default` & `static` in Interfaces

Java 8 introduced **default methods** and **static methods** in interfaces. This was done mainly to evolve the Java Collections API without breaking existing code.

```java
interface Flyable {
    void fly(); // still abstract

    // ✅ Java 8: default method — has a body!
    default void land() {
        System.out.println("Landing safely...");
    }

    // ✅ Java 8: static utility method
    static void checkAltitude() {
        System.out.println("Checking altitude...");
    }
}
```

> 💡 *"So now interfaces can have method bodies... aren't they the same as abstract classes?"*
> **No** — and here's why they're still different.

---

## 4. Remaining Key Differences (Post Java 8)

### Difference #1 — State (the biggest one)

```java
interface Engine {
    int horsepower = 100; // implicitly public static final — it's a CONSTANT
    // int rpm; ❌ You cannot have instance variables
}

abstract class Engine {
    int horsepower;         // ✅ true instance variable — each object has its own
    private String fuelType; // ✅ can be private too
}
```

### Difference #2 — Constructors

```java
abstract class Vehicle {
    String brand;

    Vehicle(String brand) { // ✅ constructor to initialize state
        this.brand = brand;
    }
}

// interface Vehicle {
//     ❌ No constructors — because no instance state to initialize
// }
```

### Difference #3 — Multiple Inheritance

```java
// ✅ A class can implement multiple interfaces
class FlyingCar implements Flyable, Drivable, Electric {
    // ...
}

// ❌ A class can only extend ONE abstract class
class FlyingCar extends Vehicle {
    // that's it, no more
}
```

### Difference #4 — `default` Method Conflicts Must Be Resolved

```java
interface A {
    default void hello() { System.out.println("Hello from A"); }
}

interface B {
    default void hello() { System.out.println("Hello from B"); }
}

// ⚠️ Compiler forces you to resolve the conflict
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello(); // you explicitly choose which one
    }
}
```

> Abstract classes don't have this problem since you extend only one.

---

## 5. Design-Level Thinking — The Real Difference

> **The question to ask yourself:**
> *"Am I modeling WHAT something IS, or WHAT something CAN DO?"*

---

### Abstract Class → Models **"IS-A"** Relationship

Things that share **identity, origin, and nature**.

```
Animal
  ├── Dog
  ├── Cat
  └── Horse
```

A Dog **IS AN** Animal. They share real biological DNA — they all have a heart, they all breathe, they all have a name.

```java
abstract class Animal {
    String name;       // shared state — every animal HAS a name
    int heartRate;     // shared state — every animal HAS a heart rate

    void breathe() {   // shared behavior — every animal breathes the SAME way
        System.out.println("Inhale... Exhale...");
    }

    abstract void makeSound(); // but each animal sounds different
}
```

---

### Interface → Models **"CAN-DO"** / Capability Relationship

Things that share **behavior**, but have nothing else in common.

```
Things that can fly:
  ├── Bird
  ├── Airplane
  ├── Drone
  └── Superman
```

A Bird, Airplane, Drone, and Superman can all fly — but they share **zero identity**. A Bird is not an Airplane. The **only thing they share is a capability**.

```java
interface Flyable {
    void fly(); // just the capability contract
}
```

---

## 6. Real-World Design Scenario — Payment System

You have: `CreditCardPayment`, `UPIPayment`, `NetBankingPayment`, `CryptoPayment`

They all **CAN** process a payment — but a CreditCard is not a UPI. They have completely different internal logic.

→ **Use Interface**

```java
interface PaymentProcessor {
    void processPayment(double amount);
    void refund(double amount);
}
```

But inside your system: `HDFCCreditCard`, `ICICICreditCard`, `SBICreditCard`

These three **ARE** credit cards. They share real state — card number, CVV, expiry. Card validation works the same for all of them.

→ **Use Abstract Class**

```java
abstract class CreditCard implements PaymentProcessor {
    String cardNumber;     // shared state
    String cvv;
    Date expiry;

    boolean validateCard() {   // shared logic — same for all credit cards
        // Luhn algorithm
    }

    abstract String getBankName(); // each card has its own bank
}
```

### The Design Structure That Scales

```
         <<interface>>
         PaymentProcessor
               |
    ┌──────────┼──────────┐
    │          │          │
CreditCard   UPI     NetBanking
(abstract)  (class)   (class)
    │
    ├── HDFCCreditCard
    ├── ICICICreditCard
    └── SBICreditCard
```

---

## 7. What Does "Holding State" Mean?

**State** = data that belongs to an object instance — data that can **differ from object to object** and **can change over time**.

```java
abstract class Animal {
    String name;    // STATE
    int age;        // STATE
    int heartRate;  // STATE
}

Animal dog = new Dog("Tommy", 3, 80);
Animal cat = new Cat("Whiskers", 5, 120);
// dog.name = "Tommy" | cat.name = "Whiskers" ✅ each object has its own copy
```

### Why Interface Cannot Hold State

```java
interface Flyable {
    int speed = 100; // looks like a variable...
}
```

The compiler secretly treats this as:

```java
interface Flyable {
    public static final int speed = 100; // what it ACTUALLY is
}
```

Three keywords that **kill state**:

| Keyword | Effect |
|---------|--------|
| `static` | Belongs to the interface itself, not any object |
| `final` | Can never be changed |
| `public` | Everyone shares the same one copy |

```java
// Interface — NOT state
Flyable bird = new Bird();
Flyable plane = new Plane();
// bird.speed = 100, plane.speed = 100 ❌ always same value = CONSTANT, not state

// Abstract class — real state
Animal dog = new Dog("Tommy");
Animal cat = new Cat("Whiskers");
// dog.name = "Tommy", cat.name = "Whiskers" ✅ different values = STATE
```

> **One Line:** State = each object owns its own copy of data that can vary and change. Interface fields are shared, fixed constants — that's the opposite of state.

---

## 8. When to Use What

| Situation | Use |
|-----------|-----|
| Defining a contract across **unrelated** classes | Interface |
| Sharing common **code/state** among closely related classes | Abstract Class |
| Need **multiple inheritance** of behavior | Interface (with `default` methods) |
| Need to manage **object state** across subclasses | Abstract Class |
| Building a **plugin/API** others will implement | Interface |
| Building a **base class** with template logic | Abstract Class |

### The Design Question Checklist

| Question | Answer |
|----------|--------|
| Do these classes share the same **nature/identity**? | Abstract Class |
| Do these classes just share a **capability/behavior**? | Interface |
| Will new, **unrelated** classes need this behavior later? | Interface |
| Is there **shared state** that all subclasses must have? | Abstract Class |
| Are you defining **what something IS**? | Abstract Class |
| Are you defining **what something CAN DO**? | Interface |

---

## 9. The Perfect Interview Answer

### Layer 1 — Open with design philosophy *(first 30 seconds)*

> *"At the design level, the core difference is the type of relationship they model. Abstract class models an **IS-A** relationship — meaning shared identity and nature. Interface models a **CAN-DO** relationship — meaning shared capability across unrelated classes."*

### Layer 2 — Give a crisp real example *(next 30 seconds)*

> *"For example, Dog and Cat both ARE Animals — so Animal makes sense as an abstract class because they share real state like `name` and `heartRate`, and real behavior like `breathe()`. But a Bird, Airplane, and Drone can all fly — they share zero identity, so `Flyable` should be an interface."*

### Layer 3 — Drop the Java 8 line to show depth *(last 15 seconds)*

> *"One thing worth mentioning — Java 8 introduced `default` and `static` methods in interfaces, which blurred the line slightly. But the fundamental design distinction still holds — interfaces cannot have instance variables or constructors, so they can never truly hold state the way an abstract class can. That's the boundary that never changed."*

**Then stop.** Let the interviewer ask a follow-up if they want to go deeper.

---

### ❌ What NOT to Say

| Weak Answer | Why It's Weak |
|-------------|--------------|
| *"Interface has no method body, abstract class can have"* | Outdated after Java 8, shows surface knowledge |
| *"You can implement multiple interfaces"* | True, but it's a syntax fact, not design thinking |
| Listing a table of differences | Sounds memorised, not understood |

---

## Summary

| | Abstract Class | Interface |
|-|---------------|-----------|
| **Models** | IS-A relationship | CAN-DO / capability |
| **State** | ✅ Can hold real instance state | ❌ Only constants |
| **Constructor** | ✅ Yes | ❌ No |
| **Multiple inheritance** | ❌ No | ✅ Yes |
| **Java 8+** | `default`/`static` not applicable | `default` and `static` methods added |

---

## 🎯 The One Line That Captures Everything

> **"Abstract class is about shared blood. Interface is about shared skill."**

A Dog and a Cat share blood — they're both Animals.
A Dog and an Airplane don't share blood — but they can both fly (hypothetically 😄).
