/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 * Represents a single chat message and manages all message-related operations
 * including sending, storing, searching, deleting, and reporting.
 *
 * @author tikiw
 */
public class Message {

    // ---Fields--------------------------------------------
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String messageStatus;

    // ---Part 3: Parallel arrays to track all messages during the session---
    private static ArrayList<String> sentMessages      = new ArrayList<>();
    private static ArrayList<String> disregardedMessages = new ArrayList<>();
    private static ArrayList<String> storedMessages    = new ArrayList<>();
    private static ArrayList<String> messageHashes     = new ArrayList<>();
    private static ArrayList<String> messageIDs        = new ArrayList<>();
    private static ArrayList<String> recipientList     = new ArrayList<>();

    // ---Constructors---------------------------------------

    /**
     * Default constructor. Generates a unique message ID.
     */
    public Message() {
        this.messageID = generateMessageID();
    }

    /**
     * Full constructor. Generates an ID and builds the message hash.
     *
     * @param messageNumber the sequence number of this message
     * @param recipient     the recipient cell number
     * @param messageText   the body of the message
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // ---ID Generation---------------------------------------

    /**
     * Generates a random 10-digit numeric message ID.
     *
     * @return a 10-digit ID string
     */
    private String generateMessageID() {
        Random random = new Random();
        long id = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(id);
    }

    // ---Validation Methods----------------------------------

    /**
     * Checks that the message ID is exactly 10 characters.
     *
     * @return true if the ID is valid, false otherwise
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    /**
     * Validates the recipient cell number format.
     * Must start with +27 and be no longer than 12 characters.
     *
     * @return success or failure message
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.startsWith("+27")) {
            if (recipient.length() <= 12) {
                return "Cell phone successfully captured.";
            }
        }
        return "Cell phone number is incorrectly formatted or does not contain an"
                + " international code. Please correct number and try again.";
    }

    /**
     * Checks that the message does not exceed 250 characters.
     *
     * @param message the message text to validate
     * @return "Message ready to send." or an error with the overflow count
     */
    public String checkMessageLength(String message) {
        if (message == null || message.length() <= 250) {
            return "Message ready to send.";
        }
        int over = message.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    /**
     * Creates a hash from the first two digits of the message ID,
     * the message number, and the first and last words of the message text.
     *
     * @return the hash string in uppercase
     */
    public String createMessageHash() {
        if (messageID == null || messageText == null || messageText.isBlank()) {
            return "";
        }
        String idPart = messageID.substring(0, 2);
        String[] words = messageText.split(" ");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
        this.messageHash = (idPart + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    /**
     * Prompts the user to choose what to do with this message:
     * Send, Disregard, or Store. Populates the relevant arrays accordingly.
     *
     * @return a status string describing the outcome
     */
    public String sentMessage() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");

        int option = 0;
        try {
            option = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return "Invalid option selected.";
        }

        switch (option) {
            case 1:
                messageStatus = "Sent";
                // Populate sent, hash, ID, and recipient arrays
                sentMessages.add(this.messageText);
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                return "Message successfully sent.";

            case 2:
                messageStatus = "Disregarded";
                disregardedMessages.add(this.messageText);
                return "Press 0 to delete message.";

            case 3:
                messageStatus = "Stored";
                messageHashes.add(this.messageHash);
                messageIDs.add(this.messageID);
                recipientList.add(this.recipient);
                try {
                    storeMessage();
                } catch (IOException ex) {
                    System.getLogger(Message.class.getName())
                            .log(System.Logger.Level.ERROR, (String) null, ex);
                }
                return "Message successfully stored.";

            default:
                return "Invalid option selected.";
        }
    }

    // ---Part 3: Array feature methods----------------------

    /**
     * Reads the messages.json file and loads all stored messages into the
     * storedMessages array. Called once at application startup after login.
     * 
     */
    public static void loadStoredMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader("messages.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("{")) {
                    JSONObject obj = new JSONObject(line);
                    if (obj.has("message")) {
                        storedMessages.add(obj.getString("message"));
                    }
                }
            }
        } catch (IOException e) {
            // File does not exist yet — no stored messages to load
            System.out.println("No stored messages file found. Starting fresh.");
        }
    }

