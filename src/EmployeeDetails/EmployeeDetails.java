/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package EmployeeDetails;

import java.util.*;

public class EmployeeDetails {

    public static void main(String[] args) throws Exception {

        int choice;
        Scanner sInt = new Scanner(System.in);
        
     
        do {
            System.out.println("*****************************************************************************");
            System.out.println("1. Add Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Update Employee");
            System.out.println("6. Sort Employee");
            System.out.println("0. Exit");
            System.out.println("*****************************************************************************");
            System.out.print("Enter your choice: ");
            while (!sInt.hasNextInt()) {
                System.out.print(sInt.next() + " is not a choice number please enter a valid choice number: ");

            }
            choice = sInt.nextInt();

            switch (choice) {
                // get employee details
                case 1:
                    Employee.addEmployee();
                    break;
                //Display empoyees
                case 2:
                    Employee.display();
                    break;
                //search employee
                case 3:
                    Employee.searchEmployee();
                    break;
                //delete employee
                case 4:
                        Employee.deleteEmployee();
                    break;
                //update employee
                case 5:
                    Employee.updateEmployee();
                    break;
                case 6:
                    Employee.sortEmployee();
                    break;
                case 0:
                    break;
                default :
                    System.out.println("Choice must be between -1 and 7");
            }

        } while (choice != 0 );
    }

}

