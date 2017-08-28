package UM.RU.LDAP;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

public class ValidateUser {

    private String domain;
    private String ldapHost;
    private String searchBase;

    public ValidateUser(String domain, String host, String dn) {
        this.domain = domain;
        this.ldapHost = host;
        this.searchBase = dn;
    }

    public Map<String, Object> CheckUser(String uname, String pass) {
        HashMap res = new HashMap();

        String returnedAtts[] = {"sn", "givenName", "name", "displayName", "memberOf"};
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + uname + "))";

        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);

        // Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapHost);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, uname + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, pass);

        LdapContext ctxGC = null;

        try {
            // This is the actual Authentication piece. Will throw javax.naming.AuthenticationException
            // if the users password is not correct. Other exceptions may include IO (server not found) etc.
            ctxGC = new InitialLdapContext(env, null);
            System.out.println("Подключение к LDAP:: " + env.get(Context.PROVIDER_URL) + " под " + env.get(Context.SECURITY_PRINCIPAL) + " прошло успешно");
            // Now try a simple search and get some attributes as defined in returnedAtts
            NamingEnumeration<SearchResult> answer = ctxGC.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                Map<String, Object> amap = null;
                if (attrs != null) {
                    amap = new HashMap<String, Object>();
                    NamingEnumeration<?> ne = attrs.getAll();
                    while (ne.hasMore()) {
                        Attribute attr = (Attribute) ne.next();
                        if (attr.size() == 1) {
                            amap.put(attr.getID(), attr.get());
                        } else {
                            HashSet<String> s = new HashSet<String>();
                            NamingEnumeration n = attr.getAll();
                            while (n.hasMoreElements()) {
                                s.add((String) n.nextElement());
                            }
                            amap.put(attr.getID(), s);
                        }
                    }
                    ne.close();
                }
                res.put("first_name", (String) amap.get("sn"));
                res.put("last_name", amap.get("givenName"));
                res.put("validation_error", "Авторизация в LDAP прошла успешно!");
                //     ctxGC.close();  // Close and clean up
                return amap;
            }
        } catch (NamingException nex) {
            nex.printStackTrace();
            res.put("validation_error", "Неверный либо имя пользователя либо пароль! " + nex);
        } catch (Exception ex) {
            ex.printStackTrace();
            res.put("validation_error", "Неверный либо имя пользователя либо пароль! " + ex);
        } finally {
            if (ctxGC != null) {
                try {
                    ctxGC.close();
                } catch (Exception e) {
                    //do something clever with the exception
                    res.put("validation_error", "Неверный либо имя пользователя либо пароль! " + e);
                }
            }
        }
        return res;
    }

}

