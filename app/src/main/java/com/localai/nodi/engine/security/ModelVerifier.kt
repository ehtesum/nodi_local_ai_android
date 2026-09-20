package com.localai.nodi.engine.security

import java.io.File
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelVerifier @Inject constructor() {

    companion object {
        const val EXPECTED_LATEST_MODEL_SHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855" // Sample SHA-256
    }

    fun verifyGgufChecksum(file: File, expectedSha256: String = EXPECTED_LATEST_MODEL_SHA256): Boolean {
        if (!file.exists()) return false
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            file.inputStream().use { fis ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (fis.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }
            val hashBytes = digest.digest()
            val computedHex = hashBytes.joinToString("") { "%02x".format(it) }
            return computedHex.equals(expectedSha256, ignoreCase = true)
        } catch (e: Exception) {
            return false
        }
    }
}
