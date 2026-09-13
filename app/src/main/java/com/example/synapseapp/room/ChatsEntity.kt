package com.example.synapseapp.room

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "DrawerItems")
data class DrawerItemsEntity(
    @PrimaryKey
    val id: String = "",
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "wholeChat")
    val wholeChat: String
)