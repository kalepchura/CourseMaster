package com.example.coursemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.coursemaster.navigation.AppNavigation
import com.example.coursemaster.navigation.Screen
import com.example.coursemaster.ui.theme.CourseMasterTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseMasterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val startDestination = if (currentUser != null) {
                        Screen.CourseList.route
                    } else {
                        Screen.Login.route
                    }
                    AppNavigation(startDestination = startDestination)
                }
            }
        }
    }
}