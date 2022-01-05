/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EmployeeDetails;

import java.io.*;
import java.text.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.Versioned;
import java.nio.file.Paths;

public class Employee implements Serializable {

    String name, surname, email, date_of_birth;

    Employee(String name, String surname, String email, String date_of_birth) {

        this.name = name;
        this.surname = surname;
        this.email = email;
        this.date_of_birth = date_of_birth;

    }

    @Override
    public String toString() {
        return name + " " + surname + " " + email + " " + date_of_birth;
    }

    static File file = new File("Employee.txt");

    static ArrayList<Employee> aList = new ArrayList<>();
    static ObjectOutputStream oos;

    static ObjectInputStream ois;
    static ListIterator li;
    static Scanner sString = new Scanner(System.in);

    
  // create object mapper instance
    static ObjectMapper mapper = new ObjectMapper();

    // convert book object to JSON file
    

//checking if the input is an email
    public static boolean isEmail(String email) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.find();
    }
//checking if email already exist

    public static boolean isEmailExist(String email) throws Exception {

        boolean found = false;
        if (file.isFile()) {
            ois = new ObjectInputStream(new FileInputStream(file));
            aList = (ArrayList<Employee>) ois.readObject();
            ois.close();
            li = aList.listIterator();
            while (li.hasNext()) {
                Employee e = (Employee) li.next();
                found = e.email.contentEquals(email);
            }
        }
        return found;
    }
