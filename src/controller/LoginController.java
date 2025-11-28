package controller;

import dao.UserDAO;
import model.User;

public class LoginController {

    private static UserDAO userDAO = new UserDAO();

    public static User loggedUser = null;

    public static boolean login(String username, String password) {
        User user = userDAO.login(username, password);

        if (user != null) {
            loggedUser = user;
            System.out.println("Login berhasil! Selamat datang, " + user.getNama_lengkap());
            return true;
        } else {
            System.out.println("Login gagal! Username atau password salah.");
            return false;
        }
    }

    public void logout() {
        loggedUser = null;
        System.out.println("Anda telah logout.");
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}
