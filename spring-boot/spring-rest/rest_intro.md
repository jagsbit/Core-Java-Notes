# What is REST?

**REST** stands for **Representational State Transfer**.

REST is an **architectural style** used to build web APIs using the **HTTP protocol**.

**API** stands for **Application Programming Interface**. It acts as a bridge between two systems so they can communicate with each other. For example, a REST API can act as a bridge between a frontend application and a backend application, and even between two backend services.

REST defines a set of rules and constraints that an API should follow to be called a **RESTful API**.

---

## Main REST Principles

### 1. Client-Server Architecture
The client sends requests to the server, and the server processes those requests and sends responses back.

### 2. Stateless Communication
The server should not store information about previous requests. Every request must contain all the required information so the server can process it independently.

### 3. Resource-Based Architecture
In REST, everything is treated as a **resource** such as users, orders, or products. Each resource is identified using a unique URL like `/users` or `/orders`.

### 4. Proper Use of HTTP Methods
REST APIs should use standard HTTP methods:

| Method | Purpose |
|--------|---------|
| `GET` | Retrieve data |
| `POST` | Create data |
| `PUT` / `PATCH` | Update data |
| `DELETE` | Remove data |

### 5. Proper Use of HTTP Status Codes
REST APIs should return meaningful HTTP status codes like:

- `200 OK`
- `201 Created`
- `404 Not Found`
- `500 Internal Server Error`

### 6. Standard Representation of Resources
Resources should be represented in standard formats such as **JSON** or **XML**. JSON is the most commonly used format in modern applications.

---

## The MOST IMPORTANT PART

### What does "Representational State Transfer" actually mean?

Most people memorize REST but don't truly understand the words.

Let's break it down **word by word**.

---

### 1. REPRESENTATIONAL

This means:

> The server does not send the actual resource directly.
> It sends a **representation** of the resource.

**Example:**

Suppose the database contains:

```
User Table:
ID = 1
Name = Jags
Email = jags@gmail.com
Password = encrypted_password
```

When client requests:

```
GET /users/1
```

Server does **NOT** send the actual database row.

Instead, it sends a **REPRESENTATION**:

```json
{
  "id": 1,
  "name": "Jags",
  "email": "jags@gmail.com"
}
```

This JSON is called the:

> **Representation of the resource**

Because it represents the actual data in a transferable format.

Common representation formats:

- **JSON** ← Most commonly used in modern REST APIs
- XML
- YAML

---

### 2. STATE

This is the most confusing word.

Here **"state"** means:

> Current data or current condition of a resource.

**Example:**

Suppose you have an order resource.

Current state:

```json
{
  "id": 101,
  "status": "PENDING"
}
```

After payment:

```json
{
  "id": 101,
  "status": "COMPLETED"
}
```

The resource changed from one **STATE** to another **STATE**.

Another example:

```
User logged out  → State 1
User logged in   → State 2
```

> State simply means: **Current condition/data of a resource**

---

### 3. TRANSFER

Transfer means:

> Moving the representation of resource state between client and server through HTTP.

**Example:**

```
Client requests user data
        ↓
Server sends JSON response
```

That JSON data transfer is: **TRANSFER**

---

## Putting Everything Together

> **Representational State Transfer** means:
> Transferring the representation of a resource's current state between client and server.

Or simpler:

> Client and server exchange resource data representations (usually JSON) over HTTP.

---

## Very Simple Real Meaning of REST

Suppose frontend requests:

```
GET /products/10
```

Backend sends:

```json
{
  "id": 10,
  "name": "Laptop",
  "price": 50000
}
```

What happened?

| Word | Meaning in this example |
|------|------------------------|
| **Resource** | Product |
| **Representation** | JSON response |
| **State** | Current product data |
| **Transfer** | Sending JSON through HTTP |

That entire process is: **Representational State Transfer**

---

## Important Corrections & Interview Points

### REST is an Architectural Style, NOT a Standard

> ✅ REST is an **architectural style**
> ❌ REST is NOT an "architectural standard"

Because REST is not an official protocol or standard like HTTP.
It is a collection of **architectural constraints/rules**.

### REST is NOT a Framework, Protocol, or Library

| What REST is NOT | What REST IS |
|-----------------|--------------|
| ❌ A framework | ✅ An architectural style |
| ❌ A protocol | ✅ A set of constraints/rules |
| ❌ A library | ✅ A design approach for web APIs |
