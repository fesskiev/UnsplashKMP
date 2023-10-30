package com.unsplash.android.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorAlertDialog(
    errorMessage: String,
    onRetryClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onRetryClick() },
        title = { Text(text = "Error") },
        text = { Text(text = errorMessage) },
        confirmButton = {
            Button(
                onClick = { onRetryClick() },
                colors = ButtonDefaults.buttonColors(),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Try again")
            }
        }
    )
}

@Composable
@Preview
fun ErrorDialogPreview() {
    ErrorAlertDialog(
        errorMessage = "An error occurred!",
        onRetryClick = { }
    )
}