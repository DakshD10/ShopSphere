# Experiment 1 — GUI Student Registration Form Using Swing

## Aim
To design and develop a Graphical User Interface (GUI) for a student registration form using the Java Swing framework that collects student details, validates input, and displays a confirmation dialog.

## Components & Features
- **Frame**: `JFrame` with title `"Student Registration Form"`, size `450x450`, absolute layout (`null`).
- **Labels**: `JLabel` for Student Name, Roll Number, Gender, and Branch.
- **Text Inputs**: `JTextField` for name, roll number, and branch.
- **Gender Selection**: Radio buttons (`JRadioButton`) for **Male** and **Female**, grouped with `ButtonGroup`.
- **Declaration Checkbox**: `JCheckBox` for accepting Terms & Conditions.
- **Buttons**:
  - `Submit`: Validates all inputs in sequence and displays a success dialog with entered data.
  - `Reset`: Clears text fields, unselects radio buttons, and unchecks terms.

## Validation Sequence
1. **Student Name**: Cannot be blank ("Please enter Student Name").
2. **Roll Number**: Cannot be blank ("Please enter Roll Number").
3. **Gender**: Either Male or Female must be selected ("Please select Gender").
4. **Branch**: Cannot be blank ("Please enter Branch").
5. **Terms & Conditions**: Must be accepted ("Please accept Terms & Conditions").

## Compilation & Execution Instructions

From this project directory (`01-swing-student-registration`):

```bash
# 1. Compile the Java source file
/opt/homebrew/opt/openjdk/bin/javac -d out src/StudentRegistration.java

# Or if 'javac' is in your system PATH:
javac -d out src/StudentRegistration.java

# 2. Run the application
/opt/homebrew/opt/openjdk/bin/java -cp out StudentRegistration

# Or if 'java' is in your system PATH:
java -cp out StudentRegistration
```

## PDF Fidelity Checklist
- [x] Class named `StudentRegistration` extending `JFrame` and implementing `ActionListener`.
- [x] Absolute layout (`setLayout(null)`) with dimensions `450x450`.
- [x] Exact coordinate bounds (`setBounds`) matching the professor's PDF.
- [x] Radio buttons limited to Male and Female, controlled by `ButtonGroup`.
- [x] Event handling through `actionPerformed(ActionEvent e)` using `e.getSource() == reset`.
- [x] Exact validation error dialog messages and order.
- [x] Formatted multiline summary message dialog on successful submission.

## Viva Questions & Key Points
1. **Why is `setLayout(null)` used?**
   It allows manual, absolute positioning of Swing components using `setBounds(x, y, width, height)`.
2. **Why is `ButtonGroup` necessary for radio buttons?**
   A `ButtonGroup` makes radio buttons mutually exclusive so only one option can be selected at a time.
3. **What is the purpose of `ActionListener`?**
   It is an interface in `java.awt.event` used to handle action events (e.g., button clicks) through the `actionPerformed(ActionEvent e)` method.
4. **What does `genderGroup.clearSelection()` do?**
   Clears any active selection among the radio buttons in the button group during form reset.
5. **Why use `setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)`?**
   It terminates the Java application process when the user closes the window.
