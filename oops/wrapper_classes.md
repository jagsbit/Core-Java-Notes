# Wrapper Classes in Java

**Wrapper classes** are classes that convert **primitive data types into objects**.

Java provides one wrapper class for each primitive type.

---

## 1. Why Wrapper Classes are Needed

Primitive types are **not objects**. Some Java features work only with objects, such as:

- **Collections** (`ArrayList`, `HashMap`)
- **Generics**
- **Frameworks**
- **Serialization**

So Java provides wrapper classes to bridge this gap.

---

## 2. Primitive Types and Their Wrapper Classes

| Primitive Type | Wrapper Class |
|---------------|--------------|
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

---

## 3. Boxing and Unboxing

### Boxing — Primitive → Object

```java
int x = 5;
Integer obj = Integer.valueOf(x); // Boxing
System.out.println(obj); // 10
```

### Unboxing — Object → Primitive

```java
Integer obj = Integer.valueOf(20);
int x = obj.intValue(); // Unboxing
```

---

## 4. Autoboxing and Auto-Unboxing (Java 5+)

Java **automatically** handles the conversion between primitives and wrapper objects.

### Autoboxing — Automatic Primitive → Object

```java
int x = 10;
Integer obj = x; // Java internally: Integer.valueOf(x)
```

### Auto-Unboxing — Automatic Object → Primitive

```java
Integer obj = 50;
int x = obj; // Java internally: obj.intValue()
```

### Complete Example

```java
public class Main {
    public static void main(String[] args) {

        // Autoboxing
        int a = 10;
        Integer obj = a;

        // Auto-unboxing
        int b = obj;

        System.out.println(a);   // 10
        System.out.println(obj); // 10
        System.out.println(b);   // 10
    }
}
```

### Autoboxing with Collections

Collections store **objects**, not primitives.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {

        ArrayList<Integer> list = new ArrayList<>();

        list.add(10); // autoboxing: Integer.valueOf(10)
        list.add(20);

        System.out.println(list); // [10, 20]
    }
}
```

> ✅ Correct: `ArrayList<Integer>`
> ❌ Wrong: `ArrayList<int>` — compile error

---

## 5. Important Methods in Wrapper Classes

| Method | Description | Returns |
|--------|-------------|---------|
| `valueOf()` | Converts primitive/String → wrapper object | Wrapper object |
| `parseInt()` | Converts String → primitive `int` | `int` |
| `toString()` | Converts value → String | `String` |
| `intValue()` | Converts Integer → `int` | `int` |

### `valueOf()` vs `parseInt()`

```java
Integer obj = Integer.valueOf("10"); // returns Integer object
int x = Integer.parseInt("123");     // returns primitive int
```

| Method | Returns |
|--------|---------|
| `Integer.valueOf("10")` | `Integer` object |
| `Integer.parseInt("10")` | primitive `int` |

---

## 6. Wrapper Class Objects Can Be `null`

```java
Integer x = null; // ✅ Valid — objects can be null

int y = null;     // ❌ Error — primitives cannot be null
```

---

## 7. Integer Caching (Important!)

### The Problem

For objects, `==` compares **references (memory addresses)**, not values.

```java
Integer a = new Integer(10);
Integer b = new Integer(10);
System.out.println(a == b); // false — different objects
```

### What Java Does Internally

Java maintains a **cache of `Integer` objects** from **-128 to 127**.

When you create an `Integer` using autoboxing or `valueOf()` within this range, Java **reuses the same cached object** instead of creating a new one.

---

### Example 1 — Value Inside Cache Range (-128 to 127)

```java
Integer a = 100;
Integer b = 100;
System.out.println(a == b); // true
```

**What happens internally:**

```java
Integer a = Integer.valueOf(100);
Integer b = Integer.valueOf(100);
```

Since `100` is inside the cache range, **both references point to the same object**:

```
      Cache
   [Integer 100]
      /     \
     a       b
```

→ `a == b` is **`true`**

---

### Example 2 — Value Outside Cache Range

```java
Integer a = 200;
Integer b = 200;
System.out.println(a == b); // false
```

Since `200` is **outside** the cache range, Java creates **two separate objects**:

```
a ----> [Integer 200]   (object 1)
b ----> [Integer 200]   (object 2)
```

→ `a == b` is **`false`** — different memory addresses

---

### Example 3 — Using `new` Bypasses Cache

```java
Integer a = new Integer(100);
Integer b = new Integer(100);
System.out.println(a == b); // false
```

> ⚠️ `new` keyword **always** creates a new object, bypassing the cache — even for values in the -128 to 127 range.

---

### Correct Way to Compare Wrapper Objects

Always use **`.equals()`** for value comparison:

```java
Integer a = 200;
Integer b = 200;
System.out.println(a.equals(b)); // true — compares values
```

---

### Integer Caching Summary

| Case | `==` Result | Reason |
|------|------------|--------|
| `Integer a = 100; Integer b = 100;` | `true` | Same cached object |
| `Integer a = 200; Integer b = 200;` | `false` | Different objects (outside cache) |
| `Integer a = new Integer(100); Integer b = new Integer(100);` | `false` | `new` bypasses cache |
| `a.equals(b)` | `true` | Compares values, not references |

> 💡 **Cache range: -128 to 127** — done for memory optimization and performance, as these values are used most frequently.

---

## 8. Wrapper Classes are Immutable

```java
Integer x = 10;
x = 20; // A new Integer object is created, x now points to it
```

The original object `10` is not modified — a new object is created.

---

## 9. Autoboxing Performance Cost

> ⚠️ Autoboxing and unboxing have a **performance cost** because objects are created.
> Primitive types are always **faster** than wrapper objects for arithmetic operations.

---

## Summary

| Concept | Description | Example |
|---------|-------------|---------|
| **Boxing** | Primitive → Wrapper object (manual) | `Integer.valueOf(10)` |
| **Unboxing** | Wrapper object → Primitive (manual) | `obj.intValue()` |
| **Autoboxing** | Primitive → Object (automatic) | `Integer obj = 10;` |
| **Auto-unboxing** | Object → Primitive (automatic) | `int x = obj;` |
| **Integer Cache** | Reuses objects for -128 to 127 | `Integer a = 100; Integer b = 100; a == b → true` |
| **Immutability** | Wrapper objects cannot be modified | New object created on reassignment |

---

## 🎯 Key Interview Tips

- Collections **only work with objects** — use `Integer`, not `int`.
- `==` compares **references** for objects; use **`.equals()`** for value comparison.
- Java caches `Integer` objects from **-128 to 127** for memory and performance optimization.
- **`new Integer()`** always bypasses the cache and creates a new object.
- `Integer.valueOf()` returns a wrapper **object**; `Integer.parseInt()` returns a **primitive**.
- Wrapper classes are **immutable** — reassignment creates a new object.
- Autoboxing/unboxing has a **performance overhead** — prefer primitives for heavy calculations.
