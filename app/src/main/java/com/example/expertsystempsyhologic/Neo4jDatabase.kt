package com.example.expertsystempsyhologic

import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Values
import org.neo4j.driver.exceptions.ClientException


class Neo4jDatabase {

    fun getQuestions(): List<String> {
        val questions = mutableListOf<String>()
        val thread = Thread {
            try {
                val driver = GraphDatabase.driver(
                    "neo4j+ssc://c64f3d12.databases.neo4j.io:7687",
                    AuthTokens.basic("neo4j", "GW4WwEHC2QgxD6AwMO0MzKoiV5At4wqnsIMYVGA2nVQ")
                )
                driver.use { driver ->
                    val session = driver.session()
                    val result = session.run("MATCH (q:Question) RETURN q.text AS questionText")

                    while (result.hasNext()) {
                        val record = result.next()
                        val questionText = record["questionText"].asString()
                        questions.add(questionText)
                    }
                    session.close()
                }
            } catch (e: ClientException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
        return questions
    }

    fun writeDiagnosis(diagnosis: String) {
        val thread = Thread {
            try {
                val driver = GraphDatabase.driver(
                    "neo4j+ssc://c64f3d12.databases.neo4j.io:7687",
                    AuthTokens.basic("neo4j", "GW4WwEHC2QgxD6AwMO0MzKoiV5At4wqnsIMYVGA2nVQ")
                )

                val session = driver.session()
                session.use {
                    it.writeTransaction { tx ->
                        val query = "CREATE (:Diagnosis {diagnosis: \$diagnosis, name: \$name, date: \$date})"
                        tx.run(query, mapOf("diagnosis" to diagnosis, "name" to Observer.name, "date" to Observer.date))
                    }
                }

                driver.close()
            } catch (e: ClientException) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
    }

}