# Relationship Mapping in Spring Data JPA

Relationship mapping in Spring Data JPA and Hibernate is used to define associations between entities.

Examples:

- One department has many employees
- One person has one passport
- One student enrolls in many courses

JPA provides annotations to map these relationships.

---

## 1. Types of Relationships

| Relationship | Meaning |
|-------------|---------|
| `@OneToOne` | One entity related to one entity |
| `@OneToMany` | One entity related to many entities |
| `@ManyToMany` | Many entities related to many entities |

---

## 2. One-to-One Relationship (`@OneToOne`)

### Meaning

One record associated with exactly one record.

**Example:**

```
Person ↔ Passport
One person has one passport
One passport belongs to one person
```

### Entity Example

#### Person Entity

```java
@Entity
public class Person {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @OneToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;
}
```

#### Passport Entity

```java
@Entity
public class Passport {

    @Id
    @GeneratedValue
    private int id;

    private String passportNo;
}
```

### MySQL Table Structure

**`desc person`**

| Field | Type | Key |
|-------|------|-----|
| id | int | PRI |
| name | varchar(255) | |
| passport_id | int | MUL |

**`desc passport`**

| Field | Type | Key |
|-------|------|-----|
| id | int | PRI |
| passport_no | varchar(255) | |

### Understanding `@JoinColumn`

```java
@JoinColumn(name = "passport_id")
```

Creates a foreign key column `passport_id` inside the **person table**.

### Default Fetch Type

> For `@OneToOne`: **EAGER** — passport loads immediately with person.

---

## 3. One-to-Many Relationship (`@OneToMany`)

### Meaning

One entity associated with many entities.

**Example:**

```
Department → Employees
One department has many employees.
```

### Important Concept

> In `OneToMany`, the **foreign key is stored on the many side table**.
> So: `employee` table stores `department_id`.

### Entity Example

#### Department Entity

```java
@Entity
public class Department {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;
}
```

#### Employee Entity

```java
@Entity
public class Employee {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @JoinColumn(name = "department_id")
    private Department department;
}
```

### MySQL Table Structure

**`desc department`**

| Field | Type | Key |
|-------|------|-----|
| id | int | PRI |
| name | varchar(255) | |

**`desc employee`**

| Field | Type | Key |
|-------|------|-----|
| id | int | PRI |
| name | varchar(255) | |
| department_id | int | MUL |

### Understanding `mappedBy`

```java
mappedBy = "department"
```

Means: Relationship is managed by the `Employee` entity.

> Prevents Hibernate from creating an extra join table.

### What Happens Without `mappedBy`?

Hibernate creates an extra mapping table unnecessarily.

Example: `department_employees` — which is not required.

### Default Fetch Type

> For `@OneToMany`: **LAZY** — employees load only when needed.

---

## 4. Many-to-Many Relationship (`@ManyToMany`)

### Meaning

Many entities associated with many entities.

**Example:**

```
Students ↔ Courses
One student can join many courses
One course can contain many students
```

### Important Concept

> A single foreign key is NOT enough. So Hibernate creates a **Join Table**.

### Entity Example

#### Student Entity (Owning Side)

```java
@Entity
public class Student {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @ManyToMany
    @JoinTable(
        name = "student_course",

        joinColumns =
            @JoinColumn(name = "student_id"),

        inverseJoinColumns =
            @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```

#### Course Entity (Inverse Side)

```java
@Entity
public class Course {

    @Id
    @GeneratedValue
    private int id;

    private String title;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
}
```

### MySQL Table Structure

**`desc student`**

| Field | Type | Key |
|-------|------|-----|
| id | int | PRI |
| name | varchar(255) | |

**`desc course`**

| Field | Type | Key |
|-------|------|-----|
| id | int | PRI |
| title | varchar(255) | |

**`desc student_course`**

| Field | Type | Key |
|-------|------|-----|
| student_id | int | MUL |
| course_id | int | MUL |

### Understanding `@JoinTable`

```java
@JoinTable(
    name = "student_course",

    joinColumns =
        @JoinColumn(name = "student_id"),

    inverseJoinColumns =
        @JoinColumn(name = "course_id")
)
```

| Part | Meaning |
|------|---------|
| `name` | Join table name |
| `joinColumns` | Current entity foreign key |
| `inverseJoinColumns` | Other entity foreign key |

### Important Rule

In bidirectional `ManyToMany`, **one side must use `mappedBy`**.

Otherwise Hibernate creates two join tables: `student_course` and `course_student`.

✅ Correct:

