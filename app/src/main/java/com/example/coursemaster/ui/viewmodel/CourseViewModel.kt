package com.example.coursemaster.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursemaster.data.model.Course
import com.example.coursemaster.data.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CourseUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class CourseViewModel : ViewModel() {
    private val repository = CourseRepository()

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    init {
        loadCourses()
    }

    // Cargar cursos en tiempo real
    private fun loadCourses() {
        viewModelScope.launch {
            repository.getCourses().collect { courses ->
                _uiState.value = _uiState.value.copy(
                    courses = courses,
                    isLoading = false
                )
            }
        }
    }

    // Crear curso
    fun createCourse(
        nombre: String,
        categoria: String,
        duracion: String,
        estado: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val course = Course(
                nombre = nombre,
                categoria = categoria,
                duracion = duracion,
                estado = estado
            )

            val result = repository.createCourse(course)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Curso creado exitosamente",
                    error = null
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message,
                    successMessage = null
                )
            }
        }
    }

    // Actualizar curso
    fun updateCourse(course: Course) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.updateCourse(course)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Curso actualizado",
                    error = null
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message,
                    successMessage = null
                )
            }
        }
    }

    // Eliminar curso
    fun deleteCourse(courseId: String) {
        viewModelScope.launch {
            val result = repository.deleteCourse(courseId)
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al eliminar: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    // Limpiar mensajes
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null
        )
    }
}