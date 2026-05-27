# Static Keyword in Java

The `static` keyword in Java is used to make a member **belong to the class itself** rather than to individual objects.

---

## Core Idea of `static`

Normally:
- Every object gets its **own copy** of variables and methods.

But when something is declared `static`:
- Only **one copy** exists
- **Shared among all objects**

### Simple Understanding

| Type | Level | Description |
|------|-------|-------------|
| **Non-static** | Object Level | Each object has separate data |
| **Static** | Class Level | Shared by all objects |

---

### Example Without Static

```java
class Student {
    int age;
}

Student s1 = new Student();
Student s2 = new Student();
```

Memory:
- `s1.age` → separate copy
- `s2.age` → separate copy

> Each object gets its **own copy**.

---

### Example With Static

```java
class Student {
    static String college = "NIT";
}
```

Now:
- Only **ONE copy** of `college` exists
- Shared by `s1`, `s2`, and **all objects**

---

## Why Static is Used?

Mainly for **Memory Optimization** — because duplicate copies are avoided.

---

## Where Static Members are Stored?

Static members belong to the **class**.

> Stored in: **Method Area / Metaspace** — NOT inside object memory (Heap).

---

## Types of Static Members

`static` can be used with:

| Type | Allowed? |
|------|----------|
| Variables | ✅ |
| Methods | ✅ |
| Blocks | ✅ |
| Nested Classes | ✅ |

---

## 1. Static Variables

A static variable is **shared by all objects**.

### Example

```java
class Student {
    int id;
    String name;
    static String college = "NIT";
}
```

### Memory Layout

```
Method Area / Metaspace
┌──────────────────────┐
│  college = "NIT"     │
└──────────────────────┘

Heap
┌──────────────────────┐
│  Student Object 1    │
│  id, name            │
├──────────────────────┤
│  Student Object 2    │
│  id, name            │
└──────────────────────┘
```

> Only **ONE copy** of the static variable exists — shared across all objects.

### Accessing Static Variable

```java
// Preferred (using class name)
Student.college

// Allowed but not preferred
s1.college
```

### Example — Object Counter

```java
class Student {
    static int count = 0;

    Student() {
        count++;
    }
}

public class Main {
    public static void main(String[] args) {
        new Student();
        new Student();
        new Student();

        System.out.println(Student.count); // Output: 3
    }
}
```

> Every object shares the **same `count`** → Output: `3`

---

### Static Variable vs Instance Variable

| Feature | Static Variable | Instance Variable |
|---------|----------------|-------------------|
| **Belongs to** | Class | Object |
| **Copies** | One copy | Separate copies per object |
| **Sharing** | Shared | Not shared |
| **Stored in** | Method Area | Heap |

---

## 2. Static Methods

Static methods **belong to the class** and can be called **without creating an object**.

### Example

```java
class MathUtils {
    static int square(int x) {
        return x * x;
    }
}

// Call directly using class name
MathUtils.square(5);
```

### Why Static Methods are Used?

When a method:
- Does **not depend on object data**
- Provides **utility functionality**

### Real-Life Examples

```java
Math.max()
Math.min()
Integer.parseInt()
// All are static methods
```

### Main Method is Static

```java
public static void main(String[] args)
```

> Because JVM must call it **before any object exists**.

---

### Important Restriction

Static methods **cannot directly access**:
- Non-static variables
- Non-static methods

**Why?** Because static methods belong to the class — no object exists automatically.

```java
class Test {
    int x = 10;

    static void show() {
        System.out.println(x); // ❌ ERROR
    }
}
```

> `x` belongs to an object — static method has **no object reference**.

### How to Access Non-static Data in Static Method?

**Create an object:**

```java
class Test {
    int x = 10;

    static void show() {
        Test t = new Test();
        System.out.println(t.x); // ✅ Works
    }
}
```

### `this` Cannot Be Used in Static Method

```java
static void show() {
    System.out.println(this.x); // ❌ ERROR
}
```

> `this` refers to the **current object**, but static methods have **no object context**.

> `super` is also **not allowed** for the same reason.

---

### Static Method vs Non-static Method

