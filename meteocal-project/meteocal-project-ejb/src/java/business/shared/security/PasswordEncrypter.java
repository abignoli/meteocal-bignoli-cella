/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.shared.security;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Andrea Bignoli
 */
public class PasswordEncrypter {

    public static String encrypt(String password) {
        return DigestUtils.sha256Hex(password);
    }
    
}
