package tn.esprit.services;

import java.util.List;

public interface IGenericService<T> {
    void add(T entity);
    void update(T entity);
    void delete(int id);
    T getById(int id);
    List<T> getAll();
}