| Feature | Static Method | Non-static Method |
|---------|--------------|-------------------|
| **Belongs to** | Class | Object |
| **Called using** | Class name | Object reference |
| **Instance data access** | ❌ Not directly | ✅ Yes |
| **`this` reference** | ❌ Not available | ✅ Available |

---

## 3. Static Blocks

A static block executes:
- **Only once**
- When the **class loads into memory**

### Syntax

```java
static {
    // initialization code
}
```

### Example

```java
class Test {
    static {
        System.out.println("Static Block");
    }

    public static void main(String[] args) {
        System.out.println("Main Method");
    }
}
```

**Output:**
```
Static Block
Main Method
```

### Why Static Block Executes First?

Because:
- Class loads **before** object creation
- Static members are initialized **during class loading**

### Use Cases of Static Blocks

- Complex initialization
- Database configuration
- Loading drivers
- Initializing static data

```java
class Config {
    static {
        System.out.println("Database Connected");
    }
}
```

### Static Block Execution Order

```
Class Loading
      ↓
Static Variables
      ↓
Static Blocks
      ↓
main()
```

### Multiple Static Blocks

Executed **top to bottom**:

```java
class Test {
    static {
        System.out.println("Block 1");
    }
    static {
        System.out.println("Block 2");
    }
}
```

**Output:**
```
Block 1
Block 2
```

---

## 4. Static Nested Class

A class inside another class declared as `static`.

### Example

```java
class Outer {
    static class Inner {
        void show() {
            System.out.println("Inner");
        }
    }
}

// Usage — no need to create Outer object
Outer.Inner obj = new Outer.Inner();
```

---

## Practical Real-World Uses of Static

### 1. Utility Classes

```java
Math.max()
Collections.sort()
// No need to create objects
```

### 2. Shared Data

- Company name
- College name
- Application configuration

### 3. Counter Variables

Tracking the number of objects created.

### 4. Singleton Design Pattern

Allows **only one object** to be created.

```java
class Singleton {

    private static Singleton obj = new Singleton();

    private Singleton() {}

    static Singleton getInstance() {
        return obj;
    }
}
```

> **Why Singleton Uses Static?** — Because the single object must be **shared globally**.

---

## Memory View of Static

```
Method Area / Metaspace
┌────────────────────────────────┐
│  Static Variables              │
│  Static Methods Metadata       │
│  Static Block Metadata         │
└────────────────────────────────┘

Heap
┌────────────────────────────────┐
│  Objects                       │
│  Instance Variables            │
└────────────────────────────────┘
```

---

## Important Rules of Static

| Rule | Description |
|------|-------------|
| **Rule 1** | Static members **belong to the class** |
| **Rule 2** | Access static members using **class name** |
| **Rule 3** | Static methods **cannot directly access** non-static members |
| **Rule 4** | `this` and `super` are **not allowed** in static context |
| **Rule 5** | Static block executes **only once** |

---

## Execution Order Example

```java
class Test {
    static int x = 10;

    static {
        System.out.println("Static Block");
    }

    Test() {
        System.out.println("Constructor");
    }

    public static void main(String[] args) {
        Test t1 = new Test();
        Test t2 = new Test();
    }
}
```

**Output:**
```
Static Block
Constructor
Constructor
```

**Why?**
- Static block runs **once** during class loading
- Constructor runs **for every object**

---

## Common Interview Questions

### Why does a Static Variable Save Memory?
> Because only **one copy** exists regardless of how many objects are created.

### Can a Static Method Be Overridden?
> **Not truly overridden** — it is called **method hiding**.

### Can a Constructor Be Static?
> ❌ **No** — Constructor belongs to object creation and cannot be static.

### Can We Access a Static Variable Without an Object?
> ✅ **Yes** — using the class name: `Student.college`

### Why is the Main Method Static?
> Because **JVM calls it before any object is created**.

---

## Final Summary

```
Static     =  Class Level
Non-static =  Object Level
```

### Memory Summary

| Feature | Static Members | Non-static Members |
|---------|---------------|-------------------|
| **Stored** | Once | Per object |
| **Sharing** | Shared among all objects | Separate copies |
| **Location** | Method Area / Metaspace | Heap |

---

## ⭐ Most Important Concept

```
Static members belong to the CLASS,
not to objects.

That is the foundation of understanding static in Java.
```

---
