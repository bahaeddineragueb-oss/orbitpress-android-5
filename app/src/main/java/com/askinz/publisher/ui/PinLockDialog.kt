package com.askinz.publisher.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PinLockDialog(
  onUnlock: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var pin by remember { mutableStateOf("") }
  var errorText by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = { Icon(Icons.Default.Lock, contentDescription = "Security Lock") },
    title = { Text("Settings PIN Lock", fontWeight = FontWeight.Bold) },
    text = {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Enter your 4–12 digit PIN to access publishing settings.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
          value = pin,
          onValueChange = {
            if (it.length <= 12 && it.all { c -> c.isDigit() }) {
              pin = it
              errorText = null
            }
          },
          label = { Text("PIN Code") },
          singleLine = true,
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          modifier = Modifier.fillMaxWidth(),
          isError = errorText != null
        )
        if (errorText != null) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(errorText!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (pin.length < 4) {
            errorText = "Enter at least 4 digits."
          } else {
            onUnlock(pin)
          }
        }
      ) {
        Text("Unlock")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
