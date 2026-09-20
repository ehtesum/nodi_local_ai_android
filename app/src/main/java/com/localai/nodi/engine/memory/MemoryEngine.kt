package com.localai.nodi.engine.memory

import com.localai.nodi.data.storage.db.MemoryEntryEntity
import com.localai.nodi.data.storage.db.NodiDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton
class MemoryEngine @Inject constructor(
    private val nodiDao: NodiDao,
    private val ranker: MemoryRanker,
    private val compressor: MemoryCompressor
) {
    // 1. Working Memory (RAM Turn Buffer)
    private val workingMemoryBuffer = ConcurrentHashMap<String, String>()

    suspend fun storeWorkingMemory(key: String, value: String) {
        workingMemoryBuffer[key] = value
        val score = ranker.calculateImportanceScore(value, System.currentTimeMillis())
        
        val tier = when {
            score >= 0.8f -> "user"
            score >= 0.6f -> "semantic"
            else -> "long_term"
        }

        withContext(Dispatchers.IO) {
            nodiDao.insertMemory(
                MemoryEntryEntity(
                    tier = tier,
                    key = key,
                    value = value,
                    importanceScore = score
                )
            )
        }
    }

    suspend fun retrieveRelevantContext(query: String): List<MemoryEntryEntity> = withContext(Dispatchers.IO) {
        return@withContext nodiDao.getTopMemories()
    }
}
