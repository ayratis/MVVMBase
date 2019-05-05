package com.ayratis.abstractapp.arch

class Action (private val action: () -> Unit) {
    fun call() { action() }
}
