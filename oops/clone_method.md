# Core Java: Understanding Object Cloning

This session covers the concept of cloning in Java, a fundamental mechanism for creating duplicate objects, and explores the differences between shallow and deep cloning.

---

## 1. What is Cloning?

**Cloning** is the process of creating an exactly duplicate object of an existing one.

While cloning humans or animals is a controversial and often banned practice in the real world due to ethical disadvantages, **cloning Java objects is a legal and highly useful feature**.

### Why Do We Need Cloning?

- **To maintain a backup copy:** If you perform risky operations on an object, you can create a clone first. If something goes wrong, you can recover the state from the original object.
- **To preserve state:** If you need to perform updates on an object but want to compare the new values against the original values later, you can use a clone to track the **"before"** state.

### Important Clarification

> Creating a new reference variable (e.g., `Test T2 = T1;`) is **NOT** cloning.

This only creates a new reference to the **same object** in memory. Any changes made via `T2` will affect the object pointed to by `T1`.

✅ **True cloning creates a distinct object in heap memory.**

---

## 2. Using the `clone()` Method

The `clone()` method is present in the `java.lang.Object` class.

### Prototype of `clone()`

```java
protected native Object clone() throws CloneNotSupportedException;
```

| Modifier / Property | Meaning |
|---------------------|---------|
| `protected`         | Must be overridden to `public` if you want to call it from other classes |
| `native`            | Implementation is in a language other than Java (usually C/C++ for performance) |
| Return Type         | Always returns an `Object`, requiring **type casting** in your code |
| Exception           | Throws `CloneNotSupportedException` (a **checked exception**) |

### Requirements for Cloning

To perform cloning, your class **must implement** the marker interface `java.lang.Cloneable`.

> ⚠️ If you do **not** implement `Cloneable`, calling `clone()` will result in a `CloneNotSupportedException` at runtime.

### Basic Example of `clone()` Method

```java
class Student implements Cloneable {
    String name;
    int rollNumber;

    Student(String name, int rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Student s1 = new Student("Alice", 101);
        Student s2 = (Student) s1.clone(); // Create a clone

        System.out.println(s1.name + " " + s1.rollNumber); // Alice 101
        System.out.println(s2.name + " " + s2.rollNumber); // Alice 101
        System.out.println(s1 == s2); // false — different objects in heap

        // Modifying clone does not affect original
        s2.name = "Bob";
        System.out.println(s1.name); // Alice (unchanged)
        System.out.println(s2.name); // Bob
    }
}
```

**Output:**
```
Alice 101
Alice 101
false
Alice
Bob
```

---

## 3. Shallow Cloning vs. Deep Cloning

### Shallow Cloning

Shallow cloning creates a **bitwise copy** of the object.

| Aspect        | Behavior |
|---------------|----------|
| **Primitives** | Values are copied directly |
| **References** | Only the reference is copied, **not the object itself** |
| **Result**     | Both original and clone point to the **same contained object** |
| **Default**    | `Object.clone()` performs shallow cloning by default |

> ⚠️ If you modify the contained object, the change reflects in **both** the main object and the clone.

**Example:**

```java
class Address {
    String city;
    Address(String city) { this.city = city; }
}

class Person implements Cloneable {
    String name;
    Address address;

    Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); // Shallow clone
    }
}

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Address addr = new Address("New York");
        Person p1 = new Person("Alice", addr);
        Person p2 = (Person) p1.clone(); // Shallow clone

        System.out.println(p1.name);           // Alice
        System.out.println(p2.name);           // Alice
        System.out.println(p1.address == p2.address); // true — same Address object!

        // Modifying address via p2 affects p1 as well
        p2.address.city = "London";
        System.out.println(p1.address.city);   // London (changed!)
        System.out.println(p2.address.city);   // London
    }
}
```

**Output:**
```
Alice
Alice
true
London
London
```

---

### Deep Cloning

Deep cloning creates an **independent copy** of the entire object graph.

| Aspect        | Behavior |
|---------------|----------|
| **Primitives** | Values are copied directly |
| **References** | A **completely new, duplicate instance** of the contained object is created |
| **Result**     | The cloned object has **no dependency** on the original's contained objects |
| **Implementation** | Programmer must **override** `clone()` and manually create new instances |

**Example:**

```java
class Address implements Cloneable {
    String city;
    Address(String city) { this.city = city; }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class Person implements Cloneable {
    String name;
    Address address;

    Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Person cloned = (Person) super.clone();
        cloned.address = (Address) address.clone(); // Deep clone the reference
        return cloned;
    }
}

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Address addr = new Address("New York");
        Person p1 = new Person("Alice", addr);
        Person p2 = (Person) p1.clone(); // Deep clone

        System.out.println(p1.address == p2.address); // false — different Address objects!

        // Modifying address via p2 does NOT affect p1
        p2.address.city = "London";
        System.out.println(p1.address.city);   // New York (unchanged)
        System.out.println(p2.address.city);   // London
    }
}
```

**Output:**
```
false
New York
London
```

---

## 4. When to Use Which?

| Scenario | Recommended Cloning |
|----------|---------------------|
| Object contains **only primitive variables** | ✅ **Shallow Cloning** — efficient, no extra code needed beyond `Cloneable` |
| Object contains **reference variables** and full independence is needed | ✅ **Deep Cloning** — ensures cloned object is fully independent of the original |

---

## Summary

| Concept | Description |
|---------|-------------|
| **Cloning** | The act of duplicating objects to save state or keep backups |
| **`Cloneable` Interface** | A mandatory marker interface for any class using the `clone()` method |
| **Shallow Copy** | Default behavior; copies references, not objects. Changes to contained objects affect both |
| **Deep Copy** | Manual implementation; copies the full object structure. Changes are isolated |

---

## 🎯 Key Interview Tip

> Always clarify that `clone()` requires the `Cloneable` interface to be implemented, and that the **default implementation is shallow cloning**.
