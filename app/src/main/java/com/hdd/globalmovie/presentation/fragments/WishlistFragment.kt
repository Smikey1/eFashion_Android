package com.hdd.globalmovie.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.domain.adapter.WishlistAdapter
import com.hdd.globalmovie.repository.WishlistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishlistFragment : Fragment() {
    private lateinit var wishlist_recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_wishlist, container, false)

        // #1: Wishlist Recycler view------------------>>>>>Start
        wishlist_recyclerView = view.findViewById<RecyclerView>(R.id.wishlist_recyclerView)
        wishlist_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val wishlistRepository= WishlistRepository()
                val wishlistResponse=wishlistRepository.getAllWishlist()
                if(wishlistResponse.success==true){
                    val wishlistItemList=wishlistResponse.data!!
                    withContext(Dispatchers.Main){
                        val adapter =WishlistAdapter(wishlistItemList,true)
                        wishlist_recyclerView.adapter =  adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }catch (ex:Exception){
                print(ex)
            }
        }
        // #1: Wishlist Recycler view------------------>>>>>End

        return view
    }


}