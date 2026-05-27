# Core Java: `finalize()` Method in `java.lang.Object`

---

## 1. What is `finalize()`?

The `finalize()` method is closely tied to the **Garbage Collector (GC)** mechanism in Java.

> 💡 **Analogy:** Think of `finalize()` as the object's **"last wish"** before being garbage collected.

---

## 2. How It Works

```
Object eligible for GC
        ↓
GC calls finalize() on the object
        ↓
finalize() executes cleanup logic
        ↓
GC destroys the object
```

---

## 3. Key Characteristics

| Property | Details |
|----------|---------|
| **Defined In** | `java.lang.Object` |
| **Signature** | `protected void finalize() throws Throwable` |
| **Called By** | The **Garbage Collector** automatically, just before destroying an object |
| **Override** | ✅ Can be overridden to add custom cleanup logic |

---

## 4. Purpose

Used to perform **cleanup activities** before an object is destroyed, such as:

- Closing **database connections**
- Closing **network sockets**
- Releasing **file handles** or other resources

---

## 5. Example

```java
class DatabaseConnection {

    DatabaseConnection() {
        System.out.println("Connection Opened");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Connection Closed — Cleanup before GC");
    }
}

public class Main {
    public static void main(String[] args) {
        DatabaseConnection con = new DatabaseConnection();
        con = null; // Object eligible for GC

        System.gc(); // Request GC (not guaranteed to run immediately)
    }
}
```

**Output (approximate):**
```
Connection Opened
Connection Closed — Cleanup before GC
```

---

## 6. Important Notes

> ⚠️ **`finalize()` is deprecated since Java 9** and removed from active use in modern Java. The preferred approach for cleanup is using **`try-with-resources`** and implementing the `AutoCloseable` interface.

---

## 7. Modern Alternative: `AutoCloseable`

The `AutoCloseable` interface (introduced in **Java 7**) allows resources to be closed automatically after a `try` block finishes — no need for `finally` or `finalize()`.

**Definition:**
```java
public interface AutoCloseable {
    void close() throws Exception;
}
```

**Usage with `try-with-resources`:**
```java
class DatabaseConnection implements AutoCloseable {

    public DatabaseConnection() {
        System.out.println("Connection Opened");
    }

    @Override
    public void close() {
        System.out.println("Connection Closed"); // Called automatically
    }
}

// Usage
try (DatabaseConnection con = new DatabaseConnection()) {
    // use connection
} // con.close() called automatically after this block
```

**Output:**
```
Connection Opened
Connection Closed
```

> 💡 Resources declared in `try(...)` are **always closed** after the block ends, even if an exception occurs.

---

## Summary

| Concept | Key Point |
|---------|-----------|
| `finalize()` | Called by GC just **before** destroying an object |
| **Purpose** | Perform last-minute cleanup (close connections, release resources) |
| **Called By** | Garbage Collector — **not** manually by the programmer |
| **Modern Alternative** | `AutoCloseable` + `try-with-resources` |

---

## 🎯 Key Interview Tips

- `finalize()` is called by the **GC**, not directly by your code.
- It is **not guaranteed** to be called — if the JVM exits before GC runs, `finalize()` may never execute.
- **Deprecated since Java 9** — always prefer `try-with-resources` for resource management in modern Java.
- You **cannot** rely on `finalize()` for critical cleanup logic.
