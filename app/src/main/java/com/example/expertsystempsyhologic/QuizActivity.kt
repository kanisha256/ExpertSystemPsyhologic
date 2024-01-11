package com.example.expertsystempsyhologic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class QuizActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiseaseQuizApp()
        }
    }

    data class FuzzyVariable(val name: String, val membershipFunction: (Double) -> Double)

    val oneFuzzy = FuzzyVariable("Pain", { if (it > 0.5) 1.0 else 0.0 })
    val twoFuzzy = FuzzyVariable("Heart", { if (it > 0.5) 1.0 else 0.0 })
    val threeFuzzy = FuzzyVariable("Temperature", { if (it > 0.5) 1.0 else 0.0 })
    val fourFuzzy = FuzzyVariable("Cough", { if (it > 0.5) 1.0 else 0.0 })
    val fiveFuzzy = FuzzyVariable("Fatigue", { if (it > 0.5) 1.0 else 0.0 })
    val sixFuzzy = FuzzyVariable("Breathlessness", { if (it > 0.5) 1.0 else 0.0 })
    val sevenFuzzy = FuzzyVariable("Cough", { if (it > 0.5) 1.0 else 0.0 })
    val eightFuzzy = FuzzyVariable("Fatigue", { if (it > 0.5) 1.0 else 0.0 })
    val nineFuzzy = FuzzyVariable("Breathlessness", { if (it > 0.5) 1.0 else 0.0 })

    @Composable
    fun DiseaseQuizApp() {
        var currentQuestionIndex by remember { mutableStateOf(0) }
        var userAnswers by remember { mutableStateOf(mutableMapOf<Int, Double>()) }
        var showDiagnosisScreen by remember { mutableStateOf(false) }

        val neo4jDatabase = Neo4jDatabase()
        val questions = neo4jDatabase.getQuestions()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDiagnosisScreen) {
                DiagnosisScreen(getDiagnosis(userAnswers))
            } else {
                Text(text = questions[currentQuestionIndex], style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            updateUserAnswer(1.0, currentQuestionIndex, userAnswers)
                            nextQuestion(questions.size, currentQuestionIndex, userAnswers) {
                                currentQuestionIndex = it
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(text = "Да")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            updateUserAnswer(0.0, currentQuestionIndex, userAnswers)
                            nextQuestion(questions.size, currentQuestionIndex, userAnswers) {
                                currentQuestionIndex = it
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        Text(text = "Нет")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showDiagnosisScreen = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Получить диагноз")
                }
            }
        }
    }

    @Composable
    fun DiagnosisScreen(diagnosis: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Diagnosis:", style = MaterialTheme.typography.h6)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = diagnosis, style = MaterialTheme.typography.body1)
        }
    }

    fun updateUserAnswer(
        answerStrength: Double,
        questionIndex: Int,
        userAnswers: MutableMap<Int, Double>
    ) {
        userAnswers[questionIndex] = answerStrength
    }

    fun nextQuestion(
        totalQuestions: Int,
        currentQuestionIndex: Int,
        userAnswers: Map<Int, Double>,
        onNextQuestion: (Int) -> Unit
    ) {

        val oneStrength = userAnswers[0] ?: 0.0
        val twoStrength = userAnswers[1] ?: 0.0
        val threeStrength = userAnswers[2] ?: 0.0
        val fourStrength = userAnswers[3] ?: 0.0
        val fiveStrength = userAnswers[4] ?: 0.0
        val sixStrength = userAnswers[5] ?: 0.0
        val sevenStrength = userAnswers[6] ?: 0.0
        val eightStrength = userAnswers[7] ?: 0.0
        val nineStrength = userAnswers[8] ?: 0.0

        val isOne = oneStrength > 0.5
        val isTwo = twoStrength > 0.5
        val isThree = threeStrength > 0.5
        val isFour = fourStrength > 0.5
        val isFive = fiveStrength > 0.5
        val isSix =  sixStrength > 0.5
        val isSeven = sevenStrength > 0.5
        val isEight = eightStrength > 0.5
        val isNine = nineStrength > 0.5

        val nextIndex = currentQuestionIndex + 1

        if (nextIndex < totalQuestions) {
            onNextQuestion(nextIndex)
        } else {

        }
    }

    fun getDiagnosis(userAnswers: Map<Int, Double>): String {
        val oneStrength = userAnswers[0] ?: 0.0
        val twoStrength = userAnswers[1] ?: 0.0
        val threeStrength = userAnswers[2] ?: 0.0
        val fourStrength = userAnswers[3] ?: 0.0
        val fiveStrength = userAnswers[4] ?: 0.0
        val sixStrength = userAnswers[5] ?: 0.0
        val sevenStrength = userAnswers[6] ?: 0.0
        val eightStrength = userAnswers[7] ?: 0.0
        val nineStrength = userAnswers[8] ?: 0.0

        val isOne = oneFuzzy.membershipFunction(oneStrength) > 0.5
        val isTwo = twoFuzzy.membershipFunction(twoStrength) > 0.5
        val isThree = threeFuzzy.membershipFunction(threeStrength) > 0.5
        val isFour = fourFuzzy.membershipFunction(fourStrength) > 0.5
        val isFive = fiveFuzzy.membershipFunction(fiveStrength) > 0.5
        val isSix = sixFuzzy.membershipFunction(sixStrength) > 0.5
        val isSeven = sevenFuzzy.membershipFunction(sevenStrength) > 0.5
        val isEight = eightFuzzy.membershipFunction(eightStrength) > 0.5
        val isNine = nineFuzzy.membershipFunction(nineStrength) > 0.5
        val diagnosis = when {
            isOne && isTwo && isFour -> "Возможно, у вас проблемы с вниманием и сердечно-сосудистыми проблемами."
            isFive && isSix && isNine -> "Ваши симптомы могут быть связаны с тревожным расстройством и депрессией."
            isThree && isSeven && isEight -> "Усталость и трудности с концентрацией могут быть следствием физической и эмоциональной перегрузки."
            isTwo && isFour && isSix -> "Перепады настроения и беспокойство могут указывать на эмоциональные и психологические проблемы."
            isOne && isFive && isNine -> "Трудности с концентрацией и чувства вины могут быть признаками расстройств внимания и депрессии."
            isThree && isEight && isNine -> "Усталость, проблемы с отношениями и сомнения в принятии решений могут свидетельствовать об эмоциональном истощении."
            isFour && isSix && isSeven -> "Проблемы со сном, раздражительность и беспокойство могут быть связаны с соматоформными расстройствами."
            isOne && isTwo && isNine -> "Трудности с вниманием и сомнения в принятии решений могут свидетельствовать о расстройствах внимания и депрессии."
            isTwo && isFive && isSeven -> "Беспокойство, перепады настроения и проблемы в отношениях могут быть признаками тревожных и депрессивных состояний."
            isFour && isEight && isNine -> "Проблемы со сном, напряженность и сомнения в принятии решений могут быть признаками тревожных расстройств и депрессии."
            isOne && isThree && isSix -> "Трудности с концентрацией, физической усталости и перепады настроения могут быть признаками общего стресса."
            isTwo && isSeven && isEight -> "Беспокойство, напряженность и проблемы в отношениях могут свидетельствовать о психосоматических проблемах."
            isFive && isSix && isEight -> "Депрессия, беспокойство и трудности в отношениях могут указывать на комплексные эмоциональные проблемы."
            isOne && isFour && isNine -> "Трудности с вниманием, проблемы со сном и сомнения в принятии решений могут быть связаны с депрессией и тревогой."
            isThree && isSeven && isNine -> "Усталость, напряженность и сомнения в принятии решений могут быть признаками эмоционального истощения."
            isTwo && isFour && isEight -> "Эмоциональная нестабильность, беспокойство и проблемы в отношениях могут быть связаны с психосоматическими факторами."
            isOne && isFive && isSeven -> "Трудности с концентрацией, чувства вины и проблемы в отношениях могут быть связаны с депрессией и социальной тревожностью."
            isFour && isSix && isEight -> "Проблемы со сном, беспокойство и эмоциональная неустойчивость могут быть признаками тревожных и аффективных расстройств."
            isTwo && isThree && isNine -> "Беспокойство, физическая усталость и сомнения в принятии решений могут быть следствием эмоционального истощения."
            else -> "Нет конкретного диагноза на основе предоставленной информации."
        }
        val neo4jDatabase = Neo4jDatabase()
        neo4jDatabase.writeDiagnosis(diagnosis)
        return diagnosis
    }
}