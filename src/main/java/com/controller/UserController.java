/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.model.User;
import com.model.dao.UserDAO;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hadt2
 */
public class UserController {

    UserDAO userDAO = new UserDAO();

    public static String hashPass(String password, String name) {
        String hashedPass = null;
        String hashing = password + name;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] mess = md5.digest(hashing.getBytes());
            BigInteger no = new BigInteger(1, mess);

            hashedPass = no.toString(16);
            while (hashedPass.length() < 32) {
                hashedPass = "0" + hashedPass;
            }

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return hashedPass;
    }

    public UserDTO login(String userName, String password) {
        User user = userDAO.authenticate(userName, hashPass(password, userName));
        return UserModelToDTO(user);
    }

    public static UserDTO UserModelToDTO(User user) {
        UserDTO udto = null;
        if (user != null) {
            udto = new UserDTO();
            udto.setId(user.getId());
            udto.setPassword(user.getPassword());
            udto.setUserName(user.getUsername());
            udto.setAuthority(user.getAuthority());
            udto.setStatus(user.getStatus());
        }
        return udto;
    }

    public static void main(String[] args) {
        UserController uController = new UserController();
        System.out.println(uController.login("admin", "12345"));
    }
}
