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
        
        System.out.print("Enter your South African phone number (+27...):");
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
         if (loggedIn) {
 
            // POE requirement: exact welcome message
            System.out.println("Welcome to QuickChat.");
 
            boolean running = true; // controls the while loop
 
            while (running) {
                System.out.println("\n──────────────────────────────");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.print("Choose an option: ");
 
                int choice = 0;
                try {
                    choice = Integer.parseInt(input.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number between 1 and 3.");
                    continue;
                }
 
                switch (choice) {
                    case 1:
                        sendMessagesFlow(input);
                        break;
                    case 2:
                        System.out.println("Coming Soon.");
                        break;
                    case 3:
                        running = false; // exits the while loop
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please enter 1, 2, or 3.");
                }
            }
 
        } else {
            System.out.println("Login failed. Exiting QuickChat.");
        }
 
        input.close();
    }
    private static void sendMessagesFlow(Scanner input) {
        
        System.out.print("\nHow many messages would you like to send?");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(input.nextLine().trim());
        }catch (NumberFormatException e) {
            System.out.println("Invalid number. Returing to menu.");
            return;
        }
        if (numMessages <= 0) {
            System.out.println("No messages to send. Returning to menu.");
            return;
        }
        for (int i = 0; i < numMessages; i++) {
            int messageNumber = i + 1;
            System.out.println("\n-Message" + messageNumber + "of" + numMessages + "----");
            
            String recipient = "";
            while (true) {
                System.out.print("Enter recipient cell number (e.g. +27831234567):");
                recipient = input.nextLine().trim();
                
                Message temp = new Message();
                temp.setRecipient(recipient);
                String check = temp.checkRecipientCell();
                System.out.println(check);
                
                if (check.equals("Cell phoe number successfully captured.")) {
                    break;
                }
            }
            String messageText = "";
            while (true) {
                System.out.print("Enter your messahe(max 250 characters):");
                messageText = input.nextLine();
                
                Message temp = new Message();
                String check = temp.checkMessageLength(messageText);
                System.out.println(check);
                
                if (check.equals("Message ready to send.")) {
                    break;
                }
            }
            Message message = new Message(i, recipient, messageText);
            
            System.out.println("\nMessage details:");
            System.out.println("  Message ID   : " + message.getMessageID());
            System.out.println("  Message Hash : " + message.getMessageHash());
            System.out.println("  Recipient    : " + message.getRecipient());
            System.out.println("  Message      : " + message.getMessageText());
            
            String outcome = message.sentMessage();
            System.out.println(outcome);
        }
        
        Message counter = new Message();
        System.out.println("\nTotal messages sent/stored this session:" + counter.returnTotalMessages());
    }
    }

