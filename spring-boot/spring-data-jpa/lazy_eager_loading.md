# Lazy Loading vs Eager Loading in Spring Data JPA

In Spring Data JPA and Hibernate, entities can have relationships.

**Example:**

```
Department → Employees
Student → Courses
Person → Passport
```

Now the important question:

> When should related data be loaded from the database?

This is controlled using:

- **Lazy Loading**
- **Eager Loading**

These are called: **Fetch Strategies**

---

## 1. What is Loading?

Suppose `Department` has `Employees`.

When you fetch a department:

```java
Department department =
    departmentRepository.findById(1).get();
```

Should employees also come immediately?
OR should employees load only when needed?

This decision is: **Lazy** or **Eager**

---

## 2. Eager Loading

### Meaning

Related entities load **immediately** along with the parent entity.

### Example

```java
@OneToOne(fetch = FetchType.EAGER)
private Passport passport;
```

### Scenario

Suppose: `Person → Passport`

You fetch person:

```java
Person person =
    personRepository.findById(1).get();
```

Hibernate automatically loads **both** `person` and `passport` at the same time.

### SQL Generated

```sql
-- Query 1
SELECT * FROM person WHERE id = 1;

-- Query 2
SELECT * FROM passport WHERE id = ?;
```

Or sometimes a single JOIN query.

### Important Point

> Even if you never call `person.getPassport()`, the passport is **already loaded**.

---

## 3. Lazy Loading

### Meaning

Related entities load **only when actually needed**.

### Example

```java
@OneToMany(fetch = FetchType.LAZY)
private List<Employee> employees;
```

### Scenario

Suppose: `Department → Employees`

You fetch department:

```java
Department department =
    departmentRepository.findById(1).get();
```

Initially:

- Only `department` loads
- Employees are **NOT loaded**

### SQL Generated Initially

```sql
SELECT * FROM department WHERE id = 1;
```

Only one query.

### When Do Employees Load?

Only when:

```java
department.getEmployees();
```

is called. Then Hibernate fires a second query:

```sql
SELECT * FROM employee
WHERE department_id = 1;
```

---

## 4. Visual Understanding

### Eager Loading

```
Fetch Department
       ↓
Fetch Employees also

Everything loaded immediately.
```

### Lazy Loading

```
Fetch Department
       ↓
Employees NOT loaded

Later:
department.getEmployees()
       ↓
Employees loaded
```

---

## 5. Why Lazy Loading Exists?

Suppose a department has **10,000 employees**.

If eager loading is used:

- All employees load immediately
- High memory usage
- Slower performance

> Lazy loading improves performance.

---

## 6. Default Fetch Types

| Relationship | Default Fetch |
|-------------|--------------|
| `@OneToOne` | EAGER |
| `@OneToMany` | LAZY |
| `@ManyToMany` | LAZY |

**Why?**

- `@OneToOne` usually involves small data
- `@OneToMany` can contain huge collections

---

## 7. Example of Eager Problem

Suppose:

```
Department → Employees → Projects → Tasks
```

If everything is eager:

- Huge object graph loads
- Many SQL queries generated

This causes **performance issues**, known as the **N+1 Query Problem**.

---

## 8. Lazy Loading Internally

Hibernate uses **Proxy Objects**.

Initially:

- Actual entity is NOT loaded
- A proxy placeholder is created

When accessed (`department.getEmployees()`):

- Hibernate replaces proxy with real data

---

## 9. What is a Proxy?

> A proxy is a **temporary fake object** used until actual data is needed.

---

## 10. Important Example

### Entity

```java
@Entity
public class Department {

    @Id
    private int id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Employee> employees;
}
```

### Code

```java
Department department =
    departmentRepository.findById(1).get();

System.out.println(department.getId());
// Employees NOT loaded yet
```

### When Are Employees Loaded?

```java
department.getEmployees();
// NOW employees load
```

---

## 11. `LazyInitializationException`

> Most important interview topic.

### Why It Happens

```java
Department department =
    departmentRepository.findById(1).get();

// Session closes

department.getEmployees();
// Hibernate cannot fetch — session already closed
// LazyInitializationException
```

---

## 12. Exception Flow

```
Fetch Department
      ↓
Session Closed
      ↓
Access Lazy Employees
      ↓
LazyInitializationException
```

---

## 13. How to Solve `LazyInitializationException`

### Solution 1: Access Inside Transaction

```java
@Transactional
public void getDepartment() {

    Department d =
        departmentRepository.findById(1).get();

    d.getEmployees();
}
```

### Solution 2: Use `JOIN FETCH`

```java
@Query("""
SELECT d
FROM Department d
JOIN FETCH d.employees
WHERE d.id = :id
""")
Department getDepartment(int id);
```

### Solution 3: DTO Projection

Fetch only required data using DTOs.

---

## 14. Why Eager is Dangerous?

Suppose:

```
User → Orders → Products → Reviews
```

If everything is eager:

- Huge nested loading
- Many joins generated
- Slow APIs

---

## 15. Why Lazy is Preferred?

Load only required data.

**Benefits:**

- Better performance
- Less memory usage
- Optimized queries

---

