package com.hdd.globalmovie.presentation.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.makeText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Order
import com.hdd.globalmovie.data.models.OrderItem
import com.hdd.globalmovie.data.models.Product
import com.hdd.globalmovie.data.models.Review
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.databinding.ActivityProductDetailsBinding
import com.hdd.globalmovie.domain.adapter.PDLAdapter
import com.hdd.globalmovie.domain.adapter.PIAdapter
import com.hdd.globalmovie.domain.adapter.ReviewAdapter
import com.hdd.globalmovie.domain.viewModel.rating.RatingVM
import com.hdd.globalmovie.domain.viewModel.rating.RatingVMF
import com.hdd.globalmovie.domain.viewModel.review.ReviewVM
import com.hdd.globalmovie.domain.viewModel.review.ReviewVMF
import com.hdd.globalmovie.presentation.fragments.ProductDescriptionFragment
import com.hdd.globalmovie.presentation.fragments.ProductOtherDetailsFragment
import com.hdd.globalmovie.presentation.fragments.ProductSpecificationFragment
import com.hdd.globalmovie.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productList: List<Product>
    private lateinit var product: Product
    private val channelID = "com.hdd.globalMovie.notificationChannel"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY= "key_reply"

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productDetailsLayout:LinearLayout

    // for product image layout
    private lateinit var productImageVP: ViewPager
    private lateinit var productImageTabLayout: TabLayout
    private lateinit var pil_fab_wishlist: FloatingActionButton
    private lateinit var pil_product_name: TextView
    private lateinit var pil_product_price: TextView
    private lateinit var pil_cutted_price: TextView
    private lateinit var pil_total_rating_text: TextView
    private lateinit var pil_product_rating_text: TextView

    // for product description layout
    private lateinit var tabTitleList: ArrayList<String>
    private lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    // for product rating layout
    private lateinit var yourRatingStar: LinearLayout
    private lateinit var rl_total_rating1: TextView
    private lateinit var rl_total_rating2: TextView
    private lateinit var rl_avg_rating_number: TextView

    // for review layout
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewList: MutableList<Review>
    private lateinit var reviewAdapter: ReviewAdapter

    private var isProductInWishlist: Boolean = false
    private var previousRating:Int? =null
    private var starRating: Int=0

    // for product cart
    private lateinit var addToCart:Button
    private lateinit var buyNow:Button
    private lateinit var productId:String

    // for view model and live data
    private lateinit var reviewVM: ReviewVM
    private lateinit var ratingVM: RatingVM
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        //Extract the data…
        val bundle = intent.extras
        val requiredProductId = bundle!!.getString("productId")!!
        productId = requiredProductId
        getProductById(requiredProductId)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID,  "eFashion Store", "Notification Channel")
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // setDisplayHomeAsUpEnabled(true) --> create back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        productDetailsLayout = findViewById(R.id.productDetailsLayout)

        val productRepository=ProductRepository()
        val factory = ReviewVMF(productRepository)
        reviewVM= ViewModelProvider(this,factory).get(ReviewVM::class.java)
        reviewVM.getAllReview(requiredProductId)

        // for product image layouts
        productImageVP = findViewById(R.id.pi_view_pager)
        productImageTabLayout = findViewById(R.id.pi_vp_dots)
        pil_fab_wishlist = findViewById(R.id.pil_fab_wishlist)
        pil_product_name = findViewById(R.id.pil_product_name)
        pil_product_price = findViewById(R.id.pil_product_price)
        pil_cutted_price = findViewById(R.id.pil_cutted_price)
        pil_product_rating_text = findViewById(R.id.pil_product_rating_text)
        pil_total_rating_text = findViewById(R.id.pil_total_rating_text)
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView)

        // for wishlist floating action button
        pil_fab_wishlist.setOnClickListener {
            if (isProductInWishlist) {
                pil_fab_wishlist.supportImageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))

                //delete from wishlist and remove color
                CoroutineScope(IO).launch {
                    try {
                        val wishlistRepository= WishlistRepository()
                        val wishlistResponse=wishlistRepository.deleteProductFromWishlist(productId)
                        if(wishlistResponse.success==true){
                            withContext(Main){
                                makeText(this@ProductDetailsActivity, "Item deleted from wishlist", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch (ex:Exception){
                        print(ex)
                    }
                }
                pil_fab_wishlist.supportImageTintList = ColorStateList.valueOf(resources.getColor(R.color.fabWishlistColor))
                isProductInWishlist=false
            } else {
                //add to wishlist and set color
                CoroutineScope(IO).launch {
                    try {
                        val wishlistRepository= WishlistRepository()
                        val wishlistResponse=wishlistRepository.addProductToWishlist(productId)
                        if(wishlistResponse.success==true){
                            withContext(Main){
                                makeText(this@ProductDetailsActivity, "Item added to wishlist", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch (ex:Exception){
                        print(ex)
                    }
                }
                pil_fab_wishlist.supportImageTintList = ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                isProductInWishlist = true
            }
        }

        // for product description layout
        viewPager = findViewById(R.id.pdl_viewPager)
        tabLayout = findViewById(R.id.pdl_tabLayout)

        // for product rating layout
        rl_total_rating1 = findViewById(R.id.rl_total_rating1)
        rl_total_rating2 = findViewById(R.id.rl_total_rating2)
        rl_avg_rating_number = findViewById(R.id.rl_avg_rating_number)
        yourRatingStar = findViewById(R.id.yourRatingStar)

        val ratingRepository=RatingRepository()
        val ratingFactory = RatingVMF(ratingRepository)
        ratingVM= ViewModelProvider(this,ratingFactory).get(RatingVM::class.java)
        ratingVM.getRatingByProduct(requiredProductId)

        ratingVM.previousRating.observe(this, Observer {
            starRating = it-1
            setRating(it-1)
        })
        // for update rating start
        for (starValue in 0 until yourRatingStar.childCount) {
            val starPosition:Int=starValue
            yourRatingStar.getChildAt(starValue).setOnClickListener {
                val result = setRating(starPosition)
                CoroutineScope(IO).launch {
                    try{
                        val ratingRepository = RatingRepository()
                        ratingRepository.addOrUpdateRating(productId,result)
                    }catch (ex:Exception){
                        print(ex)}
                }
                return@setOnClickListener
            }
        }

        binding.apdAddReview.setOnClickListener {
            val reviewDialog = Dialog(this)
            reviewDialog.setContentView(R.layout.add_review_layout)
            reviewDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val writeReview:EditText = reviewDialog.findViewById(R.id.arl_writeReview)
            val cancel:Button = reviewDialog.findViewById(R.id.arl_cancel)
            val ok:Button = reviewDialog.findViewById(R.id.arl_ok)
            cancel.setOnClickListener {
                reviewDialog.dismiss()
            }
            ok.setOnClickListener {
                makeText(this, "Add New review", Toast.LENGTH_SHORT).show()
                addReview(productId,writeReview.text.toString())
                reviewDialog.dismiss()
            }
            reviewDialog.show()
        }

        // for product cart
        addToCart=findViewById(R.id.apd_add_to_cart_btn)
        buyNow=findViewById(R.id.apd_buy_now)

        addToCart.setOnClickListener {
            addToCart(productId)
            Snackbar.make(productDetailsLayout,"Product added to cart", Snackbar.LENGTH_SHORT).show()
        }

        buyNow.setOnClickListener {
            displayNotification()
            placedSingleOrder(productId)
        }
    }

    private fun getProductById(id:String?){
        CoroutineScope(IO).launch {
            try {
                val productRepository= ProductRepository()
                val response=productRepository.getProductById(id!!)
                if(response.success==true){
                    product = response.singleProductData!!

                    withContext(Main){
                        pil_product_name.text = product.productName
                        pil_product_price.text = "Rs. ${product.productPrice}"
                        pil_cutted_price.text=  "Rs. ${product.productCuttedPrice}"
                        pil_total_rating_text.text = "Total Ratings(${product.totalRating})"
                        pil_product_rating_text.text = "${product.productRating}"
                        rl_avg_rating_number.text = "${product.productRating}"
                        rl_total_rating1.text= "${product.totalRating} ratings"
                        rl_total_rating2.text= product.totalRating.toString()

                        // setting of view pager for PIL
                        productImageVP.adapter = PIAdapter(product.productImageUrlList!!)
                        productImageTabLayout.setupWithViewPager(productImageVP)

                        tabTitleList = arrayListOf<String>("Description", "Specification", "Other Details")
                        fragmentList = arrayListOf<Fragment>(ProductDescriptionFragment(product),
                            ProductSpecificationFragment(product), ProductOtherDetailsFragment(product))

                        // setting up adapter class for view pager2 in PDL
                        val adapter = PDLAdapter(fragmentList, supportFragmentManager, lifecycle)
                        viewPager.adapter = adapter
                        TabLayoutMediator(tabLayout, viewPager) {
                                tab, position -> tab.text = tabTitleList[position]
                        }.attach()
                        reviewList = product.reviews!!
                        reviewAdapter = ReviewAdapter(reviewList.asReversed(),starRating)

                        val linearLayoutManager= object : LinearLayoutManager(this@ProductDetailsActivity) {
                            override fun canScrollVertically(): Boolean {
                                return true
                            }
                        }
                        reviewRecyclerView.layoutManager= linearLayoutManager
                        reviewRecyclerView.adapter=reviewAdapter
                    }

                }
            }catch (ex:Exception){
                print(ex)
            }
        }
    }

    private fun addReview(productId:String,reviewMessage:String) {
        CoroutineScope(IO).launch {
            try {
                val reviewRepository = ReviewRepository()
                val response = reviewRepository.addReview(productId, Review(review = reviewMessage))
                if (response.success==true){
                    withContext(Main){
                        makeText(this@ProductDetailsActivity, "Review Added", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (ex:Exception){
                print(ex)
            }
        }
        reviewList.add(Review(review = reviewMessage,userId = ServiceBuilder.user))
        reviewAdapter.notifyDataSetChanged()
    }

    @SuppressLint("RestrictedApi")
    private fun displayNotification() {
        val notificationId = 45
        val tapResultIntent = Intent(this, BillingActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            tapResultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // reply action
        val remoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Total amount to be paid is: ${product.productPrice}")
            build()
        }
        val replyAction:NotificationCompat.Action=NotificationCompat.Action.Builder(0, "Make Payment",pendingIntent).addRemoteInput(remoteInput).build()

        //action button 1
        val intent2 = Intent(this, CartActivity::class.java)
        val pendingIntent2: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action2 : NotificationCompat.Action =NotificationCompat.Action.Builder(0,"View Cart",pendingIntent2).build()
        // action button 2
        val intent3 = Intent(this, OrderActivity::class.java)
        val pendingIntent3: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent3,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action3 : NotificationCompat.Action = NotificationCompat.Action.Builder(0,"View Order",pendingIntent3).build()

        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle("eFashion Store")
            .setContentText("Make a payment to confirm your order")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(action2)
            .addAction(action3)
            .addAction(replyAction)
            .build()
        notificationManager?.notify(notificationId, notification)

    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)

        }

    }

    private fun placedSingleOrder(id:String?) {
        val orderItemList = arrayListOf<OrderItem>(
            OrderItem(qty = 1,productId = Product(_id=id!!))
        )
        CoroutineScope(IO).launch {
            try {
                val repository= OrderRepository()
                val response = repository.addProductToOrder(Order(order = orderItemList))
                if (response.success==true) {
                    withContext(Main) {
                    }
                }
            }catch (ex: Exception){
                print(ex)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setRating(starPosition: Int):Int {
        for (starValue in 0 until yourRatingStar.childCount) {
            val starOnClick:ImageView=yourRatingStar.getChildAt(starValue) as ImageView
            starOnClick.imageTintList=ColorStateList.valueOf(resources.getColor(R.color.starNotSelectedColor))
            if (starValue<=starPosition){
                starOnClick.imageTintList=ColorStateList.valueOf(resources.getColor(R.color.starSelectedColor))
            }
        }
        return starPosition+1
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_cart_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(selectedItem: MenuItem): Boolean {
        when (selectedItem.itemId) {
            R.id.pd_search -> {
                Toast.makeText(this, "Search clicked from product details", Toast.LENGTH_SHORT).show()
            }
            R.id.pd_cart -> {
                Toast.makeText(this, "Cart clicked from product details", Toast.LENGTH_SHORT).show()
            }
            // for back button action
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun addToCart(id:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartRepository= CartRepository()
                val cartResponse=cartRepository.addProductToCart(id)
                if(cartResponse.success==true){
                    withContext(Dispatchers.Main){
                        Snackbar.make(productDetailsLayout,"Product added to cart", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }catch (ex:Exception){
                print(ex)
            }
        }

    }

}