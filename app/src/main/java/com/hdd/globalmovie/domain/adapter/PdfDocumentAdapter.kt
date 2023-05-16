package com.hdd.globalmovie.domain.adapter

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.printservice.PrintDocument
import java.io.*

class PdfDocumentAdapter(context: Context,path:String):PrintDocumentAdapter() {
    var context:Context?=null
    var path = ""
    init {
        this.context=context
        this.path=path
    }
    override fun onLayout(
        p0: PrintAttributes?,
        p1: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        layoutResultCallback: LayoutResultCallback?,
        p4: Bundle?
    ) {
        if (cancellationSignal!!.isCanceled){
            layoutResultCallback!!.onLayoutCancelled()
        } else{
            val builder = PrintDocumentInfo.Builder("file name")
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()
            layoutResultCallback!!.onLayoutFinished(builder.build(),p1!=p0)
        }
    }

    override fun onWrite(
        pageRange: Array<out PageRange>?,
        parcelFileDescriptor: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        writeResultCallback: WriteResultCallback?
    ) {
        var `in`:InputStream?=null
        var out:OutputStream?=null
        try {
            val file = File(path)
            `in` = FileInputStream(file)
            out=FileOutputStream(parcelFileDescriptor!!.fileDescriptor)

            if(!cancellationSignal!!.isCanceled){
                `in`.copyTo(out)
                writeResultCallback!!.onWriteFinished(arrayOf(PageRange.ALL_PAGES))

            } else{
                writeResultCallback!!.onWriteCancelled()
            }
        }catch (ex:Exception){
            print(ex)
        } finally {
            try {
                `in`!!.close()
                out!!.close()
            }catch (ex:Exception){
                print(ex)
            }
        }
    }


}