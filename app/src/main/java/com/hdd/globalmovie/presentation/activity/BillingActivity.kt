package com.hdd.globalmovie.presentation.activity

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Order
import com.hdd.globalmovie.data.models.OrderItem
import com.hdd.globalmovie.domain.Common
import com.hdd.globalmovie.domain.adapter.GenerateBillAdapter
import com.hdd.globalmovie.domain.adapter.PdfDocumentAdapter
import com.hdd.globalmovie.repository.OrderRepository
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class BillingActivity : AppCompatActivity() {
    private val fileName: String = "order_bill.pdf"
    private lateinit var rvBill : RecyclerView
    private lateinit var total : TextView
    private lateinit var grandTotal : TextView
    private lateinit var btnBack : Button
    private lateinit var btnPrint : Button
    private var notificationManager: NotificationManager? = null
    private var totalInvoiceAmount=0
    private  lateinit var orderedItemList:List<OrderItem>
    private  lateinit var orderedBy:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        receivedInput()

        val totalPrice = intent.getIntExtra("totalPrice", 0)
        rvBill = findViewById(R.id.rvBill)
        total = findViewById(R.id.tvTotal)
        grandTotal = findViewById(R.id.tvGrandTotal)

        //for last two button
        btnBack = findViewById(R.id.ab_back)
        btnPrint = findViewById(R.id.ab_Print)

        btnBack.setOnClickListener {
            super.onBackPressed()
        }

        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    btnPrint.setOnClickListener {
                        createPDF(Common.getAppPath(this@BillingActivity)+fileName)
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }
            }).check()

