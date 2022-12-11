package com.example.pos.Activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.IdentityCredentialStore
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.pos.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException
import java.util.jar.Manifest

class Scanner_Activity : AppCompatActivity() {
    private lateinit var scv:SurfaceView
    private lateinit var barcode:BarcodeDetector
    private lateinit var camera:CameraSource
    private val REQUEST_CAMERA_PERMISSION= 201
    private lateinit var tone:ToneGenerator
    private lateinit var tv:TextView
    private lateinit var data:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        //assign variable
        tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        scv = findViewById(R.id.sc_view)
        tv = findViewById(R.id.tx_code)

    }
    private fun initialDetector(){
        barcode = BarcodeDetector.Builder(applicationContext).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        camera = CameraSource.Builder(applicationContext, barcode).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build()
        scv.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(p0: SurfaceHolder) {
                try{
                    if(ActivityCompat.checkSelfPermission(applicationContext,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                       camera.start(scv.holder)
                    }else{
                        ActivityCompat.requestPermissions(this@Scanner_Activity, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                    }

                }catch (e:IOException){
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Unable to launch camera!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                camera.stop()
            }
        })

        barcode.setProcessor(object:Detector.Processor<Barcode>{
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been stopped!", Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
                val sparseArray:SparseArray<Barcode> = p0!!.detectedItems
                if(sparseArray.size() !=0){
                    tv.post(kotlinx.coroutines.Runnable {
                        kotlin.run {
                            if(sparseArray.valueAt(0).email != null){
                                tv.removeCallbacks(null)
                                data = sparseArray.valueAt(0).email.address
                                tv.text = data
                                tone.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                            }else{
                                data = sparseArray.valueAt(0).displayValue
                                var data_f:String
                                var sb:StringBuilder = StringBuilder()
                                var arr_data = data.toCharArray()
                                for(i in 0..(arr_data.size-1)){
                                    if(arr_data[i] == '_'){
                                        sb.append("\n")
                                    }else{
                                        sb.append(arr_data[i])
                                    }
                                }
                                data_f = sb.toString()
                                tv.text = data_f
                                if(data_f != null || data_f != "" && data_f.length > 5){
                                    val log:Intent? = Intent(this@Scanner_Activity, formBarang::class.java)
                                    log!!.putExtra("kode", data_f)
                                    startActivity(log)
                                    finish()
                                }
                                tone.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                            }
                        }
                    })
                }

            }

        })

    }





    override fun onPause() {
        super.onPause()
        camera.release()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        initialDetector()

    }
}