package ca.csfoy.tp2_ac_ltn.repository;

import ca.csfoy.tp2_ac_ltn.exceptions.DatabaseExceptions;

interface Repository<T> {
    void insert(T myObject) throws DatabaseExceptions;
    T find(long id) throws DatabaseExceptions;
    void save(T myObject) throws DatabaseExceptions;
    void delete(long id) throws DatabaseExceptions;
}
