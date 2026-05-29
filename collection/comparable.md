# Java Comparable Interface — Complete Detailed Notes

---

## 1. What is Comparable?

`Comparable` is an interface in Java used to define the **natural ordering of objects**.

**Package:**
```
java.lang
```

It allows Java to understand **how objects should be sorted by default**.

### Real-Life Examples of Natural Ordering

| Class | Natural Order |
|-------|--------------|
| `Integer` | Ascending |
| `String` | Alphabetical |
| `Character` | Unicode order |

**Example:**

```java
List<Integer> list = Arrays.asList(30, 10, 20);

Collections.sort(list);

System.out.println(list);
```

**Output:**
```
[10, 20, 30]
```

Java already knows how to sort `Integer`. Why? Because `Integer` implements `Comparable<Integer>`.

---

## 2. Problem with Custom Objects

Suppose:

```java
class Student {
    int gpa;
    String name;
}
```

Now:

```java
List<Student> students = new ArrayList<>();

Collections.sort(students); // ❌ Error
```

**Why?**

Java does **NOT** know how to compare `Student` objects. Unlike `Integer`/`String`, your class has no natural ordering.

**Error:**

Usually a `ClassCastException` because Java internally tries to cast the object into `Comparable`.

---

## 3. Comparable vs Comparator

> **Very important interview question.**

| Feature | Comparable | Comparator |
|---------|-----------|------------|
| Package | `java.lang` | `java.util` |
| Method | `compareTo()` | `compare()` |
| Logic Location | Inside class | Outside class |
| Sorting Type | Natural ordering | Custom ordering |
| Number of Sorting Ways | One | Multiple |

**Comparable** — defines default sorting **inside** the class itself.

**Comparator** — defines external/custom sorting **outside** the class.

---

## 4. Implementing Comparable

Your class must implement `Comparable<T>`.

### Syntax

```java
class Student implements Comparable<Student>
```

### Main Method

```java
int compareTo(Student other)
```

### Return Rules

| Return Value | Meaning |
|--------------|---------|
| Negative | `this` comes before `other` |
| Zero | Equal |
| Positive | `this` comes after `other` |

### Visual Understanding

```
this.compareTo(other)

< 0 → this first
= 0 → equal
> 0 → other first
```

---

## 5. Example — Sort Students by GPA

```java
import java.util.*;

class Student implements Comparable<Student> {

    private int gpa;
    private String name;

    Student(int gpa, String name) {
        this.gpa = gpa;
        this.name = name;
    }

    public int getGpa() {
        return gpa;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.gpa, other.gpa);
    }

    @Override
    public String toString() {
        return gpa + " " + name;
    }
}

public class Main {

    public static void main(String[] args) {

        List<Student> list = new ArrayList<>();

        list.add(new Student(10, "Nitish"));
        list.add(new Student(3, "Harsh"));
        list.add(new Student(5, "Sumit"));

        Collections.sort(list);

        System.out.println(list);
    }
}
```

**Output:**
```
[3 Harsh, 5 Sumit, 10 Nitish]
```

### Why This Works?

Java internally calls `s1.compareTo(s2)` during sorting.

---

## 6. Ascending vs Descending Logic

### Ascending

```java
return Integer.compare(this.gpa, other.gpa);
```

Smaller GPA first.

### Descending

```java
return Integer.compare(other.gpa, this.gpa);
```

Larger GPA first.

> **Important:** Swap positions to reverse order.

---

## 7. Why Use `Integer.compare()`?

**Avoid this:**

```java
return this.gpa - other.gpa; // ❌
```

**Problem:** Integer overflow possible.

**Safe approach:**

```java
Integer.compare(a, b); // ✅
```

### For Double Values

**Use:**

```java
Double.compare(a, b); // ✅
```

**Avoid:**

```java
(int)(a - b); // ❌ precision loss, wrong results
```

---

## 8. `list.sort(null)`

> **Very important concept.**

### Why Does `null` Work?

```java
list.sort(null);
```

