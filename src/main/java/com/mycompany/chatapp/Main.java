/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.util.Scanner;

/**
 *
 * @author tikiw
 */
public class Main {
    public static void main(String[] args) {
       
        //scanner allows the user to enter information
        Scanner input = new Scanner(System.in);
        
        // Create a object of the Login class so we can call its methods
        Login login = new Login();
      
        // ---REGISTERATION SECTION---
        System.out.println("===USER REGISTRATION===");
        
        System.out.print("Enter a username:");
        String username = input.nextLine();
        
        System.out.print("Enter a password:");
        String password = input.nextLine();
        
        System.out.print("Enter your Sout African phone number (+27...):");
        String phone = input.nextLine();
        
        // Call the registerUser method and store the message it returns
        String response = login.registerUser(username, password, phone);
        
        // Show the registration message
        System.out.println(response);
        
        // ---LOGIN SECTION---
        System.out.println("\n===USER LOGIN===");
        
        System.out.print("Enter your username:");
        String loginUsername = input.nextLine();
        
        System.out.print("Enter your password:");
        String loginPassword = input.nextLine();
        
        // Call loginUser to check if details match the stored ones
        boolean loggedIn = login.loginUser(loginUsername, loginPassword);
        
        // Print out the correct login message
        String loginMessage = login.returnLoginStatus(loggedIn);
        System.out.println(loginMessage);
    }
}

