package com.android.octopusy.view.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.octopusy.R
import com.android.octopusy.model.ProductEntry
import com.android.octopusy.net.ImageRequester
import com.android.octopusy.net.JsonReader
import com.android.volley.toolbox.NetworkImageView
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import com.whty.xzfpos.base.AppBaseActivity
import java.io.IOException
import java.util.*

/**
 * @project：AndroidLib
 * @author：- octopusy on 2018/4/12 10:45
 * @email：zhangh@qtopay.cn
 */
class CoordLayoutActivity: AppBaseActivity() {

    lateinit var adapter: ProductAdapter

    override fun getBundleExtras(extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContentViewLayoutID(): Int {
        return R.layout.activity_coord_layout
    }

    override fun initViewsAndEvents() {
        val appBar = findViewById<View>(R.id.app_bar) as Toolbar
        setSupportActionBar(appBar)

        val products = readProductsList()
        val imageRequester = ImageRequester.getInstance(this)


        val headerProduct = getHeaderProduct(products)
        val headerImage = findViewById<View>(R.id.app_bar_image) as NetworkImageView
        imageRequester.setImageFromUrl(headerImage, headerProduct.url)

        val recyclerView = findViewById<View>(R.id.product_list) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.shr_column_count))
        adapter = ProductAdapter(products, imageRequester)
        recyclerView.adapter = adapter

        val bottomNavigation = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        bottomNavigation.setOnNavigationItemSelectedListener {
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            layoutManager.scrollToPositionWithOffset(0, 0)
            shuffleProducts()
            true
        }

        bottomNavigation.setOnNavigationItemReselectedListener {
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            layoutManager.scrollToPositionWithOffset(0, 0)
        }

        if (contentViewLayoutID == 0) {
            bottomNavigation.selectedItemId = R.id.category_home
        }
    }

    override fun getLoadingTargetView(): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun getHeaderProduct(products: List<ProductEntry>): ProductEntry {
        if (products.size == 0) {
            throw IllegalArgumentException("There must be at least one product")
        }

        for (i in products.indices) {
            if ("Perfect Goldfish Bowl" == products[i].title) {
                return products[i]
            }
        }
        return products[0]
    }

    private fun shuffleProducts() {
        val products = readProductsList()
        Collections.shuffle(products)
        adapter.setProducts(products)
    }

    private fun readProductsList(): ArrayList<ProductEntry> {
        val inputStream = resources.openRawResource(R.raw.products)
        val productListType = object : TypeToken<ArrayList<ProductEntry>>() {

        }.type
        try {
            return JsonReader.readJsonStream(inputStream, productListType)
        } catch (e: IOException) {
            Logger.e("Error reading JSON product list", e)
            return ArrayList()
        }

    }

    class ProductAdapter internal constructor(private var products: List<ProductEntry>?, private val imageRequester: ImageRequester) : RecyclerView.Adapter<ProductViewHolder>() {

        internal fun setProducts(products: List<ProductEntry>) {
            this.products = products
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProductViewHolder {
            return ProductViewHolder(viewGroup)
        }

        override fun onBindViewHolder(viewHolder: ProductViewHolder, i: Int) {
            viewHolder.bind(products!![i], imageRequester)
        }

        override fun getItemCount(): Int {
            return products!!.size
        }
    }

    class ProductViewHolder internal constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.adapter_product_entry, parent, false)) {
        private val imageView: NetworkImageView
        private val priceView: TextView

        private val clickListener = View.OnClickListener { v ->
            val product = v.getTag(R.id.tag_product_entry) as ProductEntry
        }

        init {
            imageView = itemView.findViewById<View>(R.id.image) as NetworkImageView
            priceView = itemView.findViewById<View>(R.id.price) as TextView
            itemView.setOnClickListener(clickListener)
        }

        internal fun bind(product: ProductEntry, imageRequester: ImageRequester) {
            itemView.setTag(R.id.tag_product_entry, product)
            imageRequester.setImageFromUrl(imageView, product.url)
            priceView.setText(product.price)
        }
    }

}