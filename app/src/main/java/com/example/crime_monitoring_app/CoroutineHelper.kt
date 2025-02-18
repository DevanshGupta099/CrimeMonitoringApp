package com.example.crime_monitoring_app

// Import necessary classes for coroutines and dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Define an object called CoroutineHelper
object CoroutineHelper {
    // Function to launch a coroutine with a suspendable lambda block
    fun launchCoroutine(block: suspend () -> Unit) {
        // Create a CoroutineScope using the IO dispatcher and launch the coroutine
        CoroutineScope(Dispatchers.IO).launch {
            block() // Execute the suspendable block of code
        }
    }
}
