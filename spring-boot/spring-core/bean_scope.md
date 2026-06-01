# What is Bean Scope in Spring?

## First Understand: What is Bean Scope?

Bean scope defines:

> How many objects Spring should create for a bean and how long they should live.

### Example

```java
@Component
class Car {
}
```

Now the question is:

> How many `Car` objects should Spring create?

That is decided by: **Bean Scope**

---

## Built-in Scopes in Spring

| Scope | Meaning |
|-------|---------|
| `singleton` | Only one object in entire container |
| `prototype` | New object every time requested |
| `request` | One object per HTTP request |
| `session` | One object per user session |
| `application` | One object per `ServletContext` |

---

## Default Scope

By default Spring uses: **Singleton Scope**

Meaning:

> Only one object is created and shared everywhere.

---

## How to Change Bean Scope

Using the `@Scope` annotation.

### Example: Singleton Scope

```java
@Component
@Scope("singleton")
class Car {
}
```

Only one object.

### Example: Prototype Scope

```java
@Component
@Scope("prototype")
class Car {
}
```

New object every time.

---

## Real Example

```java
Car c1 = context.getBean(Car.class);
Car c2 = context.getBean(Car.class);
```

**Singleton**

```
c1 == c2 → true
```

Same object.

**Prototype**

```
c1 == c2 → false
```

Different objects.

---

## What is Custom Bean Scope?

Sometimes built-in scopes are not enough.

Applications may need custom lifecycle rules.

Example:

- One bean per thread
- One bean per tenant
- One bean per logged-in user type
- One bean per transaction

Spring allows developers to create their own scope.

This is called: **Custom Bean Scope**

---

## High-Level Definition

> Custom scope means: Creating our own rules for bean creation, storage, reuse, and destruction.

---

## Real-Life Analogy

Built-in scopes are like standard room types in a hotel:

- Single room
- Double room
- Suite

But suppose a company wants:

> One special room per VIP customer group

The hotel creates a custom policy.

Similarly, Spring allows custom scope creation.

---

## When Custom Scope Is Used

### 1. Per Thread Scope

Each thread gets its own bean object.

Useful in:

- Multithreading
- Batch processing

### 2. Multi-Tenant Applications

Each tenant/customer gets separate bean instances.

Used in SaaS systems.

### 3. Workflow-Based Lifetime

Bean should live:

- Longer than request
- Shorter than singleton

---

## Internal Working of Custom Scope

To create a custom scope, implement:

```
org.springframework.beans.factory.config.Scope
```

### Main Responsibilities

Custom scope controls:

- Bean creation
- Bean retrieval
- Bean removal
- Destruction callbacks

### Important Methods

```java
Object get(String name, ObjectFactory<?> objectFactory)
```

Create/retrieve bean.

```java
Object remove(String name)
```

Remove bean.

---

## Simple Custom Thread Scope Example

### Step 1: Create Custom Scope

```java
public class ThreadScope implements Scope {

    private final ThreadLocal<Map<String, Object>> threadScope
            = ThreadLocal.withInitial(HashMap::new);

    @Override
    public Object get(String name,
                      ObjectFactory<?> objectFactory) {

        Map<String, Object> scope =
                threadScope.get();

        return scope.computeIfAbsent(
                name,
                k -> objectFactory.getObject()
        );
    }

    @Override
    public Object remove(String name) {
        return threadScope.get().remove(name);
    }

    @Override
    public void registerDestructionCallback(
            String name,
            Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return Thread.currentThread().getName();
    }
}
```

### Step 2: Register Scope

```java
@Configuration
public class AppConfig {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {

        return beanFactory -> {
            beanFactory.registerScope(
                    "thread",
                    new ThreadScope()
            );
        };
    }
}
```

### Step 3: Use Custom Scope

```java
@Component
@Scope("thread")
class UserContext {
}
```

Now:

> Each thread gets a separate `UserContext` object.

---

## Very Important Senior-Level Understanding

Custom scope is an **advanced Spring feature**.

In most applications:

```
singleton + prototype + request + session
```

are enough.

Custom scopes are mostly used in:

- Enterprise frameworks
- Multi-tenant systems
- Infrastructure-level development
- Highly customized architectures

---

## Difference Between Singleton and Prototype

| Feature | Singleton | Prototype |
|---------|-----------|-----------|
| Objects Created | One | Multiple |
| Default Scope | Yes | No |
| Managed by Spring | Fully | Partial |
| Destruction Managed | Yes | No |
| Use Case | Services | Stateful objects |

### Important Interview Point

> **Prototype bean destruction is NOT managed fully by Spring.**
> Spring only creates it. The developer manages cleanup.

---

## How to Change Scope Quickly

### Singleton

```java
@Scope("singleton")
```

### Prototype

```java
@Scope("prototype")
```

### Request Scope

```java
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
```

### Session Scope

```java
@Scope(value = WebApplicationContext.SCOPE_SESSION)
```

---

## Interview-Ready Definition

> Bean scope in Spring defines the lifecycle and visibility of a bean inside the IoC container. Spring provides built-in scopes like `singleton`, `prototype`, `request`, and `session`, and also allows developers to create **custom scopes** when application-specific lifecycle management is required.
