package GK2;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXML {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        // Khởi tạo thông tin sinh viên ghi vào danh sách student
        students.add(new Student("12", "Vo Van Phuc", "Quang Binh", "2005-06-23"));
        students.add(new Student("22", "Nguyen Thanh Truc", "Ha Noi", "2005-12-21"));
        students.add(new Student("32", "Nguyen Viet Tin", "Quang Nam", "2005-12-24"));
        students.add(new Student("42", "Truong Thi My Oanh", "Da Nang", "2005-2-21"));
        
        createStudentXMLFile(students, "student.xml");
    }

    public static void createStudentXMLFile(List<Student> students, String filename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Students");
            doc.appendChild(rootElement);

            for (Student student : students) {
                // Duyệt qua các phần tử trong hàm thông tin sinh viên
                Element studentElement = doc.createElement("Student");
                rootElement.appendChild(studentElement);

               // THuộc tính sinh viên
                studentElement.setAttribute("id", student.id);

               // Tên 
                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(student.name));
                studentElement.appendChild(nameElement);

                // Đia chi
                Element addressElement = doc.createElement("address");
                addressElement.appendChild(doc.createTextNode(student.address));
                studentElement.appendChild(addressElement);

                // Ngay thang
                Element dobElement = doc.createElement("dateOfBirth");
                dobElement.appendChild(doc.createTextNode(student.dateOfBirth));
                studentElement.appendChild(dobElement);
            }

            // Viet file vao student
            FileOutputStream fos = new FileOutputStream(new File(filename));
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(fos));
            fos.close();

            System.out.println("Tao file XML Student Thanh cong: " + filename);
        } catch (ParserConfigurationException | javax.xml.transform.TransformerException | IOException e) {
            e.printStackTrace(); // bắt lỗi ngoại lệ khi dữ liệu sai
        }
    }

  
    static class Student {
        String id;
        String name;
        String address;
        String dateOfBirth;

        public Student(String id, String name, String address, String dateOfBirth) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.dateOfBirth = dateOfBirth;
        }
    }
}