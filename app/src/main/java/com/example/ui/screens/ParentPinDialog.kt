package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ParentPinDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    verifyPin: (String) -> Boolean
) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("parent_pin_dialog"),
        shape = RoundedCornerShape(28.dp),
        containerColor = Color(0xFF1E1B4B), // Midnight Blue
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6366F1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "כניסת הורים",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "הקלידו קוד PIN לגישה להגדרות",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // PIN Dots Display
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    for (i in 0 until 4) {
                        val isFilled = i < enteredPin.length
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isError -> Color(0xFFEF4444)
                                        isFilled -> Color(0xFF38BDF8)
                                        else -> Color(0xFF475569)
                                    }
                                )
                        )
                    }
                }

                if (isError) {
                    Text(
                        text = "קוד שגוי! נסו שוב (ברירת מחדל: 1234)",
                        color = Color(0xFFF87171),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Numeric Keypad
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("C", "0", "DEL")
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    keys.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { key ->
                                Surface(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            isError = false
                                            when (key) {
                                                "C" -> enteredPin = ""
                                                "DEL" -> if (enteredPin.isNotEmpty()) enteredPin = enteredPin.dropLast(1)
                                                else -> {
                                                    if (enteredPin.length < 4) {
                                                        val newPin = enteredPin + key
                                                        enteredPin = newPin
                                                        if (newPin.length == 4) {
                                                            if (verifyPin(newPin)) {
                                                                onSuccess()
                                                            } else {
                                                                isError = true
                                                                enteredPin = ""
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        .testTag("pin_key_$key"),
                                    shape = CircleShape,
                                    color = when (key) {
                                        "C", "DEL" -> Color(0xFF334155)
                                        else -> Color(0xFF312E81)
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (key == "DEL") {
                                            Icon(
                                                imageVector = Icons.Default.Backspace,
                                                contentDescription = "Delete",
                                                tint = Color.White,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        } else {
                                            Text(
                                                text = key,
                                                color = Color.White,
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("pin_cancel_button")
            ) {
                Text("ביטול", color = Color(0xFF94A3B8), fontSize = 15.sp)
            }
        }
    )
}
