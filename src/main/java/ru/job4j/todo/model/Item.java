package ru.job4j.todo.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date created;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Collection<Category> categories = new ArrayList<>();

    public Item() {
    }

    public Item(String description, User user) {
        this.description = description;
        this.done = false;
        this.created = new Timestamp(System.currentTimeMillis());
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
            this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && done == item.done &&
                Objects.equals(description, item.description) &&
                Objects.equals(created, item.created) &&
                Objects.equals(user, item.user) &&
                Objects.equals(categories, item.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, created, done, user, categories);
    }
}
