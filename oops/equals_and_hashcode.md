# Core Java: `equals()` and `hashCode()` in `java.lang.Object`

This session covers the `equals()` and `hashCode()` methods, their relationship with the `==` operator, and the contract required for their proper implementation.

---

## 1. `==` Operator vs. `equals()` Method

### `==` (Equality Operator)

- **Purpose:** Checks if two object references point to the **exact same memory location** (reference comparison).
- **Applicability:** Can be used for both **primitives** and **objects**.
- **Constraint:** To use `==` for object comparison, there must be a valid relationship between the types (child-to-parent, parent-to-child, or same type). Otherwise, the compiler throws an **"incomparable types"** error.

### `.equals()` Method

- **Purpose:** Present in the `Object` class. By default, performs **reference comparison**, but is designed to be **overridden** for content comparison.
- **Constraint:** No restriction on the object type passed — if types are unrelated, it simply returns `false` instead of a compile-time error.

### Comparison Table

| Feature | `==` Operator | `.equals()` Method |
|---------|--------------|-------------------|
| Primary Use | Reference Comparison | Content Comparison (if overridden) |
| Override | ❌ Cannot be overridden | ✅ Can be overridden |
| Type Mismatch | Throws compile-time error | Returns `false` |

---

## 2. Key Conclusions

| # | Conclusion |
|---|-----------|
| 1 | If `r1 == r2` is **true** → `r1.equals(r2)` is **always true** |
| 2 | If `r1.equals(r2)` is **true** → `r1 == r2` **may or may not** be true |
| 3 | If `r1.equals(r2)` is **false** → `r1 == r2` is **always false** |
| 4 | If `r1 == r2` is **false** → `r1.equals(r2)` **may or may not** be false |

> 💡 **Think of it this way:** `==` implies `equals()`, but `equals()` does **not** imply `==`.

---

## 3. The `equals()` and `hashCode()` Contract

Hashing data structures like `HashSet`, `HashMap`, and `Hashtable` use **buckets** to store objects based on their hash codes.

### The Fundamental Rule

> **If two objects are equal according to `equals()`, they MUST have the same `hashCode()`.**

This ensures they are placed in the **same bucket** in hash-based collections.

### Why Override Both?

- If you override `equals()`, you **must** override `hashCode()` to satisfy the contract.
- The compiler won't force you to, but failing to do so causes **incorrect behavior** in hash-based collections.

```java
// ✅ Correct: Both overridden consistently
class Student {
    String name;
    int rollNumber;

    Student(String name, int rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student s = (Student) obj;
        return this.rollNumber == s.rollNumber && this.name.equals(s.name);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + rollNumber; // Same fields as equals()
    }
}
```

---

## 4. `String` vs `StringBuffer` — A Classic Example

| Class | `equals()` Overridden? | `hashCode()` Overridden? | Based On |
|-------|------------------------|--------------------------|----------|
| `String` | ✅ Yes | ✅ Yes | Content (characters) |
| `StringBuffer` | ❌ No | ❌ No | Memory address |

```java
String s1 = new String("hello");
String s2 = new String("hello");

System.out.println(s1 == s2);        // false (different objects)
System.out.println(s1.equals(s2));   // true  (same content)
System.out.println(s1.hashCode() == s2.hashCode()); // true

StringBuffer sb1 = new StringBuffer("hello");
StringBuffer sb2 = new StringBuffer("hello");

System.out.println(sb1.equals(sb2));  // false (not overridden)
System.out.println(sb1.hashCode() == sb2.hashCode()); // false
```

---

## 5. The Contract Visualized

```
equals() → true
     ↓
hashCode() MUST be equal

equals() → false
     ↓
hashCode() may or may not be equal (allowed, but not ideal)
```

> ⚠️ Two objects with **different content** can have the **same hash code** (a hash collision) — this is allowed. But two **equal objects** with **different hash codes** is a **contract violation**.

---

## Summary

| Concept | Key Point |
|---------|-----------|
| `==` | Reference equality — same memory address |
| `equals()` | Logical/content equality — override for custom behavior |
| `hashCode()` | Must be overridden alongside `equals()` |
| Contract | Equal objects **must** have equal hash codes |

---

## 🎯 Key Interview Tips

- **Q: When you override `equals()`, which method must you also override?**
  **A:** `hashCode()`

- **Best Practice:** Use the **same instance variables** in both `equals()` and `hashCode()` to keep the contract consistent.

- **Remember:** `==` requires type compatibility; `.equals()` is more flexible and just returns `false` for unrelated types.
