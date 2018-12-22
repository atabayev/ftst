package kz.ftsystem.yel.ftst.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface OrdersDAO {

    @Query("SELECT * FROM orders")
    List<Orders> getAll();

    @Query("DELETE FROM orders")
    void clearTable();

    @Insert
    void insert(Orders orders);

    @Update
    void update(Orders orders);

    @Delete
    void delete(Orders orders);



}
