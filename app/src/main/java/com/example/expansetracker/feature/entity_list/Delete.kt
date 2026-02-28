package com.example.expansetracker.feature.entity_list



import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expansetracker.ui.theme.Zinca


@Composable
fun DeleteConfirmationDialog(
    listTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Delete : $listTitle") },
            text = { Text(text = "Are you sure you want to delete this list?") },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismiss()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Zinca,
                        contentColor = Color.White
                    ),
                    ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Zinca,
                        contentColor = Color.White
                    ),
                    ) {
                    Text("Cancel")
                }
            }
        )
    }
}