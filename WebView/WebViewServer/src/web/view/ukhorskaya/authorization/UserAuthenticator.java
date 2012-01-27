package web.view.ukhorskaya.authorization;

import com.intellij.openapi.util.Pair;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.demo.ukhorskaya.ErrorWriter;
import org.jetbrains.demo.ukhorskaya.ErrorWriterOnServer;
import org.jetbrains.demo.ukhorskaya.ResponseUtils;
import org.jetbrains.demo.ukhorskaya.server.ServerSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 12/21/11
 * Time: 12:51 PM
 */
public class UserAuthenticator extends BasicAuthenticator {
    private final String type;

    private static String userName;

    @Override
    public Result authenticate(HttpExchange exchange) {
        String data;
        try {
            data = getPostDataFromRequest(exchange);
        } catch (IllegalArgumentException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(
                    "LOGIN", e, exchange.getRequestURI().toString()));
            return super.authenticate(exchange);
        }
        Pair<String, String> pair = parseData(data);
        if (checkCredentials(pair.first, pair.second)) {
            return new Success(new HttpPrincipal(pair.first, this.getRealm()));
        } else {
            return new Failure(401);
        }
        //To change body of overridden methods use File | Settings | File Templates.
    }

    public UserAuthenticator(String type) {
        super(type);
        this.type = type;
    }

    @Override
    public boolean checkCredentials(String login, String password) {
        if (login.equals("") || password.equals("")) {
            return false;
        }

        if (loginWithLDAP(login, password)) {
            return true;
        }


        return false;
        /*if (login.equals("root")) {
            return true;
        } else if (login.equals("facebook")) {
            facebookConnect();
        }
        return false;*/
        /*Map<String, String> map = readUsersFromFile();
        if (map == null) {
            return false;
        }
        String password = map.get(s);
        try {
            s1 = generateMD5(s1);
        } catch (NoSuchAlgorithmException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("LOGIN", e, "login: " + s));
            return false;
        } catch (UnsupportedEncodingException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("LOGIN", e, "login: " + s));
            return false;
        }
        return password != null && password.equals(s1);*/
    }

    private boolean loginWithLDAP(String login, String password) {
        try {

            LdapContext context = null;
            try {
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY,
                        "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.SECURITY_AUTHENTICATION, "Simple");
                env.put(Context.SECURITY_PRINCIPAL, login);
                //                env.put(Context.SECURITY_PRINCIPAL, "labs\\natalia.ukhorskaya");
                //                env.put(Context.SECURITY_CREDENTIALS, "DeeJou5d");
                env.put(Context.SECURITY_CREDENTIALS, password);
                env.put(Context.PROVIDER_URL, "ldap://msdc.labs.intellij.net:389");
                context = new InitialLdapContext(env, null);
                System.out.println("Connection Successful.");

            } catch (NamingException nex) {
                System.out.println("LDAP Connection: FAILED");
                //nex.printStackTrace();
                return false;
            }

            String cn;
            if (login.contains("\\")) {
                cn = login.substring(login.indexOf("\\"));
            } else {
                cn = login;
            }

            String query = "(&(objectclass=person)(samaccountname=" + cn + "))";
            String attribute = "cn";

            SearchControls ctrl = new SearchControls();
            ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration enumeration = context.search("dc=labs,dc=intellij,dc=net", query, ctrl);
            SearchResult result = (SearchResult) enumeration.next();
            if (result != null) {
                Attributes attribs = result.getAttributes();
                NamingEnumeration values = attribs.get(attribute).getAll();
                if (values.hasMore()) {
                    userName = values.next().toString();
                }
                System.out.print("Hello, " + userName);
                return true;
            }

        } catch (Throwable e) {
            // incorrect login or password
        }
        return false;
    }

    public String getUserName() {
        if (userName != null) {
            return userName;
        } else {
            return "";
        }
    }

    private void facebookConnect() {
        String api_key = "";
        String secret = "";
        /*IFacebookRestClient<Document> userClient = null;
        if (userClient == null) {
            userClient = new FacebookXmlRestClient(api_key, secret);
        }

        FacebookWebappHelper<Document> facebook = new FacebookWebappHelper<Document>(null, null, api_key, secret, userClient);
        String nextPage = "/";
        boolean redirectOccurred = facebook.requireLogin(nextPage);
        if (redirectOccurred) {
            return;
        }
        redirectOccurred = facebook.requireFrame(nextPage);
        if (redirectOccurred) {
            return;
        }

        long facebookUserID;
        try {
            facebookUserID = userClient.users_getLoggedInUser();
        } catch (FacebookException ex) {
            System.out.println("Error while fetching user's facebook ID");
            System.out.println("Error while getting cached (supplied by request params) value " +
                    "of the user's facebook ID or while fetching it from the Facebook service " +
                    "if the cached value was not present for some reason. Cached value = {}" + userClient.getCacheUserId());
            return;
        }
        System.out.println(facebookUserID);*/
    }


    private Pair<String, String> parseData(String data) {
        return new Pair<String, String>(ResponseUtils.substringBetween(data, "login=", "&"),
                ResponseUtils.substringAfter(data, "password="));
    }

    private String getPostDataFromRequest(HttpExchange exchange) {
        StringBuilder reqResponse = new StringBuilder();
        try {
            reqResponse.append(ResponseUtils.readData(exchange.getRequestBody()));
        } catch (IOException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("Read data from post request", e, "getPostDataFromRequest " + exchange.getRequestURI()));
            throw new IllegalArgumentException("Cannot read data from file");
        }

        String finalResponse = null;
        try {
            finalResponse = URLDecoder.decode(reqResponse.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("LOGIN", e, "null"));
            throw new IllegalArgumentException("Cannot read data from file");
        }
        return finalResponse;
    }

    public String addUser(String login, String password) {
        try {
            File file = new File(ServerSettings.STATISTICS_ROOT + File.separator + type + ".xml");
            if (!file.exists()) {
                return "File doesn't exists: " + file.getAbsolutePath();
            }

            Map<String, String> map = readUsersFromFile();
            if (map == null) {
                return "Impossible to read xml file: " + file.getAbsolutePath();
            }
            if (map.containsKey(login)) {
                return "User already exists";
            }

            Document document = ResponseUtils.getXmlDocument(file);
            if (document == null) {
                return "Impossible to read xml file: " + file.getAbsolutePath();
            }
            NodeList nodeList = document.getElementsByTagName("users");
            Node newNode = document.createElement("user");
            newNode.appendChild(document.createElement("login"));
            newNode.appendChild(document.createTextNode(login));
            newNode.appendChild(document.createElement("password"));
            try {
                password = generateMD5(password);
            } catch (NoSuchAlgorithmException e) {
                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("LOGIN", e, "login: " + login));
                return "Impossible to generate MD5";
            } catch (UnsupportedEncodingException e) {
                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("LOGIN", e, "login: " + login));
                return "Impossible to read password in UTF-8";
            }
            newNode.appendChild(document.createTextNode(password));
            nodeList.item(0).appendChild(newNode);

            NodeList list = document.getElementsByTagName("users");
            Node node = list.item(0);

            DOMSource source = new DOMSource(node);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            FileWriter writer = new FileWriter(file);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            writer.close();
            return "User was added";
        } catch (Throwable e) {
            e.printStackTrace();
            return "Unknown error: User wasn't added";
        }
    }

    private String generateMD5(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] userPasswordMD5 = password.getBytes("UTF-8");
        userPasswordMD5 = messageDigest.digest(userPasswordMD5);
        BigInteger bigInt = new BigInteger(1, userPasswordMD5);
        String hashText = bigInt.toString(16);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        password = hashText;
        return password;
    }

    @Nullable
    private Map<String, String> readUsersFromFile() {
        try {
            //TODO add exceptions
            Map<String, String> users = new HashMap<String, String>();

            File file = new File(ServerSettings.STATISTICS_ROOT + File.separator + type + ".xml");
            if (!file.exists()) {
                return null;
            }

            Document document = ResponseUtils.getXmlDocument(file);
            if (document == null) {
                return null;
            }
            NodeList nodeList = document.getElementsByTagName("user");
            if (nodeList == null) {
                return null;
            }
            System.out.println(nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList children = nodeList.item(i).getChildNodes();
                users.put(children.item(1).getTextContent(), children.item(3).getTextContent());
            }
            return users;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void setUserName(String name) {
        userName = name;
    }
}
