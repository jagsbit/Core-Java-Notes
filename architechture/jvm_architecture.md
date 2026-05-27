# JVM Architecture

The **Java Virtual Machine (JVM)** is the runtime environment responsible for executing Java programs.

Java follows:

> **Write Once, Run Anywhere (WORA)**

This is possible because Java code runs on **JVM** instead of directly running on the operating system.

---

## Java Program Execution Flow

```
Java Source Code (.java)
          ↓
Java Compiler (javac)
          ↓
Bytecode (.class)
          ↓
JVM
          ↓
Machine Code
          ↓
Execution on OS
```

- `javac` converts Java code into **bytecode**.
- **JVM** converts bytecode into **machine code**.

---

## What is JVM?

JVM is:

- A **virtual machine**
- Part of **JRE**
- Responsible for **executing bytecode**

> JVM is only a **specification**. Different vendors provide implementations:

| Vendor | JVM Implementation |
|--------|-------------------|
| Oracle | Oracle JVM |
| OpenJDK | OpenJDK JVM |
| IBM | IBM JVM |

---

## Main Components of JVM Architecture

```
                    JVM Architecture
┌──────────────────────────────────────────────────────┐
│              Class Loader Subsystem                  │
├──────────────────────────────────────────────────────┤
│                 Runtime Data Areas                   │
│  ┌───────────┬──────┬───────┬────┬──────────────┐   │
│  │Method Area│ Heap │ Stack │ PC │ Native Stack  │   │
│  └───────────┴──────┴───────┴────┴──────────────┘   │
├──────────────────────────────────────────────────────┤
│                  Execution Engine                    │
│  ┌─────────────┬──────────────┬───────────────────┐ │
│  │ Interpreter │ JIT Compiler │ Garbage Collector │ │
│  └─────────────┴──────────────┴───────────────────┘ │
├──────────────────────────────────────────────────────┤
│             Java Native Interface (JNI)              │
├──────────────────────────────────────────────────────┤
│               Native Method Libraries                │
└──────────────────────────────────────────────────────┘
```

---

## 1. Class Loader Subsystem

The **Class Loader** loads `.class` files into memory during runtime.

Java supports:

> **Dynamic Class Loading** — Classes are loaded **only when needed**.

### Functions of Class Loader

The class loading process has **3 phases**:

```
1. Loading  →  2. Linking  →  3. Initialization
```

---

### 1.1 Loading Phase

In this phase:

- `.class` file is **read**
- Bytecode is **loaded into JVM memory**

**Example:**

```java
Student s = new Student();
```

When JVM sees `Student` for the first time → `Student.class` gets loaded.

### Types of Class Loaders

#### 1) Bootstrap Class Loader

- **Parent** of all class loaders
- Loads **core Java classes**
- Examples: `java.lang.*`, `java.util.*`
- Location: `JAVA_HOME/jre/lib`
- Implemented in: **C/C++**

#### 2) Extension Class Loader

- Loads **extension libraries**
- Location: `JAVA_HOME/jre/lib/ext`

#### 3) Application / System Class Loader

- Loads **application-specific classes** from classpath

```bash
java -cp MyApp.jar Main
```

---

### Important Principles of Class Loading

#### 1. Visibility Principle
- **Child** class loader can access parent-loaded classes.
- **Parent** cannot access child-loaded classes.

#### 2. Uniqueness Principle
- A class already loaded by parent **should not load again**.
- Prevents **duplicate loading**.

#### 3. Delegation Principle

Class loading follows a hierarchy (parent-first strategy):

```
Application
     ↓
Extension
     ↓
Bootstrap
```

#### 4. No Unloading Principle
- Class loader **loads** classes but **does not unload** them.

---

### 1.2 Linking Phase

After loading, JVM **links** the class.

Linking contains **3 stages**:

```
A. Verification  →  B. Preparation  →  C. Resolution
```

#### A) Verification

Checks whether **bytecode is valid**.

Checks include:
- Correct syntax
- Correct stack usage
- Access rules
- Proper variable initialization

> If invalid → `java.lang.VerifyError`

#### B) Preparation

- Memory is allocated for **static variables**.
- **Default values** are assigned.

**Example:**

```java
static int x;
```

During preparation → `x = 0` *(No actual initialization yet)*

#### C) Resolution

**Symbolic references** are converted into **direct references**.

