//package com.myitsolver.baseandroidapp.logic
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import androidx.fragment.app.Fragment
//import com.myitsolver.baseandroidapp.logic.ImageUtils
//import com.myitsolver.baseandroidapp.util.scaleToWidth
//
//import com.nguyenhoanglam.imagepicker.model.Config
//import com.nguyenhoanglam.imagepicker.model.Image
//import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
//import java.lang.Exception
//
//class ImageSelectHelper(val requestCode: Int = 100, private val photoWidth: Int = 1024) {
//
//    private var fragment: Fragment? = null
//
//    lateinit var singleImageSelected: ((bmp: Bitmap, path: String) -> Unit)
//
//
//    fun init(fragment: Fragment, singleImageSelected: ((bmp: Bitmap, path: String) -> Unit)) {
//        this.fragment = fragment
//        this.singleImageSelected = singleImageSelected
//    }
//
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK && data != null) {
//            try {
//                val images: ArrayList<Image> = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)
//                if (images.size == 1) {
//                    images[0].path?.let { path ->
//                        BitmapFactory.decodeFile(path)?.let {
//                            val photo = ImageUtils.rotateImageIfNeeded(images[0].path, it)
//                                .scaleToWidth(photoWidth)
//                            singleImageSelected(photo, path)
//                        }
//
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }
//    }
//
//    fun openSelector(
//        cameraOnly: Boolean = false,
//        allowCamera: Boolean = true,
//        settings: (ImagePicker.Builder.() -> ImagePicker.Builder) = { this }
//    ) {
//        fragment?.let {
//            ImagePicker.with(it)                         //  Initialize ImagePicker with activity or fragment context
//                .setToolbarColor("#212121")         //  Toolbar color
//                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
//                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
//                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
//                .setProgressBarColor("#4CAF50")     //  ProgressBar color
//                .setBackgroundColor("#212121")      //  Background color
//                .setCameraOnly(cameraOnly)               //  Camera mode
//                .setMultipleMode(false)              //  Select multiple images or single image
//                .setFolderMode(true)                //  Folder mode
//                .setShowCamera(allowCamera)                //  Show camera button
//                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
//                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
//                .setDoneTitle("Done")               //  Done button title
//                .setLimitMessage("You have reached selection limit")    // Selection limit message
//                .setMaxSize(10)                     //  Max images can be selected
//                .setSavePath("nexus")         //  Image capture folder name
//                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
//                .setRequestCode(requestCode)                //  Set request code, default Config.RC_PICK_IMAGES
//                .setKeepScreenOn(true)              //  Keep screen on when selecting images
//                .settings()
//                .start()                           //  Start ImagePicker
//        }
//    }
//}