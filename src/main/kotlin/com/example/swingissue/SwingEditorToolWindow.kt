package com.example.swingissue

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.util.ui.components.BorderLayoutPanel

val LocalProject = staticCompositionLocalOf<Project> { error("LocalProject not provided") }

class SwingEditorToolWindow : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        System.setProperty("compose.swing.render.on.graphics", "true")
        System.setProperty("compose.interop.blending", "true")

        toolWindow.addComposePanel {
            MaterialTheme {
                CompositionLocalProvider(LocalProject provides project) {
                    IntellijEditorTextField(
                        modifier = Modifier.fillMaxSize(),
                        text = SAMPLE_CODE_BLOCK)
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