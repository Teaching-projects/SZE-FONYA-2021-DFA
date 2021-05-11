package com.myitsolver.baseandroidapp.logic;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.myitsolver.baseandroidapp.logic.ImageUtils;

import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;


public class ImageCaptureHelper {
    private static final int CAMERA_REQUEST = 32342;
    private static final int PICK_IMAGE = 54534;
    private static final int CAMERA_REQUEST_HIGH_QUALITY = 32343;
//    private static final int OWN_CAMERA_REQUEST = 6969;


    private Fragment context;
    private ContentResolver cr;
    private OnImageReadyListener listener;
    private ContentValues cv;
    private Uri imageUri;

    public ImageCaptureHelper(Fragment context, OnImageReadyListener listener) {
        this.context = context;
        this.listener = listener;
        if (context.getActivity() != null) {
            cr = context.getActivity().getContentResolver();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == OWN_CAMERA_REQUEST) {
//
//            if (resultCode == RESULT_OK) {
////                Toast.makeText(context.getContext(), "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
//                try {
//                    Uri uri = Uri.parse(data.getDataString());
//                    Bitmap photo = MediaStore.Images.Media.getBitmap(cr,uri);
//                    photo = ImageUtils.rotateImageIfNeeded(uri.getPath(), photo);
//                    photo = resize(photo, 1920,1920);
//                    listener.onReady(photo, true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if(data != null) {
//                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
//                e.printStackTrace();
//                Toast.makeText(context.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            listener.onReady(photo, null);
        }
        if (requestCode == CAMERA_REQUEST_HIGH_QUALITY && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(
                        cr, imageUri);
                photo = ImageUtils.rotateImageIfNeeded(imageUri.getPath(), photo);
                photo = resize(photo, 1920,1920);
                listener.onReady(photo, imageUri.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap photo = BitmapFactory.decodeFile(picturePath);
                cursor.close();

                //SCALE BITMAP
                int nh = (int) (photo.getHeight() * (512.0 / photo.getWidth()));
                photo = Bitmap.createScaledBitmap(photo, 512, nh, true);

                //ROTATE IF NEEDED
                photo = ImageUtils.rotateImageIfNeeded(picturePath, photo);

                listener.onReady(photo, picturePath);

            } else {
                Toast.makeText(context.getContext(), "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void selectImageFromDevice() {
        Dexter.withActivity(context.getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BasePermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent i = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        context.startActivityForResult(i, PICK_IMAGE);
                    }


                })
                .check();

    }

    public void takePhoto() {
        Dexter.withActivity(context.getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new BasePermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        context.startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                })
                .check();
    }

//    public void takeHighResPhotoWithOwnCamera() {
//
//        new MaterialCamera(context)
//                .forceCamera1()
//                .stillShot()
//                .start(OWN_CAMERA_REQUEST);
//
//    }

    public void takeHighResPhotoa() {

        Dexter.withActivity(context.getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new BasePermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Uri mHighQualityImageUri = generateTimeStampPhotoFileUri();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
                        context.startActivityForResult(intent, CAMERA_REQUEST_HIGH_QUALITY);
                    }
                })
                .check();

    }

    private Uri generateTimeStampPhotoFileUri() {
        if (cr != null) {
            cv = new ContentValues();
            cv.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis()+"");
            imageUri = context.getActivity().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
            return imageUri;
        } else {
            return null;
        }
    }

    public interface OnImageReadyListener {
        void onReady(Bitmap bitmap, String picturePath);
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
