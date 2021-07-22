package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

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
     * Добавление объекта класса User
     * @param user экмзепляр класса User, который требуется сохранить
     */
    @Override
    public void addUser(User user) throws Exception {
        this.wrapperMethod(
                session -> session.save(user));
    }

    /**
     * Поиск объекта класса User по адресу почты пользователя
     * @param email почтовый адрес пользователя для поиска
     * @return возвращается объекта класса User с переданным постовым адресом, если не найден то null
     */
    @Override
    public User findUserByEmail(String email) throws Exception {
        return (User) this.wrapperMethod(
                session -> {
                    Query query = session.createQuery("FROM ru.job4j.todo.model.User WHERE email =: email");
                    query.setParameter("email", email);
                    List rsl = query.getResultList();
                    if (rsl.size() > 0) {
                        return rsl.get(0);
                    } else {
                        return null;
                    }
                }
        );
    }

    /**
     * Поиск объекта класса User по имени пользователя
     * @param name Имя пользователя для поиска
     * @return возвращается объекта класса User с переданным именем, если не найден то null
     */
    @Override
    public User findUserByName(String name) throws Exception {
        return (User) this.wrapperMethod(
                session -> {
                    Query query = session.createQuery("FROM ru.job4j.todo.model.User WHERE name =: name");
                    query.setParameter("name", name);
                    List rsl = query.getResultList();
                    if (rsl.size() > 0) {
                        return rsl.get(0);
                    } else {
                        return null;
                    }
                }
        );
    }

    /**
     * Метод Обертка
     * @param command функция которую необходимо исполнить
     * @param <T> тип обхекта который будет возвращен
     * @return результат выполнения
     */
    private <T> T wrapperMethod(Function<Session, T> command) throws Exception{
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
     */
    @Override
    public void addItem(Item item, String[] categories) throws Exception {
        this.wrapperMethod(
                session -> {
                    for (String s : categories) {
                        Category category = session.find(Category.class, Integer.parseInt(s));
                        item.addCategory(category);
                    }
                    session.persist(item);
                    return item;
                });
    }

    /**
     * Получение списка всех заданий в хранилище
     * @return Список всех заданий в хранилище
     */
    @Override
    public List<Item> getAllItem() throws Exception {
        return this.wrapperMethod(
                session -> session.createQuery(
                        "select distinct i from Item i join fetch i.categories").list());
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        return this.wrapperMethod(
                session -> session.createQuery("FROM ru.job4j.todo.model.Category").list());
    }

    /**
     * Поиск задания в хранилище по идентификатору
     * @param id Идентификатор заданий
     * @return экземпляр Item с переданным в этот метод идентификатором, если найден, иначе null
     */
    @Override
    public Item findById(String id) throws Exception {
          return this.wrapperMethod(
                  session -> session.get(Item.class, Integer.parseInt(id)));
    }

    /**
     * Изменяем статус задания в хранилище
     * @param id идентфиикатор экземпляра Item, который необходимо изменить
     * @return истина, если элемент изменен
     */
    @Override
    public boolean replace(String id) throws Exception {
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
