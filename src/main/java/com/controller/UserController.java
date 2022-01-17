/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

import com.controller.dto.UserDTO;
import com.model.User;
import com.model.dao.UserDAO;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
        }

        return hashedPass;
    }

    public UserDTO login(String userName, String password) {
        User user = userDAO.authenticate(userName, hashPass(password, userName));
        return userModelToDTO(user);
    }

    public List<UserDTO> getAll() {
        return userDAO.getAllUser().stream()
                .map(user -> userModelToDTO(user))
                .collect(Collectors.toList());
    }

    public boolean isDuplicateUser(String username) {
        return userDAO.isDuplicatedUser(username);
    }

    public boolean addUser(String username, String password, String phone, int authority) {
        String hashedPassword = hashPass(password, username);
        User user = new User(username, hashedPassword, phone, authority);
        return userDAO.addNewUser(user);
    }

    public boolean verifiedCurrentPass(UserDTO user, String currentPass) {
        return user.getPassword().equals(hashPass(currentPass, user.getUsername()));
    }

    public boolean updatePassword(UserDTO user, String newPass) {
        return userDAO.changePassword(user.getId(), hashPass(newPass, user.getUsername()));
    }

    public boolean isDuplicatePhone(int id, String phone) {
        return userDAO.isDuplicatePhone(id, phone);
    }

    public UserDTO getUserById(int id) {
        return userModelToDTO(userDAO.getUserById(id));
    }

    public boolean updatePhone(UserDTO user, String newPhone) {
        return userDAO.changePhone(user.getId(), newPhone);
    }

    public boolean updateStatus(int id, int status) {
        return userDAO.updateUserStatus(id, status);
    }

    public static UserDTO userModelToDTO(User user) {
        UserDTO udto = null;
        if (user != null) {
            udto = new UserDTO();
            udto.setId(user.getId());
            udto.setPassword(user.getPassword());
            udto.setPhone(user.getPhone());
            udto.setUsername(user.getUsername());
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
