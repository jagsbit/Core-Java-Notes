# Most Common JPA Annotations (Excluding Relationship Annotations)

These annotations are commonly used while creating entities in JPA/Hibernate.

---

## 1. `@Entity`

Marks a Java class as a **database entity**.

**Meaning:** This class will be mapped to a database table.

```java
import jakarta.persistence.Entity;

@Entity
public class User {

}
```

> Without `@Entity`, JPA will not treat the class as a table.

---

## 2. `@Table`

Used to specify **table details**.

Mostly used when the table name differs from the class name.

```java
@Entity
@Table(name = "users")
public class User {

}
```

> If not provided, the class name becomes the table name by default.

---

## 3. `@Id`

Marks a field as the **Primary Key**.

```java
@Id
private int id;
```

> Every entity must have one primary key.

---

## 4. `@GeneratedValue`

Used for **automatic primary key generation**.

```java
@Id
@GeneratedValue
private int id;
```

Database automatically generates the ID.

### Generation Strategies

#### `AUTO`

```java
@GeneratedValue(strategy = GenerationType.AUTO)
```

JPA chooses the strategy automatically.

#### `IDENTITY`

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

Uses database **auto-increment**.

> Most commonly used in **MySQL**.

#### `SEQUENCE`

```java
@GeneratedValue(strategy = GenerationType.SEQUENCE)
```

Uses a database sequence.

> Mostly used in **Oracle/PostgreSQL**.

#### `TABLE`

```java
@GeneratedValue(strategy = GenerationType.TABLE)
```

Uses a separate table for ID generation.

> Less commonly used.

---

## 5. `@Column`

Used to configure **column properties**.

```java
@Column(name = "user_name")
private String name;
```

### Common Properties

#### Change Column Name

```java
@Column(name = "email_address")
```

#### Make Column Unique

```java
@Column(unique = true)
```

#### Prevent Null Values

```java
@Column(nullable = false)
```

#### Set Length

```java
@Column(length = 100)
```

---

## 6. `@Transient`

Field will **NOT** be stored in the database.

```java
@Transient
private int tempValue;
```

Useful for:

- Temporary calculations
- Helper variables
