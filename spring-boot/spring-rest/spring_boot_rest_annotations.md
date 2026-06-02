# Spring Boot REST API Annotations — Complete Guide

When building REST APIs in Spring Boot, annotations are heavily used to:

- Map URLs
- Handle HTTP requests
- Receive data
- Send JSON responses
- Validate inputs
- Connect layers

> Think of annotations as **instructions given to Spring**.

---

## 1. `@Controller`

### Purpose

Used to create a traditional Spring MVC controller.

It is mainly used for:

- JSP
- Thymeleaf
- HTML pages

It returns:

- View names
- Templates

### Example

```java
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home.jsp";
    }
}
```

Here, `"home.jsp"` is treated as a **view/page name**.

---

## 2. `@RestController`

### Purpose

Used to create REST APIs.

It returns:

- JSON
- XML
- Raw data

instead of JSP pages.

### Internally

`@RestController` is equivalent to:

```java
@Controller
@ResponseBody
```

### Example

```java
@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello REST API";
    }
}
```

Output:

```
Hello REST API
```

NOT a JSP page.

---

## `@Controller` vs `@RestController`

| Feature | `@Controller` | `@RestController` |
|---------|--------------|------------------|
| Used For | MVC Applications | REST APIs |
| Returns | JSP/HTML Views | JSON/XML/Data |
| Needs `@ResponseBody`? | Yes | No |
| Mostly Used In | Web pages | Backend APIs |

---

## 3. `@RequestMapping`

### Purpose

Used to map URLs to controller classes or methods.

Can be used for all HTTP methods: `GET`, `POST`, `PUT`, `DELETE`.

### Class Level Mapping

```java
@RestController
@RequestMapping("/users")
public class UserController {

}
```

Now every endpoint starts with `/users`.

### Method Level Example

```java
@RequestMapping(value="/all", method=RequestMethod.GET)
public String getUsers() {
    return "All Users";
}
```

Full URL: `/users/all`

### Why Modern Spring Rarely Uses It Directly?

Because specialized annotations are cleaner:

- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`

---

## 4. `@GetMapping`

### Purpose

Handles **HTTP GET** requests.

Used for: **Retrieving/Fetching data**

### Example

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String getUsers() {
        return "All Users";
    }
}
```

API: `GET /users`

### Example Returning Object

```java
@GetMapping("/{id}")
public User getUser() {
    return new User(1, "Jags");
}
```

Spring automatically converts the object into JSON.

Output:

```json
{
  "id": 1,
  "name": "Jags"
}
```

---

## 5. `@PostMapping`

### Purpose

Handles **HTTP POST** requests.

Used for: **Creating resources**

### Example

```java
@PostMapping
public String createUser() {
    return "User Created";
}
```

API: `POST /users`

---

## 6. `@PutMapping`

### Purpose

Handles **HTTP PUT** requests.

Used for: **Updating/Replacing complete resource**

### Example

```java
@PutMapping("/{id}")
public String updateUser(@PathVariable int id) {
    return "User Updated " + id;
}
```

---

## 7. `@PatchMapping`

### Purpose

Handles **partial updates**.

### Example

```java
@PatchMapping("/{id}")
public String patchUser(@PathVariable int id) {
    return "Partially Updated User " + id;
}
```

---

## 8. `@DeleteMapping`

### Purpose

Handles **delete requests**.

### Example

```java
@DeleteMapping("/{id}")
public String deleteUser(@PathVariable int id) {
    return "Deleted User " + id;
}
```

---

## 9. `@PathVariable`

### Purpose

Used to extract values from the URL path.

### Example

```java
@GetMapping("/{id}")
public String getUser(@PathVariable int id) {
    return "User ID: " + id;
}
```

URL: `GET /users/10`

Output: `User ID: 10`

### Multiple Path Variables

```java
@GetMapping("/{userId}/orders/{orderId}")
public String getOrder(
        @PathVariable int userId,
        @PathVariable int orderId) {

    return userId + " " + orderId;
}
```

URL: `/users/1/orders/101`

---

## 10. `@RequestParam`

### Purpose

Used to get **query parameters** from the URL.

### Example

```java
@GetMapping("/search")
public String search(@RequestParam String keyword) {
    return keyword;
}
```

URL: `/users/search?keyword=java`

Output: `java`

### Multiple Query Params

```java
@GetMapping("/filter")
public String filter(
        @RequestParam String category,
        @RequestParam int price) {

    return category + " " + price;
}
```

