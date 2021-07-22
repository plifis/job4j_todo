package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {
    public void addUser(User user) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public Object findUserByName(String name) throws Exception;
    public void addItem(Item item, String[] categories) throws Exception;
    public List<Item> getAllItem() throws Exception;
    public Item findById(String id) throws Exception;
    public boolean replace(String id) throws Exception;
    public List<Category> getAllCategories() throws Exception;
}
