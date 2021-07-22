package ru.job4j.todo.controller;

import org.hibernate.exception.ConstraintViolationException;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ConstraintViolationException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
                User user = new User(name, email,
                        req.getParameter("password"));
                try {
                    HbmStore.instOf().addUser(user);
                    HttpSession session =req.getSession();
                    session.setAttribute("user", user);
                    resp.sendRedirect(req.getContextPath() + "/index.html");
                } catch (ConstraintViolationException e) {
                    req.setAttribute("error", "Пользователь с таким email уже существует");
                    req.getRequestDispatcher("login.jsp").forward(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }
}
