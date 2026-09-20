package com.localai.nodi.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ConversationEntity::class, MessageEntity::class, MemoryEntryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class EncryptedDatabase : RoomDatabase() {
    abstract fun nodiDao(): NodiDao
}
