package com.aistudio.ultimate.plugins

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PluginRegistry {
    private val _plugins = MutableStateFlow<List<Plugin>>(emptyList())
    val plugins: StateFlow<List<Plugin>> = _plugins.asStateFlow()
    
    fun registerPlugin(plugin: Plugin) {
        _plugins.update { current ->
            if (current.any { it.id == plugin.id }) {
                current // Ignore duplicates
            } else {
                plugin.initialize()
                plugin.start()
                current + plugin
            }
        }
    }
    
    fun unregisterPlugin(pluginId: String) {
        _plugins.update { current ->
            val pluginToRemove = current.find { it.id == pluginId }
            pluginToRemove?.stop()
            current.filter { it.id != pluginId }
        }
    }
    
    fun getPlugin(pluginId: String): Plugin? {
        return _plugins.value.find { it.id == pluginId }
    }
}
