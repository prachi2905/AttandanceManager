package com.pm.attandancemanager.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by ghanshyamnayma on 23/06/16.
 */
@SuppressWarnings("deprecation")
public class AndroidVersionUtility {

    // getColor() deprecated api level 23 (in marshmallow)
    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    // getDrawable() deprecated (in LOLLIPOP)
    public static final Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    // multiple runtime permissions  (in MarshMallow)
    public static final boolean isMarshMallow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    // is lollipop and greater or not
    public static final boolean isLollipopOrGreater() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }

    // is Lollipop or lesser version
    public static final boolean isLollipopOrLess() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }
}
