package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.List;

public interface Store {
    public Item addItem(Item item);
    public List<Item> getAllItem();
}
