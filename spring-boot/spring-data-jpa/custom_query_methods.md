# Spring Data JPA Custom Query Methods

In Spring Data JPA, we can create database queries just by writing method names.

Spring Data JPA:

- Reads the method name
- Understands the keywords
- Identifies entity fields
- Automatically generates SQL/JPQL queries at runtime

This feature is called:

> **Derived Query Methods** or **Query Method Mechanism**

---

## 1. Basic Idea

Suppose we have an entity:

```java
@Entity
public class User {

    @Id
    private int id;

    private String name;

    private int age;

    private String email;

    private boolean active;
}
```

Repository:

```java
public interface UserRepository
       extends JpaRepository<User, Integer> {

}
```

Now we can create custom query methods inside the repository.

---

## 2. Main Rule / Syntax

General structure:

```
<Action>By<Property><Condition><Operator>
```

Example:

```java
findByName(String name)
```

Breakdown:

| Part | Meaning |
|------|---------|
| `find` | Action |
| `By` | Starts condition |
| `Name` | Entity field |

Spring converts it into a query automatically.

---

## 3. Action Keywords

These define the operation type.

| Keyword | Meaning |
|---------|---------|
| `findBy` | Fetch records |
| `readBy` | Fetch records |
| `getBy` | Fetch records |
| `queryBy` | Fetch records |
| `countBy` | Count records |
| `existsBy` | Check existence |
| `deleteBy` | Delete records |

### Examples

```java
findByName(String name)
countByAge(int age)
existsByEmail(String email)
deleteByName(String name)
```

---

## 4. `By` Keyword

`By` separates the **action** from the **condition**.

```java
findByName
```

Means: Find records where `name` matches.

---

## 5. Property Name Rule

After `By`, write the **exact entity field name**.

Entity fields:

```java
private String name;
private int age;
private String email;
```

✅ Valid:

```java
findByName()
findByAge()
findByEmail()
```

❌ Invalid:

```java
findByUsername()
```

> Because `username` field does not exist.

---

## 6. Equality Query (Default)

```java
List<User> findByName(String name);
```

Generated query:

```sql
SELECT * FROM user WHERE name = ?
```

Usage:

```java
userRepository.findByName("John");
```

---

## 7. Multiple Conditions

### AND

```java
List<User> findByNameAndAge(String name, int age);
```

SQL:

```sql
WHERE name = ? AND age = ?
```

### OR

```java
List<User> findByNameOrEmail(String name, String email);
```

SQL:

```sql
WHERE name = ? OR email = ?
```

---

## 8. Comparison Operators

### Greater Than

```java
List<User> findByAgeGreaterThan(int age);
```

SQL: `WHERE age > ?`

### Less Than

```java
List<User> findByAgeLessThan(int age);
```

### Greater Than Equal

```java
List<User> findByAgeGreaterThanEqual(int age);
```

### Between

```java
List<User> findByAgeBetween(int start, int end);
```

SQL: `WHERE age BETWEEN ? AND ?`

---

## 9. Like Operations

### Contains

```java
List<User> findByNameContaining(String word);
```

Example: `findByNameContaining("oh")` matches `John`, `Johnson`

### Starts With

```java
List<User> findByNameStartingWith(String prefix);
```

### Ends With

```java
List<User> findByNameEndingWith(String suffix);
```

---

## 10. Null Checks

### IS NULL

```java
List<User> findByEmailIsNull();
```

### IS NOT NULL

```java
List<User> findByEmailIsNotNull();
```

---

## 11. Boolean Queries

Suppose:

```java
private boolean active;
```

### TRUE

```java
List<User> findByActiveTrue();
```

### FALSE

```java
List<User> findByActiveFalse();
```

---

## 12. Sorting

Rule: `OrderBy<Field><Asc/Desc>`

### Ascending

```java
List<User> findByAgeOrderByNameAsc(int age);
```

SQL: `ORDER BY name ASC`

### Descending

```java
List<User> findByAgeOrderByNameDesc(int age);
```

---

## 13. Count Queries

```java
long countByAge(int age);
```

SQL:

```sql
SELECT COUNT(*) FROM user WHERE age = ?
```

---

## 14. Exists Queries

```java
boolean existsByEmail(String email);
```

Checks whether the record exists.

---

## 15. Delete Queries

```java
void deleteByName(String name);
```

SQL:

```sql
DELETE FROM user WHERE name = ?
```

---

## 16. Top / First Records

### First Record

```java
User findFirstByOrderByAgeDesc();
```

Gets the oldest user.

### Top 3 Records

```java
List<User> findTop3ByOrderByAgeDesc();
```

---

## 17. Ignore Case

```java
List<User> findByNameIgnoreCase(String name);
```

---

## 18. IN Query

```java
List<User> findByAgeIn(List<Integer> ages);
```

SQL:

```sql
WHERE age IN (?, ?, ?)
```

---

## 19. NOT Query

```java
List<User> findByNameNot(String name);
```

---

## 20. Distinct Query

```java
List<User> findDistinctByName(String name);
```

---

## 21. Nested Property Query

Suppose:

```java
class User {
    Address address;
}

class Address {
    String city;
}
```

Method:

```java
findByAddressCity(String city)
```

> Spring navigates nested objects automatically.

---

## 22. Important Rule

Property names must **exactly match** entity field names.

Example:

```java
private String firstName;
```

✅ Correct:

```java
findByFirstName()
```

❌ Wrong:

```java
findByFirstname()
```

> **CamelCase matters.**

---

## 23. Internal Working

Spring internally:

1. Reads method name
2. Identifies keywords
3. Identifies fields
4. Creates proxy implementation
5. Generates JPQL/SQL query

---

## 24. Limitation of Method Queries

Sometimes method names become too large.

Example:

```java
findByNameAndAgeGreaterThanAndEmailContainingOrderByAgeDesc()
```

Hard to read and maintain.

Then we use: **`@Query`**

---

## 25. Using `@Query`

### JPQL Query

```java
@Query("SELECT u FROM User u WHERE u.name = :name")
List<User> getUsersByName(@Param("name") String name);
```

---

## 26. Native SQL Query

```java
@Query(
 value = "SELECT * FROM users WHERE age > ?",
 nativeQuery = true
)
List<User> getUsers(int age);
```

Uses actual SQL query.

---

## 27. Important Keywords Summary

| Keyword | Meaning |
|---------|---------|
| `And` | AND condition |
| `Or` | OR condition |
| `Between` | BETWEEN |
| `LessThan` | `<` |
| `GreaterThan` | `>` |
| `Containing` | Contains |
| `StartingWith` | Starts with |
| `EndingWith` | Ends with |
| `OrderBy` | Sorting |
| `IsNull` | NULL |
| `IsNotNull` | NOT NULL |
| `In` | IN |
| `Not` | NOT |
| `IgnoreCase` | Case insensitive |
| `True` | Boolean true |
| `False` | Boolean false |

---

## 28. Complete Example

### Entity

```java
@Entity
public class Employee {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private double salary;

    private boolean active;
}
```

### Repository

```java
@Repository
public interface EmployeeRepository
       extends JpaRepository<Employee, Integer> {

    List<Employee> findByName(String name);

    List<Employee> findBySalaryGreaterThan(double salary);

    List<Employee> findByNameAndActiveTrue(String name);

    List<Employee> findByNameContaining(String word);

    long countByActiveTrue();

    boolean existsByName(String name);

    List<Employee> findTop3ByOrderBySalaryDesc();
}
```

---

## 29. Golden Rule

> **Method Name = Query**
>
> Spring Data JPA converts method names into queries automatically.
