import android.content.Context
import android.net.Uri
import com.example.newapp.Quiz
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import android.util.Log



data class QuizList(
val quiz: List<Quiz>
)

fun parseJsonToQuizList(json: String): List<Quiz> {
    val gson = Gson()
    val quizListType = object : TypeToken<QuizList>() {}.type
    val quizList = gson.fromJson<QuizList>(json, quizListType)
    return quizList.quiz
}

fun readJsonFromUri(context: Context, uri: Uri?): String? {
    if (uri == null)
    {
        Log.e("FileReader", "Error reading file from URI, URI IS NULL")
        return null
    }

    var jsonString: String? = null
    try {
        // Open an input stream from the URI
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

        // Read the input stream into a string
        inputStream?.use {
            val reader = InputStreamReader(it, StandardCharsets.UTF_8)
            val jsonBuilder = StringBuilder()
            var charRead: Int
            while (reader.read().also { charRead = it } != -1) {
                jsonBuilder.append(charRead.toChar())
            }
            jsonString = jsonBuilder.toString()
        }
    } catch (e: Exception) {
        Log.e("FileReader", "Error reading file from URI", e)
    }
    return jsonString
}