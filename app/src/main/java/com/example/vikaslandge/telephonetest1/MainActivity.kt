package com.example.vikaslandge.telephonetest1

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
 import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    var uri : Uri? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val call = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)
        val sms = ContextCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)
        val rps = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_PHONE_STATE)
        val internet =ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)
        val write =ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (call == PackageManager.PERMISSION_GRANTED && sms == PackageManager.PERMISSION_GRANTED && rps == PackageManager.PERMISSION_GRANTED
                &&internet == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED){

            run()

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.SEND_SMS,android.Manifest.permission.READ_PHONE_STATE
                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET
            ),111)
        }



    }//onCreate

    fun run(){

        b1.setOnClickListener(){
            if (!et1.text.toString().isEmpty()){
                if(!et2.text.toString().isEmpty()) {
                    var numbers =  et1.text.toString().split(",")
                    for( number in numbers) {
                        var sIntent = Intent(this@MainActivity, SentActivity::class.java)
                        var dIntent = Intent(this@MainActivity,
                                DeliverActivty::class.java)
                        var spIntent = PendingIntent.getActivity(this@MainActivity,
                                0, sIntent, 0)
                        var dpIntent = PendingIntent.getActivity(this@MainActivity,
                                0, dIntent, 0)
                        var sManager = SmsManager.getDefault()
                        sManager.sendTextMessage(et1.text.toString(), null, et2.text.toString(), spIntent, dpIntent)
                        Toast.makeText(this, "Sending.....", Toast.LENGTH_LONG).show()

                    }
                }else{
                    Toast.makeText(this,"type msg",Toast.LENGTH_LONG).show()
                }
            } else {

                Toast.makeText(this, "Number is mandatory", Toast.LENGTH_LONG).show()
            }
        }
        btncall.setOnClickListener() {
            if (!et1.text.toString().isEmpty()) {
                var i = Intent()
                i.action = Intent.ACTION_CALL
                i.data = Uri.parse("tel:${et1.text.toString()}")
                startActivity(i)

            } else {

                Toast.makeText(this, "Number is mandatory", Toast.LENGTH_LONG).show()
            }

            sendmail.setOnClickListener({

                var i = Intent( )
                i.action = Intent.ACTION_SEND
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailid.text.toString()))
                i.putExtra(Intent.EXTRA_SUBJECT, subject.text.toString())
                i.putExtra(Intent.EXTRA_TEXT,message.text.toString())
                i.putExtra(Intent.EXTRA_STREAM,uri)
                i.type = "message/rfc822"
                startActivity(i)

            })
        }
        attach.setOnClickListener(){
            var adiaolg  = AlertDialog.Builder(this)
            adiaolg.setTitle("Attachment")
            adiaolg.setMessage("attch the document to mail")
            adiaolg.setIcon(R.drawable.ic_attachment_black_24dp)
            adiaolg.setPositiveButton("Camera", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    var i = Intent("android.media.action.IMAGE_CAPTURE")
                    startActivityForResult(i,123)
                }
            })
            adiaolg.setNegativeButton("File", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    var i = Intent()
                    i.action = Intent.ACTION_GET_CONTENT
                    i.type = "*/*"
                    startActivityForResult(i,124)
                }

            })
            adiaolg.show()

        }
        javamailapi.setOnClickListener({

            var lop = LongOperation(mailid.text.toString(),
                    sendmail.text.toString(), message.text.toString())
            lop.execute()
            Toast.makeText(this,"sent",Toast.LENGTH_LONG).show()

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==124 && resultCode==Activity.RESULT_OK){

            uri = data!!.data
        }else if (requestCode==123 && resultCode == Activity.RESULT_OK){
            var bmp = data!!.extras.get("data") as Bitmap
            uri = getImageUri(this@MainActivity,bmp)
        }
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED&& grantResults[1]==PackageManager.PERMISSION_GRANTED
                && grantResults[2]== PackageManager.PERMISSION_GRANTED && grantResults[3]== PackageManager.PERMISSION_GRANTED
                && grantResults[4]==PackageManager.PERMISSION_GRANTED){

            run()

        }else{
            Toast.makeText(this,"u cant continue with app without permissions",Toast.LENGTH_LONG).show()
        }
    }
}