    /**
     * Searches the storedMessages array and returns the longest message.
     *
     * @return the longest stored message, or a message if none exist
     */
    public String displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        String longest = "";
        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return longest;
    }

    /**
     * Searches the messageIDs array for a matching ID and returns
     * the corresponding message text using parallel array indexing.
     *
     * @param id the message ID to search for
     * @return the matching message text, or "Message not found."
     */
    public String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                // Check sentMessages first, then storedMessages
                if (i < sentMessages.size()) {
                    return sentMessages.get(i);
                } else {
                    return storedMessages.get(i - sentMessages.size());
                }
            }
        }
        return "Message not found.";
    }

    /**
     * Searches all messages for those sent to a specific recipient.
     * Returns all matching messages concatenated together.
     *
     * @param recipient the recipient cell number to search for
     * @return all messages for the recipient, or "No messages found."
     */
    public String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < recipientList.size(); i++) {
            if (recipientList.get(i).equals(recipient)) {
                if (i < sentMessages.size()) {
                    results.append(sentMessages.get(i)).append("\n");
                } else if (!storedMessages.isEmpty()) {
                    int storedIndex = i - sentMessages.size();
                    if (storedIndex < storedMessages.size()) {
                        results.append(storedMessages.get(storedIndex)).append("\n");
                    }
                }
            }
        }
        if (results.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return results.toString().trim();
    }

    /**
     * Deletes a message from the arrays using its hash value.
     * Removes the matching entry and the corresponding entries in all
     * parallel arrays at the same index.
     *
     * @param hash the message hash to search for and delete
     * @return a success message with the deleted message text, or "Hash not found."
     */
    public String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String deletedMessage = "";
                if (i < sentMessages.size()) {
                    deletedMessage = sentMessages.get(i);
                    sentMessages.remove(i);
                } else {
                    int storedIndex = i - sentMessages.size();
                    if (storedIndex < storedMessages.size()) {
                        deletedMessage = storedMessages.get(storedIndex);
                        storedMessages.remove(storedIndex);
                    }
                }
                messageHashes.remove(i);
                if (i < messageIDs.size()) messageIDs.remove(i);
                if (i < recipientList.size()) recipientList.remove(i);
                return "Message: " + deletedMessage + " successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    /**
     * Generates a formatted report of all sent messages showing
     * the message hash, recipient, and message text for each entry.
     *
     * @return a formatted report string, or a message if no sent messages exist
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder report = new StringBuilder();
        report.append("=== Message Report ===\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("-------------------\n");
            report.append("Message Hash : ").append(i < messageHashes.size() ? messageHashes.get(i) : "N/A").append("\n");
            report.append("Recipient    : ").append(i < recipientList.size() ? recipientList.get(i) : "N/A").append("\n");
            report.append("Message      : ").append(sentMessages.get(i)).append("\n");
        }
        return report.toString();
    }

    /**
     * Returns the total number of messages sent or stored this session.
     *
     * @return the combined count of sent and stored messages
     */
    public int returnTotalMessages() {
        return sentMessages.size() + storedMessages.size();
    }

    /**
     * Writes this message to the messages.json file for persistent storage.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     *
     * @throws IOException if the file cannot be written
     */
    public void storeMessage() throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("messageID", this.messageID);
        obj.put("messageNumber", this.messageNumber);
        obj.put("recipient", this.recipient);
        obj.put("message", this.messageText);
        obj.put("messageHash", this.messageHash);
        obj.put("status", "Stored");

        try (FileWriter fw = new FileWriter("messages.json", true)) {
            fw.write(obj.toString() + "\n");
            System.out.println("Message saved to messages.json.");
        } catch (IOException e) {
            System.out.println("Error saving the message: " + e.getMessage());
        }
    }

    // ---Getters and Setters--------------------------------

    /** @return the message ID */
    public String getMessageID() { return messageID; }

    /** @return the message number */
    public int getMessageNumber() { return messageNumber; }

    /** @return the recipient cell number */
    public String getRecipient() { return recipient; }

    /** @return the message text */
    public String getMessageText() { return messageText; }

    /** @return the message hash */
    public String getMessageHash() { return messageHash; }

    /** @return the message status */
    public String getMessageStatus() { return messageStatus; }

    /** @return the sent messages array */
    public static ArrayList<String> getSentMessages() { return sentMessages; }

    /** @return the stored messages array */
    public static ArrayList<String> getStoredMessages() { return storedMessages; }

    /** @return the message hashes array */
    public static ArrayList<String> getMessageHashes() { return messageHashes; }

    /** @return the recipient list array */
    public static ArrayList<String> getRecipientList() { return recipientList; }

    /** @param messageID the ID to set */
    public void setMessageID(String messageID) { this.messageID = messageID; }

    /** @param messageNumber the message number to set */
    public void setMessageNumber(int messageNumber) { this.messageNumber = messageNumber; }

    /** @param recipient the recipient to set */
    public void setRecipient(String recipient) { this.recipient = recipient; }

    /** @param messageText the message text to set */
    public void setMessageText(String messageText) { this.messageText = messageText; }

    /** @param messageHash the hash to set */
    public void setMessageHash(String messageHash) { this.messageHash = messageHash; }

    /**
     * Clears all static arrays. Used between unit tests to ensure a clean state.
     */
    public static void clearMessages() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipientList.clear();
    }
}


   
           
   
   
    

