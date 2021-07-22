package ru.job4j.todo.controller;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String name = req.getParameter("login");
        String password = req.getParameter("password");
        User user = null;
        try {
            user = (User) HbmStore.instOf().findUserByName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((user != null) && (user.getPassword().equals(password))) {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/index.html");
        } else {
            req.setAttribute("error", "Неверное имя или пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

}
