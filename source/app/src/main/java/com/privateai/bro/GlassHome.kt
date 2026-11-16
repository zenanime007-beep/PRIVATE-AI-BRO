package com.privateai.bro

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun GlassHomeScreen(st: OrbState) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var overlayOn by remember { mutableStateOf(OrbService.running) }
    val micLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) Assistant.activate()
    }
    val notificationLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    val status = when (st) {
        OrbState.IDLE -> "Ready when you are"
        OrbState.WAKE -> "Waking up"
        OrbState.LISTENING -> "Listening to you"
        OrbState.VERIFYING -> "Verifying your voice"
        OrbState.THINKING -> "Thinking"
        OrbState.EXECUTING -> "Working on it"
        OrbState.SPEAKING -> "BRO is speaking"
        OrbState.CONFIRM -> "Waiting for your confirmation"
        OrbState.ERROR -> "Something needs attention"
        OrbState.OFFLINE -> "Offline mode"
        OrbState.LOCKED -> "Locked"
    }
    val accent = when (st) {
        OrbState.ERROR, OrbState.CONFIRM -> Sig.Red
        OrbState.OFFLINE -> Sig.Grey
        else -> Sig.Acid
    }

    Box(
        Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF111D36), Color(0xFF080D1B), Color(0xFF050914)))
        )
    ) {
        Box(
            Modifier.align(Alignment.TopEnd).size(240.dp)
                .background(Brush.radialGradient(listOf(Sig.Violet.copy(alpha = 0.22f), Color.Transparent)))
        )
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Mono("PRIVATE AI", 11.sp, Color.White.copy(alpha = 0.62f), bold = true)
                    Display("BRO", 34.sp, Color.White)
                }
                Tag("PERSONAL ASSISTANT", bg = Sig.Violet, fg = Color.White)
            }

            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f)) {
                    Mono("YOUR SPACE. YOUR VOICE.", 10.sp, Sig.Acid, bold = true)
                    Display(wordFor(st), 43.sp, Color.White)
                    Mono(status, 13.sp, Color.White.copy(alpha = 0.70f))
                }
                Tag(if (st == OrbState.OFFLINE) "OFFLINE" else "PRIVATE", bg = accent, fg = Color(0xFF07111F))
            }

            Spacer(Modifier.height(18.dp))
            HardCard(Modifier.fillMaxWidth(), Sig.Glass) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Mono("LIVE VOICE CORE", 10.sp, Color.White.copy(alpha = 0.68f), bold = true)
                        Mono("● " + if (st == OrbState.IDLE) "STANDBY" else "ACTIVE", 10.sp, accent, bold = true)
                    }
                    Spacer(Modifier.height(2.dp))
                    Box(
                        Modifier.fillMaxWidth().height(278.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.radialGradient(
                                    listOf(Sig.Violet.copy(alpha = 0.19f), Sig.Acid.copy(alpha = 0.06f), Color.Transparent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            factory = { viewContext ->
                                OrbView(viewContext).apply {
                                    interactive = true
                                    setOnClickListener {
                                        if (!Assistant.hasMic()) micLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        else Assistant.activate()
                                    }
                                }
                            },
                            update = {
                                it.orbState = Assistant.state
                                it.audioLevel = Assistant.audioLevel
                            },
                            modifier = Modifier.size(258.dp)
                        )
                    }
                    Mono("TAP THE ORB TO TALK", 11.sp, Sig.Acid, bold = true)
                    Mono("Touch and drag to tilt the orb", 10.sp, Color.White.copy(alpha = 0.48f))
                }
            }

            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HardCard(Modifier.weight(1f), Sig.Glass) {
                    Column {
                        Mono("YOU SAID", 10.sp, Sig.Acid, bold = true)
                        Spacer(Modifier.height(6.dp))
                        Mono(Assistant.heard.ifBlank { "Your voice message will appear here." }, 12.sp, Color.White)
                    }
                }
                HardCard(Modifier.weight(1f), Sig.Glass) {
                    Column {
                        Mono("BRO REPLIED", 10.sp, Sig.Red, bold = true)
                        Spacer(Modifier.height(6.dp))
                        Mono(Assistant.reply.ifBlank { "BRO's response will appear here." }, 12.sp, Color.White)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            HardCard(Modifier.fillMaxWidth(), Sig.Glass) {
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Mono("FLOATING ORB", 11.sp, Color.White, bold = true)
                        Tag(if (overlayOn) "ON" else "OFF", bg = if (overlayOn) Sig.Acid else Sig.Grey, fg = Color(0xFF07111F))
                    }
                    Spacer(Modifier.height(6.dp))
                    Mono("Keep BRO available above other apps.", 11.sp, Color.White.copy(alpha = 0.60f))
                    Spacer(Modifier.height(10.dp))
                    BlockButton(
                        if (overlayOn) "TURN FLOATING ORB OFF" else "ENABLE FLOATING ORB",
                        Modifier.fillMaxWidth(),
                        onClick = {
                            if (overlayOn) {
                                ctx.stopService(Intent(ctx, OrbService::class.java))
                                overlayOn = false
                            } else if (!Settings.canDrawOverlays(ctx)) {
                                ctx.startActivity(
                                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + ctx.packageName))
                                )
                            } else {
                                if (Build.VERSION.SDK_INT >= 33) notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                ctx.startForegroundService(Intent(ctx, OrbService::class.java))
                                overlayOn = true
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Display("TRY A COMMAND", 25.sp, Color.White, Modifier.weight(1f))
                Mono("EXAMPLES", 9.sp, Sig.Acid, bold = true)
            }
            Spacer(Modifier.height(10.dp))
            val examples = listOf(
                "Open WhatsApp" to "APP",
                "Torch on" to "DEVICE",
                "Timer 5 minutes" to "TOOLS",
                "Call Amma" to "CONTACT",
                "Remember my exam is Friday" to "MEMORY",
                "வாட்ஸ்அப் திற" to "தமிழ்"
            )
            examples.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    pair.forEach { (command, type) ->
                        HardCard(Modifier.weight(1f).padding(bottom = 9.dp), Sig.Glass) {
                            Column {
                                Mono(type, 9.sp, Sig.Acid, bold = true)
                                Spacer(Modifier.height(4.dp))
                                Mono(command, 12.sp, Color.White)
                            }
                        }
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            Mono(
                "VIVO TIP  ·  ALLOW AUTO-START AND UNRESTRICTED BATTERY TO KEEP THE FLOATING ORB RUNNING.",
                9.sp, Color.White.copy(alpha = 0.45f)
            )
        }
    }
}