URL: `/products/filter?category=laptop&price=50000`

---

## Difference Between `@PathVariable` & `@RequestParam`

| Feature | `@PathVariable` | `@RequestParam` |
|---------|----------------|----------------|
| Comes From | URL Path | Query String |
| Example | `/users/10` | `/users?id=10` |
| Used For | Specific resource | Filtering/searching |

---

## 11. `@RequestBody`

### Purpose

Used to receive **JSON data** from the client.

Spring converts **JSON → Java Object** automatically.

### JSON Sent By Client

```json
{
  "name": "Jags",
  "email": "jags@gmail.com"
}
```

### Controller

```java
@PostMapping
public User createUser(@RequestBody User user) {
    return user;
}
```

Spring automatically maps the JSON to the `User` object.

---

## 12. `@ResponseBody`

### Purpose

Converts return value into JSON/XML response.

Usually **not needed** with `@RestController`.

### Example

```java
@Controller
public class DemoController {

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
```

> Without `@ResponseBody`, Spring thinks `"Hello"` is a JSP page name.

---

## 13. `@RequestHeader`

### Purpose

Used to read **HTTP headers**.

### Example

```java
@GetMapping("/header")
public String header(
    @RequestHeader("Authorization") String token) {

    return token;
}
```

Header: `Authorization: Bearer xyz123`

---

## 14. `@ResponseStatus`

### Purpose

Used to send a **custom HTTP status code**.

### Example

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public String create() {
    return "Created";
}
```

Response Status: `201 CREATED`

---

## 15. `@CrossOrigin`

### Purpose

Used to allow frontend applications from **another domain/port**.

Very common in **React + Spring Boot**.

### Example

```java
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

}
```

> Without this: **CORS Error**

---

## What is a Controller in Spring Boot?

A Controller is a Java class that:

> Handles client requests and sends responses back to the client.

It acts like a **middleman between client and backend logic**.

### Simple Flow

```
Browser / React App / Mobile App
              ↓ Request
         Controller
              ↓
          Service Layer
              ↓
           Database
              ↓
         Controller
              ↓ Response
            Client
```

### Real Meaning of Controller

Suppose user opens: `GET /users`

Who receives this request first? → **Controller**

The controller decides:

- Which method should run
- What business logic to call
- What response should be returned

---

## Deep Dive: `@Controller` vs `@RestController`

### `@Controller` — Internal Behavior

By default, Spring thinks the returned value is a **View Name**.

```java
@Controller
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "home";
    }
}
```

Spring searches for: `home.jsp` or `home.html`

### `@RestController` — Internal Behavior

```java
@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "home";
    }
}
```

Output becomes: `home` — directly in the browser/Postman response.

**NOT** a JSP page.

---

## Internal Working of `@RestController`

`@RestController` is internally equivalent to:

```java
@Controller + @ResponseBody
```

### What `@ResponseBody` Does

> It tells Spring: "Do NOT treat return value as view/page name. Send it directly in HTTP response body."

### Example Using `@Controller` + `@ResponseBody`

```java
@Controller
public class TestController {

    @ResponseBody
    @GetMapping("/test")
    public String test() {
        return "Hello";
    }
}
```

Output: `Hello` — because of `@ResponseBody`.

---

## Real-World Usage

| Scenario | Annotation |
|----------|------------|
| JSP project | `@Controller` |
| Thymeleaf project | `@Controller` |
| React + Spring Boot | `@RestController` |
| Mobile backend | `@RestController` |
| REST APIs | `@RestController` |

---

## Visual Understanding

### `@Controller`

```
Client Request
      ↓
Controller
      ↓
Returns View Name
      ↓
JSP/HTML Page
```

### `@RestController`

```
Client Request
      ↓
RestController
      ↓
Returns JSON/Data
      ↓
Client receives API response
```

---

## Example Returning Object

```java
@RestController
public class UserController {

    @GetMapping("/user")
    public User getUser() {
        return new User(1, "Jags");
    }
}
```

Spring automatically converts the object into JSON:

```json
{
  "id": 1,
  "name": "Jags"
}
```

> This automatic conversion is done using the **Jackson Library**.

---

## Interview Answer

> **"Difference between `@Controller` and `@RestController`?"**

`@Controller` is used for traditional Spring MVC applications where methods return view names like JSP or HTML pages.

`@RestController` is used for REST APIs where methods return JSON/XML data directly in the HTTP response body.

> Internally, **`@RestController` = `@Controller` + `@ResponseBody`**.