| Type | Description |
|------|-------------|
| **Symbolic Reference** | Names stored in bytecode (class name, method name, variable name) |
| **Direct Reference** | Actual memory address of methods, classes, variables |

**Example:**

```java
System.out.println("Hello");
```

Bytecode stores:
- Class → `System`
- Field → `out`
- Method → `println`

During resolution, JVM finds their **actual memory locations**.

---

### 1.3 Initialization Phase

- Static variables receive **actual values**.
- **Static blocks** execute.

**Example:**

```java
static int x = 10;

static {
    System.out.println("Loaded");
}
```

**Initialization order:**
1. Parent class first
2. Child class later

---

## 2. Runtime Data Areas

These are **JVM memory areas** used during execution.

```
┌───────────────────────────────────────────────────────┐
│  Method Area │  Heap  │  Stack  │  PC  │ Native Stack │
└───────────────────────────────────────────────────────┘
```

---

### 2.1 Method Area *(Shared)*

> This is a **shared resource** — only **1 Method Area per JVM**. All JVM threads share the same Method Area, so access to method data and the process of **dynamic linking must be thread-safe**.

Stores **class-level data (including static variables)** such as:

---

#### 🔹 Classloader Reference

- Stores a reference to the **classloader** that loaded the class.

---

#### 🔹 Runtime Constant Pool

Stores per class and interface:
- **Numeric constants**
- **Field references**
- **Method references**
- **Attributes**

> When a method or field is referred to, the JVM searches the **actual memory address** of the method or field using the Runtime Constant Pool.

---

#### 🔹 Field Data

Stores per field:

| Item | Description |
|------|-------------|
| **Name** | Field name |
| **Type** | Data type of the field |
| **Modifiers** | `public`, `private`, `static`, `final`, etc. |
| **Attributes** | Additional metadata |

---

#### 🔹 Method Data

Stores per method:

| Item | Description |
|------|-------------|
| **Name** | Method name |
| **Return Type** | Return type of the method |
| **Parameter Types** | Types of parameters (in order) |
| **Modifiers** | `public`, `private`, `static`, etc. |
| **Attributes** | Additional metadata |

---

#### 🔹 Method Code

Stores per method:

| Item | Description |
|------|-------------|
| **Bytecodes** | Compiled instructions of the method |
| **Operand Stack Size** | Maximum size of the operand stack |
| **Local Variable Size** | Size of local variable array |
| **Local Variable Table** | Maps local variables to slots |
| **Exception Table** | Stores exception handler info |

##### Exception Table — Per Exception Handler:

| Item | Description |
|------|-------------|
| **Start Point** | Start of the try block |
| **End Point** | End of the try block |
| **PC Offset for Handler** | Address of the catch/finally handler code |
| **Constant Pool Index** | Reference to the exception class being caught |

---

**Example:**

```java
static int count = 10;
```

Stored in **Method Area**.

**Features:**
- Shared among **all threads**
- Only **one** Method Area per JVM
- Access must be **thread-safe** due to shared nature

---

### 2.2 Heap Area *(Shared)*

Stores:
- Objects
- Instance variables
- Arrays

**Example:**

```java
Student s = new Student();
```

> Object stored in **Heap**.

**Features:**
- **Largest** memory area
- Shared among **all threads**
- Managed by **Garbage Collector**

#### Heap Memory Structure

```
┌─────────────────────────────────────────┐
│             Young Generation            │
│  ┌──────────────┬────────┬───────────┐  │
│  │  Eden Space  │   S0   │    S1     │  │
│  └──────────────┴────────┴───────────┘  │
├─────────────────────────────────────────┤
│             Old Generation              │
│         (Long-living objects)           │
├─────────────────────────────────────────┤
│         Metaspace (Java 8+)             │
│         (Class Metadata)                │
└─────────────────────────────────────────┘
```

| Area | Description |
|------|-------------|
| **Young Generation** | Newly created objects (Eden + Survivor Spaces S0, S1) |
| **Old Generation** | Long-living objects that survived multiple GC cycles |
| **Metaspace** | Class metadata (introduced after Java 8) |

---

### 2.3 Stack Area *(Per Thread)*

Each thread gets **separate stack memory**.

Stores:
- Method calls
- Local variables
- Intermediate results

#### Stack Frame

> Every method call creates **one stack frame**.

**Example:**

```java
void add(int a, int b) { ... }
```

