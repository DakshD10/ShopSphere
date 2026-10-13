package com.shopsphere.util;

import javax.naming.*;
import java.util.Hashtable;

/**
 * JndiDemo: Educational demonstration of Java Naming and Directory Interface (JNDI).
 * Covers RTU Advanced Java Syllabus Topic:
 * - Naming / JNDI Architecture
 * - InitialContext instantiation
 * - Context creation, object binding (bind / rebind)
 * - Resource lookup (lookup)
 * - NamingEnumeration listing
 * - Container resource lookup explanation (e.g., java:comp/env/jdbc/ShopSphereDB in Apache Tomcat)
 */
public class JndiDemo {

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("  ShopSphere - Java Naming & Directory Interface (JNDI) Demonstration");
        System.out.println("====================================================================");

        try {
            // Step 1: Configure JNDI Environment
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, EducationalInitialContextFactory.class.getName());

            System.out.println("[1] Creating JNDI InitialContext via InitialContextFactory SPI...");
            Context rootCtx = new InitialContext();

            // Step 2: Creating Subcontexts (Hierarchical Naming)
            System.out.println("[2] Creating hierarchical subcontexts: 'comp' -> 'env' -> 'jdbc' & 'mail'...");
            Context compCtx = rootCtx.createSubcontext("comp");
            Context envCtx = compCtx.createSubcontext("env");
            Context jdbcCtx = envCtx.createSubcontext("jdbc");
            Context cfgCtx = envCtx.createSubcontext("config");

            // Step 3: Binding Resources into JNDI Registry
            System.out.println("[3] Binding Enterprise Resources into JNDI Directory...");

            String dbResourceDescriptor = "ShopSphereDataSource[url=jdbc:mysql://localhost:3306/shopsphere_db, maxPool=20, timeout=5000ms]";
            jdbcCtx.bind("ShopSphereDB", dbResourceDescriptor);
            System.out.println("    + Bound: 'comp/env/jdbc/ShopSphereDB' => " + dbResourceDescriptor);

            String appVersion = "ShopSphere Enterprise v1.0.0 (RTU Lab Release)";
            cfgCtx.bind("AppVersion", appVersion);
            System.out.println("    + Bound: 'comp/env/config/AppVersion' => " + appVersion);

            String supportEmail = "support@shopsphere.com";
            cfgCtx.bind("SupportEmail", supportEmail);
            System.out.println("    + Bound: 'comp/env/config/SupportEmail' => " + supportEmail);

            // Step 4: Looking Up Resources via JNDI Lookup (Standard J2EE Pattern)
            System.out.println("\n[4] Performing JNDI Resource Lookups (Simulating Servlet / DAO DataSource lookup)...");

            Object lookedUpDb = rootCtx.lookup("comp/env/jdbc/ShopSphereDB");
            System.out.println("    [JNDI Lookup Success] comp/env/jdbc/ShopSphereDB => " + lookedUpDb);

            Object lookedUpVersion = rootCtx.lookup("comp/env/config/AppVersion");
            System.out.println("    [JNDI Lookup Success] comp/env/config/AppVersion => " + lookedUpVersion);

            // Step 5: Rebinding a Resource (Updating dynamically)
            System.out.println("\n[5] Rebinding 'comp/env/config/AppVersion' to newer patch...");
            String patchedVersion = "ShopSphere Enterprise v1.0.1-RELEASE";
            cfgCtx.rebind("AppVersion", patchedVersion);
            System.out.println("    [Lookup after Rebind] => " + rootCtx.lookup("comp/env/config/AppVersion"));

            // Step 6: Listing Bindings (NamingEnumeration)
            System.out.println("\n[6] Listing all bindings registered under 'comp/env/config':");
            NamingEnumeration<Binding> bindings = cfgCtx.listBindings("");
            while (bindings.hasMore()) {
                Binding b = bindings.next();
                System.out.println("    • Name: " + b.getName() + " | Object: " + b.getObject());
            }

