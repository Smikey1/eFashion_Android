package com.hdd.globalmovie.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.domain.adapter.PSAdapter
import com.hdd.globalmovie.data.models.Product
import com.hdd.globalmovie.data.models.ProductSpecificationDetails

class ProductSpecificationFragment(private  val product: Product) : Fragment() {
    private lateinit var fps_recyclerView: RecyclerView
    private val productSpecificationList = mutableListOf<ProductSpecificationDetails>(
        ProductSpecificationDetails(featureName = "Segment",featureValue = "Ethnic"),
        ProductSpecificationDetails(featureName = "Material",featureValue = "Soft"),
        ProductSpecificationDetails(featureName = "Occasion",featureValue = "Festive"),
        ProductSpecificationDetails(featureName = "Pattern",featureValue = "Solid"),

        )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_specification, container, false)
        fps_recyclerView = view.findViewById(R.id.fps_recyclerView)
        productSpecificationList.add(ProductSpecificationDetails(featureName=product.featureName,featureValue=product.featureValue))
        val adapter = PSAdapter(productSpecificationList, requireContext())
        fps_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fps_recyclerView.adapter = adapter
        return view
    }

}