```java
// Owning side
@ManyToMany
@JoinTable(...)

// Inverse side
@ManyToMany(mappedBy = "courses")
```

### Default Fetch Type

> For `@ManyToMany`: **LAZY** — related entities load only when needed.

---

## 5. Fetch Types

Fetch type decides when related entities should load.

### Lazy Fetching

```java
fetch = FetchType.LAZY
```

Related entities load **only when accessed**.

```java
@OneToMany(fetch = FetchType.LAZY)
private List<Employee> employees;
```

Employees load only when `department.getEmployees()` is called.

### Eager Fetching

```java
fetch = FetchType.EAGER
```

Related entities **load immediately**.

```java
@OneToOne(fetch = FetchType.EAGER)
private Passport passport;
```

### Comparison

| | Lazy | Eager |
|-|------|-------|
| Loading | When needed | Immediately |
| Performance | Better | More memory usage |
| Optimization | Optimized | Slower sometimes |

### Default Fetch Types

| Relationship | Default Fetch |
|-------------|--------------|
| `@OneToMany` | LAZY |
| `@ManyToMany` | LAZY |
| `@OneToOne` | EAGER |

---

## 6. Cascade Types

Cascade means operations **automatically propagate** to child entities.

**Example:** Saving a department should also save employees automatically.

### Syntax

```java
@OneToMany(cascade = CascadeType.ALL)
```

### Cascade Types

| Cascade Type | Meaning |
|-------------|---------|
| `ALL` | All operations |
| `PERSIST` | Save operation |
| `MERGE` | Update operation |
| `REMOVE` | Delete operation |
| `REFRESH` | Refresh entity |
| `DETACH` | Detach entity |

### Example

```java
@OneToMany(
    mappedBy = "department",
    cascade = CascadeType.ALL
)
private List<Employee> employees;
```

**Meaning:**

- If department is saved → employees also save.
- If department is deleted → employees also delete.

---

## 7. Unidirectional Relationship

Only **one entity** knows the relationship.

```java
@OneToOne
private Passport passport;
```

Navigation: `Person → Passport` (one direction only)

---

## 8. Bidirectional Relationship

**Both entities** know the relationship.

```java
// In Department
@OneToMany(mappedBy = "department")
private List<Employee> employees;

// In Employee
private Department department;
```

Navigation: `Department ↔ Employee` (both directions)

### Comparison

| | Unidirectional | Bidirectional |
|-|---------------|---------------|
| Navigation | One-way | Two-way |
| Complexity | Simpler | More flexible |

---

## 9. Common Problems

### Infinite Recursion

In bidirectional relationships:

- Parent contains child
- Child contains parent

Can cause `StackOverflowError`, especially in REST APIs.

**Solved using:**

- `@JsonIgnore`
- `@JsonManagedReference`
- `@JsonBackReference`

### `LazyInitializationException`

Occurs when a lazy object is accessed after the session is closed.

---

## 10. Best Practices

### Prefer LAZY Fetching

Better performance — loads only what is needed.

### Use `mappedBy`

To avoid duplicate join tables and define ownership.

### Avoid `CascadeType.ALL` Everywhere

Accidental deletes may propagate to child entities.

### Use DTOs in APIs

Avoid exposing entities directly to prevent recursion and over-fetching.

---

## 11. Important Interview Points

### Where is the Foreign Key Stored?

| Relationship | Foreign Key Location |
|-------------|---------------------|
| `OneToOne` | Owning side |
| `OneToMany` | Many side |
| `ManyToMany` | Join table |

### Why Does `@ManyToMany` Need a Join Table?

> Because both entities can have multiple associations, a single foreign key column is not sufficient.

### What Does `mappedBy` Mean?

> The relationship is managed by another entity (the owning side).

---

## 12. Final Summary Table

| Annotation | Purpose |
|------------|---------|
| `@OneToOne` | One-to-one mapping |
| `@OneToMany` | One-to-many mapping |
| `@ManyToMany` | Many-to-many mapping |
| `@JoinColumn` | Foreign key column |
| `@JoinTable` | Join table |
| `mappedBy` | Inverse side |
| `FetchType.LAZY` | Load when needed |
| `FetchType.EAGER` | Immediate loading |
| `CascadeType.ALL` | Cascade all operations |

---

## 13. Golden Lines

> - `@OneToMany` stores the foreign key on the **many side table**.
> - `@ManyToMany` creates a **separate join table**.
> - `mappedBy` prevents **duplicate relationship mappings**.
> - **LAZY fetching** is generally preferred for better performance.
