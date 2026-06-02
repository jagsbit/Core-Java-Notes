# Repositories in Spring Data JPA

In Spring Data JPA, repositories are interfaces used for database operations.

Repositories help us:

- Avoid boilerplate DAO code
- Automatically get CRUD methods
- Generate queries automatically
- Support pagination and sorting

> Spring automatically creates implementation classes at **runtime**.

---

## 1. What is Repository?

Repository is a layer between:

- Application
- Database

It handles:

- Data access logic
- Database communication

---

## 2. Repository Hierarchy

```
Repository
    ↓
CrudRepository
    ↓
PagingAndSortingRepository
    ↓
JpaRepository
```

---

## 3. `Repository`

Basic **marker interface**.

```java
public interface UserRepository
       extends Repository<User, Integer> {

}
```

**Purpose:** Tells Spring this interface belongs to the repository layer.

> Usually not used directly.

---

## 4. `CrudRepository`

Provides basic **CRUD operations**.

```java
public interface UserRepository
       extends CrudRepository<User, Integer> {

}
```

### Common CRUD Methods

| Method | Purpose |
|--------|---------|
| `save()` | Insert / update |
| `findById()` | Fetch by ID |
| `findAll()` | Fetch all |
| `deleteById()` | Delete by ID |
| `existsById()` | Check existence |
| `count()` | Count records |

---

## 5. Example

```java
@Repository
public interface UserRepository
       extends CrudRepository<User, Integer> {

}
```

**Usage:**

```java
userRepository.save(user);

userRepository.findById(1);

userRepository.deleteById(1);
```

---

## 6. `PagingAndSortingRepository`

Extends `CrudRepository` and adds:

- Pagination
- Sorting

```java
public interface UserRepository
       extends PagingAndSortingRepository<User, Integer> {

}
```

### Pagination Example

```java
Pageable pageable =
        PageRequest.of(0, 5);

Page<User> page =
        userRepository.findAll(pageable);
```

### Sorting Example

```java
Sort sort = Sort.by("name");

userRepository.findAll(sort);
```

---

## 7. `JpaRepository`

**Most commonly used** repository.

Extends `PagingAndSortingRepository` and provides:

- CRUD
- Pagination
- Sorting
- Batch operations
- Flushing
- JPA-specific features

```java
public interface UserRepository
       extends JpaRepository<User, Integer> {

}
```

### Additional Methods in `JpaRepository`

| Method | Purpose |
|--------|---------|
| `flush()` | Synchronize with DB |
| `saveAndFlush()` | Save immediately |
| `deleteAllInBatch()` | Batch delete |
| `getReferenceById()` | Lazy reference |

---

## 8. Most Commonly Used Repository

In real projects, **`JpaRepository`** is used most of the time.

Because it already includes:

- CRUD
- Pagination
- Sorting
