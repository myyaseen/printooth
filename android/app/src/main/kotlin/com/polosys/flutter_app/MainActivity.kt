package com.polosys.flutter_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
  private val CHANNEL = "pos.polosys.com/printooth"
  private val printing = Printooth.printer()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Printooth.init(this@MainActivity)
    GeneratedPluginRegistrant.registerWith(this)
    MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, result ->
      if (call.method == "printooth"){
        var text : String = call.argument<String>("payload").toString()
        val printStatus =  getPrintooth(text)
        result.success(printStatus)
      } else if(call.method == "printoothSI"){
        var text : String = call.argument<String>("payload").toString()
//        val printStatus = -1; //TODO to implemenent
        val printStatus = printSI(text)
        result.success(printStatus)
      }
      else{
        result.notImplemented()
      }
    }
  }

  private fun printSI(payload: String): Int {
    if (!Printooth.hasPairedPrinter()) startActivityForResult(Intent(this,
            ScanningActivity::class.java),
            ScanningActivity.SCANNING_FOR_PRINTER)
    else println("hello from SI print "+ payload)
    return 1
  }

  private fun getPrintooth(payload: String): Int{
    println("hello from kotlin "+ payload)
    return 1
  }

  private fun getSomePrintables() = ArrayList<Printable>().apply {
    var printables = ArrayList<Printable>()
    var printable = TextPrintable.Builder()
            .setText("Hello World")
    printables.add(printable)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK){
      println("printer is Ready")
      println("Has Paired Printer " + Printooth.hasPairedPrinter())
    }
  }
}
