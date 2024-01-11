package com.example.expertsystempsyhologic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.exceptions.ClientException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }


    @Composable
    fun LoginScreen() {
        var fullName by remember { mutableStateOf("") }
        var dateOfBirth by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("ФИО") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            TextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Дата рождения") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    saveDataToNeo4j(fullName, dateOfBirth)
                    Observer.name = fullName
                    Observer.date = dateOfBirth
                    val intent = Intent(this@MainActivity, QuizActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Войти")
            }
            Button(
                onClick = {
                    val intent = Intent(this@MainActivity, Search::class.java)
                    startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Результаты")
            }
        }
    }

    private fun saveDataToNeo4j(fullName: String, dateOfBirth: String) {
        val thread = Thread {
            try {
                val neo4jUrl = "neo4j+ssc://c64f3d12.databases.neo4j.io:7687"
                val neo4jUsername = "neo4j"
                val neo4jPassword = "GW4WwEHC2QgxD6AwMO0MzKoiV5At4wqnsIMYVGA2nVQ"

                val driver =
                    GraphDatabase.driver(neo4jUrl, AuthTokens.basic(neo4jUsername, neo4jPassword))

                val session = driver.session()

                try {
                    val result = session.writeTransaction { transaction ->
                        val createNodeQuery = """
                CREATE (user:User {fullName: $fullName, dateOfBirth: $dateOfBirth})
                RETURN user
            """.trimIndent()

                        transaction.run(createNodeQuery)
                    }

                } finally {
                    session.close()
                    driver.close()
                }
            } catch (e: ClientException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
    }
}
