# Pagination and Sorting in Spring Data JPA

In Spring Data JPA, pagination and sorting are provided automatically through repository methods.

These features come mainly from:

- `PagingAndSortingRepository`
- `JpaRepository`

Since `JpaRepository` extends both, **pagination** and **sorting** are automatically available.

---

## 1. What is Pagination?

Pagination means:

> Fetching data in **small chunks/pages** instead of loading all records at once.

**Example:**

Suppose the database contains **10,000 users**.

Loading all users at once:

- Slow
- High memory usage
- Bad performance

Instead:

```
Page 1 → first 10 users
Page 2 → next 10 users
Page 3 → next 10 users
```

This is **pagination**.

---

## 2. Real World Example

Examples:

- YouTube videos
- Amazon products
- Instagram posts
- Google search results

All use pagination.

---

## 3. Pagination Classes in Spring Data JPA

| Class | Purpose |
|-------|---------|
| `Pageable` | Pagination request |
| `Page` | Paginated response |
| `PageRequest` | Implementation of `Pageable` |

---

## 4. Basic Pagination Example

### Entity

```java
@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private int age;
}
```

### Repository

```java
public interface UserRepository
       extends JpaRepository<User, Integer> {

}
```

No need to write a pagination method manually.

`JpaRepository` already provides:

```java
Page<T> findAll(Pageable pageable)
```

---

## 5. Service Layer Example

```java
Pageable pageable = PageRequest.of(0, 5);

Page<User> page = userRepository.findAll(pageable);
```

---

## 6. Understanding `PageRequest.of()`

```java
PageRequest.of(pageNumber, pageSize)
```

**Example:**

```java
PageRequest.of(0, 5)
```

| Value | Meaning |
|-------|---------|
| `0` | First page |
| `5` | 5 records per page |

---

## 7. Important Point

> Page numbers start from **0**, NOT from 1.

---

## 8. Generated SQL

For MySQL:

```sql
LIMIT 5 OFFSET 0
```

Second page:

```java
PageRequest.of(1, 5)
```

SQL:

```sql
LIMIT 5 OFFSET 5
```

---

## 9. Accessing Page Data

```java
Page<User> page = userRepository.findAll(pageable);
```

### Get Actual Records

```java
List<User> users = page.getContent();
```

### Total Elements

```java
page.getTotalElements();
```

### Total Pages

```java
page.getTotalPages();
```

### Current Page Number

```java
page.getNumber();
```

### Is First Page

```java
page.isFirst();
```

### Is Last Page

```java
page.isLast();
```

---

## 10. Complete Pagination Example

```java
Pageable pageable = PageRequest.of(0, 3);

Page<User> page = userRepository.findAll(pageable);

System.out.println(page.getContent());

System.out.println(page.getTotalPages());

System.out.println(page.getTotalElements());
```

---

## 11. What is Sorting?

Sorting means arranging records in:

- Ascending order
- Descending order

Examples:

- Sort users by age
- Sort products by price
- Sort employees by salary

---

## 12. Sorting Using Spring Data JPA

Spring provides the `Sort` class.

### Example

```java
Sort sort = Sort.by("name");
```

Default: **Ascending order**

---

## 13. Descending Order

```java
Sort sort = Sort.by("name").descending();
```

---

## 14. Fetch Sorted Data

```java
List<User> users = userRepository.findAll(sort);
```

---

## 15. SQL Generated

```sql
ORDER BY name ASC
```

or

```sql
ORDER BY name DESC
```

---

## 16. Multiple Sorting Fields

```java
Sort sort = Sort.by("age")
                .descending()
                .and(Sort.by("name"));
```

SQL:

```sql
ORDER BY age DESC, name ASC
```

---

## 17. Pagination + Sorting Together

Most commonly used.

### Example

```java
Pageable pageable = PageRequest.of(
    0,
    5,
    Sort.by("name").descending()
);

Page<User> page = userRepository.findAll(pageable);
```

---

## 18. Meaning

- First page
- 5 records
- Sorted by name **descending**

---

## 19. Generated SQL

```sql
SELECT * FROM user
ORDER BY name DESC
LIMIT 5 OFFSET 0
```

---

## 20. Custom Pagination Query Method

Pagination also works with custom query methods.

**Example:**

```java
Page<User> findByAgeGreaterThan(
    int age,
    Pageable pageable
);
```

**Usage:**

```java
Pageable pageable = PageRequest.of(0, 5);

Page<User> page =
    userRepository.findByAgeGreaterThan(20, pageable);
```

---

## 21. REST API Example

### Controller

```java
@GetMapping("/users")
public Page<User> getUsers(
        @RequestParam int page,
        @RequestParam int size) {

    Pageable pageable =
            PageRequest.of(page, size);

    return userRepository.findAll(pageable);
}
```

**API Call:**

```
/users?page=0&size=5
```

---

## 22. Why Pagination is Important

**Without pagination:**

- Huge memory usage
- Slow APIs
- Poor performance

**With pagination:**

- Fast response
- Optimized database access
- Scalable applications

---

## 23. Repository Hierarchy Connection

```
Repository
    ↓
CrudRepository
    ↓
PagingAndSortingRepository
    ↓
JpaRepository
```

> Pagination and sorting features come from `PagingAndSortingRepository`.

---

## 24. Important Interfaces

| Interface/Class | Purpose |
|----------------|---------|
| `Pageable` | Pagination request |
| `Page` | Paginated response |
| `PageRequest` | Implementation of `Pageable` |
| `Sort` | Sorting configuration |

---

## 25. Most Important Interview Point

> `JpaRepository` already provides pagination and sorting support through `Pageable` and `Sort`.

---

## 26. Complete Example

### Repository

```java
public interface UserRepository
       extends JpaRepository<User, Integer> {

    Page<User> findByNameContaining(
        String word,
        Pageable pageable
    );
}
```

### Service

```java
Pageable pageable =
    PageRequest.of(
        0,
        5,
        Sort.by("age").descending()
    );

Page<User> users =
    userRepository.findByNameContaining(
        "a",
        pageable
    );
```

---

## 27. Final Summary

| Feature | Purpose |
|---------|---------|
| Pagination | Fetch records page by page |
| Sorting | Arrange records in order |
| `Pageable` | Pagination request |
| `Page` | Pagination response |
| `Sort` | Sorting object |
| `PageRequest` | `Pageable` implementation |

---

## 28. Golden Line

> Spring Data JPA provides built-in pagination and sorting support using `Pageable`, `PageRequest`, and `Sort`.
