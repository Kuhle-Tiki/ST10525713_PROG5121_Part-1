/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author tikiw
 */
public class MessageTest {
    // ----Test Objects--------------
    /** Message 1 - Test Case 1: Valid Recipient*/
    private Message message1;
    
    /** Message 2 - Test Case 2: Invalid Recipient*/
    private Message message2;
    
    // ----Setup-------------------
    /** Runs before every single test*/
    @BeforeEach
    public void setUp() {
        Message.clearMessages();
        
        // Test Case 1: Valid
        message1 = new Message();
        message1.setMessageNumber(0);
        message1.setRecipient("+27718693002");
        message1.setMessageText("Hi Mike, can you join us for dinner tonight?");
        
        // Test Case 2: Invalid 
        message2 = new Message();
        message2.setMessageNumber(1);
        message2.setRecipient("08575975889");
        message2.setMessageText("Hi keagan, did you recieve the payment?");
    }
    // ---Length Test----------
    /** Message under 250 characters, must return to "Message ready to send, therefore success."*/
    @Test
    public void testCheckMessageLength_validMessage_returnsSuccess() {
        String result = message1.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", result);
    }
    /** if above 250 characters, it must return as a failure*/
    @Test
    public void testCheckMessageLength_over250chars_returnsFailureWithCount(){
        String longMessage = "A".repeat(260);
        String result = message1.checkMessageLength(longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }
    /**Message == 250 characters should return as "Message ready to send."*/
    @Test
    public void testcheckMessageLength_exactlyAtLimit_returnSuccess() {
        String result = message1.checkMessageLength("B".repeat(250));
        assertEquals("Message ready to send.", result);
    }
    /** characters == 251 must return as a failure with a 1 count.*/
    @Test
    public void testCheckMessageLength_oneOver_returnsFailureCountof1() {
        String result = message1.checkMessageLength("C".repeat(251));
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.", result);
        
    }
    // ---Recipient Validation Test-------
    /** Case Test 1: +277718693002 is valid therefore must return success message*/
    @Test
    public void testCheckRecipientCell_validNumber_returnsSuccess() {
        assertEquals("Cell phone successfully captured.", message1.checkRecipientCell());
    }
    /** Test Case 2: 08575975889 has no international code therefore it must fail*/
    @Test
    public void testCheckRecipientCell_invalidNumber_returnsFailure() {
        assertEquals("Cell phone is incorrectly formatted or does not contain an"+ " international code. Please correct number and try again.", message2.checkRecipientCell());
    }
    // ---Message Hash Test------------
    /** Case Test 1: hash must end with :0
     * first 2 chars of the ID are random 
     */
    @Test
    public void testCreateMessageHash_endWithExpectedWords() {
        String hash = message1.createMessageHash();
        assertTrue(hash.endsWith(":0:HITONIGHT"), "Hash should end with :0:HITONIGHT but was:" + hash);
    }
    /**
     * hash mus be entirely uppercase
     */
    @Test
    public void testCreatMessageHash_isUppercase() {
        String hash = message1.createMessageHash();
        assertEquals(hash.toUpperCase(), hash, "Hash must be fully uppercase");
    }
    /**
     * loop tests
     * check whether the hash ends with the correct suffix
     */
    @Test
    public void testCreateMessageHash_multipleMessages_loopTest() {
        String[][] testData = {
            {"0", "Hi Mike, can you join us for dinner tonight?", "HITONIGHT"},
            {"1", "Hi Keegan, did you receive the payment?", "HIPAYMENT"},
            {"2", "Hello world", "HELLOWORLD"}
        };
        for (String[] data : testData) {
            Message msg = new Message();
            msg.setMessageNumber(Integer.parseInt(data[0]));
            msg.setMessageText(data[1]);
            
            String hash = msg.createMessageHash();
            String expectedSuffix = ":" + data[0] + ":" + data[2];
            
            assertTrue(hash.endsWith(expectedSuffix), "Message" + data[0] + "hash should end with" + expectedSuffix + "but was:" + hash);
        }
    }
    // ---Message ID tests-------
    /**
     * new message must have a non null ID
     */
    @Test
    public void testCheckMessageID_generatedID_isNotNull() {
        assertNotNull("Message ID should not be null", message1.getMessageID());
    }
    /**
     * checkMessageID() it must return true: 10 chars
     */
    @Test
    public void testCheckMessageID_generatedID_isExactly10Chars() {
        assertTrue(message1.checkMessageID(), "checkMessageID() should return true for a 10-char ID");
        
        // Print the confirmation string
        System.out.println("Message ID generated:" + message1.getMessageID());
    }
    // ----sentMessage Test-----------
    /**
     * inner help that stimulates user's menu
     */
    static class TestableMessage extends Message {
        private final int simulatedChoice;
        
        TestableMessage(int choice) {
            super();
            this.simulatedChoice = choice;
        }
        
        @Override
        public String sentMessage() {
            switch (simulatedChoice) {
                case 1: return "Message successfully sent.";
                case 2: return "Press 0 to delete the message.";
                case 3: return "Message successfuly stored ";
                default: return "Invalid option selected.";
            }
        }
        
    
    }
    /**
     * User selects: option 1
     */
    @Test 
    public void testSentMessage_userSelected_returnsCorrectString() {
        assertEquals("Message successfully sent.", new TestableMessage(1).sentMessage());
    }
    /**
     * User selects: option 2
     */
    @Test
    public void testSentMessage_userSelectsDisregard_returnsCorrectString() {
        assertEquals("Press 0 to delete message.", new TestableMessage(2).sentMessage());
    }
    /**
     * User selects: option 3
     */
    @Test
    public void testSentMessage_userSelectsStire_returnCorrectString() {
        assertEquals("Message successfully stored.", new TestableMessage(3).sentMessage());
                
    }
}
