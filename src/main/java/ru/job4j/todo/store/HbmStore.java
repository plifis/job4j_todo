package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.List;

public class HbmStore implements Store, AutoCloseable{
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata()
            .buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }


    @Override
    public Item addItem(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.beginTransaction();
        Item result = session.get(Item.class, item.getId());
        session.close();
        return result;
    }

    @Override
    public List<Item> getAllItem() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("FROM ru.job4j.todo.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Item findById(String id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = session.get(Item.class, Integer.parseInt(id));
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
//        Item temp = session.get(Item.class, Integer.parseInt(id));
//        if (temp == null) {
//            session.close();
//            return false;
//        }
//        session.getTransaction().commit();
//        session.beginTransaction();
        item.setId(Integer.parseInt(id));
        session.update(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public void close() throws Exception {

    }
}
