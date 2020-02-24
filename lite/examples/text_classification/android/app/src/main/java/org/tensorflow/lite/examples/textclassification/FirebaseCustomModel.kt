package org.tensorflow.lite.examples.textclassification

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.logging.Logger

class FirebaseCustomModel {


    var firebaseModelInterpreter: FirebaseModelInterpreter? = null

    companion object {
        private val TAG = "TextClassificationDemo"
    }


    fun downloadFirebaseCustomModel(){
        val remoteModel = FirebaseCustomRemoteModel.Builder("Profanity-Classifier").build()
        val conditions = FirebaseModelDownloadConditions.Builder()
//            .requireWifi()
            .build()

        FirebaseModelManager.getInstance().download(remoteModel, conditions)
            .addOnCompleteListener {
                Log.d(TAG, "Firebase Custom Model Downloaded")
            }.addOnFailureListener {
            }


        val firebaseModelManager = FirebaseModelManager.getInstance()

        firebaseModelManager.isModelDownloaded(remoteModel)
            .continueWithTask { task ->
                val conditions = if (task.result != null && task.result == true) {
                    FirebaseModelDownloadConditions.Builder()
                        .requireWifi()
                        .build() // Update condition that requires wifi.
                } else {
                    FirebaseModelDownloadConditions.Builder().build(); // Download condition.
                }
                firebaseModelManager.download(remoteModel, conditions)
            }
    }

    fun loadFirebaseCustomModel(){
        val remoteModel = FirebaseCustomRemoteModel.Builder("Profanity-Classifier").build()

        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
            .addOnSuccessListener { isDownloaded ->
                if(isDownloaded){
                    val options = FirebaseModelInterpreterOptions.Builder(remoteModel).build()
                    firebaseModelInterpreter = FirebaseModelInterpreter.getInstance(options)
                }else {
                    downloadFirebaseCustomModel()
                }
            }
    }


    /** Classify an input string and returns the classification results.  */
    @WorkerThread
    fun runInferenceOnFirebaseCustomModel(text: String) {
        // Pre-prosessing.
        val inputOutputOptions = createInputOutputOptions()

//        val inputs = FirebaseModelInputs.Builder()
//            .add(input) // add() as many input arrays as your model requires
//            .build()
//
//        firebaseModelInterpreter?.run(inputs, inputOutputOptions)
//            ?.addOnSuccessListener { result ->
//                // [START_EXCLUDE]
//                // [START mlkit_read_result]
//                val output = result.getOutput<Array<FloatArray>>(0)
//                val probabilities = output[0]
//                // [END mlkit_read_result]
//                useInferenceResult(probabilities)
//            }
//            ?.addOnFailureListener { e ->
//                // Task failed with an exception
//                // ...
//                Log.e(TAG, e)
//            }

    }

    private fun createInputOutputOptions(): FirebaseModelInputOutputOptions {
        // [START mlkit_create_io_options]
        val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 256))
            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 2))
            .build()
        // [END mlkit_create_io_options]
        return inputOutputOptions
    }

    private fun useInferenceResult(probabilities: FloatArray) {
        // [START mlkit_use_inference_result]
//        val reader = BufferedReader(
//            InputStreamReader(this.context.assets.open("labels.txt")))
//        for (i in probabilities.indices) {
//            val label = reader.readLine()
//            Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]))
//        }
        // [END mlkit_use_inference_result]
    }
}