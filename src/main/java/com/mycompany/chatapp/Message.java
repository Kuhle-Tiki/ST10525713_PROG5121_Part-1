/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author tikiw
 */
public class Message {
    //---Fields--------------------------------------------
    
   private String messageID;
   private int messageNumber;
   private String recipient;
   private String messageText;
   private String messageHash;
   private String messageStatus;
   private static ArrayList<Message> sentMessages = new ArrayList<>();
   
   // ---Constructors---------------------------------------
   public Message() {
       this.messageID = generateMessageID();
   }
   public Message(int messageNumber, String recipient, String messageText) {
       this.messageID = generateMessageID();
       this.messageNumber = messageNumber;
       this.recipient = recipient;
       this.messageHash = createMessageHash();
   }
   //---ID Generation---------------------------------------
   private String generateMessageID() {
       Random random = new Random();
       long id = 1_000_000_000L + (long)(random.nextDouble() * 9_000_000_000L);
       return String.valueOf(id);
   }
   //---Validation Methods----------------------------------
   public boolean checkMessageID() {
       return messageID != null && messageID.length() <=10;
   }
   public String checkRecipientCell() {
       if (recipient != null && recipient.startsWith("+27")) {
           String numberPart = recipient.substring(1);
           if (recipient.length() <= 12) {
               return "Cell phone successfully captured.";
           }
           
       }
       return "Cell phone number is incorrectly formatted or does not contain an" + " international code. Please correct number and try again.";
   }
   public String checkMessageLength(String message) {
       if (message == null || message.length() <= 250) {
           return "Message ready to send.";
       }
       int over = message.length() - 250;
       return "Message exceeds 250 characters by " + over + "; please reduce the size.";
   }
   public String createMessageHash() {
       if (messageID == null || messageText == null || messageText.isBlank()) {
           return "";
       }
       String idPart = messageID.substring(0,2);
       String[] words = messageText.split(" ");
       String firstWord = words[0].replaceAll("[^a-zA-Z]", "");
       String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "");
       this.messageHash = (idPart + ":" + messageNumber + ":" firstWord + lastWord).toUpperCase();
       return messageHash;
   }
   public String sentMessage() {
       Scanner scanner = new Scanner(System.in);
       
       System.out.println("\nWhat would you like to do with this message?");
       System.out.println("1) Send Message");
       System.out.println("2) Disregard Message");
       System.out.println("3) Store Message to send late");
       
       int option = 0;
       try {
           option = Integer.parseInt(scanner.nextLine().trim());
       } catch (NumberFormatException e) {
           return "Invalid option selected.";
       }
       switch (option) {
           case 1:
               messageStatus = "Sent";
               sentMessages.add(this);
               return "Message successfully sent";
           case 2:
               messageStatus = "Disregarded";
               return "Press 0 to delete message.";
           case 3:
               messageStatus = "Stored";
               sentMessages.add(this);
           {
               try {
                   storeMessage();
               } catch (IOException ex) {
                   System.getLogger(Message.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
               }
           }
               return "Message successfully stored.";

           default:
               return "Invalid option selected.";
       }
   }
   public String printMessages() {
       if (sentMessages.isEmpty()) {
           return "No messages sent yet.";
       }
       StringBuilder sb = new StringBuilder();
       for (Message msg : sentMessages) {
           sb.append("-------------------\n");
           sb.append("Message ID : ").append(msg.messageID).append("\n");
           sb.append("Message Hash : ").append(msg.messageHash).append("\n");
           sb.append("Recipient    : ").append(msg.recipient).append("\n");
           sb.append("Message      : ").append(msg.messageText).append("\n");
           sb.append("Status       : ").append(msg.messageStatus).append("\n");
       }
       return sb.toString();
   }
   public int returnTotalMessages() {
       return sentMessages.size();
   }
   public void storeMessage() throws IOException {
       JSONObject obj = new JSONObject();
        obj.put("messageID",     this.messageID);
        obj.put("messageNumber", this.messageNumber);
        obj.put("recipient",     this.recipient);
        obj.put("message",       this.messageText);
        obj.put("messageHash",   this.messageHash);
        obj.put("status",        "Stored");
        
        try (FileWriter fw = new FileWriter("messages.json", true)) {
            fw.write(obj.toString(2) + "\n");
            System.out.println("Message saved to messages.json.");  
        }catch (IOException e) {
            System.out.println("Error saving the message:" + e.getMessage());
        }
   }
   public String getMessageID() {return messageID;}
   public int getMessageNumber() {return messageNumber;}
   public String getRecipient() {return recipient;}
   public String getMessageText() {return messageText;}
   public String getMessageHash() {return messageHash;}
   public String getMessageStatus() {return messageStatus;}
    
   public void setMessageID(String messageID) {this.messageID = messageID;}
   public void setMessageNumber(int messageNumber) {this.messageNumber = messageNumber;}
   public void setRecipient(String recipient) {this.recipient = recipient;}
   public void setMessageText(String messageText) {this.messageText = messageText;}
   public void setMessageHash(String messageHash) {this.messageHash = messageHash;}
   
   
   public static void clearMessages() {sentMessages.clear();}

    private void toUpperCase() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
   
           
   
   
    

