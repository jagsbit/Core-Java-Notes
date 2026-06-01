# Interview Answer

Bean lifecycle is the complete sequence of steps followed by the Spring IoC container from bean creation until bean destruction.

The lifecycle mainly includes the following phases:

1. **Container Initialization**
   When the application starts, the Spring IoC container is initialized and loads configuration metadata such as annotations, XML, or Java configuration.

2. **Bean Instantiation**
   Spring scans components, registers bean definitions, and creates bean objects using constructors or factory methods.

3. **Dependency Injection**
   The container resolves and injects all required dependencies into the bean using constructor, setter, or field injection.

4. **Initialization**
   After dependency injection, initialization callbacks are executed. If we want some action to happen immediately after bean creation, we can define it using the `@PostConstruct` method.

5. **Bean Usage**
   Once fully initialized, the bean is ready to be used by the application.

6. **Destruction**
   Before the container destroys the bean during application shutdown, cleanup logic can be written inside the `@PreDestroy` method, such as closing database connections or releasing resources.

---

# What is Bean Life Cycle in Spring?

**Bean Life Cycle** means:

> The sequence of steps followed by the Spring IoC container from bean creation to bean destruction.

In simple words:

```
Bean born → configured → used → destroyed
```

Spring manages the complete life of a bean.

---

## High-Level Flow

```
Container Starts
      ↓
Bean Instantiation
      ↓
Dependency Injection
      ↓
Initialization
      ↓
Bean Ready to Use
      ↓
Destruction
```

---

## Real-Life Analogy

Think of an employee joining a company.

```
Hiring
↓
Assign Laptop & Resources
↓
Training
↓
Employee Starts Working
↓
Employee Leaves Company
↓
Resources Returned
```

Spring bean lifecycle works similarly.

---

## 1. Container Initialization

When Spring application starts:

```java
SpringApplication.run(App.class, args);
```

the IoC container starts.

### What Happens Internally?

Spring:

- Reads configuration
- Scans beans
- Creates bean definitions
- Prepares infrastructure

Configuration may come from:

- Annotations
- XML
- Java config

### Example

```java
@Component
@Service
@Repository
@Configuration
```

Spring scans these classes.

### Bean Definition

Spring creates metadata like:

- Bean Name
- Class Type
- Scope
- Dependencies
- Init Methods
- Destroy Methods

---

## 2. Bean Instantiation

Now Spring creates the object.

Example:

```java
@Component
class Engine {
}
```

Spring internally does something similar to:

```java
new Engine();
```

using **reflection**.

### Important Point

At this stage:

- Object exists
- BUT dependencies are **not injected** yet

---

## 3. Dependency Injection

Now Spring resolves dependencies.

Example:

```java
@Component
class Car {

    private final Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }
}
```

Spring:

1. Finds `Engine`
2. Creates `Engine`
3. Injects into `Car`

### Injection Types

Spring may inject dependencies using:

- Constructor injection
- Setter injection
- Field injection

### Important Point

After this phase:

> Bean becomes **fully configured**.

---

## 4. Initialization Phase

Now bean is initialized.

This phase is used for:

- Validation
- Opening resources
- Initializing cache
- Database connections
- Starting threads

### Ways to Define Init Logic

#### Using `@PostConstruct`

```java
@PostConstruct
public void init() {
    System.out.println("Bean initialized");
}
```

Executed automatically after dependency injection.

#### Using `InitializingBean`

```java
class Car implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        System.out.println("Initialized");
    }
}
```

#### Using Custom Init Method

```java
@Bean(initMethod = "init")
public Car car() {
    return new Car();
}
```

### Important Understanding

> Initialization means: Bean is now **completely ready for use**.

---

## 5. Bean Ready for Use

Now the application uses the bean normally.

Example:

```java
car.drive();
```

This is the actual **working phase**.

---

## 6. Destruction Phase

When the application shuts down:

> Spring destroys beans before removing them from memory.

### Why Destruction Is Needed

To release resources like:

- DB connections
- Sockets
- Threads
- File streams

Otherwise memory/resource leaks may happen.

### Ways to Define Destroy Logic

#### Using `@PreDestroy`

```java
@PreDestroy
public void cleanup() {
    System.out.println("Bean destroyed");
}
```

#### Using `DisposableBean`

```java
class Car implements DisposableBean {

    @Override
    public void destroy() {
        System.out.println("Destroyed");
    }
}
```

#### Using Custom Destroy Method

```java
@Bean(destroyMethod = "cleanup")
```

---

## Complete Example

```java
@Component
class Car {

    public Car() {
        System.out.println("Constructor called");
    }

    @PostConstruct
    public void init() {
        System.out.println("Initialization");
    }

    public void drive() {
        System.out.println("Driving");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Cleanup");
    }
}
```

### Execution Flow

```
Container Started
↓
Constructor called
↓
Dependencies Injected
↓
@PostConstruct executed
↓
Bean Ready
↓
drive() called
↓
@PreDestroy executed
↓
Container Shutdown
```

---

## Important Senior-Level Concepts

### BeanPostProcessor

Spring internally allows custom processing:

- Before initialization
- After initialization

This is heavily used internally for:

- AOP proxies
- Transactions
- Security
- `@Autowired` handling

### Example Internal Flow

```
Instantiate Bean
↓
Inject Dependencies
↓
BeanPostProcessor Before Init
↓
@PostConstruct
↓
BeanPostProcessor After Init
↓
Bean Ready
```

---

## Important Interview Points

### `@PreDestroy` and Singleton Beans

> `@PreDestroy` works mainly for **Singleton Beans** because Spring fully manages their lifecycle.

**Prototype beans** are not fully destroyed by the container.

### About "Custom Utility Method"

This is **NOT** actually a lifecycle phase.

Example:

```java
public void calculateSalary() {
}
```

It is just a normal business method.

Spring lifecycle does **not** automatically call it.

> So in interviews, avoid saying **"Utility method is a lifecycle phase"** because technically it is not.

---

## Summary Table

| Phase | Description | Common Annotations/Interfaces |
|-------|-------------|-------------------------------|
| Container Initialization | Spring reads config and creates bean definitions | `@Component`, `@Configuration` |
| Bean Instantiation | Spring creates object using reflection | — |
| Dependency Injection | Dependencies are injected | `@Autowired`, Constructor |
| Initialization | Custom init logic executed | `@PostConstruct`, `InitializingBean` |
| Bean Ready | Bean used by application | — |
| Destruction | Cleanup logic executed before shutdown | `@PreDestroy`, `DisposableBean` |
