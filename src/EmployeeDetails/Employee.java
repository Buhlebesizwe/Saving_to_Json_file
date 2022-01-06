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
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.nio.file.*;

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

    static File file = new File("Employee.json");
    static ArrayList<Employee> aList = new ArrayList<>();
    static ArrayList<Employee> zList = new ArrayList<>();
    static ObjectOutputStream oos;
    static ObjectInputStream ois;
    static ListIterator li;
    static Scanner sString = new Scanner(System.in);

//add employee to json file
    static void saveToJson(ArrayList object) throws IOException {
        Gson gson = new Gson();
        try (
                Reader reader = Files.newBufferedReader(Paths.get("Employee.json"))) {
            // convert JSON array to list of users
            ArrayList<Employee> users = new Gson().fromJson(reader, new TypeToken<ArrayList<Employee>>() {
            }.getType());

            users.addAll(object);
                String json = gson.toJson(users);
                try (Writer writer = new FileWriter(file)) {
                    writer.write(json);
                    writer.flush();
                    
                }

        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
    //update json

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
        try (
                Reader reader = Files.newBufferedReader(Paths.get("Employee.json"))) {
            // convert JSON array to list of users
            ArrayList<Employee> users = new Gson().fromJson(reader, new TypeToken<ArrayList<Employee>>() {
            }.getType());
            reader.close();
            li = users.listIterator();
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
        saveToJson(aList);

    }

//Display Employees
    public static void display() throws IOException {
        try {
            try (Reader reader = Files.newBufferedReader(Paths.get("Employee.json"))) {

                List<Employee> users = new Gson().fromJson(reader, new TypeToken<List<Employee>>() {
                }.getType());
                System.out.println("Employees List");
                users.forEach(System.out::println);
                reader.close();
            }

        } catch (JsonIOException | JsonSyntaxException | IOException ex) {
            System.out.println("File does not exist");
        }
    }
//Search employee

    public static void searchEmployee() throws FileNotFoundException, IOException, ClassNotFoundException {
        boolean found = false;
        try (
                Reader reader = Files.newBufferedReader(Paths.get("Employee.json"))) {
            // convert JSON array to list of users
            ArrayList<Employee> users = new Gson().fromJson(reader, new TypeToken<ArrayList<Employee>>() {
            }.getType());
            reader.close();

            System.out.println("*****************************************************************************");
            System.out.print("Enter Employee email to search: ");
            String email = sString.nextLine();

            li = users.listIterator();
            while (li.hasNext()) {
                Employee e = (Employee) li.next();
                if (e.email.contentEquals(email)) {
                    System.out.println("Employee found");
                    System.out.println(e);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Employee not found.");
            }
        }
    }

// update employee
    public static void updateEmployee() throws IOException, ClassNotFoundException {
        boolean found = false;
        try (
                Reader reader = Files.newBufferedReader(Paths.get("Employee.json"))) {
            // convert JSON array to list of users
            ArrayList<Employee> users = new Gson().fromJson(reader, new TypeToken<ArrayList<Employee>>() {
            }.getType());
            reader.close();

            System.out.println("*****************************************************************************");
            System.out.print("Enter Employee email to update: ");
            String email = sString.nextLine();

            li = users.listIterator();
            while (li.hasNext()) {
                Employee e = (Employee) li.next();
                if (e.email.contentEquals(email)) {
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
                    li.set(new Employee(name, surname, email, date_of_birth));
                    found = true;
                }
            }
            if (found) {
                Gson gson = new Gson();
                String json = gson.toJson(users);
                try (Writer writer = new FileWriter(file)) {
                    writer.write(json);
                    writer.flush();
                    writer.close();
                    System.out.println("Record updated");
                }
            } else {
                System.out.println("Employee not found.");
            }
        }
    }

//Delete employee
    public static void deleteEmployee() throws FileNotFoundException, IOException, ClassNotFoundException {
        boolean found = false;
        try (
                Reader reader = Files.newBufferedReader(Paths.get("Employee.json"))) {

            ArrayList<Employee> users = new Gson().fromJson(reader, new TypeToken<ArrayList<Employee>>() {
            }.getType());
            reader.close();

            System.out.println("*****************************************************************************");
            System.out.print("Enter Employee email to delete: ");
            String email = sString.nextLine();

            li = users.listIterator();
            while (li.hasNext()) {
                Employee e = (Employee) li.next();
                if (e.email.contentEquals(email)) {
                    li.remove();
                    found = true;
                }
            }
            if (found) {
                Gson gson = new Gson();
                String json = gson.toJson(users);
                try (Writer writer = new FileWriter(file)) {
                    writer.write(json);
                    writer.flush();
                    writer.close();
                    System.out.println("Record deleted");
                }
            } else {
                System.out.println("Employee not found.");
            }
        }
    }

//sort employees
    public static void sortEmployee() throws FileNotFoundException, IOException, ClassNotFoundException {
        try (Reader reader = Files.newBufferedReader(Paths.get("Employee.json"))) {

            List<Employee> users = new Gson().fromJson(reader, new TypeToken<List<Employee>>() {
            }.getType());
            reader.close();

            Collections.sort(users, new NameComparator());
            Gson gson = new Gson();
            String json = gson.toJson(users);
            try (Writer writer = new FileWriter(file)) {
                writer.write(json);
                writer.flush();
                writer.close();
            }
            System.out.println("*****************************************************************************");
            System.out.println("Sorted List");
            li = users.listIterator();
            while (li.hasNext()) {
                System.out.println(li.next());
            }
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
