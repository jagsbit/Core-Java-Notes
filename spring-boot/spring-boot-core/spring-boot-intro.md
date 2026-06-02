# What is Spring Boot?

## Introduction

Spring Boot is a framework built on top of the **Spring Framework** that simplifies the development of Java applications, especially web applications and microservices.

Before Spring Boot, developing applications using Spring required:

- Large XML configuration
- Manual dependency management
- Server setup (Tomcat)
- Complex project configuration
- Boilerplate code

Spring Boot solves these problems by providing:

- Auto configuration
- Embedded servers
- Starter dependencies
- Production-ready features
- Convention over configuration

---

## Why Spring Boot if We Already Have Spring?

### Problem with Traditional Spring

In traditional Spring Framework:

- You manually configure beans
- Add dependencies one by one
- Configure `DispatcherServlet` manually
- Configure Tomcat separately
- Write XML configuration
- Handle version compatibility

A simple web project could take **hours** just for setup.

### Spring Boot Solution

Spring Boot reduces configuration and setup time.

**Traditional Spring** — You configure:

- `DispatcherServlet`
- `ViewResolver`
- `DataSource`
- `TransactionManager`
- Tomcat server
- Maven dependencies manually

**Spring Boot** — You only:

- Add starter dependency
- Write business logic
- Run application

> Spring Boot automatically configures the rest.

---

## Core Features of Spring Boot

### 1. Auto Configuration

Spring Boot automatically configures components based on dependencies present in the classpath.

**Example:**

If `spring-boot-starter-web` is added, Spring Boot automatically configures:

- Tomcat
- Spring MVC
- Jackson JSON converter
- `DispatcherServlet`

You don't configure them manually.

### 2. Embedded Server

No need to install external Tomcat.

Spring Boot provides embedded:

- **Tomcat** (default)
- Jetty
- Undertow

You simply run:

```bash
mvn spring-boot:run
```

Application starts immediately.

### 3. Starter Dependencies

Spring Boot provides pre-configured dependency bundles called **starters**.

Instead of adding many dependencies manually, you add one starter.

**Example:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This automatically adds:

- Spring MVC
- Jackson
- Validation
- Embedded Tomcat
- Logging

---

## What are Spring Boot Starters?

### Definition

> Starters are dependency packages that contain all required libraries for a particular feature.

They simplify dependency management.

### Common Spring Boot Starters

| Starter | Purpose |
|---------|---------|
| `spring-boot-starter-web` | Web applications & REST APIs |
| `spring-boot-starter-data-jpa` | Database with JPA/Hibernate |
| `spring-boot-starter-security` | Authentication & authorization |
| `spring-boot-starter-test` | Testing |
| `spring-boot-starter-thymeleaf` | Thymeleaf template engine |
| `spring-boot-starter-validation` | Bean validation |
| `spring-boot-starter-actuator` | Monitoring |

---

## What are Transitive Dependencies?

### Definition

> When you add one dependency, Maven automatically downloads dependencies required by that dependency. These are called **transitive dependencies**.

### Example

If you add `spring-boot-starter-web`, Maven automatically downloads:

- `spring-web`
- `spring-webmvc`
- `jackson`
- `tomcat`
- Logging libraries

You didn't add them manually.

### Advantages of Transitive Dependencies

- Less manual work
- Compatible versions
- Faster development
- Simplified configuration

---

## How to Exclude a Transitive Dependency?

Suppose you want to remove default Tomcat and use **Jetty** instead.

By default, `spring-boot-starter-web` includes `spring-boot-starter-tomcat` as a transitive dependency.

### Step 1: Exclude Tomcat

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>

    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### Step 2: Add Jetty

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

Now your application uses **Jetty** instead of Tomcat.

### Flow

```
Default:
spring-boot-starter-web
        ↓
Embedded Tomcat

After Exclusion:
spring-boot-starter-web
        ↓
Tomcat removed
        ↓
Add Jetty
        ↓
Embedded Jetty
```

---

## JAR Packaging vs WAR Packaging

### Default Packaging in Spring Boot

By default, Spring Boot creates **executable JAR files**.

```
myapp.jar
```

You can run directly:

```bash
java -jar myapp.jar
```

Because embedded Tomcat is **inside** the JAR.

### What is WAR Packaging?

**WAR** = Web Application Archive

Used when deploying application to external servers like:

- External Tomcat
- WebLogic
- JBoss

### How to Change JAR to WAR?

#### Step 1: Change Packaging in `pom.xml`

```xml
<packaging>war</packaging>
```

#### Step 2: Extend `SpringBootServletInitializer`

```java
@SpringBootApplication
public class MyApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

#### Step 3: Mark Tomcat as Provided

Because the external server already provides Tomcat.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```

### JAR vs WAR

| Feature | JAR | WAR |
|---------|-----|-----|
| Server Included | Yes | No |
| Deployment | Direct execution | External server |
| Easy for Microservices | Yes | Less |
| Portable | High | Medium |
| Modern Usage | Mostly used | Older enterprise apps |

---

## Fundamental Concepts of Spring Boot

### 1. Convention Over Configuration

Spring Boot follows sensible defaults.

You configure only when necessary.

### 2. Auto Configuration

Automatic setup based on dependencies.

### 3. Dependency Injection

Objects are managed by the **Spring IoC Container**.

Instead of:

```java
Service s = new Service();
```

Spring creates and injects objects automatically.

### 4. Inversion of Control (IoC)

Control of object creation moves from the programmer to the Spring container.

### 5. Embedded Server

No manual deployment needed.

### 6. Production Ready Features

Spring Boot provides:

- Health monitoring
- Metrics
- Logging
- Actuator endpoints

---

## Spring Boot Application Architecture

### Overall Flow

```
Client
   ↓
Controller
   ↓
Service Layer
   ↓
Repository Layer
   ↓
Database
```

---

### 1. Controller Layer

**Responsibility:** Handles HTTP requests and responses. Acts as the entry point of the application.

Example responsibilities:

- Receive request
- Validate input
- Call service layer
- Return response

**Example:**

```
GET /users/1
```

Controller receives this request.

---

### 2. Service Layer

**Responsibility:** Contains **business logic**. This is the brain of the application.

Examples:

- Salary calculation
- Payment processing
- Order validation
- Business rules

> Controller should **not** contain business logic.

---

### 3. Repository Layer

**Responsibility:** Communicates with the database. Handles CRUD operations.

Examples:

- `save()`
- `findById()`
- `delete()`
- `update()`

Usually works using:

- Spring Data JPA
- Hibernate

---

### 4. Entity / Model Layer

**Responsibility:** Represents database tables as Java classes.

Example:

```
User table → User class
```

Contains fields matching database columns.

---

### 5. Database Layer

Stores persistent data.

Examples:

- MySQL
- PostgreSQL
- Oracle
- MongoDB

---

## Complete Request Flow

### Example: User Registration

**Step 1: Client Sends Request**

```
POST /register
```

JSON data sent.

**Step 2: Controller Receives Request**

Controller accepts request and forwards to service layer.

**Step 3: Service Layer Processes Logic**

Checks:

- Email already exists?
- Password validation?
- Business rules?

**Step 4: Repository Layer Saves Data**

Repository interacts with database.

**Step 5: Database Stores Data**

Data inserted into table.

**Step 6: Response Returned**

Success response returned to client.

---

## Architecture Responsibilities

| Layer | Responsibility |
|-------|---------------|
| Controller | Handle requests/responses |
| Service | Business logic |
| Repository | Database interaction |
| Entity | Data representation |
| Database | Persistent storage |

---

## Why This Architecture is Important

### Separation of Concerns

Each layer has **one responsibility**.

**Benefits:**

- Easier maintenance
- Cleaner code
- Better testing
- Reusability
- Scalability

---

## Typical Spring Boot Project Structure

```
src/main/java
    └── com.example.project
            ├── controller
            ├── service
            ├── repository
            ├── entity
            ├── dto
            ├── config
            └── exception
```

---

## Spring Boot Internal Flow

```
Request
   ↓
DispatcherServlet
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
JPA/Hibernate
   ↓
Database
```

### Role of `DispatcherServlet`

`DispatcherServlet` is the **front controller** of Spring MVC.

Responsibilities:

- Receives all requests
- Finds correct controller
- Sends request to controller
- Returns response to client

> It is **automatically configured** in Spring Boot.

---

## Why Spring Boot Became Popular

- Rapid development
- Less configuration
- Microservice friendly
- Embedded servers
- Easy deployment
- Cloud ready
- Strong ecosystem
- Production-ready monitoring
