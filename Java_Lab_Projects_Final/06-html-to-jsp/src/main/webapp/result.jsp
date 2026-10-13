<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Result</title>
    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --success: #16a34a;
            --bg: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #e2e8f0;
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

        .badge-success {
            display: inline-block;
            font-size: 0.75rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            background: #dcfce7;
            color: #15803d;
            padding: 4px 10px;
            border-radius: 9999px;
            margin-bottom: 12px;
        }

        h1 {
            font-size: 1.65rem;
            font-weight: 700;
            color: var(--text-main);
            margin-bottom: 16px;
        }

        hr {
            border: none;
            border-top: 1px solid var(--border);
            margin: 16px 0 20px;
        }

        h3 {
            font-size: 1.15rem;
            font-weight: 600;
            color: var(--text-main);
            margin-bottom: 16px;
        }

        .details-list {
            background: #f8fafc;
            border: 1px solid var(--border);
            border-radius: 8px;
            padding: 16px 20px;
            margin-bottom: 24px;
        }

        .detail-row {
            padding: 8px 0;
            border-bottom: 1px dashed var(--border);
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 0.95rem;
        }

        .detail-row:last-child {
            border-bottom: none;
        }

        .detail-label {
            font-weight: 600;
            color: var(--text-muted);
        }

        .detail-val {
            font-weight: 600;
            color: var(--text-main);
        }

        .raw-paragraphs {
            margin-bottom: 20px;
            font-size: 0.95rem;
            line-height: 1.8;
        }

        .raw-paragraphs p {
            margin: 4px 0;
        }

        .btn-link {
            display: inline-block;
            text-align: center;
            width: 100%;
            background: var(--primary);
            color: #ffffff;
            text-decoration: none;
            padding: 12px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 0.95rem;
            transition: background 0.15s ease;
        }

        .btn-link:hover {
            background: var(--primary-hover);
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
        <span class="badge-success">&#10003; Form Processed Successfully</span>
        <h1>Registration Successful</h1>
        <hr>

        <h3>Student Details</h3>

        <!-- Verbatim output matching Professor PDF Page 4 using JSP Expression Language -->
        <div class="raw-paragraphs">
            <p><strong>Name:</strong> ${name}</p>
            <p><strong>Email:</strong> ${email}</p>
            <p><strong>Course:</strong> ${course}</p>
            <p><strong>Age:</strong> ${age}</p>
        </div>

        <div class="details-list">
            <div class="detail-row">
                <span class="detail-label">EL Expression \${name}</span>
                <span class="detail-val">${name}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">EL Expression \${email}</span>
                <span class="detail-val">${email}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">EL Expression \${course}</span>
                <span class="detail-val">${course}</span>
            </div>
            <div class="detail-row">
                <span class="detail-label">EL Expression \${age}</span>
                <span class="detail-val">${age}</span>
            </div>
        </div>

        <a class="btn-link" href="student.jsp">&larr; Register Another Student</a>

        <div class="flow-note">
            <strong>JSP Expression Language (EL):</strong><br>
            Retrieved via <code>request.getAttribute()</code> passed by <code>HelloServlet.java</code> using <code>RequestDispatcher.forward()</code>.
        </div>
    </div>
</body>
</html>