package com.example.synapseapp.room

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import data.DrawerItemsType


@Dao
interface Dao{
    /**
     * Добавляет новый элемент в таблицу.
     * Если элемент с таким id уже существует - он будет заменён.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrawerItem(item: DrawerItemsEntity)

    /**
     * Получение только title и id
     */
//    @Query("SELECT id, title FROM DrawerItems")
//    suspend fun getShortItems(): List<DrawerItemsType>

    /**
     * Обновляет существующий элемент.
     */
    @Update
    suspend fun updateDrawerItem(item: DrawerItemsEntity)


    /**
     * Удаляет элемент из таблицы по объекту DrawerItemsEntity.
     */
    @Delete
    suspend fun deleteDrawerItem(item: DrawerItemsEntity)

    /**
     * Получает все элементы из таблицы DrawerItems.
     */
    @Query("SELECT * FROM DrawerItems")
    suspend fun getAllDrawerItems(): List<DrawerItemsEntity>

    @Query("SELECT id, title FROM DrawerItems")
    suspend fun getIdTitleDrawerItems(): List<DrawerItemsType>


    /**
     * Получает элемент по его String id.
     * Если элемент не найден - возвращает null.
     */
    @Query("SELECT * FROM DrawerItems WHERE id = :id")
    suspend fun getDrawerItemById(id: String): DrawerItemsEntity?

    /**
     * Удаляет элемент из таблицы по его String id.
     */
    @Query("DELETE FROM DrawerItems WHERE id = :id")
    suspend fun deleteDrawerItemById(id: String)

    /**
     * Получает title элемента по его String id.
     */
    @Query("SELECT title FROM DrawerItems WHERE id = :id")
    suspend fun getTitleById(id: String): String?


    /**
     * Получает весь текст чата wholeChat по его String id.
     */
    @Query("SELECT wholeChat FROM DrawerItems WHERE id = :id")
    suspend fun getWholeChatById(id: String): String?


    /**
     * Изменяет название чата title по его String id.
     */
    @Query("UPDATE DrawerItems SET title = :title WHERE id = :id")
    suspend fun updateTitle(id: String, title: String)


    /**
     * Изменяет весь текст чата wholeChat по его String id.
     */
    @Query("UPDATE DrawerItems SET wholeChat = :wholeChat WHERE id = :id")
    suspend fun updateWholeChat(id: String, wholeChat: String)
}