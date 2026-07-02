package com.aistudio.ultimate.plugins

interface Plugin {
    val id: String
    val name: String
    val description: String
    val version: String
    
    fun initialize()
    fun start()
    fun stop()
}
