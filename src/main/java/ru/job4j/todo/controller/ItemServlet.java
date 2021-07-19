package ru.job4j.todo.controller;


import org.json.JSONArray;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ItemServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        String[] categories = req.getParameterValues("categories[]");
        Item item = new Item(req.getParameter("description"), user);
        HbmStore.instOf().addItem(item, categories);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        JSONArray array = new JSONArray(HbmStore.instOf().getAllItem());
        writer.println(array);
        writer.flush();
    }
}

