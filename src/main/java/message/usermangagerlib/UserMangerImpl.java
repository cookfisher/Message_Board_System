package message.usermangagerlib;
import message.separetedInterface.UserManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

public class UserMangerImpl implements UserManager {
    private String username;
    private String password;
    private String group;
    private File file;
    private List<String> grps = new ArrayList<String>();

    public UserMangerImpl() {
    }

    public UserMangerImpl(String username, String password, File file) {
        this.username = username;
        this.password = password;
        this.file = file;
    }

    private String helper(File file){
        try {
            Object obj = new JSONParser().parse(new FileReader(file));
            // typecasting
            JSONObject jo = (JSONObject) obj;
            JSONArray ja  = (JSONArray) jo.get("Users");
            Iterator itr1 = ja.iterator();
            while (itr1.hasNext()){
                Iterator<Map.Entry> itr2 = ((Map)itr1.next()).entrySet().iterator();
                while (itr2.hasNext()){
                    Map.Entry pair = itr2.next();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isAuthenticated(String username, String password, File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
//            file = new File("src/main/webapp/WEB-INF/userinfo1.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            //Create Xpath
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            XPathExpression expr = xpath.compile("//user[username = '" + username + "' and password = '" + password + "']");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            return nodes.getLength()==1;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String isBelongTo(String username, File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            //Create Xpath
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            XPathExpression expr = xpath.compile("//member[username = '" + username + "']/group/text()");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            String grp = nodes.item(0).getNodeValue();
            return grp;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getGroupView(String group) {
        switch (group) {
            case "concordia":
                grps.add("concordia");
                grps.add("encs");
                grps.add("comp");
                grps.add("soen");
                break;
            case "encs":
                grps.add("encs");
                grps.add("comp");
                grps.add("soen");
                break;
            case "comp":
                grps.add("comp");
                break;
            case "soen":
                grps.add("soen");
                break;
        }
        return grps;
    }

    public String getUsername() {
        return username;
    }

    public String getGroup() {
        return group;
    }

    public List<String> getGrps() {
        return grps;
    }

    public static void main(String[] args) throws Exception {
        String user = "jane";
        String pwd = "WEShXnZWP+3RGED9b0Dqew==";
        String group = "concordia";
        File file = new File("src/main/webapp/WEB-INF/userinfo1.xml");
        File file1 = new File("src/main/webapp/WEB-INF/membership.xml");
        File file2 = new File("src/main/webapp/WEB-INF/groups.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
//            File fileIS = new File("src/main/webapp/WEB-INF/userinfo1.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file2);
            //Create Xpath
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
//            XPathExpression expr = xpath.compile("//group[@parent = '" + group + "']");
            XPathExpression expr = xpath.compile("//descendant-or-self::group[@parent = '" + group + "'][text()]");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
//
////            XPathExpression expr1 = xpath.compile("//user[username = '" + user + "' and password = '" + pwd + "']/membership/text()");
////            Object result1 = expr1.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                System.out.println(nodes.item(i).getNodeValue());
            }
            System.out.println(nodes.getLength());
//            List<String> grps = new ArrayList<String>();
//            String temp;
//            grps.add(nodes.item(0).getNodeValue());
//            for (int i = 0; i < nodes.getLength(); i++) {
//                temp = nodes.item(i).getChildNodes().item(1).getTextContent();
//                grps.add(temp);
//            }

//             do{
//                temp = nodes.item(0).getChildNodes().item(1).getTextContent();
//                grps.add(temp);
//                expr = xpath.compile("//group[@parent = '" + temp + "']");
//                result = expr.evaluate(doc, XPathConstants.NODESET);
//                nodes = (NodeList) result;
//            }while (result != null);
//            for (int i = 0; i < nodes.getLength(); i++) {
//                String grp1 = nodes.item(i).getChildNodes().item(i).getTextContent();
//                System.out.println(grp1);
//            }
            System.out.println(nodes);
////            System.out.println(nodes.item(0).getNodeValue());
//            String grp = nodes.item(0).getNodeValue();
//            System.out.println(grp);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("=============");
        UserMangerImpl uImpl = new UserMangerImpl();
        System.out.println(uImpl.isAuthenticated(user,pwd,file));
        System.out.println(uImpl.isBelongTo(user,file1));
        System.out.println(uImpl.getGroupView(group));
    }

}
