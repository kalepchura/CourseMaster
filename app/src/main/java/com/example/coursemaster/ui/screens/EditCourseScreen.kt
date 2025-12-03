package com.example.coursemaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coursemaster.data.model.Course
import com.example.coursemaster.ui.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseScreen(
    courseId: String,
    onNavigateBack: () -> Unit,
    viewModel: CourseViewModel = viewModel()
) {
    var course by remember { mutableStateOf<Course?>(null) }
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Activo") }
    var expandedEstado by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val uiState by viewModel.uiState.collectAsState()

    // Cargar curso actual
    LaunchedEffect(courseId) {
        val currentCourse = uiState.courses.find { it.id == courseId }
        if (currentCourse != null) {
            course = currentCourse
            nombre = currentCourse.nombre
            categoria = currentCourse.categoria
            duracion = currentCourse.duracion
            estado = currentCourse.estado
            isLoading = false
        }
    }

    // Navegar de vuelta cuando se actualiza exitosamente
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            viewModel.clearMessages()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Curso") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Curso") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = duracion,
                    onValueChange = { duracion = it },
                    label = { Text("Duración") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = expandedEstado,
                    onExpandedChange = { expandedEstado = it }
                ) {
                    OutlinedTextField(
                        value = estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedEstado
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedEstado,
                        onDismissRequest = { expandedEstado = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Activo") },
                            onClick = {
                                estado = "Activo"
                                expandedEstado = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Inactivo") },
                            onClick = {
                                estado = "Inactivo"
                                expandedEstado = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        course?.let { currentCourse ->
                            val updatedCourse = currentCourse.copy(
                                nombre = nombre,
                                categoria = categoria,
                                duracion = duracion,
                                estado = estado
                            )
                            viewModel.updateCourse(updatedCourse)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading &&
                            nombre.isNotBlank() &&
                            categoria.isNotBlank() &&
                            duracion.isNotBlank()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Actualizar Curso")
                    }
                }
            }
        }
    }
}