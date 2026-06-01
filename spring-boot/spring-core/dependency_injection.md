# Interview Answer

Dependency Injection is a mechanism in which the dependencies required by an object are provided externally by the Spring IoC container instead of the object creating them manually using the `new` keyword.

Using DI helps achieve loose coupling because classes depend on abstractions rather than concrete implementations. It also improves maintainability, flexibility, and unit testing, since mock implementations can easily be injected during testing.

Spring mainly supports three types of dependency injection:

1. Constructor Injection
2. Setter Injection
3. Field Injection

Among them, constructor injection is the most recommended approach.

In constructor injection, dependencies are provided during object creation itself, ensuring that the object cannot be created without its required dependencies. If a required bean is missing, Spring throws a `NoSuchBeanDefinitionException` during application startup, which supports **fail-fast behavior**.

Constructor injection also allows dependencies to be declared as `final`, meaning their references cannot be changed after object creation. This makes the object more stable and immutable.

It is also better for unit testing because dependencies can be passed directly through the constructor using mock objects. In field injection, private dependencies are injected using reflection, making testing more difficult and reducing encapsulation.

Setter injection and field injection can allow the object to exist in a partially initialized state because the object is created first and dependencies are injected later.

Therefore, constructor injection is preferred in modern Spring Boot applications because it provides better **immutability**, **null safety**, **maintainability**, and **testability**.

---

# What is Dependency Injection (DI)?

Dependency Injection is a design pattern used to achieve **IoC (Inversion of Control)**.

It means:

> Instead of an object creating its dependencies itself, the dependencies are **provided (injected)** by the Spring IoC container.

---

## Simple Example Without DI

Suppose we have:

- `Car`
- `Engine`

**Without DI:**

```java
class Engine {
}

class Car {

    private Engine engine;

    public Car() {
        engine = new Engine();
    }
}
```

### Problem Here

`Car` directly creates `Engine`.

This causes:

- Tight coupling
- Difficult testing
- Hard to replace implementation

**Example:**

If tomorrow:

```
PetrolEngine → DieselEngine
```

You must modify `Car`.

This violates:

- Open Closed Principle
- Loose coupling

---

## With Dependency Injection

```java
class Car {

    private Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }
}
```

Now:

- `Car` does not create `Engine`
- Someone else provides it

That **"someone else"** is the **Spring IoC container**.

---

## High-Level Understanding

DI says:

> Don't create dependencies yourself.
> Ask the container to provide them.

---

## Real Spring Example

```java
@Component
class Engine {
}
```

```java
@Component
class Car {

    private Engine engine;

    @Autowired
    public Car(Engine engine) {
        this.engine = engine;
    }
}
```

Spring automatically:

- Creates `Engine`
- Creates `Car`
- Injects `Engine` into `Car`

---

## Why Dependency Injection is Needed

### 1. Loose Coupling

Classes depend on abstraction, not implementation.

Example:

```java
interface PaymentService {
}
```

Now implementations can change easily.

### 2. Better Testability

Mock dependencies can be injected during testing.

Example:

```
MockPaymentService
```

instead of real payment service.

> Very important in unit testing.

### 3. Better Maintainability

- Dependency management becomes centralized.
- No manual object creation everywhere.

### 4. Better Flexibility

Easy to switch implementations.

Example:

```
MySQL → PostgreSQL
Stripe → Razorpay
```

without modifying business logic.

### 5. Reduced Boilerplate Code

Spring handles:

- Object creation
- Wiring
- Lifecycle management

---

## Relationship Between IoC and DI

Students commonly confuse this.

**IoC**

> A principle/concept.
> "Control should be handled by container."

**DI**

> A technique to achieve IoC.
> "Dependencies are injected externally."

---

## Types of Dependency Injection in Spring

Spring mainly supports:

1. Constructor Injection
2. Setter Injection
3. Field Injection

---

## 1. Constructor Injection

Dependencies are injected using the **constructor**.

### Example

```java
@Component
class Engine {
}
```

```java
@Component
class Car {

    private final Engine engine;

    @Autowired
    public Car(Engine engine) {
        this.engine = engine;
    }
}
```

### Internal Working

Spring:

1. Creates `Engine`
2. Calls constructor of `Car`
3. Passes `Engine` object

### Advantages

#### Immutable Dependencies

Dependency cannot change after object creation.

#### Mandatory Dependencies

Object cannot exist without required dependencies.

#### Better Unit Testing

Easy constructor-based mock injection.

#### Recommended by Spring Team

Most preferred approach.

### Senior-Level Best Practice

> Always prefer **Constructor Injection**, especially in Spring Boot applications.

### Why Modern Spring Often Omits `@Autowired`

Since **Spring 4.3**:

If a class has only one constructor:

```java
@Component
class Car {

    private final Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }
}
```

Spring automatically injects the dependency.

---

## 2. Setter Injection

Dependencies are injected using **setter methods**.

### Example

```java
@Component
class Car {

    private Engine engine;

    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
```

### Internal Working

Spring:

1. Creates object using default constructor
2. Calls setter method
3. Injects dependency

### Advantages

#### Optional Dependencies

Useful when dependency is optional.

#### More Flexible

Dependency can be changed later.

### Disadvantages

Object may exist in an incomplete state.

Example:

> `Car` created without `Engine` → Possible `NullPointerException`.

### When Used

Rare in modern Spring Boot applications.

Mostly used for:

- Optional dependencies
- Legacy systems

---

## 3. Field Injection

Dependencies injected **directly into fields**.

### Example

```java
@Component
class Car {

    @Autowired
    private Engine engine;
}
```

### Internal Working

Spring uses **reflection** to inject dependency directly.

### Advantages

- Less code.

### Disadvantages (Very Important)

#### Hard to Unit Test

Cannot inject mocks easily.

#### Breaks Encapsulation

Spring accesses private fields directly.

#### Hidden Dependencies

Dependencies not visible in constructor.

#### Difficult Immutability

Cannot make field `final`.

### Senior-Level Recommendation

> **Avoid Field Injection** in production-grade applications.
> Many companies discourage it.

---

## Comparison Table

| Feature | Constructor Injection | Setter Injection | Field Injection |
|---------|-----------------------|------------------|-----------------|
| Recommended | ✅ Yes | ⚠️ Sometimes | ❌ No |
| Immutability | Yes | No | No |
| Mandatory Dependencies | Yes | No | No |
| Optional Dependencies | No | Yes | Yes |
| Unit Testing | Easy | Medium | Difficult |
| Null Safety | High | Medium | Low |
| Boilerplate | Medium | Medium | Low |

---

## Real Enterprise Usage

### Mostly Used

**Constructor Injection**

Because:

- Cleaner design
- Immutable dependencies
- Easier testing
- Better maintainability

---

## Real Internal Spring Flow

Suppose:

```java
@Service
class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Spring internally:

**Step 1**

Scans bean definitions.

**Step 2**

Finds dependency:

```
OrderService → PaymentService
```

**Step 3**

Creates `PaymentService`.

**Step 4**

Calls constructor of `OrderService`.

**Step 5**

Injects dependency.

---

## Advanced Interview Insight

Spring DI works using:

- Reflection
- `BeanFactory`
- `BeanDefinition`
- Autowiring
- Dependency Resolution Algorithm

Internally Spring resolves:

- Matching bean type
- Qualifiers
- Primary bean
- Bean scopes

before injecting a dependency.

---

## One-Line Interview Answer

> Dependency Injection is a design pattern in Spring where the IoC container creates and injects required dependencies into objects instead of the objects creating them manually, enabling **loose coupling**, **better maintainability**, and **easier testing**.
