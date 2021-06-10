package ru.job4j.todo.controller;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReplaceServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        Item item = HbmStore.instOf().findById(id);
        boolean done = item.isDone();
        item.setDone(!done);
        HbmStore.instOf().replace(id, item);
        if (item.isDone() != done) {
            resp.getWriter().write("Статус задачи изменен " + item.getId());
            resp.getWriter().flush();
        } else {
            resp.getWriter().write("Не удалось изменить статус задачи");
            resp.getWriter().flush();
        }
    }
}
