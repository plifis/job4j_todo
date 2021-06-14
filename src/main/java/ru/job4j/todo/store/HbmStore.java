package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import javax.persistence.Query;
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


    /**
     * Метод Обертка
     * @param command функция которую необходимо исполнить
     * @param <T> тип обхекта который будет возвращен
     * @return результат выполнения
     */
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

    /**
     * Добавление нового задания в хранилище
     * @param item экземпляр задания, который необходимо добавить
     * @return экземпляр, который был добавлен в хранилище
     */
    @Override
    public Item addItem(Item item) {
        return (Item) this.wrapperMethod(
                session -> session.save(item));
    }

    /**
     * Получение списка всех заданий в хранилище
     * @return Список всех заданий в хранилище
     */
    @Override
    public List<Item> getAllItem() {
        return this.wrapperMethod(
                session -> session.createQuery("FROM ru.job4j.todo.model.Item").list());
    }

    /**
     * Поиск задания в хранилище по идентификатору
     * @param id Идентификатор заданий
     * @return экземпляр Item с переданным в этот метод идентификатором, если найден, иначе null
     */
    @Override
    public Item findById(String id) {
          return this.wrapperMethod(
                  session -> session.get(Item.class, Integer.parseInt(id)));
    }

    /**
     * Изменяем статус задания в хранилище
     * @param id идентфиикатор экземпляра Item, который необходимо изменить
     * @return истина, если элемент изменен
     */
    @Override
    public boolean replace(String id) {
        return this.wrapperMethod(
                session -> {
                    Query query = session.createQuery("UPDATE ru.job4j.todo.model.Item SET done =: done WHERE id =: id");
                    query.setParameter("done", true);
                    query.setParameter("id", Integer.parseInt(id));
                    query.executeUpdate();
                    return true;
                    });
    }

    @Override
    public void close() throws Exception {
        this.sf.close();
    }
}
