package com.aistudio.ultimate.agents

import com.aistudio.ultimate.domain.Agent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AgentMessage(
    val senderId: String,
    val receiverId: String?,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

class AgentOrchestrator {
    private val _agents = MutableStateFlow<List<Agent>>(emptyList())
    val agents: StateFlow<List<Agent>> = _agents.asStateFlow()

    private val _messages = MutableSharedFlow<AgentMessage>(extraBufferCapacity = 64)
    val messages: SharedFlow<AgentMessage> = _messages.asSharedFlow()
    
    private val _sharedState = MutableStateFlow<Map<String, Any>>(emptyMap())
    val sharedState: StateFlow<Map<String, Any>> = _sharedState.asStateFlow()

    fun registerAgent(agent: Agent) {
        _agents.update { current ->
            if (current.any { it.id == agent.id }) current else current + agent
        }
    }

    fun unregisterAgent(agentId: String) {
        _agents.update { current -> current.filter { it.id != agentId } }
    }

    fun dispatchMessage(message: AgentMessage) {
        _messages.tryEmit(message)
    }

    fun updateSharedState(key: String, value: Any) {
        _sharedState.update { current ->
            current + (key to value)
        }
    }

    suspend fun executeTaskWithCoordination(initialTask: String, primaryAgentId: String): String {
        val primaryAgent = _agents.value.find { it.id == primaryAgentId }
            ?: throw IllegalArgumentException("Primary agent not found")
        
        // Simulating agent orchestration
        dispatchMessage(AgentMessage("orchestrator", primaryAgentId, "Start task: \$initialTask"))
        val result = primaryAgent.executeTask(initialTask)
        dispatchMessage(AgentMessage(primaryAgentId, "orchestrator", "Task complete: \$result"))
        
        return result
    }
}
