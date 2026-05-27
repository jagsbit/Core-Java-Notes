# Core Java: `java.lang` Package - Part 1

This session covers the foundational concepts of the `java.lang` package, focusing on its importance in Java programming and an in-depth analysis of the `Object` class and its `toString()` method.

---

## 1. Introduction to `java.lang` Package

| Property | Details |
|----------|---------|
| **Importance** | The most crucial package in Core Java and SCJP/OCJP certification — effectively the **"heart"** of the Java language |
| **Mandatory Nature** | It is impossible to write a functional Java program without `java.lang` |
| **Default Availability** | **No explicit import needed** — the JVM automatically makes all classes and interfaces available to every Java program |

### Key Classes in `java.lang`

- `Object`
- `String`
- `StringBuffer`
- Wrapper classes (`Integer`, `Double`, `Character`, etc.)
- `Math`, `Thread`, `System`, and more

> 💡 Unlike `java.util` or `java.io`, you **never** need to write `import java.lang.*;` — it is always available by default.

---

## 2. The `Object` Class

### Definition

> `java.lang.Object` is the **root of the Java class hierarchy**.

**Every class in Java is a child of the `Object` class, either directly or indirectly.**

### Why a Root Class?

The `Object` class defines the most **common, universally applicable methods** that every Java object needs (e.g., `toString()`, `equals()`, `hashCode()`, `clone()`). By making `Object` the parent, these methods are **automatically available** to every object.

---

### Inheritance Clarification

| Type | Example | Relationship to `Object` |
|------|---------|--------------------------|
| **Direct child** | `class A {}` | `A` directly extends `Object` |
| **Indirect child** | `class A extends B {}` | `A → B → Object` (multi-level) |

> ⚠️ **No Multiple Inheritance:** Java does **not** support multiple inheritance for classes. A class extending another class and also inheriting from `Object` is **multi-level inheritance**, not multiple inheritance.

```
Object
  └── B
       └── A
```

---

### Methods in the `Object` Class

The `Object` class contains **11 commonly used methods**:

| # | Method |
|---|--------|
| 1 | `toString()` |
| 2 | `hashCode()` |
| 3 | `equals()` |
| 4 | `clone()` |
| 5 | `getClass()` |
| 6 | `finalize()` |
| 7 | `wait()` |
| 8 | `wait(long timeout)` |
| 9 | `wait(long timeout, int nanos)` |
| 10 | `notify()` |
| 11 | `notifyAll()` |

> 📝 **Note:** Strictly speaking there are 12 methods, but `registerNatives()` is for **internal JVM use** and is not available to child classes, so it is excluded from standard discussions.

---

## 3. The `toString()` Method

### Purpose

> Used to get a **String representation** of an object.

### When Is It Called?

Whenever you attempt to **print an object reference directly**, the JVM internally calls `.toString()` on it.

```java
System.out.println(s); // JVM internally calls s.toString()
```

---

### Default Behavior

By default, the `Object` class implementation of `toString()` returns:

```
ClassName@HashCodeInHexadecimalForm
```

**Example output:**
```
Student@87591759
```

This is **not meaningful** for real-world applications.

---

### Overriding `toString()`

It is **highly recommended** to override `toString()` in your own classes to return meaningful, readable data.

**Without Override:**
```java
class Student {
    String name;
    int rollNumber;

    Student(String name, int rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
    }
}

public class Main {
    public static void main(String[] args) {
        Student s = new Student("Alice", 101);
        System.out.println(s); // Output: Student@87591759
    }
}
```

**With Override:**
```java
class Student {
    String name;
    int rollNumber;

    Student(String name, int rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
    }

    @Override
    public String toString() {
        return name + " " + rollNumber;
    }
}

public class Main {
    public static void main(String[] args) {
        Student s = new Student("Alice", 101);
        System.out.println(s); // Output: Alice 101
    }
}
```

### Built-in Classes That Override `toString()`

Many built-in Java classes have already overridden `toString()` to provide meaningful output:

- `String`
- Wrapper classes (`Integer`, `Double`, etc.)
- Collection classes (`ArrayList`, `HashMap`, etc.)

---

## Summary

| Concept | Description |
|---------|-------------|
| **`java.lang` Package** | Fundamental and auto-imported; the heart of Java |
| **`Object` Class** | Universal parent of all Java classes; provides essential utility methods |
| **`toString()` Method** | Should be overridden to provide human-readable information about an object's state |

---

## 🎯 Key Takeaways

- **Interview Tip:** Know exactly how many methods are in `Object` (**11**) and why `toString()` is necessary.
- **Coding Practice:** Always override `toString()` in custom domain objects to improve **debugging and logging**.
- **Inheritance:** Remember that all classes, if not explicitly extending another, are **direct children of `Object`**.
