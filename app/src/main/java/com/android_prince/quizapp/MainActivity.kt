package com.android_prince.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.android_prince.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.etNoOfQuestion.setText("10")

        //for category spinner
        val categoryAdapter = ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.spinner_category,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.spinnerSelectCategory.adapter = categoryAdapter

        //for difficulty spinner
        val difficultyAdapter = ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.spinner_difficulty,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.spinnerSelectDifficulty.adapter = difficultyAdapter

        //for type spinner
        val typeAdapter = ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.spinner_type, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.spinnerSelectType.adapter = typeAdapter
        binding!!.spinnerSelectType.setSelection(1)

        //button
        binding!!.btnStartQuiz.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, QuizActivity::class.java).putExtra(
                    "quiz_api",
                    "https://opentdb.com/api.php?amount=" + binding!!.etNoOfQuestion.text.toString() + "&category=" + selectedCategory(
                        selectedCategory(
                            binding!!.spinnerSelectCategory.selectedItem.toString()
                        )!!
                    ).toString() + "&difficulty=" + selectedDifficulty(binding!!.spinnerSelectDifficulty.selectedItem.toString()).toString() + "&type=multiple"
                )
            )
            finish()
        }

    }
    private fun selectedCategory(categoryCode: String): String? {
        var categoryCode: String? = categoryCode
        when (categoryCode) {
            "Any Category" -> categoryCode = ""
            "General Knowledge" -> categoryCode = "9"
            "Entertainment: Books" -> categoryCode = "10"
            "Entertainment: Film" -> categoryCode = "11"
            "Entertainment: Music" -> categoryCode = "12"
            "Entertainment: Musicals & Theatres" -> categoryCode = "13"
            "Entertainment: Television" -> categoryCode = "14"
            "Entertainment: Video Games" -> categoryCode = "15"
            "Entertainment: Board Games" -> categoryCode = "16"
            "Science & Nature" -> categoryCode = "17"
            "Science: Computers" -> categoryCode = "18"
            "Science: Mathematics" -> categoryCode = "19"
            "Mythology" -> categoryCode = "20"
            "Sports" -> categoryCode = "21"
            "Geography" -> categoryCode = "22"
            "History" -> categoryCode = "23"
            "Politics" -> categoryCode = "24"
            "Art" -> categoryCode = "25"
            "Celebrities" -> categoryCode = "26"
            "Animals" -> categoryCode = "27"
            "Vehicles" -> categoryCode = "28"
            "Entertainment: Comics" -> categoryCode = "29"
            "Science: Gadgets" -> categoryCode = "30"
            "Entertainment: Japanese Anime & Manga" -> categoryCode = "31"
            "Entertainment: Cartoon & Animations" -> categoryCode = "32"
        }
        return categoryCode
    }

    private fun selectedDifficulty(difficultyCode: String): String? {
        var difficultyCode: String? = difficultyCode
        when (difficultyCode) {
            "Any Difficulty" -> difficultyCode = ""
            "Easy" -> difficultyCode = "easy"
            "Medium" -> difficultyCode = "medium"
            "Hard" -> difficultyCode = "hard"
        }
        return difficultyCode
    }

    private fun selectedType(typeCode: String): String? {
        var typeCode: String? = typeCode
        when (typeCode) {
            "Any Type" -> typeCode = ""
            "Multiple Choice" -> typeCode = "multiple"
            "True / False" -> typeCode = "boolean"
        }
        return typeCode
    }
}