@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.newapp
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.newapp.ui.theme.NewAppTheme
import com.example.quizapp.QuizDatabaseHelper
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.annotation.OptIn
import androidx.compose.material3.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.media3.common.util.UnstableApi
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.AttachFile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dbHelper = QuizDatabaseHelper(this)

        // Use lifecycleScope to run database query asynchronously
        dbHelper.deleteAllQuizQuestions()
        if (dbHelper.getAllQuizQuestions().size == 0)
        {
            dbHelper.insertQuizQuestion("What's the capital of Poland?", "Warsaw")
            dbHelper.insertQuizQuestion("What's the capital of France?", "Paris")
            dbHelper.insertQuizQuestion("What's the capital of Spain?", "Madrid")
        }



        setContent {
            var quizQuestions by remember { mutableStateOf<List<Quiz>?>(null) }  // Store quizQuestions in a state variable
            var currentIndex by remember { mutableStateOf(0) }
            var showAnswer by remember { mutableStateOf(false) }
            var selectedFileUri by remember { mutableStateOf<Uri?>(null) }  // For storing the selected file URI


            val openFileLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = { uri: Uri? ->
                    selectedFileUri = uri
                }
            )

            // Use lifecycleScope to run the database query asynchronously
            LaunchedEffect(Unit) {
                quizQuestions = dbHelper.getAllQuizQuestions()
            }

            NewAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Quiz App") },
                            actions = {
                                IconButton(onClick = {
                                    // Trigger the file picker when the icon is clicked
                                    openFileLauncher.launch(arrayOf("*/*"))  // You can restrict the file type if needed
                                }) {
                                    Icon(Icons.Filled.AttachFile, contentDescription = "Load File")
                                }
                            }
                        )
                    },
                    content = { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {  // Apply innerPadding here
                            // Center the content vertically and horizontally
                            Box(
                                modifier = Modifier
                                    .weight(1f) // Ensures the Box takes up remaining space
                                    .fillMaxWidth()  // Ensure Box does not exceed screen width
                                    .padding(innerPadding),  // Apply innerPadding to Box
                                contentAlignment = Alignment.Center // Center the content
                            ) {
                                quizQuestions?.let {
                                    val quiz = it.getOrNull(currentIndex)
                                    quiz?.let {
                                        Box(
                                            modifier = Modifier
                                                .clickable(
                                                    onClick = { showAnswer = !showAnswer },
                                                    indication = null,  // Disable ripple effect
                                                    interactionSource = remember { MutableInteractionSource() } // Remove ripple effect
                                                )
                                                .padding(8.dp)
                                        ) {
                                            if (showAnswer) {
                                                // Show answer
                                                Text(
                                                    text = "A: ${quiz.answer}",
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            } else {
                                                // Show question
                                                Text(
                                                    text = "Q: ${quiz.question}",
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        }

                                    }
                                }
                            }

                            // Add Spacer to ensure space between content and buttons
                            Spacer(modifier = Modifier.weight(0.1f))

                            // Row for the buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(innerPadding),  // Apply innerPadding to Row
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Left Button (ArrowBack)
                                IconButton(
                                    onClick = {
                                        if (currentIndex > 0) {
                                            currentIndex -= 1
                                            showAnswer = false
                                        }
                                    }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                                }

                                // Right Button (ArrowForward)
                                IconButton(
                                    onClick = {
                                        quizQuestions?.let {
                                            if (currentIndex < it.size - 1) {
                                                currentIndex += 1  // Go to the next question
                                                showAnswer = false
                                            }

                                        }
                                    }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                                }
                            }
                        }
                        selectedFileUri?.let {
                            Text(text = "Selected file: $it")
                        }

                    }
                    )
                }


            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
