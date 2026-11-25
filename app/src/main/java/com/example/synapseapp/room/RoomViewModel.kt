package com.example.synapseapp.room

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.synapseapp.MyApp
// import com.example.synapseapp.airequests.ChatHistory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.ChatMessageBox
import data.DrawerItemsType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomViewModel(database: MainDb) : ViewModel() {
    private val db = database
    private val gson = Gson()
    private val chatListType = object : TypeToken<List<ChatMessageBox>>() {}.type

    fun insertDrawerItem(id: String, title: String, messages: List<ChatMessageBox>) {
        viewModelScope.launch {
            val gson = Gson()
            val jsonString = gson.toJson(messages)
            val item = DrawerItemsEntity(
                id = id,
                title = title,
                wholeChat = jsonString,
            )
            db.dao.insertDrawerItem(item)
        }
    }



//    suspend fun getWholeChatById(id: String): List<ChatMessageBox> =
//        withContext(Dispatchers.Default) {
//            val jsonString = db.dao.getWholeChatById(id) ?: return@withContext emptyList()
//            gson.fromJson<List<ChatMessageBox>>(jsonString, chatListType)
//        }
suspend fun getWholeChatById(id: String): List<ChatMessageBox> =
    withContext(Dispatchers.Default) {

        Log.d("ROOM_DEBUG", "REQUESTED ID = '$id'")

        val jsonString = db.dao.getWholeChatById(id)

        Log.d("ROOM_DEBUG", "JSON FROM DB = '$jsonString'")

        if (jsonString == null) {
            Log.e("ROOM_DEBUG", "CHAT NOT FOUND FOR ID = '$id'")
            return@withContext emptyList()
        }

        gson.fromJson<List<ChatMessageBox>>(
            jsonString,
            chatListType
        ) ?: emptyList()
    }

    fun getShortItems(allChats: SnapshotStateList<DrawerItemsType>) {
        viewModelScope.launch {
            val shortItems = db.dao.getIdTitleDrawerItems()
            allChats.clear()
            shortItems.forEach { item ->
                allChats.add(
                    DrawerItemsType(
                        id = item.id,
                        title = item.title
                    )
                )
                Log.v("item.wholeChat",item.toString())
            }
            Log.v("allChats", allChats.toString())
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