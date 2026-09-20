package com.localai.nodi.engine.knowledge

import javax.inject.Inject
import javax.inject.Singleton

data class DocumentChunk(
    val chunkIndex: Int,
    val textContent: String,
    val citationInfo: String
)

@Singleton
class ChunkingService @Inject constructor() {

    fun splitTextIntoChunks(documentTitle: String, rawText: String, chunkSize: Int = 300, overlap: Int = 50): List<DocumentChunk> {
        val words = rawText.split("\\s+".toRegex()).filter { it.isNotBlank() }
        if (words.isEmpty()) return emptyList()

        val chunks = mutableListOf<DocumentChunk>()
        var index = 0
        var chunkIdx = 0

        while (index < words.size) {
            val end = (index + chunkSize).coerceAtMost(words.size)
            val chunkWords = words.subList(index, end)
            val chunkText = chunkWords.joinToString(" ")

            chunks.add(
                DocumentChunk(
                    chunkIndex = chunkIdx++,
                    textContent = chunkText,
                    citationInfo = "$documentTitle (Section ${chunkIdx})"
                )
            )

            if (end == words.size) break
            index += (chunkSize - overlap).coerceAtLeast(1)
        }

        return chunks
    }
}
