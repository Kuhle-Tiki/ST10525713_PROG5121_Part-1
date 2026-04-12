/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author tikiw
 */
public class Login {
  // Step 4: Store user details
    String username;
    String password;
    String phoneNumber;
    
   // Step 5: Userename Validation
    public boolean checkUsername(String username) {
        // boolean result is retuned directly 
        // It will be 'true' only if both conditions are met
        return username.contains("-")&& username.length() <= 5;
    }
    public boolean checkPasswordComplexity(String password){
        // Step 6: Password Validation
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }else if (Character.isDigit(c)) {
                hasNumber = true;
            }else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;        
    }
   public boolean checkCellPhoneNumber(String phone) {
       // Step 7: Cell phone number validation
       return phone.startsWith("+27") && phone.length() <= 12;
   }
  public String registerUser(String username, String password, String phoneNumber){
      // Step 8: Register User Method
      if (!checkUsername(username)) {
          return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
      }
      // Check if the password meets complexity requirements
      if (!checkPasswordComplexity(password)) {
          return "Password is not correctly formatted; please ensure that the password containe at least eight character, a capital letter, a numebr, and a special character";
      }
      // Check if phone number is valid 
      if (!checkCellPhoneNumber(phoneNumber)) {
          return "Cell phone number incorrectly formatted or does not contain international code";
      }
      // if everything passes, store the data in the class variables
      this.username = username;
      this.password = password;
      this.phoneNumber = phoneNumber;
      
      // Return success message
      return "User registered successfully";
  } 
public boolean loginUser(String username, String password) {
   // Step 9: Login Feature
    return this.username.equals(username) && this.password.equals(password);
} 
public String returnLoginStatus(boolean success) {
    if (success) {
        return "Welcome" + username + "It is great to see you again.";
    }else {
        return "Username or password incorrect, please try again.";
    }
}
}
