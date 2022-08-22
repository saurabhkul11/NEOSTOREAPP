package com.example.neostore.fragment
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.neostore.R
import com.example.neostore.api.RetrofitClient
import com.example.neostore.models.ProductDetail
import com.example.neostore.models.RatingResponse
import kotlinx.android.synthetic.main.activity_rating.*
import kotlinx.android.synthetic.main.activity_rating.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class RatingPopUpScreen : DialogFragment()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun updateRating(productId: String?, rate: Int?) {
        RetrofitClient.getClient.ratingProduct(productId!!,rate!!).enqueue(object : Callback<RatingResponse?> {
            override fun onResponse(
                call: Call<RatingResponse?>,
                response: Response<RatingResponse?>
            ) {
                    if(response.body() != null){
                        val responseMsg = response.body()!!.user_msg
                        Toast.makeText(context,"$responseMsg",Toast.LENGTH_LONG).show()
                        dismiss()

                    }else
                        Toast.makeText(context,"${response.errorBody()}",Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<RatingResponse?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.activity_rating, container, false)
        val productId =  arguments?.getString("productId")
        val imageUrl = arguments?.getString("ImageURl")
        val productName = arguments?.getString("productName")
        val rate = view.ratingbarsubmision.rating.toInt()
        Glide.with(this@RatingPopUpScreen).load(imageUrl).into(view.itemimage)
        view.txtItemName.text = productName
        view.btnRateNow.setOnClickListener(){
            updateRating(productId,rate)
        }
        return view
    }

}