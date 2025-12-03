package com.example.coursemaster.data.repository

import com.example.coursemaster.data.model.Course
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CourseRepository {
    private val db = FirebaseFirestore.getInstance()
    private val coursesCollection = db.collection("courses")

    suspend fun createCourse(course: Course): Result<String> {
        return try {
            val docRef = coursesCollection.document()
            val newCourse = course.copy(id = docRef.id)
            docRef.set(newCourse.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCourses(): Flow<List<Course>> = callbackFlow {
        val listener = coursesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val courses = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Course::class.java)?.copy(id = doc.id)
            } ?: emptyList()

            trySend(courses)
        }

        awaitClose { listener.remove() }
    }

    suspend fun updateCourse(course: Course): Result<Unit> {
        return try {
            coursesCollection.document(course.id)
                .update(course.toMap())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCourse(courseId: String): Result<Unit> {
        return try {
            coursesCollection.document(courseId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}