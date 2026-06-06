# Understanding Collectors.groupingBy() Thoroughly in Java Streams

`groupingBy()` is one of the **MOST IMPORTANT** collectors in Java Streams.

It is heavily used in:

- Backend development
- Spring Boot projects
- Data aggregation
- Reporting
- Analytics
- Interview questions

---

## What does groupingBy() do?

It **groups elements** based on some condition/key.

Think of it like SQL:

```sql
GROUP BY
```

---

## Real-Life Analogy

Suppose you have students:

| Name  | Department |
|-------|------------|
| Ram   | CSE        |
| Shyam | ECE        |
| Aman  | CSE        |
| Ravi  | ECE        |

After grouping by department:

```
CSE -> [Ram, Aman]
ECE -> [Shyam, Ravi]
```

---

## Basic Syntax

```java
Collectors.groupingBy(classifier)
```

---

## Important Concept

`groupingBy()` returns a:

```
Map<Key, List<Value>>
```

because elements are grouped into lists.

---

## First Example — Group Even and Odd Numbers

```java
List<Integer> list = List.of(1, 2, 3, 4, 5, 6);

Map<String, List<Integer>> result =
    list.stream()
        .collect(Collectors.groupingBy(
            x -> x % 2 == 0 ? "Even" : "Odd"
        ));

System.out.println(result);
```

### Output:

```
{
 Odd=[1, 3, 5],
 Even=[2, 4, 6]
}
```

### Understanding Step-by-Step

For each element:

```
1 -> Odd
2 -> Even
3 -> Odd
4 -> Even
```

Java automatically creates groups.

---

## Structure of groupingBy

```java
groupingBy(
    classifierFunction
)
```

The classifier decides:

> "Which group does this element belong to?"

---

## Most Common Real Example — Group Employees by Department

### Employee Class

```java
class Employee {
    String name;
    String department;

    Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public String toString() {
        return name;
    }
}
```

### Usage

```java
List<Employee> employees = List.of(
    new Employee("Ram", "IT"),
    new Employee("Shyam", "HR"),
    new Employee("Aman", "IT")
);

Map<String, List<Employee>> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment
             ));

System.out.println(result);
```

### Output:

```
{
 HR=[Shyam],
 IT=[Ram, Aman]
}
```

---

## VERY IMPORTANT — Default Return Type

```
Map<K, List<T>>
```

| Symbol | Meaning              |
|--------|----------------------|
| K      | Group key            |
| T      | Original element type |

---

## How Java Internally Thinks

Java internally does something like:

```java
Map<String, List<Employee>> map = new HashMap<>();

for (Employee e : employees) {

    String key = e.getDepartment();

    if (!map.containsKey(key)) {
        map.put(key, new ArrayList<>());
    }

    map.get(key).add(e);
}
```

`groupingBy()` **automates** this.

---

## Multi-Level Grouping

> Very important interview topic.

Suppose `Employee` has:
- `department`
- `gender`

You can group **twice**.

### Example

```java
Map<String, Map<String, List<Employee>>> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.groupingBy(Employee::getGender)
             ));
```

### Output Structure

```
Department
    -> Gender
           -> List<Employee>
```

Example:

```
IT
   Male   -> [...]
   Female -> [...]

HR
   Male   -> [...]
```

---

## Downstream Collectors

This is the **REAL POWER** of `groupingBy`.

### Syntax:

```java
groupingBy(classifier, downstreamCollector)
```

Instead of `List`, you can collect something else.

---

### Example — Count Employees per Department

```java
Map<String, Long> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.counting()
             ));
```

#### Output:

```
{
 IT=2,
 HR=1
}
```

---

### Example — Average Salary by Department

```java
Map<String, Double> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.averagingDouble(Employee::getSalary)
             ));
```

---

### Example — Sum Salary by Department

```java
Map<String, Double> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.summingDouble(Employee::getSalary)
             ));
```

---

### Example — Find Max Salary Employee Per Department

```java
Map<String, Optional<Employee>> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.maxBy(
                     Comparator.comparing(Employee::getSalary)
                 )
             ));
```

> **Why `Optional`?**
> Because a group may theoretically be empty.

---

## mapping() inside groupingBy()

> Very important concept.

Suppose you only want employee **names**.

Instead of:

```
IT -> [Employee objects]
```

you want:

```
IT -> [Ram, Aman]
```

### Example

```java
Map<String, List<String>> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.mapping(
                     Employee::getName,
                     Collectors.toList()
                 )
             ));
```

### Output

```
{
 IT=[Ram, Aman],
 HR=[Shyam]
}
```

---

## groupingBy with Set

Avoid duplicates.

```java
Map<String, Set<String>> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 Collectors.mapping(
                     Employee::getName,
                     Collectors.toSet()
                 )
             ));
```

---

## groupingBy with TreeMap

By default, **`HashMap`** is used. You can change it.

### Example

```java
Map<String, List<Employee>> result =
    employees.stream()
             .collect(Collectors.groupingBy(
                 Employee::getDepartment,
                 TreeMap::new,
                 Collectors.toList()
             ));
```

### Full Syntax

```java
groupingBy(
    classifier,
    mapFactory,
    downstreamCollector
)
```

---

## Three Versions of groupingBy

### 1. Basic

```java
groupingBy(classifier)
```

Returns:

```
Map<K, List<T>>
```

### 2. With downstream collector

```java
groupingBy(classifier, downstream)
```

### 3. Full version

```java
groupingBy(classifier, mapFactory, downstream)
```

---

## Difference Between partitioningBy and groupingBy

> Very common interview question.

### partitioningBy

Only **TWO** groups: `true` / `false`

```java
Collectors.partitioningBy(x -> x % 2 == 0)
```

Result:

```
Map<Boolean, List<Integer>>
```

### groupingBy

Can create **MANY** groups.

```java
Collectors.groupingBy(x -> x % 3)
```

Output:

```
0 -> [...]
1 -> [...]
2 -> [...]
```

---

## Real Backend Use Cases

### 1. Orders by Status

```
PENDING   -> [...]
DELIVERED -> [...]
CANCELLED -> [...]
```

### 2. Students by Grade

```
A -> [...]
B -> [...]
C -> [...]
```

### 3. Blog Posts by Category

```
Tech    -> [...]
Sports  -> [...]
Finance -> [...]
```

### 4. Users by Country

```
India -> [...]
USA   -> [...]
Japan -> [...]
```

---

## Performance Note

`groupingBy()` internally uses:

```
HashMap
```

Operations are generally **O(1)** average time.

---

## Most Important Patterns to Remember

### Group into Lists

```java
groupingBy(Employee::getDepartment)
```

### Count

```java
groupingBy(
    Employee::getDepartment,
    counting()
)
```

### Sum

```java
groupingBy(
    Employee::getDepartment,
    summingInt(Employee::getSalary)
)
```

### Average

```java
groupingBy(
    Employee::getDepartment,
    averagingDouble(Employee::getSalary)
)
```

### Convert Values

```java
groupingBy(
    Employee::getDepartment,
    mapping(Employee::getName, toList())
)
```

---

## Interview-Level Summary

> `groupingBy` means:
> **Create groups based on a key and collect values inside each group**

### Mental Model

Think:

```
KEY -> COLLECTION OF VALUES
```

Examples:

```
Department -> Employees
Category   -> Products
Country    -> Users
Grade      -> Students
```
