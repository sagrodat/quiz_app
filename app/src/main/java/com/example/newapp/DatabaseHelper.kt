// QuizDatabaseHelper.kt
package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.newapp.Quiz

class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Define constants for database name and version
    companion object {
        const val DATABASE_NAME = "quizDatabase.db"
        const val DATABASE_VERSION = 1
    }

    // This method is called to create the database and set up the tables
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_QUERY = """
            CREATE TABLE quiz (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question TEXT NOT NULL,
                answer TEXT NOT NULL
            )
        """
        db.execSQL(CREATE_TABLE_QUERY)  // Execute the query to create the table
    }

    // Insert a new quiz question into the database
    fun insertQuizQuestion(question: String, answer: String): Long {
        val db = this.writableDatabase  // Open database in writable mode
        val values = ContentValues()
        values.put("question", question)
        values.put("answer", answer)

        // Insert the new record and return the ID of the inserted row
        val id = db.insert("quiz", null, values)
        db.close()  // Don't forget to close the database after operation
        return id
    }

    // Update an existing quiz question by ID
    fun updateQuizQuestion(id: Int, newQuestion: String, newAnswer: String): Int {
        val db = this.writableDatabase  // Open database in writable mode
        val values = ContentValues()
        values.put("question", newQuestion)
        values.put("answer", newAnswer)

        // Update the record where the id matches and return the number of rows affected
        val rowsUpdated = db.update(
            "quiz",
            values,
            "id = ?",
            arrayOf(id.toString())  // Use the ID parameter to identify the record
        )
        db.close()  // Close the database after operation
        return rowsUpdated  // Returns the number of rows updated (should be 1 or 0)
    }

    // This method is called when the database needs to be upgraded (e.g., if schema changes)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // For simplicity, we drop the table and recreate it (in real apps, use proper migration strategy)
        db.execSQL("DROP TABLE IF EXISTS quiz")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllQuizQuestions(): List<Quiz> {
        val db = this.readableDatabase  // Open the database in readable mode
        val cursor = db.query(
            "quiz",  // The table name
            arrayOf("id", "question", "answer"),  // Columns to fetch
            null,  // No WHERE clause (fetch all rows)
            null,  // No WHERE arguments
            null,  // No GROUP BY
            null,  // No HAVING
            null   // No ORDER BY
        )

        val quizList = mutableListOf<Quiz>()  // A list to hold the quiz questions

        // Loop through the cursor and convert each row to a Quiz object
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val question = cursor.getString(cursor.getColumnIndex("question"))
            val answer = cursor.getString(cursor.getColumnIndex("answer"))

            // Create a Quiz object and add it to the list
            val quiz = Quiz(id, question, answer)
            quizList.add(quiz)
        }

        cursor.close()  // Close the cursor after we're done with it
        db.close()  // Close the database connection
        return quizList  // Return the list of quiz questions
    }

    fun deleteAllQuizQuestions() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM quiz")  // Deletes all rows from the 'quiz' table
        db.close()
    }

    fun insertQuizQuestions(quizQuestions: List<Quiz>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues()
            for (quiz in quizQuestions) {
                values.put("question", quiz.question)
                values.put("answer", quiz.answer)
                db.insert("quiz", null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }


}
