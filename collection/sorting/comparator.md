# Java Comparator — Complete Detailed Notes

---

## 1. What is Comparator?

`Comparator` is an **interface** in Java used for **custom sorting of objects**.

**Package:**
```
java.util.Comparator
```

It allows us to define:
- Ascending order
- Descending order
- Custom object sorting

> Without modifying the original class.

### Why Comparator is Needed?

Some classes already have **natural ordering**.

**Example:**
- `Integer` → ascending
- `String` → alphabetical

But sometimes we want **custom sorting**.

**Example:**
- Sort strings by length
- Sort students by GPA
- Sort employees by salary descending

`Comparator` helps achieve this.

---

## 2. Comparator Interface

`Comparator` is a **Functional Interface** because it contains only **one abstract method**.

### Main Method

```java
int compare(T o1, T o2)
```

### Meaning of Return Values

| Return Value | Meaning |
|--------------|---------|
| Negative | `o1` comes before `o2` |
| Zero | Both equal |
| Positive | `o1` comes after `o2` |

### Visual Understanding

```
compare(o1, o2)

< 0  → o1 first
= 0  → equal
> 0  → o2 first
```

---

## 3. Sorting Numbers Example

```java
List<Integer> list = Arrays.asList(30, 10, 20);

Collections.sort(list);

System.out.println(list);
```

**Output:**
```
[10, 20, 30]
```

Default sorting is **ascending order** because `Integer` already implements `Comparable`.

---

## 4. Traditional Comparator Implementation

Before Java 8, we usually created **separate classes**.

### Example — Sort Strings by Length

```java
import java.util.*;

class StringLengthComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}

public class Main {

    public static void main(String[] args) {

        List<String> words = Arrays.asList(
                "Java",
                "SpringBoot",
                "C"
        );

        Collections.sort(words, new StringLengthComparator());

        System.out.println(words);
    }
}
```

**Output:**
```
[C, Java, SpringBoot]
```

### Why This Works?

Suppose:
```
"Java" = 4
"C"    = 1
```

Comparison:
```
4 - 1 = positive → "Java" comes after "C"
```

---

## 5. Ascending vs Descending Logic

### Ascending

```java
return o1 - o2;
```

Smaller elements come first.

### Descending

```java
return o2 - o1;
```

Larger elements come first.

### Example

```java
List<Integer> list = Arrays.asList(10, 30, 20);

list.sort((a, b) -> b - a);

System.out.println(list);
```

**Output:**
```
[30, 20, 10]
```

---

## 6. Lambda Expressions (Java 8+)

Since `Comparator` is a functional interface, **lambda expressions** can be used.

### Old Style

```java
Collections.sort(list, new Comparator<Integer>() {

    @Override
    public int compare(Integer o1, Integer o2) {
        return o1 - o2;
    }
});
```

### Modern Lambda Style

```java
list.sort((a, b) -> a - b);
```

> Much shorter and cleaner.

---

## 7. Sorting Custom Objects

> **Very important interview topic.**

### Student Class Example

```java
class Student {

    private String name;
    private double gpa;

    public Student(String name, double gpa) {
        this.name = name;
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        return name + " " + gpa;
    }
}
```

### Problem

```java
Collections.sort(students); // ❌ Error
```

Because `Student` has no natural ordering — it does **NOT** implement `Comparable`.

### Solution → Comparator

```java
students.sort((s1, s2) ->
    Double.compare(s1.getGpa(), s2.getGpa())
);
```

### Why `Double.compare()`?

**Wrong approach:**

```java
return (int)(s1.getGpa() - s2.getGpa()); // ❌
```

**Problem:** precision loss, incorrect comparisons.

**Correct approach:**

```java
Double.compare(a, b); // ✅ Safely handles decimal values
```

### Example

```java
List<Student> students = new ArrayList<>();

students.add(new Student("Rahul", 8.5));
students.add(new Student("Amit", 9.2));
students.add(new Student("Kiran", 7.8));

students.sort((s1, s2) ->
        Double.compare(s1.getGpa(), s2.getGpa()));

System.out.println(students);
```

**Output:**
```
[Kiran 7.8, Rahul 8.5, Amit 9.2]
```

---

## 8. `Comparator.comparing()`

Java 8 introduced clean utility methods.

### Syntax

```java
Comparator.comparing(keyExtractor)
```

### Example

```java
students.sort(
    Comparator.comparing(Student::getGpa)
);
```

