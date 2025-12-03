package com.example.coursemaster.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coursemaster.ui.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object CourseList : Screen("course_list")
    object CreateCourse : Screen("create_course")
    object EditCourse : Screen("editCurso/{courseId}") {
        fun createRoute(courseId: String) = "editCurso/$courseId"
    }
}

@Composable
fun AppNavigation(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.CourseList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.CourseList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.CourseList.route) {
            CourseListScreen(
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateCourse.route)
                },
                onNavigateToEdit = { courseId ->
                    navController.navigate(Screen.EditCourse.createRoute(courseId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.CreateCourse.route) {
            CreateCourseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.EditCourse.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            EditCourseScreen(
                courseId = courseId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}