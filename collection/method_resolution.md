# Java Method Overloading Resolution & `remove()` Confusion

---

## Why `list.remove(1)` ≠ `list.remove(Integer.valueOf(1))`?

This happens because of **method overloading** in `ArrayList`.

`ArrayList` has two different `remove()` methods:

```java
remove(int index)
```

and

```java
remove(Object o)
```

---

## Case 1: `list.remove(1)`

```java
list.remove(1);
```

Here `1` is a **primitive int**.

Java sees `remove(int index)` as the best match.

So it removes **element at index 1**, NOT value `1`.

### Example

```java
List<Integer> list = new ArrayList<>();

list.add(10);
list.add(20);
list.add(30);

list.remove(1);

System.out.println(list);
```

**Output:**
```
[10, 30]
```

> Because `20` was at index `1`.

---

## Case 2: `list.remove(Integer.valueOf(1))`

```java
list.remove(Integer.valueOf(1));
```

Now the argument type is an **Integer object**.

So Java chooses `remove(Object o)`.

Now it removes **value/object** `1`.

### Example

```java
List<Integer> list = new ArrayList<>();

list.add(1);
list.add(2);
list.add(3);

list.remove(Integer.valueOf(1));

System.out.println(list);
```

**Output:**
```
[2, 3]
```

---

## Why Doesn't Autoboxing Happen Here?

You may think:

```
1 → Integer.valueOf(1)
```

should happen automatically.

But **Java method resolution rules** say:

> Exact primitive match is preferred over autoboxing.

So `remove(int)` is preferred over `remove(Integer)` because:
- No conversion needed
- Primitive match is more specific

---

## Visual Understanding

```
list.remove(1)

1 is int
↓
remove(int index)
↓
remove index 1
```

vs

```
list.remove(Integer.valueOf(1))

Argument is Integer object
↓
remove(Object o)
↓
remove value 1
```

---

## ⚠️ Another Important Example

```java
List<Integer> list = new ArrayList<>();

list.add(100);
list.add(200);
list.add(300);

list.remove(200);
```

What happens?

```
remove(200)
→ remove index 200
→ Index 200 doesn't exist
→ IndexOutOfBoundsException ❌
```

**Correct way:**

```java
list.remove(Integer.valueOf(200)); // ✅
```

---

## Best Practice

If working with `List<Integer>` and you want to remove **by value**:

**Always use:**

```java
list.remove(Integer.valueOf(value));
```

**or**

```java
list.remove((Integer) value);
```

---

## Method Resolution Priority in Java

> This is a very important Java concept called **Method Overloading Resolution**.

When multiple overloaded methods exist, Java decides which method to call using a **priority order**.

Java chooses the **most specific matching method**.

### Priority Order

| Priority | Conversion Type |
|----------|----------------|
| 1 | **Exact Match** |
| 2 | **Primitive Widening** |
| 3 | **Autoboxing** |
| 4 | **Varargs** |

> Java checks in this order. As soon as it finds a suitable match, it stops searching.

---

## 1. Exact Match (Highest Priority)

If parameter type **exactly matches** argument type, Java chooses it immediately.

### Example

```java
class Test {

    static void show(int x) {
        System.out.println("int");
    }

    public static void main(String[] args) {
        show(10);
    }
}
```

**Output:**
```
int
```

Because `10` is `int` and parameter is `int` — perfect exact match.

---

## 2. Primitive Widening

If exact match not found, Java tries **widening**.

Widening means: **smaller primitive → bigger primitive**

### Valid Widening

```
byte → short → int → long → float → double

char → int → long → float → double
```

### Example

```java
class Test {

    static void show(long x) {
        System.out.println("long");
    }

    public static void main(String[] args) {
        int a = 10;
        show(a);
    }
}
```

**Output:**
```
long
```

> `int → long` widening happened.

---

## 3. Autoboxing

If widening is not possible, Java tries converting **primitive into wrapper object**.

### Example

```java
class Test {

    static void show(Integer x) {
        System.out.println("Integer");
    }

    public static void main(String[] args) {
        int a = 10;
        show(a);
    }
}
```

**Output:**
```
Integer
```

> `int → Integer` autoboxing occurred.

---

## 4. Varargs (Lowest Priority)

Varargs means `method(int... x)`.

Java uses this **only if nothing else matches**.

### Example

```java
class Test {

    static void show(int... x) {
        System.out.println("varargs");
    }

    public static void main(String[] args) {
        show(10);
    }
}
```

**Output:**
```
varargs
```

---

## Complete Priority Example

```java
class Test {

    static void show(int x) {
        System.out.println("Exact Match");
    }

    static void show(long x) {
        System.out.println("Widening");
    }

    static void show(Integer x) {
        System.out.println("Autoboxing");
    }

    static void show(int... x) {
        System.out.println("Varargs");
    }

    public static void main(String[] args) {
        show(10);
    }
}
```

**Output:**
```
Exact Match
```

> Exact match has highest priority.

---

## Step-by-Step Priority Demonstration

### Remove Exact Match → Widening wins

```java
class Test {

    static void show(long x) {
        System.out.println("Widening");
    }

    static void show(Integer x) {
        System.out.println("Autoboxing");
    }

    static void show(int... x) {
        System.out.println("Varargs");
    }

    public static void main(String[] args) {
        show(10);
    }
}
```

**Output:**
```
Widening
```

> Widening has higher priority than autoboxing.

---

### Remove Widening → Autoboxing wins

```java
class Test {

    static void show(Integer x) {
        System.out.println("Autoboxing");
    }

    static void show(int... x) {
        System.out.println("Varargs");
    }

    public static void main(String[] args) {
        show(10);
    }
}
```

**Output:**
```
Autoboxing
```

---

### Remove Autoboxing → Varargs wins

```java
class Test {

    static void show(int... x) {
        System.out.println("Varargs");
    }

    public static void main(String[] args) {
        show(10);
    }
}
```

**Output:**
```
varargs
```

---

## Why is Widening Preferred Over Autoboxing?

Because widening is considered **more efficient** — no object creation needed.

**Example:**

```
int → long
```

Just changes representation.

But autoboxing:

```
int → Integer
```

Creates **object overhead**.

---

## Important Rule: No Boxing + Widening Together

Java allows **Widening OR Boxing**, but Java does **NOT** prefer **Boxing + Widening together** unless necessary.

### Example

```java
static void show(Long x)
```

Calling:

```java
show(10); // ❌ Invalid
```

Because:

```
int → Integer → Long
```

Requires boxing + widening reference. Java avoids such combinations.

---

## Another Important Example

```java
class Test {

    static void show(Object x) {
        System.out.println("Object");
    }

    static void show(int... x) {
        System.out.println("Varargs");
    }

    public static void main(String[] args) {
        show(10);
    }
}
```

**Output:**
```
Object
```

**Why?**

```
int → Integer → Object   (autoboxing)
```

Autoboxing has higher priority than varargs.

---

## Final Priority Table

| Priority | Conversion Type |
|----------|----------------|
| 1 | Exact Match |
| 2 | Primitive Widening |
| 3 | Autoboxing |
| 4 | Varargs |

---

## Quick Revision Shortcut

```
EWBV

E → Exact
W → Widening
B → Boxing
V → Varargs
```

> Remember: **Exact > Widening > Boxing > Varargs**

---

## Interview Point ⭐

> The `remove()` confusion with `List<Integer>` is one of the **most commonly asked Java Collection interview questions**.

**Key takeaway:**

```java
list.remove(1);               // removes by INDEX
list.remove(Integer.valueOf(1)); // removes by VALUE
```