Equivalent to:

```java
(s1, s2) -> Double.compare(s1.getGpa(), s2.getGpa())
```

### Method Reference

```java
Student::getGpa
```

Shortcut for:

```java
student -> student.getGpa()
```

---

## 9. Descending Order

Using `reversed()`:

```java
students.sort(
    Comparator.comparing(Student::getGpa)
              .reversed()
);
```

**Output:** Highest GPA first.

---

## 10. `thenComparing()`

Used for **tie-breaker sorting**.

### Example — Sort by GPA, then by Name if GPA is same

```java
students.sort(
    Comparator.comparing(Student::getGpa)
              .thenComparing(Student::getName)
);
```

### Example Data

```
Rahul 8.5
Amit  8.5
Kiran 9.1
```

**Output:**
```
Amit  8.5
Rahul 8.5
Kiran 9.1
```

> GPA is same → Names sorted alphabetically.

---

## 11. Multiple Comparator Chaining

```java
Comparator.comparing(Student::getDept)
          .thenComparing(Student::getGpa)
          .thenComparing(Student::getName)
```

> Very common in real projects.

---

## 12. Null Handling

Without handling, a `NullPointerException` may occur.

### Safe Null Handling

```java
Comparator.nullsFirst(
    Comparator.comparing(Student::getName)
)
```

**OR**

```java
Comparator.nullsLast(
    Comparator.comparing(Student::getName)
)
```

---

## 13. Comparator vs Comparable

> **Very important interview question.**

### Comparable

| Feature | Comparable |
|---------|-----------|
| Package | `java.lang` |
| Method | `compareTo()` |
| Sorting Type | Natural ordering |
| Modifies Class | Yes |

### Comparator

| Feature | Comparator |
|---------|-----------|
| Package | `java.util` |
| Method | `compare()` |
| Sorting Type | Custom ordering |
| Modifies Class | No |

### Example

**Comparable** — Student class itself defines ordering:

```java
class Student implements Comparable<Student>
```

**Comparator** — Separate sorting logic:

```java
Comparator<Student>
```

---

## 14. Stability in Sorting

Java's sorting algorithms are usually **stable**.

Meaning: **Equal elements keep their original order.**

Important for multi-level sorting.

### Example

Before sorting:
```
Amit  8.5
Rahul 8.5
```

After sorting by GPA:
```
Amit  8.5
Rahul 8.5
```

> Order preserved.

---

## 15. Common Interview Questions

### Q1. Why is Comparator a Functional Interface?

Because it has **one abstract method**: `compare()`.

### Q2. Can we sort objects without `Comparable`?

Yes, using `Comparator`.

### Q3. Which is better: `Comparable` or `Comparator`?

| Use `Comparable` | Use `Comparator` |
|------------------|------------------|
| Single natural ordering | Multiple sorting logics |

### Q4. Why use `Comparator.comparing()`?

Cleaner and more readable code.

### Q5. How to sort in descending order?

```java
.reversed()
```

---

## 16. Complete Real Example

```java
import java.util.*;

class Student {

    private String name;
    private double gpa;

    public Student(String name, double gpa) {
        this.name = name;
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        return name + " " + gpa;
    }
}

public class Main {

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();

        students.add(new Student("Rahul", 8.5));
        students.add(new Student("Amit", 9.1));
        students.add(new Student("Kiran", 8.5));

        students.sort(
                Comparator.comparing(Student::getGpa)
                          .thenComparing(Student::getName)
        );

        System.out.println(students);
    }
}
```

**Output:**
```
[Kiran 8.5, Rahul 8.5, Amit 9.1]
```

---

## 17. Final Revision Notes

### Key Points

- ✅ `Comparator` = custom sorting
- ✅ `compare(o1, o2)` method
- ✅ Negative → `o1` first
- ✅ Positive → `o2` first
- ✅ Lambda supported
- ✅ `Comparator.comparing()` simplifies sorting
- ✅ `thenComparing()` for tie-breakers
- ✅ `reversed()` for descending
- ✅ Stable sorting

### Quick Syntax Revision

**Ascending:**
```java
(a, b) -> a - b
```

**Descending:**
```java
(a, b) -> b - a
```

**Object Sorting:**
```java
Comparator.comparing(Student::getGpa)
```

**Multiple Sorting:**
```java
Comparator.comparing(Student::getGpa)
          .thenComparing(Student::getName)
```
