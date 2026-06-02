# Configuring Spring Data JPA with MySQL

This setup is used to connect:

- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL Database

---

## 1. Required Dependencies

| Dependency | Purpose |
|------------|---------|
| Spring Data JPA | ORM and repository support |
| MySQL Driver | Connect Java application to MySQL |
| Spring Boot Starter Web | Build web application/API |

---

## 2. Maven Dependencies (pom.xml)

Add these dependencies inside `<dependencies></dependencies>`.

### Spring Data JPA Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

Purpose:

- Provides JPA support
- Provides Hibernate internally
- Provides repository support

### MySQL Driver Dependency

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

Purpose:

- Connects application with MySQL database

### Spring Web Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Purpose:

- Build REST APIs/web applications

---

## 3. Complete pom.xml

```xml
<dependencies>

    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- MySQL Driver -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

</dependencies>
```

---

## 4. What Happens Internally?

When we add `spring-boot-starter-data-jpa`, Spring Boot automatically brings:

- Hibernate
- JPA API
- Transaction support
- Spring ORM support

> So usually we do **NOT** add Hibernate dependency manually.

---

## 5. Database Configuration

Configure database connection inside:

```
src/main/resources/application.properties
```

---

## 6. Basic MySQL Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb

spring.datasource.username=root

spring.datasource.password=root

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

## 7. Explanation

### Database URL

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
```

| Part | Meaning |
|------|---------|
| `jdbc:mysql` | JDBC protocol for MySQL |
| `localhost` | Database server |
| `3306` | MySQL default port |
| `mydb` | Database name |

### Username

```properties
spring.datasource.username=root
```

Database username.

### Password

```properties
spring.datasource.password=root
```

Database password.

### Driver Class

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

MySQL JDBC driver class.

---

## 8. JPA / Hibernate Configuration

### Show SQL Queries

```properties
spring.jpa.show-sql=true
```

Shows generated SQL in console. Useful for learning/debugging.

### Format SQL

```properties
spring.jpa.properties.hibernate.format_sql=true
```

Formats SQL properly.

### Database Dialect

```properties
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

Tells Hibernate which SQL syntax to generate.

---

## 9. DDL Auto Configuration

Most important property.

```properties
spring.jpa.hibernate.ddl-auto=update
```

Controls table creation behavior.

---

## 10. `ddl-auto` Options

| Value | Meaning |
|-------|---------|
| `create` | Create tables every time |
| `create-drop` | Create and drop on shutdown |
| `update` | Update existing tables |
| `validate` | Only validate schema |
| `none` | No schema management |

### Most Common

```properties
spring.jpa.hibernate.ddl-auto=update
```

Reason:

- Keeps old data
- Updates schema automatically

---

## 11. Complete `application.properties`

```properties
# Database Configuration

spring.datasource.url=jdbc:mysql://localhost:3306/mydb

spring.datasource.username=root

spring.datasource.password=root

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


# Hibernate Configuration

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## 12. Project Flow After Configuration

```
Spring Boot Application
        ↓
Spring Data JPA
        ↓
Hibernate
        ↓
JDBC Driver
        ↓
MySQL Database
```

---

## 13. Example Entity

```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}
```

---

## 14. Example Repository

```java
public interface UserRepository
       extends JpaRepository<User, Integer> {

}
```

---

## 15. What Happens on Application Start?

Spring Boot:

1. Reads `application.properties`
2. Creates datasource
3. Configures Hibernate
4. Creates `EntityManager`
5. Scans entities
6. Creates tables
7. Creates repository implementations automatically

---

## 16. Auto Configuration

One of the biggest advantages of Spring Boot:

> Very little manual configuration required.

Earlier in Spring: XML configuration was needed.

Now: mostly **properties + annotations**.

---

## 17. Common Errors

### Database Not Created

**Error:**

```
Unknown database 'mydb'
```

**Solution:** Create database manually in MySQL.

```sql
CREATE DATABASE mydb;
```

### Wrong Password

**Error:**

```
Access denied for user
```

**Check:** Username and password.

### Driver Missing

**Error:**

```
Cannot load driver class
```

**Check:** MySQL dependency added correctly.

---

## 18. Important Interview Points

### Does Spring Data JPA Use Hibernate?

> Yes. By default, Spring Boot uses **Hibernate** internally as the JPA implementation.

### Why Add MySQL Driver?

> Because Hibernate cannot directly talk to the database. Communication happens through the **JDBC driver**.

### Why `ddl-auto=update`?

> Because Hibernate automatically creates/updates tables from entities.

---

## 19. Final Summary

| Component | Purpose |
|-----------|---------|
| Spring Data JPA | Repository + ORM support |
| Hibernate | JPA implementation |
| MySQL Driver | Database connectivity |
| `application.properties` | Configuration file |
| `ddl-auto` | Table creation behavior |
| `show-sql` | Print SQL queries |

---

## 20. Golden Line

> Spring Boot auto-configures Spring Data JPA and Hibernate using dependencies and `application.properties`, requiring minimal manual configuration.
