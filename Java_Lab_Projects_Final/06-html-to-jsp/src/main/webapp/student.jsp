<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Information</title>
    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --bg: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #e2e8f0;
            --input-border: #cbd5e1;
            --radius: 12px;
            --shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.03);
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background-color: var(--bg);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }

        .container {
            width: 100%;
            max-width: 520px;
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 36px 32px;
        }

        .badge {
            display: inline-block;
            font-size: 0.75rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            background: #dbeafe;
            color: #1e40af;
            padding: 4px 10px;
            border-radius: 9999px;
            margin-bottom: 12px;
        }

        h1 {
            font-size: 1.65rem;
            font-weight: 700;
            color: var(--text-main);
            margin-bottom: 8px;
        }

        .subtitle {
            font-size: 0.9rem;
            color: var(--text-muted);
            margin-bottom: 24px;
            line-height: 1.4;
        }

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            font-size: 0.875rem;
            font-weight: 600;
            color: var(--text-main);
            margin-bottom: 6px;
        }

        input[type="text"],
        input[type="email"],
        input[type="number"] {
            width: 100%;
            padding: 10px 14px;
            font-size: 0.95rem;
            border: 1.5px solid var(--input-border);
            border-radius: 8px;
            background-color: #ffffff;
            color: var(--text-main);
            transition: border-color 0.15s ease, box-shadow 0.15s ease;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="number"]:focus {
            outline: none;
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }

        .actions {
            margin-top: 24px;
        }

        input[type="submit"] {
            width: 100%;
            background: var(--primary);
            color: #ffffff;
            border: none;
            border-radius: 8px;
            padding: 12px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.15s ease, transform 0.05s ease;
        }

        input[type="submit"]:hover {
            background: var(--primary-hover);
        }

        input[type="submit"]:active {
            transform: scale(0.99);
        }

        .flow-note {
            margin-top: 24px;
            padding: 12px 14px;
            background: #f1f5f9;
            border-radius: 8px;
            font-size: 0.8rem;
            color: var(--text-muted);
            line-height: 1.5;
        }

        .flow-note code {
            font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
            background: #e2e8f0;
            padding: 2px 5px;
            border-radius: 4px;
            color: #1e293b;
        }
    </style>
</head>
<body>
    <div class="container">
        <span class="badge">Experiment 6: HTML to JSP Conversion</span>
        <h1>Student Information</h1>
        <p class="subtitle">Enter student credentials below. The form submits via POST to <code>HelloServlet</code> (<code>/hello</code>) which forwards to <code>result.jsp</code>.</p>

        <form action="hello" method="post">
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" placeholder="e.g. Daksh Duhlani" required>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" placeholder="e.g. daksh@example.com" required>
            </div>

            <div class="form-group">
                <label for="course">Course:</label>
                <input type="text" id="course" name="course" placeholder="e.g. B.Tech Computer Science" required>
            </div>

            <div class="form-group">
                <label for="age">Age:</label>
                <input type="number" id="age" name="age" min="1" max="120" placeholder="e.g. 21" required>
            </div>

            <div class="actions">
                <input type="submit" value="Register">
            </div>
        </form>

        <div class="flow-note">
            <strong>MVC Request Flow:</strong><br>
            <code>student.jsp</code> &rarr; POST <code>/hello</code> &rarr; <code>HelloServlet.doPost()</code> &rarr; <code>request.setAttribute()</code> &rarr; <code>forward()</code> &rarr; <code>result.jsp</code>
        </div>
    </div>
</body>
</html>