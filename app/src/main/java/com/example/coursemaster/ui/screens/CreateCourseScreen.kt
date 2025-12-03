package com.example.coursemaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coursemaster.ui.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCourseScreen(
    onNavigateBack: () -> Unit,
    viewModel: CourseViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Activo") }
    var expandedEstado by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Navegar de vuelta cuando se crea exitosamente
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            viewModel.clearMessages()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Curso") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mostrar error si existe
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Curso") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // Campo Categoría
            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("ej: Programación, Marketing") },
                singleLine = true
            )

            // Campo Duración
            OutlinedTextField(
                value = duracion,
                onValueChange = { duracion = it },
                label = { Text("Duración") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("ej: 40 horas, 3 meses") },
                singleLine = true
            )

            // Dropdown Estado
            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = { expandedEstado = it }
            ) {
                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
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

            // Botón Guardar
            Button(
                onClick = {
                    if (nombre.isNotBlank() && categoria.isNotBlank() &&
                        duracion.isNotBlank()) {
                        viewModel.createCourse(nombre, categoria, duracion, estado)
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
                    Text("Guardar Curso")
                }
            }
        }
    }
}