            // Step 7: Educational Summary for Viva
            System.out.println("\n====================================================================");
            System.out.println("  JNDI Academic Viva Summary:");
            System.out.println("  1. What is JNDI? Java Naming and Directory Interface provides a unified");
            System.out.println("     API to access multiple naming and directory services (LDAP, DNS, RMI,");
            System.out.println("     and J2EE / Tomcat container object registries).");
            System.out.println("  2. In Tomcat web applications, DataSource is configured in context.xml");
            System.out.println("     and looked up in Java code via: InitialContext ctx = new InitialContext();");
            System.out.println("     DataSource ds = (DataSource) ctx.lookup(\"java:comp/env/jdbc/ShopSphereDB\");");
            System.out.println("  3. Benefits: Decouples database credentials and pool configuration");
            System.out.println("     from Java source code; configured by system administrators.");
            System.out.println("====================================================================");

        } catch (Exception e) {
            System.err.println("JNDI Demo Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * InMemoryContext: Lightweight educational implementation of javax.naming.Context
     * allowing standalone JVM execution without container setup.
     */
    public static class InMemoryContext implements Context {
        private final java.util.Map<String, Object> bindings = new java.util.concurrent.ConcurrentHashMap<>();
        private final String name;

        public InMemoryContext() { this.name = "root"; }
        public InMemoryContext(String name) { this.name = name; }

        @Override
        public Object lookup(String name) throws NamingException {
            if (name.contains("/")) {
                String[] parts = name.split("/", 2);
                Object next = bindings.get(parts[0]);
                if (next instanceof Context) {
                    return ((Context) next).lookup(parts[1]);
                }
                throw new NameNotFoundException("Context not found: " + parts[0]);
            }
            Object obj = bindings.get(name);
            if (obj == null) throw new NameNotFoundException("Name not bound: " + name);
            return obj;
        }

        @Override
        public void bind(String name, Object obj) throws NamingException {
            if (bindings.containsKey(name)) throw new NameAlreadyBoundException(name);
            bindings.put(name, obj);
        }

        @Override
        public void rebind(String name, Object obj) throws NamingException {
            bindings.put(name, obj);
        }

        @Override
        public Context createSubcontext(String name) throws NamingException {
            InMemoryContext sub = new InMemoryContext(name);
            bind(name, sub);
            return sub;
        }

        @Override
        public NamingEnumeration<Binding> listBindings(String name) {
            java.util.List<Binding> list = new java.util.ArrayList<>();
            for (java.util.Map.Entry<String, Object> entry : bindings.entrySet()) {
                list.add(new Binding(entry.getKey(), entry.getValue()));
            }
            java.util.Iterator<Binding> it = list.iterator();
            return new NamingEnumeration<>() {
                @Override public boolean hasMore() { return it.hasNext(); }
                @Override public Binding next() { return it.next(); }
                @Override public boolean hasMoreElements() { return it.hasNext(); }
                @Override public Binding nextElement() { return it.next(); }
                @Override public void close() {}
            };
        }

        // Unused interface methods stubbed for compliance
        @Override public Object lookup(Name name) throws NamingException { return lookup(name.toString()); }
        @Override public void bind(Name name, Object obj) throws NamingException { bind(name.toString(), obj); }
        @Override public void rebind(Name name, Object obj) throws NamingException { rebind(name.toString(), obj); }
        @Override public void unbind(Name name) {}
        @Override public void unbind(String name) { bindings.remove(name); }
        @Override public void rename(Name oldName, Name newName) {}
        @Override public void rename(String oldName, String newName) {}
        @Override public NamingEnumeration<NameClassPair> list(Name name) { return null; }
        @Override public NamingEnumeration<NameClassPair> list(String name) { return null; }
        @Override public NamingEnumeration<Binding> listBindings(Name name) { return null; }
        @Override public void destroySubcontext(Name name) {}
        @Override public void destroySubcontext(String name) { bindings.remove(name); }
        @Override public Context createSubcontext(Name name) throws NamingException { return createSubcontext(name.toString()); }
        @Override public Object lookupLink(Name name) { return null; }
        @Override public Object lookupLink(String name) { return null; }
        @Override public NameParser getNameParser(Name name) { return null; }
        @Override public NameParser getNameParser(String name) { return null; }
        @Override public Name composeName(Name name, Name prefix) { return null; }
        @Override public String composeName(String name, String prefix) { return prefix + "/" + name; }
        @Override public Object addToEnvironment(String propName, Object propVal) { return null; }
        @Override public Object removeFromEnvironment(String propName) { return null; }
        @Override public Hashtable<?, ?> getEnvironment() { return new Hashtable<>(); }
        @Override public void close() {}
        @Override public String getNameInNamespace() { return name; }
    }

    /**
     * Factory class required by javax.naming.spi.NamingManager to instantiate the Context.
     */
    public static class EducationalInitialContextFactory implements javax.naming.spi.InitialContextFactory {
        private static final InMemoryContext SHARED_CONTEXT = new InMemoryContext();

        @Override
        public Context getInitialContext(Hashtable<?, ?> environment) {
            return SHARED_CONTEXT;
        }
    }
}
