package com.example.mrshopercapstone.view.main

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.mrshopercapstone.R
import com.example.mrshopercapstone.databinding.FragmentItemDetilsBinding
import com.example.mrshopercapstone.models.items.CartModel
import com.example.mrshopercapstone.models.items.ItemModel
import com.example.mrshopercapstone.models.items.Rating
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


private const val TAG = "ItemDetilsFragment"
class ItemDetilsFragment : Fragment() {
    private lateinit var binding: FragmentItemDetilsBinding
    private val itemViewModel: ItemViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    lateinit var cartItem: ItemModel
    lateinit var cartDatilsItem : CartModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetilsBinding.inflate(layoutInflater ,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observers()
        cartObserver()
        Log.d(TAG, "ID: ${itemViewModel.id}")
//        val cart = ItemModel(
//            "true",
//            "true",
//            itemViewModel.id,
//            itemViewModel.image,
//            itemViewModel.price.toDouble(),
//
//            itemViewModel.title
//
//        )
//        observers()
//        cartObserver()
////        observers()
        binding.registerButton.setOnClickListener(){
//            cartObserver()
//            observers()
            cartViewModel.addMyCart(cartItem)
            findNavController().navigate(R.id.action_itemDetilsFragment3_to_cartFragment3)
        }
        ///for downloading
        binding.DownloadimageButton.setOnClickListener(){
            val imageUrl = cartItem.image
            val request = DownloadManager.Request(Uri.parse(imageUrl))
                .setTitle("image")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setAllowedOverMetered(true)

            val manger = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manger.enqueue(request)
        }
        // for the sharing the image
        binding.shareImageButton.setOnClickListener {
            val image:Bitmap?= getBitmapFromView(binding.itemImageView)
            val url = Uri.parse(image.toString())
            val share= Intent(Intent.ACTION_SEND)
            share.type="image/*"
            share.putExtra(Intent.EXTRA_STREAM,getImageUri(requireActivity(),image!!))
            startActivity(Intent.createChooser(share, "Share Via:"))

        }

    }
       private fun getBitmapFromView(view: ImageView):Bitmap?{
       val bitmap= Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
       val paint= Canvas(bitmap)
       view.draw(paint)
       return bitmap

}
        private fun getImageUri(inContext: Context, inImage:Bitmap): Uri?{
            val byte= ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG,100,byte)
            val path= MediaStore.Images.Media.insertImage(inContext.contentResolver,inImage,"Title",null)
            return Uri.parse(path)

        }
       ///////////////////////////////////////////////////////


    fun observers(){
        itemViewModel.selectedItemMutableLiveData
            .observe(viewLifecycleOwner,{
                it?.let {
                    cartItem = it
                    binding.CatogoryTextView.text = it.category
                    binding.TitleTextView.text = it.title
                    binding.descripitionTextView.text = it.description
                    Picasso.get().load(it.image).into(binding.itemImageView)
                    itemViewModel.selectedItemMutableLiveData.postValue(null)

                }
//                cartItem = it
//                binding.CatogoryTextView.text = it.category
//                binding.TitleTextView.text = it.title
//                binding.descripitionTextView.text = it.description
//                Picasso.get().load(it.image).into(binding.itemImageView)
            })

    }
    fun cartObserver(){
        cartViewModel.selcetedItemMutableLiveData.observe(
            viewLifecycleOwner,{
                it?.let {
                    cartDatilsItem = it
                    binding.CatogoryTextView.text = it.category
                    binding.TitleTextView.text = it.title
                    binding.descripitionTextView.text = it.description
                    Picasso.get().load(it.image).into(binding.itemImageView)
                    cartViewModel.selcetedItemMutableLiveData.postValue(null)

                }
            }
        )
    }
}