//      grandTotal.text = totalPrice.toString()
        generateBill()
    }

    private fun generateBill() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository= OrderRepository()
                val response = repository.getAllOrder()
                if (response.success==true){
                    val orderDataList = response.data!!
                    withContext(Dispatchers.Main){
                        val order=orderDataList.asReversed()[0]
                        orderedBy = order.userId?.fullname!!
                        var totalAmt = 0
                        for(orderItem in order.order) {
                            val product = orderItem.productId
                            totalAmt += product.productPrice!! * orderItem.qty
                        }
                        totalInvoiceAmount=totalAmt
                        total.text = "Rs. $totalAmt"
                        grandTotal.text = "Rs. $totalAmt"
                        orderedItemList=orderDataList.asReversed()[0].order
                        rvBill.adapter = GenerateBillAdapter(orderDataList.asReversed()[0].order)
                        rvBill.layoutManager = LinearLayoutManager(this@BillingActivity)
                    }

                }
            }catch (ex:Exception){

            }
        }
    }

    private fun receivedInput(){
        val KEY_REPLY= "key_reply"
        val intent = this.intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        if (remoteInput!=null){
            val inputString =remoteInput.getCharSequence(KEY_REPLY).toString()
//            Toast.makeText(this, "Received $remoteInput", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "Received ${ServiceBuilder.latestOrderId}", Toast.LENGTH_SHORT).show()
            val channelID = "com.hdd.globalMovie.notificationChannel"
            val notificationId = 45
            val receivedNotification = NotificationCompat.Builder(this, channelID)
                .setContentTitle("Ordered Successful")
                .setContentText("Your payment received")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_HIGH).build()

            notificationManager?.notify(notificationId,receivedNotification)
        }
    }

    private fun createPDF(path: String) {
        if (File(path).exists()){
            File(path).delete()
        }
        try {
            val document= Document()
            PdfWriter.getInstance(document, FileOutputStream(path))

            // Open to write document
            document.open()

            //setting document
            document.pageSize= PageSize.A4
            document.addCreationDate()
            document.addAuthor("eFashion Store")
            document.addCreator("Auto Generated Bill")

            //Font Setting
            val eFashionColor = BaseColor(206,0,0,255)
            val colorAccent = BaseColor(0,153,204,255)
            val shippingColor = BaseColor(0,128,0,255)
            val eFashionFontSize =41.0f
            val invoiceFontSize =40.0f
            val labelFontSize = 20.0f

            // Custom Font
            val fontName = BaseFont.createFont("assets/fonts/niconne.ttf","UTF-8", BaseFont.EMBEDDED)
            val invoiceFont = BaseFont.createFont("assets/fonts/invoice.ttf","UTF-8", BaseFont.EMBEDDED)

            // Add eFashion Style
            val eFashionStyle = Font(fontName,eFashionFontSize, Font.BOLD,eFashionColor)

            // total Invoice Style
            val invoiceStyle = Font(invoiceFont,invoiceFontSize, Font.NORMAL,eFashionColor)

            // Add order style to document
            val orderStyle = Font(invoiceFont,labelFontSize, Font.NORMAL,colorAccent)

            // Add labelStyle to document
            val labelStyle = Font(invoiceFont,labelFontSize, Font.NORMAL, BaseColor.BLACK)

            //Add style for order items
            val itemStyle = Font(invoiceFont,labelFontSize, Font.NORMAL, BaseColor.BLACK)

            //Add style for shipping
            val shippingStyle = Font(invoiceFont,labelFontSize, Font.NORMAL, shippingColor)

            //Add style for total
            val totalStyle = Font(invoiceFont,labelFontSize, Font.NORMAL, eFashionColor)

            // logo
            addNewItem(document,"eFashion Store", Element.ALIGN_TOP,eFashionStyle)

            //order date
            addNewItem(document,"Order Date: 2021/09/30", Element.ALIGN_RIGHT,orderStyle)

            // invoice heading
            addNewItem(document,"Total Invoice", Element.ALIGN_CENTER,invoiceStyle)
            addLineSpace(document)
            addNewItemWithLeftAndRight(document,"Order No: ${orderedItemList[0]._id?.substring(15,24)}", "Order By: $orderedBy",orderStyle,orderStyle)
            addLineSeparator(document)
            addNewItemWithLeftCenterRight(document,"Product", "Quantity","Price",labelStyle)
            addLineSeparator(document)

            // list of order items
            for(item in orderedItemList){
                addNewItemWithLeftCenterRight(document,"${item.productId.productName}", "${item.qty}","Rs. ${item.productId.productPrice}",itemStyle)
            }

            // for total
            addLineSeparator(document)
            addNewItem(document,"Total:   Rs. $totalInvoiceAmount", Element.ALIGN_RIGHT,totalStyle)
            addLineSpace(document)

            // for free shipping charge
            addNewItem(document,"Shipping Charge:   Free", Element.ALIGN_LEFT,shippingStyle)
            addLineSeparator(document)

            //For Total Item
            addLineSeparator(document)
            addNewItem(document,"Grand Total:   Rs. $totalInvoiceAmount", Element.ALIGN_RIGHT,totalStyle)

            //close document
            document.close()
            Toast.makeText(this,"Bill Generated", Toast.LENGTH_SHORT).show()

            printPDF()
        }catch (ex:Exception){
            print(ex)
        }
    }

    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

        try {
            val printAdapter = PdfDocumentAdapter(this,Common.getAppPath(this)+fileName)
            printManager.print("Document",printAdapter, PrintAttributes.Builder().build())
        }catch (ex:Exception){
            print(ex)
        }
    }

    @Throws(DocumentException::class)
    private fun addNewItemWithLeftAndRight(document: Document, leftText: String, rightText: String, leftTextFont: Font, rightTextFont: Font) {
        val chunkTextLeft=Chunk(leftText,leftTextFont)
        val chunkTextRight=Chunk(rightText,rightTextFont)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)
    }

    @Throws(DocumentException::class)
    private fun addNewItemWithLeftCenterRight(document: Document, leftText: String, centerText: String,rightText: String, labelStyle: Font) {
        val chunkTextLeft=Chunk(leftText,labelStyle)
        val chunkTextCenter=Chunk(centerText,labelStyle)
        val chunkTextRight=Chunk(rightText,labelStyle)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextCenter)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)
    }

    @Throws(DocumentException::class)
    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor= BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk= Chunk(text,style)
        val p = Paragraph(chunk)
        p.alignment=align
        document.add(p)
    }

}