//Date validation

    public static boolean isDate(String isDate) {
        Pattern p = Pattern.compile("[A-Z,a-z,&%$#@!()*^]");
        Matcher m;

        boolean validation = false;
        String date_of_birth;

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setLenient(false);

            m = p.matcher(isDate);
            try {
                if (m.find()) {
                    System.out.print("Please enter a valid date of birth. \n");
                    validation = false;
                } else {
                    String dateParts[] = isDate.split("/");

                    // Getting  year from date
                    String year = dateParts[2];

                    int theYear = Integer.parseInt(year);
                    LocalDate current_date = LocalDate.now();
                    int current_year = current_date.getYear();

                    if (current_year - theYear < 18) {
                        System.out.print("An employee cannot be younger than  18, please enter a valid date of birth. \n");
                        validation = false;
                    } else if (current_year - theYear >= 18) {
                        date_of_birth = isDate;
                        Date date = format.parse(date_of_birth);
                        validation = true;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.print("Please enter a valid date of birth. \n");
            }
        } catch (ParseException e) {
            System.out.print("Please enter a valid date of birth. \n");
            validation = false;
        }
        date_of_birth = isDate;

        return validation;
    }

//Add employee
    public static void addEmployee() throws FileNotFoundException, IOException, Exception {
        int number = 0;
        do {

            System.out.print("Enter Employee Name: ");
            while (!sString.hasNext("[A-Za-z]*")) {
                System.out.print(sString.nextLine() + " is not a valid name please enter a valid name: ");
            }
            String name = sString.nextLine();

            System.out.print("Enter Emplyee Surname: ");
            while (!sString.hasNext("[A-Za-z]*")) {
                System.out.print(sString.nextLine() + " is not a valid surname please enter a valid surnname: ");
            }
            String surname = sString.nextLine();

            String email;
            String str;
            boolean eValidation;
            do {
                System.out.print("Enter Employee Email: ");
                str = sString.nextLine();
                if (Employee.isEmailExist(str) == true) {
                    System.out.print("Email already exist. \n");
                    eValidation = false;
                } else if (Employee.isEmail(str)) {
                    eValidation = true;
                } else {
                    System.out.print("Invalid email. \n");
                    eValidation = false;
                }
                email = str;
            } while (eValidation == false);

            String date_of_birth;
            String d;
            boolean isDate;
            do {
                System.out.print("Enter your date of birth(dd/MM/yyyy): ");
                d = sString.nextLine();
                isDate = Employee.isDate(d) == true;
                date_of_birth = d;
            } while (isDate == false);
            aList.add(new Employee(name, surname, email, date_of_birth));
            number++;
        } while (number < 0);
        ObjectMapper.writeValue(Paths.get("books.json").toFile(), aList);

        oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(aList);
        oos.flush();
        oos.close();
    }

//Display Employees
    public static void display() throws FileNotFoundException, IOException, ClassNotFoundException {
        if (file.isFile()) {
            ois = new ObjectInputStream(new FileInputStream(file));
            aList = (ArrayList<Employee>) ois.readObject();
            ois.close();

            System.out.println("*****************************************************************************");
            li = aList.listIterator();
            while (li.hasNext()) {
                System.out.println(li.next());
            }
        } else {
            System.out.println("There are no employees to display");
        }
    }
//Search employee

    public static void searchEmployee() throws FileNotFoundException, IOException, ClassNotFoundException {

        if (file.isFile()) {
            ois = new ObjectInputStream(new FileInputStream(file));
            aList = (ArrayList<Employee>) ois.readObject();
            ois.close();
            boolean found = false;

            System.out.println("*****************************************************************************");
            System.out.print("Enter Employee email to search: ");
            String email = sString.nextLine();
            System.out.println("*****************************************************************************");

            li = aList.listIterator();
            while (li.hasNext()) {
                Employee e = (Employee) li.next();
                if (e.email.contentEquals(email)) {
                    System.out.println(e);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Employee not found.");
            }
        } else {
            System.out.println("File does not exist");
        }
    }

// update employee
    public static void updateEmployee() throws IOException, ClassNotFoundException {
        if (file.isFile()) {
            ois = new ObjectInputStream(new FileInputStream(file));
            aList = (ArrayList<Employee>) ois.readObject();
            ois.close();

            System.out.print("Enter Employee email to update: ");
            String empEmail = sString.nextLine();
            boolean found = false;
            System.out.println("*****************************************************************************");

            li = aList.listIterator();
            while (li.hasNext()) {
                Employee e = (Employee) li.next();
                if (e.email.contentEquals(empEmail)) {

                    System.out.print("Enter Employee Name: ");
                    while (!sString.hasNext("[A-Za-z]*")) {
                        System.out.print(sString.nextLine() + " is not a valid name please enter a valid name: ");
                    }
                    String name = sString.nextLine();

                    System.out.print("Enter Emplyee Surname: ");
                    while (!sString.hasNext("[A-Za-z]*")) {
                        System.out.print(sString.nextLine() + " is not a valid surname please enter a valid surnname: ");
                    }
                    String surname = sString.nextLine();

                    String date_of_birth = null;
                    String d;
                    boolean isDate;
                    do {
                        System.out.print("Enter your date of birth(dd/MM/yyyy): ");
                        d = sString.nextLine();
                        isDate = Employee.isDate(d) == true;
                        date_of_birth = d;
                    } while (isDate == false);
                    li.set(new Employee(name, surname, empEmail, date_of_birth));
                    found = true;
                }
            }
            if (found) {
                oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(aList);
                oos.close();
                System.out.println("Record updated successfully");
            } else {
                System.out.println("Employee not found.");
            }

        } else {
            System.out.println("File does not exist");
        }
    }

//Delete employee
    public static void deleteEmployee() throws FileNotFoundException, IOException, ClassNotFoundException {

        if (file.isFile()) {
            ois = new ObjectInputStream(new FileInputStream(file));
            aList = (ArrayList<Employee>) ois.readObject();
            ois.close();

            System.out.println("*****************************************************************************");
            System.out.print("Enter Employee email to delete: ");
            String email = sString.nextLine();
            boolean found = false;

            System.out.println("*****************************************************************************");
            li = aList.listIterator();
            while (li.hasNext()) {
                Employee e = (Employee) li.next();
                if (e.email.contentEquals(email)) {
                    li.remove();
                    found = true;
                }
            }
            if (found) {
                oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(aList);
                oos.flush();
                oos.close();
                System.out.println("Record deleted successfully");
            } else {
                System.out.println("Employee not found.");
            }
        } else {
            System.out.println("File does not exist");
        }
    }

//sort employees
    public static void sortEmployee() throws FileNotFoundException, IOException, ClassNotFoundException {
        if (file.isFile()) {
            ois = new ObjectInputStream(new FileInputStream(file));
            aList = (ArrayList<Employee>) ois.readObject();
            ois.close();

            Collections.sort(aList, new NameComparator());

            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(aList);
            oos.flush();
            oos.close();

            System.out.println("*****************************************************************************");
            li = aList.listIterator();
            while (li.hasNext()) {
                System.out.println(li.next());
            }
        } else {
            System.out.println("File does not exist");
        }
    }
}

class NameComparator implements Comparator<Employee> {

    // override the compare() method
    @Override
    public int compare(Employee e1, Employee e2) {
        return e1.name.compareTo(e2.name);
    }
}
