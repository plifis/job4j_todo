package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.many.CarMark;
import ru.job4j.todo.many.CarModel;
import ru.job4j.todo.model.User;

public class HbmRunner {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            CarModel corolla = new CarModel("Corolla");
            session.save(corolla);
            CarModel camry = new CarModel("Camry");
            session.save(camry);
            CarModel rav = new CarModel("Rav4");
            session.save(rav);
            CarModel prado = new CarModel("Prado");
            session.save(prado);
            CarModel supra = new CarModel("Supra");
            session.save(supra);

            CarMark toyota = new CarMark("Toyota");
            toyota.addCarModel(session.load(CarModel.class, 1));
            toyota.addCarModel(session.load(CarModel.class, 2));
            toyota.addCarModel(session.load(CarModel.class, 3));
            toyota.addCarModel(session.load(CarModel.class, 4));
            toyota.addCarModel(session.load(CarModel.class, 5));
            session.save(toyota);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
