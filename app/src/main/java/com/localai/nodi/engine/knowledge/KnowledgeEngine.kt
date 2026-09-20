package com.localai.nodi.engine.knowledge

import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.CopyOnWriteArrayList

data class IndexedDocument(
    val id: String,
    val title: String,
    val chunks: List<DocumentChunk>,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Singleton
class KnowledgeEngine @Inject constructor(
    private val chunkingService: ChunkingService
) {
    private val indexedDocuments = CopyOnWriteArrayList<IndexedDocument>()

    init {
        // Pre-load a sample knowledge base document about Bengali AI ethics and Nodi guidelines
        ingestDocument(
            title = "Nodi Offline Privacy Manifesto & Bangladesh Knowledge Base",
            rawText = """
                Nodi is built to operate completely offline on Android smartphones using ARM64 processors.
                It never connects to the cloud, ensuring user personal notes and conversations remain encrypted.
                In Bengali culture, 'Nodi' symbolizes a river flowing peacefully with knowledge and emotional support.
                Zero telemetry or usage metrics are transmitted outside the physical device.
            """.trimIndent()
        )
    }

    fun ingestDocument(title: String, rawText: String): IndexedDocument {
        val chunks = chunkingService.splitTextIntoChunks(title, rawText)
        val doc = IndexedDocument(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            chunks = chunks
        )
        indexedDocuments.add(doc)
        return doc
    }

    fun getIndexedDocuments(): List<IndexedDocument> = indexedDocuments

    fun queryKnowledgeBase(query: String, topK: Int = 3): List<DocumentChunk> {
        val queryWords = query.lowercase().split("\\s+".toRegex()).filter { it.length > 2 }
        if (queryWords.isEmpty()) return emptyList()

        val allChunks = indexedDocuments.flatMap { it.chunks }
        
        // Simple TF keyword similarity scoring for offline simulated RAG
        return allChunks.map { chunk ->
            val lowerText = chunk.textContent.lowercase()
            val score = queryWords.count { lowerText.contains(it) }
            Pair(chunk, score)
        }
        .filter { it.second > 0 }
        .sortedByDescending { it.second }
        .take(topK)
        .map { it.first }
    }
}
