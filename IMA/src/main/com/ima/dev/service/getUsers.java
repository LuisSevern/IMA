package main.com.ima.dev.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


public class getUsers {

	
	public getUsers() {
		super();
	}

	public String searchCredentials(String username) {
		try {
			Hashtable env = new Hashtable();
			
		    InitialContext initContext = new InitialContext();  
		    String secAuthentication = (String) initContext.lookup("java:comp/env/SECURITY_AUTHENTICATION");			
		    String secPrincipal = (String) initContext.lookup("java:comp/env/SECURITY_PRINCIPAL");
		    String secCredentials = (String) initContext.lookup("java:comp/env/SECURITY_CREDENTIALS");			
		    String providerUrl = (String) initContext.lookup("java:comp/env/PROVIDER_URL");			
		    
			env.put(Context.SECURITY_AUTHENTICATION, secAuthentication);
			env.put(Context.SECURITY_PRINCIPAL, secPrincipal);
			env.put(Context.SECURITY_CREDENTIALS, secCredentials);
			env.put(Context.PROVIDER_URL, providerUrl);
		    
//			HashMap ldapProperties = this.readLdapProperties();						
//			env.put(Context.SECURITY_AUTHENTICATION, (String)ldapProperties.get("SECURITY_AUTHENTICATION"));
//			env.put(Context.SECURITY_PRINCIPAL, (String)ldapProperties.get("SECURITY_PRINCIPAL"));
//			env.put(Context.SECURITY_CREDENTIALS, (String)ldapProperties.get("SECURITY_CREDENTIALS"));
//			env.put(Context.PROVIDER_URL, (String)ldapProperties.get("PROVIDER_URL"));
			
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");			
			DirContext ctx = new InitialDirContext(env);
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration answer = ctx.search("", "uid=" + username, ctls);
			ctx.close();
			System.err.println("AMM IE: CONNECTED TO LDAP");
			return printSearchEnumeration(answer);
		} catch (NamingException nex) {
			System.out.println("Active Directory Connection: FAILED");
			nex.printStackTrace();
			return null;
		}
	}

	public String printSearchEnumeration(NamingEnumeration retEnum) {
		try {
			while (retEnum.hasMore()) {
				SearchResult sr = (SearchResult) retEnum.next();
				// System.out.println(">>" + sr.getNameInNamespace());
				return sr.getNameInNamespace();
			}
			return null;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean checkConnection(String username, String password) {
		boolean conn = false;
		boolean userInGroup = false;

		if (username != null) {
			try{
				// First of all is to check that the user belongs to the proper group of users
            	username=username.replaceAll("\\s+", "");
				InitialContext initContext = new InitialContext();  
				String secAuthentication = (String) initContext.lookup("java:comp/env/SECURITY_AUTHENTICATION");			
				String secPrincipal = (String) initContext.lookup("java:comp/env/SECURITY_PRINCIPAL");
				String secCredentials = (String) initContext.lookup("java:comp/env/SECURITY_CREDENTIALS");			
				String providerUrl = (String) initContext.lookup("java:comp/env/PROVIDER_URL");			
				Hashtable env = new Hashtable();		    
				env.put(Context.SECURITY_AUTHENTICATION, secAuthentication);
				env.put(Context.SECURITY_PRINCIPAL, secPrincipal);
				env.put(Context.SECURITY_CREDENTIALS, secCredentials);
				env.put(Context.PROVIDER_URL, providerUrl);
				env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
				DirContext ctx = new InitialDirContext(env);
				SearchControls ctls = new SearchControls();
				ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				String allowedGroup = (String) initContext.lookup("java:comp/env/ALLOWED_GROUP");
				System.err.println("User to athonticate: " + username);
				System.err.println("Belonging relationship to the group: " + allowedGroup);
				NamingEnumeration answer = ctx.search(allowedGroup, "objectclass=*", ctls);
		        while (answer.hasMore ()) {
		        	System.err.println("We are checking into the group");
		            SearchResult result = (SearchResult) answer.next ();    
		            Attributes attrs = result.getAttributes ();
		            System.err.println("Values into attrs: " + attrs.getAll());
		            NamingEnumeration enumAttrs = attrs.get("member").getAll();
		            System.err.println("Value of enumAttrs.hasMore(): " + enumAttrs.hasMore() + "\n Value of userInGroup: " + userInGroup);
		            while (enumAttrs.hasMore() && !userInGroup){
		            	Object obj = (Object) enumAttrs.next();
		            	System.err.println("Class of the object: " + obj.getClass().getName());
		            	if (obj instanceof String){
			            	String currentUserOfGroup = (String) obj;
			            	currentUserOfGroup=currentUserOfGroup.replaceAll("\\s+", "");
		            		System.err.println("Checking the object: " + currentUserOfGroup);
			            	if ((currentUserOfGroup != null) && (currentUserOfGroup.equalsIgnoreCase(username))){
			            		userInGroup = true;
			            		System.err.println("AMM IE: The user- " + username + " -belongs to the group- " + allowedGroup);
			            	}
		            	}
		            	
		            }

		        }
			
		        // If the user belongs to the proper group, the credentials have to be checked
		        if (userInGroup){				
		        	env = new Hashtable();
		        	env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		        	env.put(Context.PROVIDER_URL, providerUrl);
		        	env.put(Context.SECURITY_AUTHENTICATION, secAuthentication);
		        	env.put(Context.SECURITY_PRINCIPAL, username);
		        	env.put(Context.SECURITY_CREDENTIALS, password);
		        	// connect to LDAP
					ctx = new InitialDirContext(env);
					conn = true;
					System.err.println("AMM IE: User logged");
		        }
				ctx.close();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				conn = false;
			} finally {
				return conn;
			}
		} else
			return conn;
	}
	
//	private HashMap readLdapProperties(){
//		String secAuth, secPrincipal, secCredential, providerUrl = "";
//		HashMap ldapProperties = new HashMap();
//    	Properties prop = new Properties();    	 
//    	try {    		
//    		InputStream is = this.getClass().getClassLoader().getResourceAsStream("conf/ldap.properties");
//    		prop.load(is);    		
//    		secAuth = prop.getProperty("SECURITY_AUTHENTICATION");
//    		secPrincipal = prop.getProperty("SECURITY_PRINCIPAL");
//    		secCredential = prop.getProperty("SECURITY_CREDENTIALS");
//    		providerUrl = prop.getProperty("PROVIDER_URL");
//    		ldapProperties.put("SECURITY_AUTHENTICATION", secAuth);
//    		ldapProperties.put("SECURITY_PRINCIPAL", secPrincipal);
//    		ldapProperties.put("SECURITY_CREDENTIALS", secCredential);
//    		ldapProperties.put("PROVIDER_URL", providerUrl);
//    	} catch (IOException ex) {
//    		System.err.println("The LDAP properties file does not exist.");
//        }		
//		return ldapProperties;
//	}
}
