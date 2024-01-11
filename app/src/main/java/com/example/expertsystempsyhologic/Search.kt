package com.example.expertsystempsyhologic

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import org.neo4j.driver.exceptions.ClientException

class Search : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Search()
        }
    }

    @Composable
    fun Search(){
        val onlineUsers = remember { mutableStateOf<List<String>>(emptyList()) }
        var searchText by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Поиск среди пользователей в сети") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { getUsersOnline(onlineUsers, searchText) }) {
                Text("Искать")
            }

            // Отобразите результаты поиска
            onlineUsers.value.forEach {
                Text(it)
            }

        }
    }

    fun getUsersOnline(onlineUsers: MutableState<List<String>>, searchText: String) {
        Thread {
            try {
                val driver = GraphDatabase.driver(
                    "neo4j+ssc://c64f3d12.databases.neo4j.io:7687",
                    AuthTokens.basic("neo4j", "GW4WwEHC2QgxD6AwMO0MzKoiV5At4wqnsIMYVGA2nVQ")
                )

                val session: Session = driver.session()

                val query = """
                MATCH (d:Diagnosis)
                WHERE d.name CONTAINS '$searchText'
                RETURN d.name AS userEmail, d.diagnosis AS diagnosis
                """

                val result = session.run(query)

                val usersList = mutableListOf<String>()

                while (result.hasNext()) {
                    val record = result.next()
                    val userEmail = record.get("userEmail").asString()
                    val diagnosis = record.get("diagnosis").asString()

                    // Добавьте результаты поиска в список
                    usersList.add("User Email: $userEmail, Diagnosis: $diagnosis")
                }

                // Обновите состояние с результатами поиска
                onlineUsers.value = usersList

                session.close()
                driver.close()

            } catch (e: ClientException) {
                // Обработка ошибок
            }
        }.start()
    }


}
