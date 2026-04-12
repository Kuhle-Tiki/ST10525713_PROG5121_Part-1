/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author tikiw
 */
public class LoginTest {
    Login instance = new Login();
    
    // Test Username Validation
    @Test
    public boolean testUsername()  {      
       Assertions.assertTrue(instance.checkUsername("kyl_1"));
    // Test data from mark sheet: "K-Tiki" (Failure)
       Assertions.assertFalse(instance.checkUsername("kyle!!!!!"));
        return false;
    }
    // Test Password Complexity
    @Test
    public void testPasswordComplexity() {
        // Meets all complexity rules
        Assertions.assertTrue(instance.checkPasswordComplexity("Ch&&sec@ke99!"));
    
        // Does not meet complexity rules
        Assertions.assertFalse(instance.checkPasswordComplexity("password"));
    }
    @Test
    public void testCellPhoneNumber(){
        // Cell number starts with +27 and is within 12 characters
        Assertions.assertTrue(instance.checkCellPhoneNumber("+27838968976"));
        // Cell phone number has +27 missing and contaims missing digits
        Assertions.assertFalse(instance.checkCellPhoneNumber("08966553"));
    }
    // Test Registation & Login 
    @Test
    public void testRegistrationAndLogin() {
        // User is registered successfully
        String regStatus = instance.registerUser("kyl_1", "Ch&&sec@ke99!","+27838968976");
        Assertions.assertEquals("User registered successfully", regStatus);
    
        // Final test
        Assertions.assertTrue(instance.loginUser("kyl_1", "Ch&&sec@ke99!"));
        boolean loginResult = false;
        Assertions.assertTrue(loginResult);
        String statusMsg = instance.returnLoginStatus(loginResult);
        Assertions.assertEquals("Welcomen kyl_1, it is great to see you again", statusMsg);
        
        
}  
      
}
