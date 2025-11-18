# 📸 Android Pose Detection (Jetpack Compose + ML Kit)

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)

Dự án mã nguồn mở minh họa cách xây dựng ứng dụng **Phát hiện tư thế (Pose Detection)** và **Trích xuất khung xương (Skeleton Tracking)** theo thời gian thực trên Android.

Dự án được phát triển hoàn toàn bằng **Kotlin** + **Jetpack Compose**, sử dụng sức mạnh của **Google ML Kit** để xử lý AI trực tiếp trên thiết bị (On-device Machine Learning).

## ✨ Tính năng nổi bật
- [x] **Real-time Tracking:** Phát hiện & theo dõi chuyển động cơ thể với độ trễ thấp.  
- [x] **Skeleton Drawing:** Vẽ 33 điểm mốc (landmarks) & các đường nối.  
- [x] **Modern UI:** Giao diện 100% Compose (Material 3).  
- [x] **CameraX Integration:** Tích hợp CameraX với `ImageAnalysis`.  
- [x] **Auto Scaling:** Tự điều chỉnh tỷ lệ khớp hình theo kích thước thiết bị.

## 🛠 Công nghệ sử dụng
- **Ngôn ngữ:** Kotlin  
- **UI:** Jetpack Compose (Material3)  
- **Camera:** CameraX (LifecycleCameraController)  
- **AI/ML:** Google ML Kit — Pose Detection API  
- **Quản lý phiên bản:** Gradle Version Catalog (`libs.versions.toml`)

## 🚀 Cài đặt & Chạy thử

### Yêu cầu
- Android Studio **Ladybug** hoặc mới hơn  
- Thiết bị Android **Android 7.0+**, hoặc Emulator có **Webcam**

### Cách chạy
1. Clone dự án:
    ```bash
    git clone https://github.com/lee-vtruong/Android-Pose-Skeleton.git
    ```
2. Mở bằng Android Studio.  
3. Sync Gradle.  
4. Nhấn **Run (▶)** và cấp quyền camera.

## 🤝 Đóng góp
Hoan nghênh mọi ý kiến, báo lỗi hoặc Pull Request.  
Tạo Issue tại:  
`https://github.com/lee-vtruong/Android-Pose-Skeleton/issues`
