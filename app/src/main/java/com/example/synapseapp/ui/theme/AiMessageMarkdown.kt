package com.example.synapseapp.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.rememberMarkdownState
import data.ChatMessageBox
@Composable
fun AiMessageMarkdown(box: ChatMessageBox){

    Markdown(
        modifier = Modifier.padding(8.dp),
        markdownState = rememberMarkdownState(
            content = box.text,
            retainState = true,
            immediate = false
        ),
        colors = markdownColor(
            text = if(box.errorFlag) Color.Red else MaterialTheme.colorScheme.onSurface,
            codeBackground = Color(0xFF2D2D2D),
            dividerColor = Color.Gray,
        ),
        typography = typography(),
        components = markdownComponents(
            codeBlock = { MarkdownHighlightedCodeBlock(it.content, it.node, showHeader = true) },
            codeFence = { MarkdownHighlightedCodeFence(it.content, it.node, showHeader = true) }
        )
    )
    //if (box.errorFlag) box.errorFlag = false
}

@Composable
fun typography() = markdownTypography(
    h1 = MaterialTheme.typography.headlineLarge.copy(
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        fontSize = MaterialTheme.typography.titleLarge.fontSize

    ),
    h2 = MaterialTheme.typography.headlineMedium.copy(
        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
        fontSize = MaterialTheme.typography.titleMedium.fontSize
    ),
    h3 = MaterialTheme.typography.headlineMedium.copy(
        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
        fontSize = MaterialTheme.typography.titleSmall.fontSize
    ),
    text = MaterialTheme.typography.bodyLarge.copy(
        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    ),
    inlineCode = MaterialTheme.typography.bodyMedium.copy(
        fontFamily = FontFamily.Monospace,
        color = Color.White,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    ),
    quote = MaterialTheme.typography.bodyMedium.copy(
        fontStyle = FontStyle.Italic,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    ),
    code = MaterialTheme.typography.bodyMedium.copy(
        fontFamily = FontFamily.Monospace,
        color = Color.White,
        fontSize = 14.sp
    )
)