New stack frame is created when `add()` is called.

#### Stack Frame Contains:

| Component | Description |
|-----------|-------------|
| **Local Variable Array** | Stores method parameters and local variables |
| **Operand Stack** | Temporary workspace for calculations |
| **Frame Data** | Exception info and method symbolic references |

**Example — Operand Stack:**

```java
int c = a + b;
// Values of a and b stored temporarily in operand stack
```

#### Stack Operations

- **Push** — Method call added to stack
- **Pop** — Method removed after execution

#### Errors Related to Stack

| Error | Cause |
|-------|-------|
| `StackOverflowError` | Excessive stack usage (e.g., infinite recursion) |
| `OutOfMemoryError` | JVM cannot allocate memory |

**Example — StackOverflowError:**

```java
void fun() {
    fun(); // Infinite recursion
}
```

---

### 2.4 PC Register (Program Counter) *(Per Thread)*

Each thread has a **separate PC register**.

Stores:
- Address of the **currently executing instruction**

**Example:**

```
Instruction 1  ← PC points here
Instruction 2
Instruction 3
```

> PC keeps track of which instruction is being executed.

---

### 2.5 Native Method Stack

Stores **native method execution information**.

Native methods are written in:
- **C**
- **C++**

**Example:**

```java
System.loadLibrary();
```

---

## 3. Execution Engine

Responsible for **executing bytecode**.

Main components:

```
1. Interpreter
2. JIT Compiler
3. Garbage Collector
```

---

### 3.1 Interpreter

Reads bytecode **line-by-line** and executes it.

| ✅ Advantages | ❌ Disadvantages |
|--------------|-----------------|
| Fast startup | Slow for repeated method calls |
| Simpler execution | Same bytecode interpreted repeatedly |

---

### 3.2 JIT Compiler (Just-In-Time)

**Improves performance** by converting bytecode into **native machine code**.

#### Working of JIT

```
Bytecode
   ↓
JIT Compiler
   ↓
Native Code
   ↓
Stored in Cache
   ↓
Fast Execution
```

#### Hotspot Optimization

> Frequently executed code is called a **Hotspot**.
> JIT **optimizes hotspot methods** for faster execution.

| ✅ Advantages of JIT |
|----------------------|
| Faster execution |
| Reduces repeated interpretation |
| Improves overall performance |

---

### 3.3 Garbage Collector (GC)

**Automatically removes unused objects** from Heap.

#### When Object Becomes Eligible for GC

```java
obj = null;
// Object becomes unreachable → eligible for GC
```

#### GC Process

```
Unused Object
      ↓
GC identifies
      ↓
Memory reclaimed
```

| ✅ Advantages of GC |
|---------------------|
| Automatic memory management |
| Prevents memory leaks |
| Improves memory efficiency |

#### Calling GC Manually

```java
System.gc();
```

> Only **requests** JVM to run GC. **Not guaranteed.**

#### Types of Garbage Collectors

| Collector | Description |
|-----------|-------------|
| **Serial GC** | Single-threaded, for small apps |
| **Parallel GC** | Multi-threaded, for throughput |
| **G1 GC** | Balanced, default modern collector |
| **ZGC** | Ultra-low latency collector |

---

## 4. Java Native Interface (JNI)

**JNI** allows Java to interact with **native languages** like C and C++.

### Why JNI is Needed

Used for:
- **Hardware access**
- **OS-level operations**
- **Existing native libraries**

---

## 5. Native Method Libraries

Collection of **native libraries** required by JVM.

Usually:
- `.dll` files (Windows)
- `.so` files (Linux/macOS)

---

## Summary

| Component | Purpose |
|-----------|---------|
| **Class Loader Subsystem** | Loads `.class` files into JVM memory |
| **Method Area** | Stores class metadata, static variables |
| **Heap Area** | Stores objects and instance variables |
| **Stack Area** | Stores method calls and local variables (per thread) |
| **PC Register** | Tracks current instruction (per thread) |
| **Native Method Stack** | Stores native method execution info |
| **Interpreter** | Executes bytecode line-by-line |
| **JIT Compiler** | Compiles hot bytecode to native code for speed |
| **Garbage Collector** | Reclaims memory from unused objects |
| **JNI** | Bridges Java with native C/C++ code |
| **Native Method Libraries** | Native `.dll`/`.so` libraries used by JVM |

---
