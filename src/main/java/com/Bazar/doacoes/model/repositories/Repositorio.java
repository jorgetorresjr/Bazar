package com.Bazar.doacoes.model.repositories;

import java.sql.SQLException;
import java.util.List;

public interface Repositorio<KEY,CLAZZ>{
    public void create(CLAZZ c)throws ClassNotFoundException, SQLException;
    public void update(CLAZZ c)throws ClassNotFoundException, SQLException;
    public CLAZZ read(KEY k)throws ClassNotFoundException, SQLException;
    public void delete(CLAZZ c)throws ClassNotFoundException, SQLException;
    public List<CLAZZ> readAll()throws ClassNotFoundException, SQLException;
}
