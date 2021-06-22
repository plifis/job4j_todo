package ru.job4j.todo.many;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_marks")
public class CarMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarModel> list = new ArrayList<>();


    public CarMark() {

    }

    public CarMark(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<CarModel> list) {
        this.list = list;
    }

    public void addCarModel(CarModel model) {
        this.list.add(model);
    }
}
