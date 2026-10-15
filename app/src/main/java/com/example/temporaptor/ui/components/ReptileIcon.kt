package com.example.temporaptor.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.temporaptor.ui.theme.AmberGlow
import com.example.temporaptor.ui.theme.EmeraldAccent

/**
 * Custom High-Detail Vector Turtle Head & Terrarium Emblem for TempoRaptor.
 * Features multi-layered carapace scutes, glowing reptilian eye, and snout details.
 */
@Composable
fun TurtleHeadIcon(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    tint: Color = EmeraldAccent,
    accentColor: Color = AmberGlow,
    drawBackgroundShield: Boolean = true
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        if (drawBackgroundShield) {
            // Outer Carapace Shield Shadow/Glow
            val outerShieldPath = Path().apply {
                moveTo(w * 0.5f, h * 0.16f)
                cubicTo(w * 0.16f, h * 0.16f, w * 0.08f, h * 0.35f, w * 0.08f, h * 0.60f)
                cubicTo(w * 0.08f, h * 0.82f, w * 0.25f, h * 0.98f, w * 0.5f, h * 0.98f)
                cubicTo(w * 0.75f, h * 0.98f, w * 0.92f, h * 0.82f, w * 0.92f, h * 0.60f)
                cubicTo(w * 0.92f, h * 0.35f, w * 0.84f, h * 0.16f, w * 0.5f, h * 0.16f)
                close()
            }
            drawPath(
                path = outerShieldPath,
                color = tint.copy(alpha = 0.25f)
            )

            // Inner Carapace Base
            val innerShieldPath = Path().apply {
                moveTo(w * 0.5f, h * 0.20f)
                cubicTo(w * 0.22f, h * 0.20f, w * 0.14f, h * 0.36f, w * 0.14f, h * 0.58f)
                cubicTo(w * 0.14f, h * 0.76f, w * 0.28f, h * 0.92f, w * 0.5f, h * 0.94f)
                cubicTo(w * 0.72f, h * 0.92f, w * 0.86f, h * 0.76f, w * 0.86f, h * 0.58f)
                cubicTo(w * 0.86f, h * 0.36f, w * 0.78f, h * 0.20f, w * 0.5f, h * 0.20f)
                close()
            }
            drawPath(
                path = innerShieldPath,
                color = tint
            )

            // Carapace Hex Scute Pattern
            val scutePath = Path().apply {
                // Main Spine
                moveTo(w * 0.5f, h * 0.24f)
                lineTo(w * 0.5f, h * 0.90f)

                // Upper Ribs
                moveTo(w * 0.28f, h * 0.34f)
                lineTo(w * 0.5f, h * 0.44f)
                lineTo(w * 0.72f, h * 0.34f)

                // Middle Ribs
                moveTo(w * 0.20f, h * 0.54f)
                lineTo(w * 0.5f, h * 0.64f)
                lineTo(w * 0.80f, h * 0.54f)

                // Lower Ribs
                moveTo(w * 0.30f, h * 0.76f)
                lineTo(w * 0.5f, h * 0.84f)
                lineTo(w * 0.70f, h * 0.76f)
            }
            drawPath(
                path = scutePath,
                color = Color.Black.copy(alpha = 0.4f),
                style = Stroke(
                    width = w * 0.05f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // Turtle Head & Neck Contour
        val headPath = Path().apply {
            moveTo(w * 0.5f, h * 0.02f)
            cubicTo(w * 0.34f, h * 0.02f, w * 0.30f, h * 0.12f, w * 0.30f, h * 0.24f)
            cubicTo(w * 0.40f, h * 0.27f, w * 0.60f, h * 0.27f, w * 0.70f, h * 0.24f)
            cubicTo(w * 0.70f, h * 0.12f, w * 0.66f, h * 0.02f, w * 0.5f, h * 0.02f)
            close()
        }
        drawPath(
            path = headPath,
            color = tint
        )

        // Turtle Snout & Mouth Line
        val mouthPath = Path().apply {
            moveTo(w * 0.42f, h * 0.09f)
            cubicTo(w * 0.48f, h * 0.11f, w * 0.52f, h * 0.11f, w * 0.58f, h * 0.09f)
        }
        drawPath(
            path = mouthPath,
            color = Color.Black.copy(alpha = 0.5f),
            style = Stroke(width = w * 0.03f, cap = StrokeCap.Round)
        )

        // Turtle Eyes (Left and Right)
        val eyeLeftCenter = Offset(w * 0.40f, h * 0.13f)
        val eyeRightCenter = Offset(w * 0.60f, h * 0.13f)

        drawCircle(
            color = Color.Black,
            radius = w * 0.045f,
            center = eyeLeftCenter
        )
        drawCircle(
            color = Color.Black,
            radius = w * 0.045f,
            center = eyeRightCenter
        )

        drawCircle(
            color = accentColor,
            radius = w * 0.035f,
            center = eyeLeftCenter
        )
        drawCircle(
            color = accentColor,
            radius = w * 0.035f,
            center = eyeRightCenter
        )

        // Eye Pupils (Dark Vertical Pupil)
        drawCircle(
            color = Color.Black,
            radius = w * 0.018f,
            center = eyeLeftCenter
        )
        drawCircle(
            color = Color.Black,
            radius = w * 0.018f,
            center = eyeRightCenter
        )

        // Catchlight reflections
        drawCircle(
            color = Color.White,
            radius = w * 0.01f,
            center = Offset(w * 0.39f, h * 0.12f)
        )
        drawCircle(
            color = Color.White,
            radius = w * 0.01f,
            center = Offset(w * 0.59f, h * 0.12f)
        )

        // Center Basking Sun / Heat Emblem on Shell
        val emblemCenter = Offset(w * 0.5f, h * 0.54f)
        drawCircle(
            color = accentColor,
            radius = w * 0.085f,
            center = emblemCenter
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.8f),
            radius = w * 0.04f,
            center = emblemCenter
        )
    }
}
