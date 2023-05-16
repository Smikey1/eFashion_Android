package com.hdd.globalmovie.data.localDataSource
import androidx.room.*
import com.hdd.globalmovie.data.models.Product

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Query("Select * from Product")
    suspend fun fetchAllProduct():MutableList<Product>


}