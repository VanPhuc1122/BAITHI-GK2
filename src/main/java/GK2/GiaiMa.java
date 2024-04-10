package GK2;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.util.Base64;

public class GiaiMa {
    public static void main(String[] args) {
        decodeResultsFromFile("kq.xml");
    }

    private static void decodeResultsFromFile(String filename) {
        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Student");

            System.out.println("Ket qua ma hoa:");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = MaHoa(element.getElementsByTagName("id").item(0).getTextContent());
                    String age = MaHoa(element.getElementsByTagName("age").item(0).getTextContent());
                    String sum = MaHoa(element.getElementsByTagName("sum").item(0).getTextContent());
                    String isDigit = MaHoa(element.getElementsByTagName("isDigit").item(0).getTextContent());
                    
                    System.out.println("Student ID: " + id);
                    System.out.println("Age: " + age);
                    System.out.println("Sum: " + sum);
                    System.out.println("Digit Prime: " + isDigit);
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String MaHoa(String encodedString) {
        // dùng để giải mã dữ liệu và trả về mảng byte chứa dữ liệu được giải mã
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}