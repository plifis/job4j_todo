package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;
import java.util.List;
import java.util.function.Function;

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

    private <T> T wrapperMethod(Function<Session, T> command) {
            Session session = sf.openSession();
            session.beginTransaction();
            try {
                T rsl = command.apply(session);
                session.getTransaction().commit();
                return rsl;
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            } finally {
                session.close();
            }


    }

    @Override
    public Item addItem(Item item) {
        return (Item) this.wrapperMethod(
                session -> session.save(item));

//        Session session = sf.openSession();
//        session.beginTransaction();
//        session.save(item);
//        session.getTransaction().commit();
//        session.beginTransaction();
//        Item result = session.get(Item.class, item.getId());
//        session.close();
//        return result;
    }

    @Override
    public List<Item> getAllItem() {
        return this.wrapperMethod(
                session -> session.createQuery("FROM ru.job4j.todo.model.Item").list());
//        Session session = sf.openSession();
//        session.beginTransaction();
//        List result = session.createQuery("FROM ru.job4j.todo.model.Item").list();
//        session.getTransaction().commit();
//        session.close();
//        return result;
    }

    @Override
    public Item findById(String id) {
          return this.wrapperMethod(
                  session -> session.get(Item.class, Integer.parseInt(id)));
//        Session session = sf.openSession();
//        session.beginTransaction();
//        Item item = session.get(Item.class, Integer.parseInt(id));
//        session.getTransaction().commit();
//        session.close();
//        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        item.setId(Integer.parseInt(id));
        return this.wrapperMethod(
                session -> {
                    session.update(item);
                    return true;
                    });
//        Session session = sf.openSession();
//        session.beginTransaction();
//        item.setId(Integer.parseInt(id));
//        session.update(item);
//        session.getTransaction().commit();
//        session.close();
//        return true;
    }

    @Override
    public void close() throws Exception {

    }
}
