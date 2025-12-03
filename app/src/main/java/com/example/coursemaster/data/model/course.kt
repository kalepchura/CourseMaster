package com.example.coursemaster.data.model

import com.google.firebase.auth.FirebaseAuth

data class Course(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val duracion: String = "",
    val estado: String = "",
    val userId: String = ""
) {
    constructor() :
            this("", "",
                "", "",
                "", "")
    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "duracion" to duracion,
            "estado" to estado,
            "userId" to userId
        )
    }
}