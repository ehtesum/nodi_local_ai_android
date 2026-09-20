# Nodi (নদী) — Offline AI Companion for Android

Fully offline, privacy-first AI assistant for Android. Runs quantized LLMs (GGUF) directly on ARM64 hardware with zero network permissions. Features a 7-tier memory hierarchy, on-device RAG knowledge engine, and adaptive resource management for mobile hardware constraints.

## Architecture

```
nodi_local_ai_android/
├── app/src/main/java/com/example/nodi/
│   ├── OrchestratorService       Central inference + RAG pipeline
│   ├── ResourceManager           CPU/GPU thermal & RAM monitoring
│   ├── MemoryEngine              7-tier memory hierarchy
│   ├── KnowledgeEngine           Offline TF-IDF document RAG
│   ├── VoiceEngine               Offline STT + Bengali Piper TTS
│   ├── EmotionEngine             Sentiment evaluation
│   ├── ui/                       Jetpack Compose UI
│   └── crypto/                   SQLCipher encryption, SHA-256 verification
├── app/build.gradle.kts          Build configuration
├── Nodi.apk                      Pre-built debug APK
└── build.gradle.kts              Root build file
```

### Security Model
- No `android.permission.INTERNET` in manifest — zero external network access
- SQLCipher AES-256 encryption for local storage
- SHA-256 verification of GGUF model files before loading
- FLAG_SECURE on all sensitive windows

## Setup

### Running the Pre-built APK

```bash
adb install Nodi.apk
```

### Building from Source

```bash
# Linux / macOS:
./gradlew assembleDebug

# Windows:
.\gradlew.bat assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

### Loading a Model

1. Download a GGUF model (e.g., `Phi-3-mini-4k-instruct.Q4_K_M.gguf`)
2. Push to device: `adb push model.gguf /sdcard/Nodi/models/`
3. Open Model Vault tab → verify SHA-256 → activate

## Hardware Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| Android | 10 (API 29) | 13+ (API 33) |
| CPU | ARM64 (8 cores) | Snapdragon 8 Gen 1+ |
| RAM | 4 GB | 8 GB |
| Storage | 4 GB free (for model) | 8 GB |
| GPU | Adreno 618+ | Adreno 730+ |

### Build Environment
| Component | Requirement |
|-----------|-------------|
| JDK | 17+ |
| Android SDK | API 34 compile SDK |
| Android Studio | Hedgehog or newer |

## 🚧 Works to be Done / Future Roadmap

- [ ] **Vulkan / NDK GPU Acceleration**: Implement native Vulkan JNI bindings for GGML/llama.cpp to accelerate model inference on Adreno/Mali GPUs.
- [ ] **Android System Intent Dispatcher**: Add structured function calling schemas enabling Nodi to set system alarms, calendar events, and toggle device settings.
- [ ] **On-Device Vision (LLaVA / Moondream)**: Integrate multimodal GGUF vision weights for local image analysis and camera feed queries.
- [ ] **HNSW Vector Indexing**: Replace the current TF-IDF knowledge engine with an on-device HNSW vector database for semantic RAG search over local PDFs.
- [ ] **Encrypted P2P Local Sync**: Add zero-cloud P2P memory synchronization across local devices via WiFi Direct.
