package com.yancy.imageconvert;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Log;

/**
 * 说明：
 *
 * @author Yancy
 * @date 2018/8/28
 */
public class BitmapConvertUtils {
    /**
     * 图片转换成NV21二进制数组
     * 支持将ARGB_8888和RGB565格式的Bitmap转换成NV21格式byte数组
     *
     * @param bitmap bitmap格式图片
     * @return 二进制数组
     */
    public static byte[] bitmapToNV21Bytes(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            byte[] data = new byte[width * height * 3 / 2];
            BitmapConvert bitmapConvert = new BitmapConvert();
            bitmapConvert.initial(width, height, BitmapConvert.CP_PAF_NV21);
            if (bitmapConvert.convert(bitmap, data)) {
                bitmapConvert.destroy();
                return data;
            }
            bitmapConvert.destroy();
        }
        return null;
    }

    /**
     * 图片转换成BGR24二进制数组
     * 支持将ARGB_8888和RGB565格式的Bitmap转换成BGR24格式byte数组
     *
     * @param bitmap bitmap格式图片
     * @return 二进制数组
     */
    public static byte[] bitmapToBGR24Bytes(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            byte[] data = new byte[width * height * 3];
            BitmapConvert bitmapConvert = new BitmapConvert();
            bitmapConvert.initial(width, height, BitmapConvert.CP_PAF_BGR24);
            if (bitmapConvert.convert(bitmap, data)) {
                bitmapConvert.destroy();
                return data;
            }
            bitmapConvert.destroy();
        }
        return null;
    }

    /**
     * 图片转换成NV12二进制数组
     * 支持将ARGB_8888和RGB565格式的Bitmap转换成NV12格式byte数组
     *
     * @param bitmap bitmap格式图片
     * @return 二进制数组
     */
    public static byte[] bitmapToNV12Bytes(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            byte[] data = new byte[width * height * 3 / 2];
            BitmapConvert bitmapConvert = new BitmapConvert();
            bitmapConvert.initial(width, height, BitmapConvert.CP_PAF_NV12);
            if (bitmapConvert.convert(bitmap, data)) {
                bitmapConvert.destroy();
                return data;
            }
            bitmapConvert.destroy();
        }
        return null;
    }

    /**
     * NV21格式的数据转换成Bitmap
     *
     * @param context 上下文
     * @param data    数据
     * @param width   宽
     * @param height  高
     * @return 转换后的 bitmap
     */
    public static Bitmap nv21BytesToBitmap(Context context, byte[] data, int width, int height) {
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(data.length);
        Allocation in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);
        Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
        Allocation out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        in.copyFrom(data);
        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        Bitmap bmpout = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        out.copyTo(bmpout);

        return bmpout;
    }
}
