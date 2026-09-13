package com.example.synapseapp.room

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.synapseapp.MyApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.ChatMessageBox
import data.DrawerItemsType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class RoomViewModel(database: MainDb) : ViewModel() {
    private val db = database

    fun insertDrawerItem(allChats: SnapshotStateList<DrawerItemsType>) {
        viewModelScope.launch {
                val gson = Gson()
                val jsonString = gson.toJson(allChats.lastOrNull()?.wholeChat)
                val item = DrawerItemsEntity(
                    id = allChats.last().id,
                    title = allChats.last().title,
                    wholeChat = jsonString
                )
                db.dao.insertDrawerItem(item)
        }
    }

    suspend fun getWholeChatById(id: String): List<ChatMessageBox> {
        val jsonString = db.dao.getWholeChatById(id)
        val gson = Gson()
        val result: List<ChatMessageBox> = gson.fromJson(
            jsonString,
            object : TypeToken<List<ChatMessageBox>>() {}.type
        )
        Log.v("result", result.toString())
    return result

    }

    fun getShortItems(allChats: SnapshotStateList<DrawerItemsType>) {
        viewModelScope.launch {
            val shortItems = db.dao.getShortItems()
            allChats.clear()
            shortItems.forEach { item ->
                allChats.add(0,
                    DrawerItemsType(
                        id = item.id,
                        title = item.title,
                        wholeChat = emptyList()
                    )
                )
            }

        }
    }

    fun deleteDrawerItemById(id: String) {
        viewModelScope.launch {
            db.dao.deleteDrawerItemById(id)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as MyApp).database
                return RoomViewModel(database) as T
            }
        }
    }
}