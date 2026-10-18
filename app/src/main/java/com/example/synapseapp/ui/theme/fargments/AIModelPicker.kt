package com.example.synapseapp.ui.theme.fargments

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun AIModelPicker(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onItemClick: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        offset = DpOffset(
            x = (-10).dp,
            y = 15.dp
        ),
        shape = RoundedCornerShape(25.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(300.dp).height(600.dp)
    ) {

    }
}