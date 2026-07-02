package com.example.presentation.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DashboardState(
    val activeAgents: List<String> = listOf("CodeAssistant", "DataAnalyzer", "SystemMonitor"),
    val loadedModels: List<String> = listOf("Llama-3-8B", "Mistral-7B"),
    val installedPlugins: List<String> = listOf("Github Integration", "Jira Sync", "Local Linter")
)

class DashboardViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()
}
