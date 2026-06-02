# Java Persistence Ecosystem Notes

---

## 1. Problem Before ORM

Earlier in Java applications, developers directly interacted with databases using:

**JDBC (Java Database Connectivity)**

Example steps:

1. Create connection
2. Write SQL queries
3. Execute query
4. Convert database rows into Java objects manually
5. Close resources

This caused:

- Huge boilerplate code
- Manual mapping
- Difficult maintenance
- Database dependent code

---

## 2. ORM (Object Relational Mapping)

### What is ORM?

ORM is a technique used to map:

```
Java Objects ↔ Database Tables
```

Meaning:

| Java | Database |
|------|----------|
| Java class | Table |
| Object | Row |
| Variables | Columns |

**Example:**

```java
class User {
    int id;
    String name;
}
```

Can be mapped to:

| id | name |
|----|------|
| 1  | John |

ORM frameworks automatically convert:

- Java objects → SQL queries
- SQL results → Java objects

**Benefits:**

- Less SQL writing
- Cleaner code
- Database independence
- Easier maintenance

---

## 3. JPA (Java Persistence API)

### What is JPA?

> **Jakarta Persistence** is a **specification**.

JPA is:

- ❌ NOT a framework
- ❌ NOT an implementation

It only defines:

- Rules
- Interfaces
- Annotations
- Standards for ORM

### JPA Provides

**Interfaces:**

- `EntityManager`
- `EntityTransaction`
- `Query`

**Annotations:**

- `@Entity`
- `@Table`
- `@Id`
- `@Column`
- `@GeneratedValue`

### Example Entity

```java
@Entity
public class User {

    @Id
    private int id;

    private String name;
}
```

JPA says:

- How entities should behave
- How ORM communication should happen

> But JPA itself **cannot perform database operations**. It needs an **implementation**.

---

## 4. Hibernate

### What is Hibernate?

> **Hibernate** is an ORM framework.

Hibernate:

- Implements JPA specification
- Provides actual ORM functionality

So Hibernate is:

- **JPA Implementation**
- **ORM Tool**

### Responsibilities of Hibernate

Hibernate internally:

- Generates SQL queries
- Manages objects
- Maps tables ↔ objects
- Handles caching
- Manages relationships
- Performs CRUD operations

### Example Flow

```
Java Object
    ↓
Hibernate
    ↓
SQL Query
    ↓
Database
```

### Without Hibernate (Using only JDBC)

```java
String sql = "insert into users values (?, ?)";
PreparedStatement ps = con.prepareStatement(sql);
```

Large amount of manual code.

### With Hibernate

```java
session.save(user);
```

Hibernate generates SQL automatically.

---

## 5. Other JPA Implementations

Besides Hibernate:

- EclipseLink
- OpenJPA
- DataNucleus

All implement JPA specification.

> But **Hibernate is the most popular**.

---

## 6. Problem Even After Hibernate + JPA

Even with Hibernate, developers still had to write:

- DAO classes
- `EntityManager` code
- Transaction handling
- CRUD methods
- Pagination logic
- Sorting logic

**Example:**

```java
public void save(User user) {
    entityManager.persist(user);
}
```

Repeated in every project.

> Still too much **boilerplate**.

---

## 7. Spring Data

### What is Spring Data?

> **Spring Data** is a Spring project.

**Purpose:** Simplify database access.

It provides:

- Common repository abstraction
- Automatic implementation generation
- Less boilerplate code

### Spring Data Modules

| Module | Database Type |
|--------|--------------|
| Spring Data JPA | Relational DB |
| Spring Data MongoDB | MongoDB |
| Spring Data Redis | Redis |
| Spring Data Cassandra | Cassandra |
| Spring Data Elasticsearch | Elasticsearch |

---

## 8. Spring Data JPA

### What is Spring Data JPA?

> **Spring Data JPA** is a module of Spring Data.

It is built on top of:

- JPA
- Hibernate

**Purpose:** Reduce boilerplate code further.

---

## 9. Main Idea of Spring Data JPA

Instead of writing implementation manually:

```java
public class UserDao {

    public void save(User user) {
        entityManager.persist(user);
    }
}
```

We simply create an interface:

```java
public interface UserRepository
       extends JpaRepository<User, Integer> {

}
```

> Spring **automatically creates implementation** at runtime.

---

## 10. How Spring Data JPA Works

Internally:

- Spring creates proxy classes
- Proxy provides implementation
- Uses Hibernate internally
- Hibernate communicates with database

### Internal Flow

```
Application
    ↓
Spring Data JPA
    ↓
Hibernate (JPA Implementation)
    ↓
Database
```

---

## 11. JpaRepository

`JpaRepository` provides ready-made methods.

Examples:

- `save()`
- `findAll()`
- `findById()`
- `deleteById()`
- `count()`
- `existsById()`

> No implementation required.

---

## 12. Repository Hierarchy

```
Repository
    ↓
CrudRepository
    ↓
PagingAndSortingRepository
    ↓
JpaRepository
```

### `Repository`

Marker interface. Tells Spring:

> "This interface is a repository layer."

### `CrudRepository`

Provides:

- Create
- Read
- Update
- Delete methods

### `PagingAndSortingRepository`

Adds:

- Pagination
- Sorting support

### `JpaRepository`

Adds:

- JPA specific features
- Batch operations
- Flushing
- Advanced support

> Most commonly used.

---

## 13. Custom Query Methods

Spring Data JPA can **generate queries from method names**.

**Example:**

```java
List<User> findByName(String name);
```

Spring internally generates SQL automatically.

---

## 14. Important Annotations

### `@Entity`

Marks class as a database entity.

```java
@Entity
class User {}
```

### `@Id`

Marks the primary key.

```java
@Id
private int id;
```

### `@GeneratedValue`

Auto increment ID.

```java
@GeneratedValue
private int id;
```

### `@Table`

Specify table name.

```java
@Table(name = "users")
```

### `@Column`

Specify column details.

```java
@Column(name = "user_name")
```

---

## 15. Complete Ecosystem Relationship

```
JPA
 ↓
Specification

Hibernate
 ↓
Implementation of JPA

Spring Data
 ↓
Spring project for simplifying data access

Spring Data JPA
 ↓
Module built on top of JPA/Hibernate
```

---

## 16. Final Summary

| Technology | Type | Purpose |
|------------|------|---------|
| JDBC | API | Direct database communication |
| ORM | Technique | Object ↔ Table mapping |
| JPA | Specification | ORM standards |
| Hibernate | Framework | JPA implementation |
| Spring Data | Spring Project | Simplify data access |
| Spring Data JPA | Module | Simplify JPA/Hibernate usage |

---

## 17. One-Line Definitions

### JPA
> JPA is a specification that defines standards for ORM in Java.

### Hibernate
> Hibernate is an ORM framework and implementation of JPA.

### Spring Data
> Spring Data is a Spring project that simplifies database access.

### Spring Data JPA
> Spring Data JPA is a Spring Data module that reduces boilerplate code for JPA/Hibernate.
