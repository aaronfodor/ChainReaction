package com.aaronfodor.android.chain_reaction

import android.content.res.AssetManager
import android.os.SystemClock
import android.os.Trace
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object TFLiteService{

    // the inference model
    var model: Interpreter? = null
    // GPU delegate to run the model on device GPU
    var gpuDelegate: GpuDelegate? = null

    val BASE_PATH = "/"
    val MODEL_PATH = "model.tflite"
    // implementation-defined and platform-dependent
    val NUM_THREADS = -1

    // enable logging for debugging purposes
    var enableLogging = true

    // contains the recognized text sequence - array of shape [DIM_BATCH_SIZE, MAX_RESULTS, NUM_CHARACTERS]
    var output = Array(1) { FloatArray(9) }

    /**
     * Load the model file from Assets
     */
    fun loadModelFile(assets: AssetManager): MappedByteBuffer {
        val fileDescriptor = assets.openFd(BASE_PATH + MODEL_PATH)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        inputStream.close()
        return  byteBuffer
    }

    fun buildModel(mappedByteBuffer: MappedByteBuffer, numThreads: Int): Interpreter {
        val compatibility = CompatibilityList()

        val options = Interpreter.Options().apply {
            if(compatibility.isDelegateSupportedOnThisDevice){
                // GPU inference
                val delegateOptions = compatibility.bestOptionsForThisDevice
                this.addDelegate(GpuDelegate(delegateOptions))
            }
            else{
                // CPU inference
                this.setNumThreads(numThreads)
            }
        }

        return Interpreter(mappedByteBuffer, options)
    }

    fun inference(inputArray: FloatArray): FloatArray{

        // output values will appear in this HashMap
        val outputMap = HashMap<Int, Any>()
        outputMap[0] = output

        // Run model inference
        Trace.beginSection("Inference")
        val startInferenceTime = SystemClock.uptimeMillis()

        // inference call on the model
        model?.runForMultipleInputsOutputs(arrayOf(inputArray), outputMap)
        // return an empty list if the model is not ready
            ?: run {
                log("INFERENCE FAILURE: Model has not been loaded")
                return FloatArray(0)
            }

        val inferenceDuration = SystemClock.uptimeMillis() - startInferenceTime
        log("Inference duration: $inferenceDuration")
        Trace.endSection()

        log("inference results: $outputMap[0]")

        return output[0]
    }

    private fun log(message: String){
        if(enableLogging){
            Log.println(Log.VERBOSE, "[TFLite model]", message)
        }
    }

    fun close(){
        model?.close()
        gpuDelegate?.close()

        model = null
        gpuDelegate = null
    }

}