This means: **Use natural ordering** defined by `compareTo()` inside the class.

### Internally

```java
Collections.sort(list);
```

uses `compareTo()` if comparator is `null`.

---

## 9. Comparator vs Comparable Usage

### Use `Comparable` When

There is **one obvious/default sorting**.

**Example:**
- Student by roll number
- Employee by ID

### Use `Comparator` When

Need **multiple sorting strategies**.

**Example:**
- Sort by GPA
- Sort by name
- Sort by age

### Real Project Practice

```
Comparable  → default sorting
Comparator  → custom sorting
```

---

## 10. TreeSet and TreeMap Requirement

> **Very important.**

`TreeSet` stores elements in **sorted order**.

If objects don't implement `Comparable`:

```java
TreeSet<Student> set = new TreeSet<>(); // ❌ Runtime error
```

**Why?** `TreeSet` needs ordering logic. Either `Comparable` **OR** `Comparator` must exist.

---

## 11. `compareTo()` vs `compare()`

### Comparable

```java
compareTo(T other)
```

Only **1 parameter** — because `this` object is implicit.

### Comparator

```java
compare(T o1, T o2)
```

Needs **2 parameters** — because comparison logic is external.

---

## 12. Example of `compareTo()`

```java
Student s1 = new Student(10, "A");
Student s2 = new Student(5, "B");

System.out.println(s1.compareTo(s2));
```

**Output:**
```
positive value
```

Because `10 > 5`.

---

## 13. Multiple Sorting Problem with Comparable

`Comparable` supports **only one natural ordering**.

Cannot define GPA sorting, Name sorting, and Age sorting all together naturally.

> That's why `Comparator` exists.

---

## 14. Combining Comparable + Comparator

Very common in real projects.

**Example:**
- `Comparable` → sort by ID
- `Comparator` → sort by GPA/name

```java
Collections.sort(list);         // uses natural ordering (Comparable)

list.sort(nameComparator);      // uses custom ordering (Comparator)
```

---

## 15. Common Interview Questions

### Q1. Why is `Comparable` in `java.lang`?

Because natural ordering is **core language functionality**.

### Q2. Which method must be overridden?

```java
compareTo()
```

### Q3. Can we sort without `Comparable`?

Yes, using `Comparator`.

### Q4. Why does `compareTo` take one parameter?

Because `this` object is already available.

### Q5. Which is more flexible?

`Comparator` — because multiple sorting logics are possible.

---

## 16. Complete Example with Descending GPA

```java
import java.util.*;

class Student implements Comparable<Student> {

    private double gpa;
    private String name;

    Student(double gpa, String name) {
        this.gpa = gpa;
        this.name = name;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa);
    }

    @Override
    public String toString() {
        return gpa + " " + name;
    }
}

public class Main {

    public static void main(String[] args) {

        List<Student> list = new ArrayList<>();

        list.add(new Student(8.5, "Rahul"));
        list.add(new Student(9.1, "Amit"));
        list.add(new Student(7.8, "Kiran"));

        Collections.sort(list);

        System.out.println(list);
    }
}
```

**Output:**
```
[9.1 Amit, 8.5 Rahul, 7.8 Kiran]
```

---

## 17. Final Revision Notes

### Key Points

- ✅ `Comparable` defines natural ordering
- ✅ `compareTo()` method required
- ✅ `compareTo` takes one argument
- ✅ Negative → `this` first
- ✅ Positive → `other` first
- ✅ `list.sort(null)` uses `compareTo()`
- ✅ `Comparable` = one default sorting
- ✅ `Comparator` = multiple custom sorting

### Quick Revision Table

| Feature | Comparable | Comparator |
|---------|-----------|------------|
| Method | `compareTo()` | `compare()` |
| Parameters | 1 | 2 |
| Logic Location | Inside class | Outside class |
| Flexibility | Less | More |

### Shortcut to Remember

```
Comparable  → Default / Natural sorting

Comparator  → Custom sorting
```
