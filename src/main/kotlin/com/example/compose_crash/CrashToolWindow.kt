package com.example.compose_crash

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.util.ui.components.BorderLayoutPanel

data class BatchIcon(val name: String)

class CrashToolWindow : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        System.setProperty("compose.swing.render.on.graphics", "true")

        toolWindow.addComposePanel {
            MaterialTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    val icons = remember { (0..100).map { BatchIcon(name = "Icon $it") } }

                    Box(modifier = Modifier.fillMaxSize()) {
                        val state = rememberLazyGridState()

                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            state = state,
                            columns = GridCells.Adaptive(200.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(items = icons, key = { it.name }) { batchIcon ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem()
                                ) {
                                    Text(text = batchIcon.name, modifier = Modifier.padding(16.dp))
                                }
                            }
                        }

                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(state),
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd)
                        )
                    }
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