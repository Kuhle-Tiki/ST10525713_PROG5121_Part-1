/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.util.Scanner;

/**
 * 
 *
 * @author tikiw
 */
public class Main {

    /**
     * Main method. Runs registration, login, and the main menu loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Scanner allows the user to enter information
        Scanner input = new Scanner(System.in);

        // Create an object of the Login class so we can call its methods
        Login login = new Login();

        // ---REGISTRATION SECTION---
        System.out.println("===USER REGISTRATION===");

        System.out.print("Enter a username: ");
        String username = input.nextLine();

        System.out.print("Enter a password: ");
        String password = input.nextLine();

        System.out.print("Enter your South African phone number (+27...): ");
        String phone = input.nextLine();

        // Call the registerUser method and store the message it returns
        String response = login.registerUser(username, password, phone);
        System.out.println(response);

        // ---LOGIN SECTION---
        System.out.println("\n===USER LOGIN===");

        System.out.print("Enter your username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = input.nextLine();

        // Call loginUser to check if details match the stored ones
        boolean loggedIn = login.loginUser(loginUsername, loginPassword);

        // Print the correct login message
        String loginMessage = login.returnLoginStatus(loggedIn);
        System.out.println(loginMessage);

        if (loggedIn) {

            // POE requirement: exact welcome message
            System.out.println("Welcome to QuickChat.");

            // Part 3: Load any previously stored messages from JSON into memory
            Message.loadStoredMessages();

            boolean running = true; // controls the while loop

            while (running) {
                System.out.println("\n──────────────────────────────");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");
                System.out.print("Choose an option: ");

                int choice = 0;
                try {
                    choice = Integer.parseInt(input.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number between 1 and 4.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        sendMessagesFlow(input);
                        break;
                    case 2:
                        System.out.println(Message.printMessages());
                        break;
                    case 3:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    case 4:
                        storedMessagesMenu(input);
                        break;
                    default:
                        System.out.println("Invalid option. Please enter 1, 2, 3, or 4.");
                }
            }

        } else {
            System.out.println("Login failed. Exiting QuickChat.");
        }

        input.close();
    }

    /**
     * Handles the flow for sending one or more messages.
     * Prompts for recipient and message text, validates both,
     * then lets the user choose to send, disregard, or store each message.
     *
     * @param input the shared Scanner instance
     */
    private static void sendMessagesFlow(Scanner input) {

        System.out.print("\nHow many messages would you like to send? ");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
            return;
        }
        if (numMessages <= 0) {
            System.out.println("No messages to send. Returning to menu.");
            return;
        }

        for (int i = 0; i < numMessages; i++) {
            int messageNumber = i + 1;
            System.out.println("\n--- Message " + messageNumber + " of " + numMessages + " ---");

            // Validate recipient
            String recipient = "";
            while (true) {
                System.out.print("Enter recipient cell number (e.g. +27831234567): ");
                recipient = input.nextLine().trim();

                Message temp = new Message();
                temp.setRecipient(recipient);
                String check = temp.checkRecipientCell();
                System.out.println(check);

                if (check.equals("Cell phone successfully captured.")) {
                    break;
                }
            }

            // Validate message text
            String messageText = "";
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
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
        System.out.println("\nTotal messages sent/stored this session: " + counter.returnTotalMessages());
    }

    /**
     * Displays the Stored Messages sub-menu (option 4) and handles
     * the six sub-options: display all stored, longest message,
     * search by ID, search by recipient, delete by hash, and full report.
     *
     * @param input the shared Scanner instance
     */
    private static void storedMessagesMenu(Scanner input) {
        boolean inSubMenu = true;

        while (inSubMenu) {
            System.out.println("\n──────────────────────────────");
            System.out.println("=== Stored Messages Menu ===");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message by ID");
            System.out.println("d) Search all messages for a recipient");
            System.out.println("e) Delete a message by hash");
            System.out.println("f) Display full message report");
            System.out.println("0) Return to main menu");
            System.out.print("Choose an option: ");

            String option = input.nextLine().trim().toLowerCase();

            Message msg = new Message();

            switch (option) {
                case "a":
                    // Display stored messages list
                    if (Message.getStoredMessages().isEmpty()) {
                        System.out.println("No stored messages found.");
                    } else {
                        System.out.println("\n=== Stored Messages ===");
                        for (int i = 0; i < Message.getStoredMessages().size(); i++) {
                            System.out.println((i + 1) + ") " + Message.getStoredMessages().get(i));
                        }
                    }
                    break;

                case "b":
                    System.out.println("\nLongest stored message:");
                    System.out.println(msg.displayLongestMessage());
                    break;

                case "c":
                    System.out.print("Enter message ID to search: ");
                    String searchID = input.nextLine().trim();
                    System.out.println(msg.searchByMessageID(searchID));
                    break;

                case "d":
                    System.out.print("Enter recipient number to search: ");
                    String searchRecipient = input.nextLine().trim();
                    System.out.println(msg.searchByRecipient(searchRecipient));
                    break;

                case "e":
                    System.out.print("Enter message hash to delete: ");
                    String hash = input.nextLine().trim();
                    System.out.println(msg.deleteByHash(hash));
                    break;

                case "f":
                    System.out.println(Message.printMessages());
                    break;

                case "0":
                    inSubMenu = false;
                    break;

                default:
                    System.out.println("Invalid option. Please enter a, b, c, d, e, f, or 0.");
            }
        }
    }
}
