package com.example.testqualtrics

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.testqualtrics.databinding.ActivityMainBinding
import com.qualtrics.digital.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val BranchId = "vf"
        const val ProjectId = "ZN_6hyerIjfAjcOOp0"
        const val InterceptId = "SI_7XaiurZR0BGK58i"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.btnShowSurvey.setOnClickListener {
            Qualtrics.instance().evaluateProject(MyCallback(this))
        }

        // Add line for init Qualtrics
        Qualtrics.instance().initializeProject(BranchId, ProjectId, InterceptId, this);
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    // Show Qualtrics survey
    class MyCallback(private val context: Context) : IQualtricsProjectEvaluationCallback {
        override fun run(targetingResults: MutableMap<String, TargetingResult>) {
            for(result in targetingResults.entries){
                if(result.value.passed()){
                    Qualtrics.instance().displayIntercept(context, result.key)
                }
            }
        }
    }
}