package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {
    public void addUser(User user) throws Exception;
    public User findUserByEmail(String email);
    public Object findUserByName(String name);
    public void addItem(Item item, String[] categories);
    public List<Item> getAllItem();
    public Item findById(String id);
    public boolean replace(String id);
    public List<Category> getAllCategories();
}
