# Experiment 2 — Calculator Using Java Swing Following MVC Architecture

## Aim
To design and develop a GUI-based calculator using Java Swing that performs basic arithmetic operations using the MVC (Model-View-Controller) architecture.

---

## Architectural Breakdown (MVC)

1. **Model (`CalculatorModel.java`)**
   - Contains business logic and arithmetic operations: `add`, `subtract`, `multiply`, `divide`.
   - Contains no GUI components.
   - Handles division by zero by returning `0` (as specified in the professor's lab code).

2. **View (`CalculatorView.java`)**
   - Represents the Swing GUI presentation layer.
   - Contains:
     - `JFrame` with title `"MVC Calculator"`, size `400x300`, and absolute positioning (`setLayout(null)`).
     - Input text fields: `txtNum1` (First Number) and `txtNum2` (Second Number).
     - Four operation buttons: `btnAdd` (`+`), `btnSub` (`-`), `btnMul` (`*`), and `btnDiv` (`/`).
     - Output text field: `txtResult` with `txtResult.setEditable(false)` for read-only display.
   - Performs no calculations or business logic.

3. **Controller (`CalculatorController.java`)**
   - Implements `ActionListener` to handle button click events.
   - Acts as the intermediary between `CalculatorView` and `CalculatorModel`.
   - Reads inputs from `CalculatorView`, calls the corresponding arithmetic method on `CalculatorModel`, and displays the resulting value in `CalculatorView.txtResult`.
   - Robustness improvement: catches `NumberFormatException` to prevent crashes when input fields are empty or non-numeric.

4. **Entry Point (`Main.java`)**
   - Instantiates `CalculatorView`, `CalculatorModel`, and `CalculatorController`.

---

## Intentional Differences & Small Improvements from PDF

1. **Division by Zero Behavior**:
   - The PDF explicitly returns `0` when `b == 0` in `CalculatorModel.divide(double a, double b)`. This exact behavior is preserved with an explanatory code comment noting compliance with the supplied lab practical.
2. **Robustness Improvement for Invalid Inputs**:
   - The PDF code directly parses numbers using `Double.parseDouble(view.txtNum1.getText())` without a `try-catch` block, which causes an unhandled `NumberFormatException` crash if fields are empty or invalid.
   - `CalculatorController.java` wraps parsing in a `try-catch (NumberFormatException ex)` and displays a `JOptionPane.showMessageDialog` prompting the user to enter valid numbers without altering the underlying architecture.

---

## Compilation & Execution Instructions

From this project directory (`02-swing-mvc-calculator`):

```bash
# 1. Compile all Java source files
/opt/homebrew/opt/openjdk/bin/javac -d out src/*.java

# Or if 'javac' is in your system PATH:
javac -d out src/*.java

# 2. Run the application
/opt/homebrew/opt/openjdk/bin/java -cp out Main

# Or if 'java' is in your system PATH:
java -cp out Main
```

---

## Expected Output

```text
-----------------------------
| MVC Calculator            |
-----------------------------
First Number   [ 25        ]
Second Number  [ 10        ]

 [+]  [-]  [*]  [/]

Result         [ 35.0      ]
-----------------------------
```

---

## PDF Requirement Mapping Checklist

- [x] **Model (`CalculatorModel.java`)**: Implements `add`, `subtract`, `multiply`, `divide` with no GUI code.
- [x] **Division by zero handling**: Returns `0` when `b == 0` matching the PDF logic.
- [x] **View (`CalculatorView.java`)**: `JFrame` with size `(400, 300)`, absolute layout (`null`), matching exact component bounds and names (`txtNum1`, `txtNum2`, `txtResult`, `btnAdd`, `btnSub`, `btnMul`, `btnDiv`).
- [x] **Read-only result**: `txtResult.setEditable(false)`.
- [x] **Controller (`CalculatorController.java`)**: Implements `ActionListener`, attaches listener to `btnAdd`, `btnSub`, `btnMul`, `btnDiv`, performs operations via `model`, updates `txtResult`.
- [x] **Main (`Main.java`)**: Creates `CalculatorView`, `CalculatorModel`, and `CalculatorController`.
- [x] **Separation of Concerns**: Pure arithmetic in Model, pure UI in View, pure orchestration/event handling in Controller.

---

## Viva Questions & Key Points

1. **What is MVC Architecture?**
   MVC is a software architectural pattern that separates an application into three interconnected parts: Model (data & business logic), View (user interface presentation), and Controller (processes user input and updates Model and View).

2. **What are the three components of MVC?**
   - **Model**: Manages data, rules, and business logic.
   - **View**: Visual representation of the data / GUI.
   - **Controller**: Accepts user inputs, coordinates between Model and View.

3. **Why do we separate Model and View?**
   To achieve loose coupling, modularity, and high maintainability. Business logic can be modified or tested independently of UI layout changes.

4. **What is the purpose of `ActionListener`?**
   It is an interface in `java.awt.event` used to receive and handle action events, such as button clicks, via the `actionPerformed(ActionEvent e)` method.

5. **What is the difference between `JFrame` and `JPanel`?**
   `JFrame` is a top-level window container with a title bar and border, whereas `JPanel` is a lightweight container used inside a frame to group and organize components.

6. **Why is `setEditable(false)` used?**
   It makes the text field read-only so the user cannot directly edit the output display while still allowing programmatically setting text.

7. **Which package contains Swing classes?**
   `javax.swing` (built on top of the Abstract Window Toolkit, `java.awt`).

8. **What happens when division by zero occurs?**
   In this lab implementation, the `divide` method checks `if (b == 0)` and returns `0` (or `0.0`) to avoid IEEE 754 `Infinity` or unchecked exceptions.

9. **What is event-driven programming?**
   A programming paradigm where program execution is determined by events such as user actions (mouse clicks, key presses) or system messages.

10. **What is the advantage of MVC over writing everything in one class?**
    It prevents "spaghetti code" by promoting separation of concerns, reusability of business logic, easier unit testing, and team collaboration.
