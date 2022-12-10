package com.android_prince.quizapp

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android_prince.quizapp.databinding.ActivityQuizBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {
    var binding: ActivityQuizBinding? = null
    private lateinit var quizQueAnsModels: List<QuizQueAnsModel>
    private var progressDialog: ProgressDialog? = null
    var quesLength = 0
    var quesNoStatus = 0
    var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        progressDailog()
        quizQueAnsModels = ArrayList()
        (quizQueAnsModels as ArrayList<QuizQueAnsModel>).clear()
        fetchData(intent.getStringExtra("quiz_api")!!)
        binding!!.btnOption1.setOnClickListener { setQuizQues(++quesNoStatus, 1) }

        binding!!.btnOption2.setOnClickListener { setQuizQues(++quesNoStatus, 2) }

        binding!!.btnOption3.setOnClickListener { setQuizQues(++quesNoStatus, 3) }

        binding!!.btnOption4.setOnClickListener { setQuizQues(++quesNoStatus, 4) }
        binding!!.btnSubmit.setOnClickListener { alertDailog() }

    }
    private fun fetchData(url: String) {
        progressDialog!!.show()
        val request = StringRequest(
            Request.Method.GET, url,
            { response: String? ->
                try {
                    val results = JSONObject(response)
                    val result = results.getString("results")
                    val jsonArray = JSONArray(result)
                    quesLength = jsonArray.length()
                    Log.e("response", response!!)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val inn_corr = jsonObject.getString("incorrect_answers")
                        val jsonArray1 = JSONArray(inn_corr)
                        val quizQueAnsModel = QuizQueAnsModel(
                            jsonObject.getString("question"),
                            jsonArray1[0].toString(),
                            jsonArray1[1].toString(),
                            jsonArray1[2].toString(),
                            jsonObject.getString("correct_answer"),
                            jsonObject.getString("correct_answer")
                        )
                       quizQueAnsModels = quizQueAnsModels+quizQueAnsModel
                    }
                    binding!!.totalQuestion.text = "Total Questions : " + jsonArray.length()
                    setQuizQues(quesNoStatus)
                    progressDialog!!.dismiss()
                } catch (e: JSONException) {
                    Toast.makeText(this@QuizActivity, e.toString(), Toast.LENGTH_LONG).show()
                    progressDialog!!.dismiss()
                }
            }) { error: VolleyError ->
            progressDialog!!.dismiss()
            Toast.makeText(this@QuizActivity, error.toString(), Toast.LENGTH_LONG).show()
        }
        request.retryPolicy = DefaultRetryPolicy(
            1000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
    private fun progressDailog() {
        progressDialog = ProgressDialog(this@QuizActivity)
        progressDialog!!.setContentView(R.layout.loading)
        progressDialog!!.setTitle("Please Wait...")
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Searching")
    }
    private fun alertDailog() {
        val alertDialog = AlertDialog.Builder(this@QuizActivity).create()
        alertDialog.setTitle("Report Card")
        alertDialog.setMessage("Your Quiz Score is " + score)
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            "Exit"
        ) { dialog: DialogInterface?, which: Int ->
            finish()
            quesNoStatus = 0
            score = 0
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    private fun setQuizQues(index: Int) {
        binding!!.btnOption1.setText(quizQueAnsModels.get(index).option_1)
        binding!!.btnOption2.setText(quizQueAnsModels.get(index).option_2)
        binding!!.btnOption3.setText(quizQueAnsModels.get(index).option_3)
        binding!!.btnOption4.setText(quizQueAnsModels.get(index).option_4)
        binding!!.txtQuestion.text =
            "Ques" + (index + 1) + " " + quizQueAnsModels.get(index).question
    }
    private fun setQuizQues(index: Int, option: Int) {
        if (index <= quesLength - 1) {
            binding!!.btnOption1.setText(quizQueAnsModels.get(index).option_1)
            binding!!.btnOption2.setText(quizQueAnsModels.get(index).option_2)
            binding!!.btnOption3.setText(quizQueAnsModels.get(index).option_3)
            binding!!.btnOption4.setText(quizQueAnsModels.get(index).option_4)
            binding!!.txtQuestion.text =
                "Ques" + (index + 1) + " " + quizQueAnsModels.get(index).question
        }
        if (index <= quesLength) {
            when (option) {
                1 -> if (quizQueAnsModels.get(index - 1)
                        .answer == quizQueAnsModels.get(index - 1).option_1
                ) ++score
                2 -> if (quizQueAnsModels.get(index - 1)
                        .answer == quizQueAnsModels.get(index - 1).option_2
                ) ++score
                3 -> if (quizQueAnsModels.get(index - 1)
                        .answer == quizQueAnsModels.get(index - 1).option_3
                ) ++score
                4 -> if (quizQueAnsModels.get(index - 1)
                        .answer == quizQueAnsModels.get(index - 1).option_4
                ) ++score
            }
        }
    }
}