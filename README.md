# Android Pose Detection with Jetpack Compose & ML Kit

Dự án Android mẫu này minh họa cách xây dựng ứng dụng **Phát hiện tư thế (Pose Detection)** và vẽ khung xương (Skeleton Tracking) theo thời gian thực.

Dự án sử dụng công nghệ hiện đại nhất của Android: **Jetpack Compose**, **CameraX** và **Google ML Kit**.

## 📱 Tính năng
- [x] Xem trước Camera theo thời gian thực (sử dụng CameraX).
- [x] Phát hiện 33 điểm mốc trên cơ thể người (ML Kit Pose Detection).
- [x] Vẽ khung xương và khớp nối lên màn hình (Compose Canvas).
- [x] Tự động điều chỉnh tỷ lệ khung xương khớp với mọi kích thước màn hình.
- [x] Hỗ trợ cả Camera trước và sau (Mặc định dùng Camera sau).

## 🛠 Công nghệ sử dụng
- **Ngôn ngữ:** Kotlin
- **Giao diện:** Jetpack Compose (Material3)
- **Camera:** CameraX (với `LifecycleCameraController`)
- **AI/ML:** Google ML Kit (Pose Detection API)
- **Build System:** Gradle (Kotlin DSL + Version Catalog)

## 🚀 Cách cài đặt và chạy
1. **Clone dự án về máy:**
   ```bash
   git clone [https://github.com/username-cua-ban/Android-MLKit-Compose-PoseDetection.git](https://github.com/lee-vtruong/Android-Pose-Skeleton.git)
