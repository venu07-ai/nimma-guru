package com.example.nimma_guru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nimma_guru.ui.NimmaGuruApp
import com.example.nimma_guru.ui.theme.NimmaguruTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NimmaguruTheme {
                NimmaGuruApp()
            }
        }
    }
}
