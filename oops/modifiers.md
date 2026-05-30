# Modifiers in Java

**Modifiers** are keywords in Java used to define:

- **Accessibility (visibility)** of classes, methods, variables, and constructors
- **Behavior/properties** of classes and their members

They are divided into two types:

1. **Access Modifiers**
2. **Non-Access Modifiers**

---

## 1. Access Modifiers

Access modifiers control **who can access** a class, method, variable, or constructor.

Java has **4 access modifiers**:

| Modifier | Same Class | Same Package | Subclass (Different Package) | Different Package |
|----------|-----------|-------------|------------------------------|-------------------|
| `private` | ✅ | ❌ | ❌ | ❌ |
| `default` (no modifier) | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

---

### 1. `private`

Accessible **only inside the same class**.

```java
class Test {
    private int x = 10;

    void display() {
        System.out.println(x); // accessible here
    }
}
```

**Use:** Data hiding, Encapsulation

---

### 2. `default` (Package-Private)

When **no modifier** is written. Accessible only within the **same package**.

```java
class Test {
    int x = 20; // default access
}
```

**Use:** Package-level access

---

### 3. `protected`

Accessible:
- Inside the **same package**
- Outside the package through **inheritance**

```java
class Parent {
    protected void show() {
        System.out.println("Protected Method");
    }
}
```

**Use:** Inheritance-related access

---

### 4. `public`

Accessible **from anywhere**.

```java
public class Test {
    public void display() {
        System.out.println("Hello");
    }
}
```

**Use:** APIs, methods/classes intended for general use

---

## 2. Non-Access Modifiers

These modifiers define **special properties or behavior**.

| Modifier | Purpose |
|----------|---------|
| `static` | Belongs to the class rather than an object |
| `final` | Prevents modification |
| `abstract` | Incomplete class/method |
| `synchronized` | Thread safety |
| `volatile` | Direct memory access in multithreading |
| `transient` | Skip during serialization |
| `native` | Method implemented in another language |
| `strictfp` | Consistent floating-point calculations |

---

### 1. `static`

Belongs to the **class** instead of individual objects.

```java
class Test {
    static int count = 0;
}
```

**Used with:** variables, methods, blocks, nested classes

---

### 2. `final`

Prevents modification.

| Applied To | Effect |
|-----------|--------|
| **Variable** | Cannot change value |
| **Method** | Cannot be overridden |
| **Class** | Cannot be inherited |

```java
final int x = 10;           // Final variable

final void show() { }       // Final method

final class A { }           // Final class
```

---

### 3. `abstract`

Used for **incomplete implementation**.

```java
// Abstract class
abstract class Animal {
    abstract void sound();  // Abstract method — must be implemented by child class
}
```

---

### 4. `synchronized`

Used in **multithreading** for thread safety. Allows **only one thread** at a time.

```java
synchronized void display() {
    System.out.println("Thread Safe");
}
```

---

### 5. `volatile`

Value is always read from **main memory**, not from thread-local cache.

```java
volatile boolean flag = true;
```

> ⚠️ `volatile` provides **visibility**, not full thread safety. It does not prevent race conditions.

**Use:** Multithreading scenarios where a variable is read/written by multiple threads.

---

### 6. `transient`

Variable is **skipped during serialization**.

```java
transient int password;
```

**Use:** Sensitive data that should not be saved/transferred.

---

### 7. `native`

Method is implemented in **C/C++** using JNI (Java Native Interface).

```java
native void connect();
```

**Use:** Platform-specific operations, performance-critical code.

---

### 8. `strictfp`

Ensures **consistent floating-point results** on all platforms.

```java
strictfp class Test {
}
```

---

## Summary

### Access Modifiers

| Modifier | Visibility |
|----------|-----------|
| `private` | Same class only |
| `default` | Same package |
| `protected` | Same package + subclasses |
| `public` | Everywhere |

### Non-Access Modifiers

| Modifier | Key Purpose |
|----------|------------|
| `static` | Class-level member |
| `final` | Prevent change/override/inheritance |
| `abstract` | Incomplete, must be overridden |
| `synchronized` | One thread at a time |
| `volatile` | Always read from main memory |
| `transient` | Skip during serialization |
| `native` | Implemented in native code |
| `strictfp` | Consistent floating-point math |

---

## 🎯 Important Interview Points

- **`default` modifier** is also called **package-private**.
- **Top-level classes** can only be `public` or `default`.
- **`abstract` and `final` cannot be used together** — `abstract` requires overriding, `final` prevents it.
- **`static` methods cannot be overridden** — they are **hidden**, not overridden.
- **`private` methods are not inherited** by subclasses.
- **`volatile` does not provide thread safety** — it only provides **visibility** (ensures latest value is read from main memory).
