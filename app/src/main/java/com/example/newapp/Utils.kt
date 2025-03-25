//package com.example.newapp
//
//import android.content.Context
//import android.net.Uri
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//import java.io.InputStreamReader
//import com.example.newapp.Quiz
//import com.example.quizapp.QuizDatabaseHelper
//import com.squareup.moshi.JsonAdapter
//import okio.Okio
//import okio.buffer
//import okio.source
//import java.io.InputStream
//import kotlin.collections.emptyList
//
//suspend fun loadJsonFile(context: Context, uri: Uri) {
//    context.contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
//        // Użyj Okio do stworzenia BufferedSource z InputStream
//        val bufferedSource = inputStream.source().buffer()
//
//        // Tworzymy Moshi i adapter
//
//        // Parsowanie JSON do listy Quiz
//
//        val moshi: Moshi = Moshi.Builder().build()
//
//        val jsonAdapter: JsonAdapter<Quiz> = moshi.adapter<Quiz>()
//
//        // Wstaw dane do bazy
//        val dbHelper = QuizDatabaseHelper(context)
//        dbHelper.insertQuizQuestions(quizQuestions)
//    }
//}
//