## 16. Real World Analogy

| | Analogy |
|-|---------|
| **Eager Loading** | Ordering a full meal immediately even before you know what you need |
| **Lazy Loading** | Order items only when you need them |

---

## 17. When to Use Eager?

Use **Eager** when:

- Related data is always needed
- Relationship is small
- `@OneToOne` usually

**Example:** `Person → Passport`

---

## 18. When to Use Lazy?

Use **Lazy** when:

- Collections are involved
- Large datasets
- Better performance needed

**Example:** `Department → Employees`, `Student → Courses`

---

## 19. Best Practice

> Most enterprise applications prefer **LAZY loading by default**, then explicitly fetch needed data.

---

## 20. Important Interview Point

> Lazy loading loads related entities **only when accessed**, while eager loading loads related entities **immediately** with the parent entity.

---

## 21. Final Summary Table

| | Lazy Loading | Eager Loading |
|-|-------------|---------------|
| Loading Time | When needed | Immediately |
| Performance | Better | Can reduce performance |
| Memory | Efficient | Higher usage |
| Internal Mechanism | Uses proxy objects | Directly loads objects |
| Risk | `LazyInitializationException` | Avoids lazy exception |

---

## 22. Deep Dive: EAGER vs LAZY with `findAll()`

### Scenario

One `Student` has many `Courses`.

### Entities

```java
@Entity
public class Student {

    @Id
    private int id;

    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Course> courses;
}
```

```java
@Entity
public class Course {

    @Id
    private int id;

    private String title;
}
```

### Database Data

**student**

| id | name |
|----|------|
| 1 | John |
| 2 | Mike |

**course**

| id | title |
|----|-------|
| 101 | Java |
| 102 | Spring |
| 103 | DBMS |

**student_course**

| student_id | course_id |
|------------|-----------|
| 1 | 101 |
| 1 | 102 |
| 2 | 103 |

### You Execute

```java
List<Student> students =
    studentRepository.findAll();
```

---

### CASE 1 → EAGER Loading

```java
@OneToMany(fetch = FetchType.EAGER)
```

**What Hibernate Does:**

```sql
-- First: load all students
SELECT * FROM student;
```

Then because fetch type is **EAGER**, Hibernate also loads courses for every student.

```sql
-- For John
SELECT c.*
FROM course c
JOIN student_course sc ON c.id = sc.course_id
WHERE sc.student_id = 1;

-- For Mike
SELECT c.*
FROM course c
JOIN student_course sc ON c.id = sc.course_id
WHERE sc.student_id = 2;
```

**Total Queries:**

```
1 query for students
+
N queries for courses (one per student)
```

| Students | Total Queries |
|----------|--------------|
| 2 students | 3 queries |
| 100 students | 101 queries |

> This is called the **N+1 Query Problem**.

**Final Objects in Memory:**

After `findAll()`, students **already contain courses**.

```java
students.get(0).getCourses();
// Does NOT hit database again — already loaded
```

---

### CASE 2 → LAZY Loading

```java
@OneToMany(fetch = FetchType.LAZY)
```

**You Execute:**

```java
List<Student> students =
    studentRepository.findAll();
```

**SQL Generated — ONLY:**

```sql
SELECT * FROM student;
```

Courses are **NOT loaded**. Hibernate creates **proxy objects** instead.

**Now Suppose You Access:**

```java
students.get(0).getCourses();
```

Hibernate fires a query NOW:

```sql
SELECT c.*
FROM course c
JOIN student_course sc ON c.id = sc.course_id
WHERE sc.student_id = 1;
```

**Accessing Second Student:**

```java
students.get(1).getCourses();
```

Another query:

```sql
SELECT c.*
FROM course c
JOIN student_course sc ON c.id = sc.course_id
WHERE sc.student_id = 2;
```

---

### EAGER vs LAZY — Key Difference

| | EAGER | LAZY |
|-|-------|------|
| Queries happen | Immediately during `findAll()` | Only when `getCourses()` is called |
| Data in memory | Full object graph | Only parent entity |

---

### Visual Understanding

```
EAGER
findAll()
   ↓
students loaded
courses loaded (immediately)

LAZY
findAll()
   ↓
students loaded only

getCourses()
   ↓
courses loaded later (on demand)
```

---

### Real-World Problem

Suppose:

- **1000 students**, each has **20 courses**

With **EAGER**:

- Huge data loads immediately even if frontend only needs student names
- Performance becomes bad

---

### Why LAZY is Preferred

- Load data only when required
- Less memory usage
- Optimized queries
- Faster APIs

---

### But LAZY Also Has a Problem

```java
List<Student> students =
    studentRepository.findAll();

// Transaction/session closes

students.get(0).getCourses();
// LazyInitializationException — session already closed
```

---

## 23. Golden Lines

> - With **EAGER** loading, related entities are fetched **immediately** during parent fetch.
> - With **LAZY** loading, related entities are fetched **only when accessed**.
> - LAZY loading is generally preferred in enterprise applications for **better performance**.
> - EAGER loading with collections can cause the **N+1 Query Problem**.
> - Always use `@Transactional` or `JOIN FETCH` when accessing lazy collections.
