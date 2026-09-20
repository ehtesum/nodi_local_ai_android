package com.localai.nodi.engine.memory

import com.localai.nodi.data.storage.db.MessageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryCompressor @Inject constructor() {

    fun compressConversationHistory(messages: List<MessageEntity>): String {
        if (messages.isEmpty()) return ""
        val userTurnCount = messages.count { it.role == "user" }
        val topics = messages
            .filter { it.role == "user" }
            .takeLast(5)
            .joinToString("; ") { it.content.take(30) + "..." }
        
        return "[Compressed Summary of $userTurnCount turns]: Topics discussed include $topics."
    }
}
