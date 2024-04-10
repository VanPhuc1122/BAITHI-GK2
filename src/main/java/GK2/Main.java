/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GK2;

import java.io.*;
import java.util.concurrent.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

class Student {
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

class TinhTongTuoi implements Runnable {
    // sử dụng hàng đợi để lưu thông tin sinh viên
    private BlockingQueue<Student> studentQueue;
    private BlockingQueue<String> resultQueue;

    public TinhTongTuoi(BlockingQueue<Student> studentQueue, BlockingQueue<String> resultQueue) {
        this.studentQueue = studentQueue;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Student student = studentQueue.take();
                if (student == null || student.id.equals("ABC")) {
                    resultQueue.put("ABC");
                    break;
                }
                int age = TinhTongTUoi(student.dateOfBirth);
                int sum = TinhTongSo(student.dateOfBirth);
                boolean isPrime = isPrime(sum);
                resultQueue.put("<Student>\n<id>" + MaHoa(student.id) + "</Id>\n<age>" + MaHoa(String.valueOf(age)) + "</Age>\n<sum>" + MaHoa(String.valueOf(sum)) + "</Sum>\n<isDigit>" + MaHoa(String.valueOf(isPrime)) + "</Digit>\n</Student>");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int TinhTongTUoi(String dob) {
        // Lấy ra ngày 
        LocalDate birthDate = LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE);
        // Lấy ra ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        // Ngày hiện tại - ngày sinh
        return currentDate.getYear() - birthDate.getYear();
    }

    private int TinhTongSo(String dob) {
        int sum = 0;
        for (char c : dob.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            }
        }
        return sum;
    }
     // Hàm kiểm tra số nguyên tố
    private boolean isPrime(int n) {
       if(n <= 1){
           return false;
       }
       for(int i = 2; i <= Math.sqrt(n); i++){
           if(n % i == 0){
               return false;
           }
       }
       return true;
    }

    private String MaHoa(String data) {
        
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
}

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        // Initialize student information
        students.add(new Student("12", "Vo Van Phuc", "Quang Binh", "2005-06-23"));
        students.add(new Student("22", "Nguyen Thanh Truc", "Ha Noi", "2005-12-21"));
        students.add(new Student("32", "Nguyen Viet Tin", "Quang Nam", "2005-12-24"));
        students.add(new Student("42", "Truong Thi My Oanh", "Da Nang", "2005-02-21"));

        // Pass student list and output filename to CreateXML
        
 
  
        BlockingQueue<Student> studentQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> resultQueue = new LinkedBlockingQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        // thực thi đọc file student.xml từ danh sách hàng đợi sinh viên
        executor.execute(new Reader("student.xml", studentQueue));
        executor.execute(new TinhTongTuoi(studentQueue, resultQueue));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("kq.xml"));
            writer.write("<Students>\n");
            while (true) {
                String result = resultQueue.take();
                if (result.equals("ABC")) {
                    writer.write("</Students>");
                    writer.close();
                    break;
                }
                writer.write(result + "\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}

class Reader implements Runnable {
    private String filename;
    private BlockingQueue<Student> studentQueue;

    public Reader(String filename, BlockingQueue<Student> studentQueue) {
        this.filename = filename;
        this.studentQueue = studentQueue;
    }

    @Override
    public void run() {
        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList danhsach = doc.getElementsByTagName("Student");

            for (int temp = 0; temp < danhsach.getLength(); temp++) {
                Node nNode = danhsach.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String id = eElement.getAttribute("id");
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String address = eElement.getElementsByTagName("address").item(0).getTextContent();
                    String dob = eElement.getElementsByTagName("dateOfBirth").item(0).getTextContent();
                    studentQueue.put(new Student(id, name, address, dob));
                }
            }
           
            studentQueue.put(new Student("ABC", "", "", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}