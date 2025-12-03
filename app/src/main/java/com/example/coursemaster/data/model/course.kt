package com.example.coursemaster.data.model

data class Course(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val duracion: String = "",
    val estado: String = ""

    constructor() : this("", "", "", "", "")

    // Convertir a Map para Firestore
    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "duracion" to duracion,
            "estado" to estado
        )
    }
}

// Si prefieres Product en lugar de Course:
/*
data class Product(
    val id: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val stock: Int = 0,
    val categoria: String = ""
) {
    constructor() : this("", "", 0.0, 0, "")

    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "precio" to precio,
            "stock" to stock,
            "categoria" to categoria
        )
    }
}
*/