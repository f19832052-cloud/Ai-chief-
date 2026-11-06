package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.data.model.Recipe
import com.example.data.repository.AiChefPresetData
import com.example.ui.theme.AllergenAmber
import com.example.ui.theme.AllergenAmberContainer
import com.example.ui.theme.Charcoal
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerRedContainer
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshLime
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.SoftSageLight
import com.example.ui.theme.WarmIvory
import com.example.ui.viewmodel.AiChefViewModel
import java.io.File

@Composable
fun HomeScreen(
    viewModel: AiChefViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val previewUri by viewModel.previewImageUri.collectAsState()
    val previewBitmap by viewModel.previewBitmap.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysisError by viewModel.analysisError.collectAsState()
    val notFoodWarning by viewModel.notFoodWarning.collectAsState()
    val isRealAiOnline by viewModel.isRealAiOnline.collectAsState()

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // High-res camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            viewModel.setImageForAnalysis(tempCameraUri, null)
        }
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val photoFile = File.createTempFile("chef_capture_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(
                context,
                "Camera permission is needed to take a food photo.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Zero-permission Android Photo Picker for Gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.setImageForAnalysis(uri, null)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // AI Status Pill
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isRealAiOnline) FreshLime else SoftSage)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isRealAiOnline) Icons.Default.AutoAwesome else Icons.Default.Info,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isRealAiOnline) "Gemini Vision Active" else "Smart Culinary Engine • Demo Ready",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Brand Headline
            Text(
                text = "See it. Cook it.",
                fontFamily = PlayfairDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 42.sp,
                color = ForestGreen
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Photograph food you see anywhere. AI Chef instantly identifies the dish, breaks down practical measurements, and calculates student budget costs.",
                fontSize = 14.sp,
                color = CharcoalMuted,
                lineHeight = 20.sp
            )
        }

        // Photo Capture & Preview Area
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SoftSageBorder, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val hasImage = previewUri != null || previewBitmap != null

                    if (hasImage) {
                        // Image Preview with controls
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.33f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(SoftSage)
                        ) {
                            if (previewBitmap != null) {
                                Image(
                                    bitmap = previewBitmap!!.asImageBitmap(),
                                    contentDescription = "Selected dish",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (previewUri != null) {
                                AsyncImage(
                                    model = previewUri,
                                    contentDescription = "Selected dish",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Remove button
                            IconButton(
                                onClick = { viewModel.clearImagePreview() },
                                modifier = Modifier
                                    .testTag("remove_photo_button")
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isAnalyzing) {
                            // Loading state
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = ForestGreen,
                                    strokeWidth = 3.dp,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "AI Chef is analyzing your dish...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = ForestGreen
                                )
                                Text(
                                    text = "Identifying ingredients, measurements & student budget ratios",
                                    fontSize = 12.sp,
                                    color = CharcoalMuted
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        // Retake / pick again
                                        galleryLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .testTag("retake_photo_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Retake",
                                        fontWeight = FontWeight.SemiBold,
                                        color = ForestGreen
                                    )
                                }

                                Button(
                                    onClick = { viewModel.analyzeCurrentPhoto() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ForestGreen,
                                        contentColor = FreshLime
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .height(50.dp)
                                        .testTag("analyze_photo_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = FreshLime,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Analyze Recipe",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Empty photo picker prompt
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(SoftSageLight)
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Snap Food or Choose Image",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Charcoal
                            )

                            Text(
                                text = "Works with cooked meals, takeout, street food or fresh ingredients",
                                fontSize = 12.sp,
                                color = CharcoalMuted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val hasCam = ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CAMERA
                                        ) == PackageManager.PERMISSION_GRANTED
                                        if (hasCam) {
                                            val photoFile = File.createTempFile("chef_capture_", ".jpg", context.cacheDir)
                                            val uri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.fileprovider",
                                                photoFile
                                            )
                                            tempCameraUri = uri
                                            cameraLauncher.launch(uri)
                                        } else {
                                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ForestGreen,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("open_camera_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = FreshLime,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Open Camera",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        galleryLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("choose_gallery_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoLibrary,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "From Gallery",
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreen,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Non-food / Ambiguity Warning banner
        item {
            AnimatedVisibility(visible = notFoodWarning != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AllergenAmberContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = AllergenAmber,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Non-Food Detected",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = AllergenAmber
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = notFoodWarning ?: "",
                                fontSize = 13.sp,
                                color = Charcoal,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Analysis Error Banner
        item {
            AnimatedVisibility(visible = analysisError != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DangerRedContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = DangerRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Analysis Notice",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DangerRed
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = analysisError ?: "",
                                fontSize = 13.sp,
                                color = Charcoal
                            )
                        }
                    }
                }
            }
        }

        // Instant Try Preset Dishes (Essential for quick demo & testing)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Instant Test Dishes",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ForestGreen
                    )
                    Text(
                        text = "Tap to test flow",
                        fontSize = 12.sp,
                        color = CharcoalMuted
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(AiChefPresetData.sampleRecipes) { sample ->
                        PresetSampleCard(
                            recipe = sample,
                            onClick = { viewModel.selectPresetSample(sample) }
                        )
                    }
                }
            }
        }

        // Scanning Guidelines Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SoftSageBorder, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = null,
                            tint = ForestGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "3 Tips for Accurate AI Recognition",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = ForestGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "1. Natural Light: Avoid harsh flash shadows so AI detects natural herb colors.",
                        fontSize = 12.sp,
                        color = Charcoal,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Text(
                        text = "2. Center the Dish: Frame the main bowl/plate clearly in the center.",
                        fontSize = 12.sp,
                        color = Charcoal,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Text(
                        text = "3. Include Side Bowls: Side sauces and garnishes help AI infer hidden marinades.",
                        fontSize = 12.sp,
                        color = Charcoal,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PresetSampleCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .width(180.dp)
            .border(1.dp, SoftSageBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("sample_dish_${recipe.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(FreshLime)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${recipe.confidenceScore}% Match",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                }

                Text(
                    text = "${recipe.cookTimeMinutes}m",
                    fontSize = 11.sp,
                    color = CharcoalMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = recipe.dishName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Charcoal,
                maxLines = 2,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${recipe.difficulty} • ${recipe.ingredients.size} ingredients",
                fontSize = 11.sp,
                color = CharcoalMuted
            )
        }
    }
}
