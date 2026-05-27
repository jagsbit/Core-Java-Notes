# Inner Classes in Java

An **Inner Class** is a class declared inside another class.
The class inside is called the **Inner Class**, and the outer class is called the **Outer Class**.

Inner classes help in:

- Better **code organization**
- **Encapsulation**
- Grouping **related classes** together
- Accessing **outer class members** easily

> 💡 The inner class can access **all members** of the outer class, including **private members**.

---

## Syntax

```java
class OuterClass {

    class InnerClass {
        // inner class code
    }
}
```

---

## Basic Example

```java
class Outer {

    private int x = 10;

    class Inner {

        void display() {
            System.out.println("x = " + x);
        }
    }
}

public class Main {
    public static void main(String[] args) {

        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.display();
    }
}
```

**Output:**
```
x = 10
```

---

## Features of Inner Classes

| Feature | Description |
|---------|-------------|
| **Access Private Members** | Can access private data members and methods of the outer class |
| **Better Encapsulation** | Related classes can be grouped together |
| **Improves Readability** | Keeps helper classes close to where they are used |
| **Namespace Management** | Avoids naming conflicts |

---

## Types of Inner Classes in Java

Java supports **four types** of inner classes:

| # | Type | Description |
|---|------|-------------|
| 1 | **Member Inner Class** | Non-static class at member level |
| 2 | **Method Local Inner Class** | Declared inside a method |
| 3 | **Static Nested Class** | Declared with `static` keyword |
| 4 | **Anonymous Inner Class** | No name, one-time use |

---

## 1. Member Inner Class

A **member inner class** is a non-static class created inside another class at the member level. It behaves like a normal member of the outer class.

### Example

```java
class Outer {

    private int num = 100;

    class Inner {

        void show() {
            System.out.println("num = " + num);
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.show();
    }
}
```

**Output:**
```
num = 100
```

### Important Points

- Can access **all members** of the outer class, including private members.
- Object creation syntax:
  ```java
  Outer.Inner obj = outer.new Inner();
  ```
- **Before Java 16:** Static members were not allowed except `static final` constants.
- **After Java 16:** Static members are allowed if they do not depend on the outer instance.

---

## 2. Method Local Inner Class

A **method local inner class** is declared inside a method and can be used **only inside that method**.

### Example

```java
class Outer {

    void display() {

        System.out.println("Inside display method");

        class Inner {

            void show() {
                System.out.println("Inside inner class");
            }
        }

        Inner obj = new Inner();
        obj.show();
    }
}

public class Main {

    public static void main(String[] args) {

        Outer outer = new Outer();
        outer.display();
    }
}
```

**Output:**
```
Inside display method
Inside inner class
```

### Accessing Local Variables

Method local inner classes can access:
- `final` variables
- **Effectively final** variables (not modified after assignment)

```java
class Outer {

    void display() {

        int x = 50; // effectively final

        class Inner {

            void show() {
                System.out.println("x = " + x);
            }
        }

        Inner obj = new Inner();
        obj.show();
    }
}
```

**Output:**
```
x = 50
```

### Important Points

| Property | Details |
|----------|---------|
| **Scope** | Exists only inside the method |
| **Access** | Can access effectively final local variables |
| **Cannot use** | `private`, `protected`, `static`, `transient` |
| **Can use** | `final`, `abstract` |

---

## 3. Static Nested Class

A **static nested class** is declared using the `static` keyword inside another class.

Because it is `static`:
- It does **NOT** depend on the outer class object.
- It can access **only static members** of the outer class directly.

### Example

```java
class Outer {

    static int x = 200;

    static class Inner {

        void show() {
            System.out.println("x = " + x);
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Outer.Inner obj = new Outer.Inner(); // No outer object needed
        obj.show();
    }
}
```

**Output:**
```
x = 200
```

### Important Points

- No outer object needed:
  ```java
  Outer.Inner obj = new Outer.Inner();
  ```
- Can access **static members** directly.
- **Cannot** access non-static members directly.
- **Memory efficient** — used when the inner class does not need an outer object reference.

---

## 4. Anonymous Inner Class

An **anonymous inner class** is a class **without a name**. It is declared and instantiated at the same time, used for **one-time implementation**.

Mostly used for:
- Implementing **interfaces**
- **Event handling**
- **Functional-style** programming

### Example: Implementing an Interface

```java
interface Greeting {

    void sayHello();
}

public class Main {

    public static void main(String[] args) {

        Greeting g = new Greeting() {

            @Override
            public void sayHello() {
                System.out.println("Hello from Anonymous Inner Class");
            }
        };

        g.sayHello();
    }
}
```

**Output:**
```
Hello from Anonymous Inner Class
```

### Important Points

| Property | Details |
|----------|---------|
| **No class name** | The class has no name |
| **One-time use** | Usually used when object is needed only once |
| **Syntax** | `new InterfaceName() { // implementation };` |
| **No constructors** | Cannot have constructors (no name to call) |

---

## Difference Between Inner Classes

| Type | Declared Where | Needs Outer Object | Access |
|------|---------------|-------------------|--------|
| **Member Inner Class** | Inside class | ✅ Yes | All members |
| **Method Local Inner Class** | Inside method | ✅ Yes | Final / effectively final local vars |
| **Static Nested Class** | Inside class (`static`) | ❌ No | Only static members directly |
| **Anonymous Inner Class** | Inside expression | Depends | One-time implementation |

---

## Advantages & Disadvantages

### ✅ Advantages

| Advantage | Description |
|-----------|-------------|
| **Better Encapsulation** | Hide helper classes from the outside world |
| **Cleaner Code** | Keeps related code together |
| **Easy Access** | Directly access outer class members |
| **Improves Maintainability** | Related logic remains grouped |

### ❌ Disadvantages

| Disadvantage | Description |
|-------------|-------------|
| **More Complex Syntax** | Especially object creation syntax |
| **Reduced Readability** | Too many nested classes can make code difficult |
| **Tight Coupling** | Inner class depends heavily on outer class |

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Inner Class** | A class declared inside another class |
| **Member Inner Class** | Non-static, needs outer object |
| **Method Local Inner Class** | Exists only inside the method |
| **Static Nested Class** | No outer object needed; accesses only static members |
| **Anonymous Inner Class** | No name; one-time interface/class implementation |

---

## 🎯 Key Interview Tips

- Java has **4 types** of inner classes — know each with an example.
- **Anonymous inner classes** are the most commonly used in event handling and callbacks.
- **Static nested classes** do not hold a reference to the outer class — making them more memory efficient.
- Method local inner classes can only access **effectively final** variables.
- Inner classes **cannot be instantiated** without an outer class object (except static nested classes).
