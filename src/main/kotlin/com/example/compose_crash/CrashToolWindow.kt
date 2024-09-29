package com.example.compose_crash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.util.ui.components.BorderLayoutPanel

class CrashToolWindow : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.addComposePanel {
            MaterialTheme {
                Column(modifier = Modifier.height(IntrinsicSize.Max)) {
                    TextField(
                        value = "test",
                        onValueChange = { },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        }
    }
}

private fun ToolWindow.addComposePanel(
    displayName: String = "",
    isLockable: Boolean = true,
    content: @Composable ComposePanel.() -> Unit,
) = PluginWindow(content = content)
    .also { contentManager.addContent(contentManager.factory.createContent(it, displayName, isLockable)) }

class PluginWindow(
    height: Int = 800,
    width: Int = 800,
    y: Int = 0,
    x: Int = 0,
    content: @Composable ComposePanel.() -> Unit,
) : BorderLayoutPanel() {

    init {
        add(
            ComposePanel().apply {
                setBounds(x = x, y = y, width = width, height = height)
                setContent {
                    content()
                }
            },
        )
    }
}