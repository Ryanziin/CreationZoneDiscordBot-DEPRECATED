/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ryanlopes.creationzone.discordbot.registers;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RegistersSecurity {
    
    private byte[] token;
    
    public RegistersSecurity(String token){
        this.token = token.getBytes();
    }
    
    public String encrypt(String value) throws IllegalArgumentException {
        Key salt = this.getSalt();
        
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, salt);
            byte[] encrypted_value = cipher.doFinal(value.getBytes());
            return Base64.encode(encrypted_value);
        } catch(Exception e){
            e.printStackTrace();
        }
        
        throw new IllegalArgumentException("Ocorreu um erro ao tentar encriptar.");
    }
    
    public String decrypt(String value){
        Key salt = this.getSalt();
        
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, salt);
            byte[] decrypted_value = Base64.decode(value);
            return new String(cipher.doFinal(decrypted_value));
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Key getSalt(){
        return new SecretKeySpec(this.token, "AES");
    }
    
}