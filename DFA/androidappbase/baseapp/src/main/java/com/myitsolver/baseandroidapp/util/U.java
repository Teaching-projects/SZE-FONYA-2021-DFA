package com.myitsolver.baseandroidapp.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myitsolver.baseandroidapp.util.maps.Location;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Patrik on 2017. 08. 08..
 */

public class U {
    public static String getFormattedCount(Long count) {
        if (count == null) return "0";
        if (count < 1000) return count + "";
        if (count < 1000000) return ((int) (count/1000) + "k");
        return ((int) (count/1000000) + "m");
    }

    public static List<View> getAllChildren(View v) {
        if (v == null)
            return new ArrayList<>();
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            //Do not add any parents, just add child elements
            result.addAll(getAllChildren(child));
        }
        return result;
    }

    public static CharSequence[] getListAsCharObject(List os) {
        CharSequence[] c = new CharSequence[os.size()];
        for (int i = 0; i < os.size(); i++) {
            c[i] = os.get(i).toString();
        }
        return c;
    }
    public static CharSequence[] getListAsCharObject(Object[] os) {
        CharSequence[] c = new CharSequence[os.length];
        for (int i = 0; i < os.length; i++) {
            c[i] = os[i].toString();
        }
        return c;
    }
    public static String getSelectedPlaceHolder(List list, String defValue){
        String s = "";
        if (list == null || list.isEmpty()){
            s = defValue;
        }else{
            s = TextUtils.join(", ",list);
        }
        return s;
    }

    public static String getSelectedPlaceHolder(Object object, String defValue){
        String s = "";
        if (object == null){
            s = defValue;
        }else{
            s = object.toString();
        }
        return s;
    }

    public static byte[] drawableToByte(Drawable d){
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return stream.toByteArray();
    }

    public static int dpToPx(Context context,int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi*1f / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.densityDpi*1f / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(@NonNull String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
  @SuppressWarnings("deprecation")
    public static Spanned fromHtml(@NonNull String html, TextView tv){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY, new GlideImageGetter(tv), null);
        } else {
            result = Html.fromHtml(html, new GlideImageGetter(tv), null);
        }
        return result;
    }

    public static View getSeparatorLine(Context context){
        View v = new View(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
//        v.setBackgroundColor(ContextCompat.getColor(context, R.color.sepharatorLine));
        return v;
    }

    public static Point getScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    public static String getLang(){
        return Locale.getDefault().getLanguage();
    }

    public static void openMapsNavigation(Location location,Context context){
        String url = "http://maps.google.com/maps?"/*saddr="+49+","+17+"&*/+"daddr="+location.getLat()+","+location.getLng();

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(url));
        context.startActivity(intent);
    }

    public static String getFormattedDate(Date date){
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Budapest"));
        return sdf.format(date);
    }
}
