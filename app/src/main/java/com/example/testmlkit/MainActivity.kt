// ĐẢM BẢO TÊN PACKAGE NÀY KHỚP VỚI BẠN
package com.example.testmlkit

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF // <<< ĐÃ SỬA: Import đúng PointF của Android
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner // <-- Cảnh báo 'deprecated' ở đây, BỎ QUA NÓ
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.testmlkit.ui.theme.TestMLKitTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestMLKitTheme {
                CameraScreen()
            }
        }
    }
}

@Composable
fun CameraScreen() {
    var hasCameraPermission by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionStatus) {
            hasCameraPermission = true
        } else {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    var pose by remember { mutableStateOf<Pose?>(null) }
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    // Lưu kích thước ảnh
    var imageWidth by remember { mutableStateOf(480f) } // Giá trị mặc định
    var imageHeight by remember { mutableStateOf(640f) } // Giá trị mặc định

    DisposableEffect(key1 = analysisExecutor) {
        onDispose {
            analysisExecutor.shutdown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                executor = analysisExecutor,
                onPoseDetected = { detectedPose, width, height ->
                    pose = detectedPose
                    imageWidth = width.toFloat()
                    imageHeight = height.toFloat()
                }
            )

            pose?.let {
                PoseOverlay(
                    pose = it,
                    imageWidth = imageWidth,
                    imageHeight = imageHeight,
                    modifier = Modifier.fillMaxSize()
                )
            }

        } else {
            Text(
                text = "Ứng dụng cần quyền Camera để hoạt động.",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    executor: ExecutorService,
    onPoseDetected: (Pose, Int, Int) -> Unit // Trả về Pose VÀ Kích thước ảnh
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val poseDetector = remember {
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()
        PoseDetection.getClient(options)
    }

    val cameraController = remember {
        LifecycleCameraController(context)
    }

    LaunchedEffect(key1 = cameraController) {
        cameraController.setImageAnalysisAnalyzer(
            executor,
            // --- ĐÃ SỬA: Bỏ ký tự '@' ---
            ImageAnalysis.Analyzer { imageProxy ->
                processImageProxy(poseDetector, imageProxy) { detectedPose ->
                    // Gửi pose và kích thước ảnh
                    onPoseDetected(detectedPose, imageProxy.width, imageProxy.height)
                }
            }
        )
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                controller = cameraController
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        }
    )

    LaunchedEffect(key1 = lifecycleOwner) {
        cameraController.bindToLifecycle(lifecycleOwner)
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun processImageProxy(
    poseDetector: PoseDetector,
    imageProxy: ImageProxy,
    onPoseDetected: (Pose) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        poseDetector.process(image)
            .addOnSuccessListener { pose ->
                onPoseDetected(pose)
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}

/**
 * Composable để vẽ khung xương (ĐÃ SỬA LỖI TYPO)
 */
@Composable
fun PoseOverlay(
    pose: Pose,
    imageWidth: Float,
    imageHeight: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val widthRatio = canvasWidth / imageWidth
        val heightRatio = canvasHeight / imageHeight
        val scaleFactor = minOf(widthRatio, heightRatio)

        val offsetX = (canvasWidth - imageWidth * scaleFactor) / 2
        val offsetY = (canvasHeight - imageHeight * scaleFactor) / 2

        // Hàm helper để chuyển đổi tọa độ
        fun translate(point: PointF): Offset {
            val x = point.x * scaleFactor + offsetX
            val y = point.y * scaleFactor + offsetY
            return Offset(x, y)
        }

        val landmarkPairs = listOf(
            Pair(pose.getPoseLandmark(11), pose.getPoseLandmark(13)), // L_SHOULDER -> L_ELBOW
            Pair(pose.getPoseLandmark(13), pose.getPoseLandmark(15)), // L_ELBOW -> L_WRIST
            Pair(pose.getPoseLandmark(12), pose.getPoseLandmark(14)), // R_SHOULDER -> R_ELBOW
            Pair(pose.getPoseLandmark(14), pose.getPoseLandmark(16)), // R_ELBOW -> R_WRIST
            Pair(pose.getPoseLandmark(11), pose.getPoseLandmark(12)), // L_SHOULDER -> R_SHOULDER
            Pair(pose.getPoseLandmark(23), pose.getPoseLandmark(24)), // L_HIP -> R_HIP
            Pair(pose.getPoseLandmark(11), pose.getPoseLandmark(23)), // L_SHOULDER -> L_HIP
            Pair(pose.getPoseLandmark(12), pose.getPoseLandmark(24)), // R_SHOULDER -> R_HIP
            Pair(pose.getPoseLandmark(23), pose.getPoseLandmark(25)), // L_HIP -> L_KNEE

            // --- DÒNG SỬA LỖI ĐÂY ---
            // Sửa 'getPoselandmark' thành 'getPoseLandmark'
            Pair(pose.getPoseLandmark(25), pose.getPoseLandmark(27)), // L_KNEE -> L_ANKLE

            Pair(pose.getPoseLandmark(24), pose.getPoseLandmark(26)), // R_HIP -> R_KNEE
            Pair(pose.getPoseLandmark(26), pose.getPoseLandmark(28))  // R_KNEE -> R_ANKLE
        )

        for (pair in landmarkPairs) {
            val start = pair.first
            val end = pair.second

            if (start != null && end != null) {
                val startPoint = translate(start.position)
                val endPoint = translate(end.position)
                drawLine(
                    color = Color.White,
                    start = startPoint,
                    end = endPoint,
                    strokeWidth = 6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        pose.allPoseLandmarks.forEach { landmark ->
            if (landmark != null) {
                drawCircle(
                    color = Color.Cyan,
                    radius = 8.dp.toPx(),
                    center = translate(landmark.position)
                )
            }
        }
    }
}