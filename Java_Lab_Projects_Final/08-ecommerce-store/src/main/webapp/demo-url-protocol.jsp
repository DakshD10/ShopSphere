<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.*, java.io.*, java.util.*" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String targetUrlStr = request.getParameter("targetUrl");
    if (targetUrlStr == null || targetUrlStr.trim().isEmpty()) {
        // Default to current server's context URL or local loopback
        targetUrlStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    }

    URL parsedUrl = null;
    URLConnection connection = null;
    HttpURLConnection httpConn = null;
    Map<String, List<String>> headerFields = null;
    String contentSnippet = "";
    String errorMessage = null;
    InetAddress targetIp = null;
    InetAddress localHost = null;

    try {
        localHost = InetAddress.getLocalHost();
    } catch (Exception ignored) {}

    try {
        parsedUrl = new URL(targetUrlStr);
        try {
            targetIp = InetAddress.getByName(parsedUrl.getHost());
        } catch (Exception ignored) {}

        connection = parsedUrl.openConnection();
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);

        if (connection instanceof HttpURLConnection) {
            httpConn = (HttpURLConnection) connection;
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "ShopSphere-Java-Protocol-Demo/1.0");
        }

        headerFields = connection.getHeaderFields();

        // Read first few lines of content
        try (InputStream in = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null && lineCount < 15) {
                sb.append(line).append("\n");
                lineCount++;
            }
            contentSnippet = sb.toString();
        }
    } catch (Exception e) {
        errorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>URL & Protocol Handlers Demo · ShopSphere</title>
    <%@ include file="fragments/header.jsp" %>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

<%@ include file="fragments/navbar.jsp" %>

<div class="container py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Home</a></li>
            <li class="breadcrumb-item active" aria-current="page">URL & Protocol Handler Demonstration</li>
        </ol>
    </nav>

    <!-- Header Banner -->
    <div class="card border-0 shadow-sm mb-4 bg-primary text-white">
        <div class="card-body p-4">
            <div class="d-flex align-items-center justify-content-between">
                <div>
                    <h2 class="fw-bold mb-1"><i class="bi bi-globe me-2"></i>Java Network Protocol & URL Demonstration</h2>
                    <p class="mb-0 text-white-50">RTU Advanced Java Syllabus: <code>java.net.URL</code>, <code>URLConnection</code>, <code>HttpURLConnection</code>, Protocol Handlers, and <code>InetAddress</code></p>
                </div>
                <span class="badge bg-light text-primary fs-6 px-3 py-2 fw-semibold">Syllabus Topic 13 & 24</span>
            </div>
        </div>
    </div>

    <!-- URL Inspector Form -->
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body p-4">
            <h5 class="fw-bold text-navy mb-3"><i class="bi bi-search me-2"></i>Inspect URL Connection & Headers</h5>
            <form method="GET" action="demo-url-protocol.jsp" class="row g-3">
                <div class="col-md-9">
                    <label for="targetUrl" class="form-label fw-semibold">Target URL to Probe:</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-link-45deg"></i></span>
                        <input type="text" class="form-control" id="targetUrl" name="targetUrl"
                               value="<%= targetUrlStr %>" required>
                    </div>
                    <div class="form-text">Try local endpoint <code><%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/products</code> or any external HTTP/HTTPS URL.</div>
                </div>
                <div class="col-md-3 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                        <i class="bi bi-arrow-repeat me-1"></i> Probe URL
                    </button>
                </div>
            </form>
        </div>
    </div>

    <% if (errorMessage != null) { %>
        <div class="alert alert-warning border-0 shadow-sm d-flex align-items-center mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill fs-4 me-3 text-warning"></i>
            <div>
                <strong>Probing Notice:</strong> <%= errorMessage %><br>
                <small class="text-muted">Parsed URL components are still displayed below using <code>java.net.URL</code>.</small>
            </div>
        </div>
    <% } %>

    <div class="row g-4">
        <!-- Card 1: URL Breakdown -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm h-100">
                <div class="card-header bg-white py-3 border-bottom">
                    <h5 class="card-title mb-0 fw-bold text-navy"><i class="bi bi-diagram-3 me-2"></i>java.net.URL Parsing Components</h5>
                </div>
                <div class="card-body p-0">
                    <% if (parsedUrl != null) { %>
                    <table class="table table-striped table-hover mb-0">
                        <tbody>
                            <tr>
                                <th class="ps-4 w-40 text-muted">Protocol</th>
                                <td><span class="badge bg-primary"><%= parsedUrl.getProtocol() %></span></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Host Name</th>
                                <td><code><%= parsedUrl.getHost() %></code></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Port</th>
                                <td><%= parsedUrl.getPort() != -1 ? parsedUrl.getPort() : parsedUrl.getDefaultPort() + " (default)" %></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Path</th>
                                <td><code><%= parsedUrl.getPath() != null && !parsedUrl.getPath().isEmpty() ? parsedUrl.getPath() : "/" %></code></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Query Parameters</th>
                                <td><code><%= parsedUrl.getQuery() != null ? parsedUrl.getQuery() : "(none)" %></code></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Authority</th>
                                <td><code><%= parsedUrl.getAuthority() %></code></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Resolved Host IP</th>
                                <td><%= targetIp != null ? targetIp.getHostAddress() : "Unresolved" %></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Local Machine IP</th>
                                <td><%= localHost != null ? localHost.getHostAddress() + " (" + localHost.getHostName() + ")" : "Unknown" %></td>
                            </tr>
                        </tbody>
                    </table>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- Card 2: URLConnection & HTTP Headers -->
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm h-100">
                <div class="card-header bg-white py-3 border-bottom">
                    <h5 class="card-title mb-0 fw-bold text-navy"><i class="bi bi-file-earmark-code me-2"></i>URLConnection Metadata & Headers</h5>
                </div>
                <div class="card-body p-0">
                    <% if (connection != null) { %>
                    <table class="table table-striped table-hover mb-0">
                        <tbody>
                            <tr>
                                <th class="ps-4 w-40 text-muted">Connection Type</th>
                                <td><code><%= connection.getClass().getName() %></code></td>
                            </tr>
                            <% if (httpConn != null) { %>
                            <tr>
                                <th class="ps-4 text-muted">HTTP Response Code</th>
                                <td>
                                    <% try { %>
                                        <span class="badge <%= httpConn.getResponseCode() == 200 ? "bg-success" : "bg-warning" %>">
                                            <%= httpConn.getResponseCode() %> <%= httpConn.getResponseMessage() %>
                                        </span>
                                    <% } catch (Exception e) { %>
                                        <span class="text-muted">N/A</span>
                                    <% } %>
                                </td>
                            </tr>
                            <% } %>
                            <tr>
                                <th class="ps-4 text-muted">Content-Type</th>
                                <td><code><%= connection.getContentType() != null ? connection.getContentType() : "Unknown" %></code></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Content-Length</th>
                                <td><%= connection.getContentLength() != -1 ? connection.getContentLength() + " bytes" : "Streamed / Chunked" %></td>
                            </tr>
                            <tr>
                                <th class="ps-4 text-muted">Date Header</th>
                                <td><%= new Date(connection.getDate()) %></td>
                            </tr>
                        </tbody>
                    </table>

                    <% if (headerFields != null && !headerFields.isEmpty()) { %>
                        <div class="p-3 border-top bg-light">
                            <h6 class="fw-bold text-navy mb-2"><i class="bi bi-list-columns me-1"></i>Raw HTTP Header Map:</h6>
                            <div class="bg-dark text-light p-3 rounded font-monospace small overflow-auto" style="max-height: 180px;">
                                <% for (Map.Entry<String, List<String>> h : headerFields.entrySet()) { %>
                                    <div><strong class="text-warning"><%= h.getKey() != null ? h.getKey() : "Status" %>:</strong> <%= String.join(", ", h.getValue()) %></div>
                                <% } %>
                            </div>
                        </div>
                    <% } %>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- Card 3: Streamed Content Snippet -->
        <div class="col-12">
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-white py-3 border-bottom">
                    <h5 class="card-title mb-0 fw-bold text-navy"><i class="bi bi-file-earmark-text me-2"></i>Sample Content Stream (InputStream)</h5>
                </div>
                <div class="card-body">
                    <% if (!contentSnippet.isEmpty()) { %>
                        <pre class="bg-dark text-light p-3 rounded mb-0 font-monospace small overflow-auto" style="max-height: 250px;"><code><%= contentSnippet.replace("<", "&lt;").replace(">", "&gt;") %></code></pre>
                    <% } else { %>
                        <p class="text-muted mb-0">No stream content captured for this URL.</p>
                    <% } %>
                </div>
            </div>
        </div>

        <!-- Card 4: Academic Viva Notes -->
        <div class="col-12">
            <div class="card border-0 shadow-sm bg-white border-start border-4 border-info">
                <div class="card-body p-4">
                    <h5 class="fw-bold text-navy mb-3"><i class="bi bi-mortarboard me-2"></i>Academic Concepts: Protocol Handlers in Java</h5>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <div class="p-3 rounded bg-light">
                                <h6 class="fw-bold text-primary mb-1">1. URL Architecture</h6>
                                <p class="small text-muted mb-0">A <code>URL</code> represents a Uniform Resource Locator pointing to a resource on the web. It uses the syntax <code>protocol://host:port/path?query#ref</code>.</p>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="p-3 rounded bg-light">
                                <h6 class="fw-bold text-primary mb-1">2. Protocol Handlers</h6>
                                <p class="small text-muted mb-0">Java extensible protocol handler mechanism (<code>URLStreamHandler</code>) routes protocols like <code>http</code>, <code>https</code>, <code>file</code>, <code>jar</code>, and <code>ftp</code> to concrete <code>URLConnection</code> subclasses.</p>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="p-3 rounded bg-light">
                                <h6 class="fw-bold text-primary mb-1">3. Content Handlers</h6>
                                <p class="small text-muted mb-0"><code>ContentHandler</code> maps MIME types (e.g. <code>image/jpeg</code>, <code>text/html</code>) to Java objects retrieved via <code>url.getContent()</code>.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/fragments/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
