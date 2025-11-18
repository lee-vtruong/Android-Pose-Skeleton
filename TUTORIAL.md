# 📚 Hướng dẫn kỹ thuật: Xây dựng ứng dụng Pose Detection

Tài liệu này giải thích chi tiết các bước triển khai và logic hoạt động của ứng dụng trích xuất khung xương sử dụng ML Kit + Jetpack Compose.

## 1. Cấu trúc dự án

- **MainActivity.kt** — chứa toàn bộ logic chính  
- **CameraPreview** — composable hiển thị camera & gửi frame để phân tích  
- **PoseOverlay** — composable vẽ khung xương (Canvas)

---

## 2. Pipeline xử lý

1. **Camera Capture** — CameraX thu nhận khung hình  
2. **Analysis** — ML Kit phân tích → trả về Pose  
3. **Rendering** — Compose vẽ skeleton 

---

## 3. Chi tiết từng phần

### 🔹 Phần 1 — Tích hợp CameraX với Compose

```kotlin
AndroidView(
    factory = { ctx ->
        PreviewView(ctx).apply {
            controller = cameraController
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
)
```

---

### 🔹 Phần 2 — Phân tích hình ảnh (The Brain)

```kotlin
imageProxy.close() // Bắt buộc để tránh treo CameraX
```

---

### 🔹 Phần 3 — Quy đổi tọa độ (Scale & Offset)

```kotlin
val scaleFactor = minOf(canvasWidth / imageWidth, canvasHeight / imageHeight)

fun translate(point: PointF): Offset {
    val x = point.x * scaleFactor + offsetX
    val y = point.y * scaleFactor + offsetY
    return Offset(x, y)
}
```

---

### 🔹 Phần 4 — Vẽ khung xương

Sử dụng **Canvas của Compose** để vẽ 33 landmarks và connection.

---

## 4. Các thư viện chính

- `androidx.camera`  
- `com.google.mlkit:pose-detection`  
- `androidx.compose.ui`  

---

## 5. Tổng kết

Dự án minh họa sức mạnh của Jetpack Compose + ML Kit:  
Chỉ với ~300 dòng code đã tích hợp được AI thời gian thực vào ứng dụng Android.
