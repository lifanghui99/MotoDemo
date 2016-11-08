package com.cneop.util;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lfh on 2016/11/3 0003.
 */
public class ImageUtil {


    /**
     * @param path 图片位置
     * @return 返回bitmap
     */
    public static Bitmap getBitmap(String path) {
        Bitmap bt = BitmapFactory.decodeFile(path);
        return bt;
    }
    public static Bitmap getBitmapFromPath(final String s, final int n, final int n2) {
        final BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(s, bitmapFactoryOptions);
        bitmapFactoryOptions.inSampleSize = calculateInSampleSize(bitmapFactoryOptions, n, n2);
        bitmapFactoryOptions.inJustDecodeBounds = false;
        Bitmap decodeFile = BitmapFactory.decodeFile(s, bitmapFactoryOptions);
        return decodeFile;
    }
    private static int calculateInSampleSize(final BitmapFactory.Options bitmapFactoryOptions, final int n,
                                             final int n2) {
        final int outHeight = bitmapFactoryOptions.outHeight;
        final int outWidth = bitmapFactoryOptions.outWidth;
        return 1;
    }

    public static boolean compressBmpFileToFile(String preFileName, int filesize, String fileName){
        boolean result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(filesize<100){
            filesize=100;
        }
        Bitmap bmp = getBitmap(preFileName);
        Bitmap bmp1=compressImage(bmp,filesize);
        result= savePic(bmp1,fileName);
        return  result;
    }

    /**
     * @param bmp
     * @param file
     */
    public static boolean compressBmpToFile(Bitmap bmp, int filesize, File file){
        boolean result=false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        if(filesize<100){
            filesize=100;
        }

        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > filesize) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            result=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }
    /**
     * @param bmp  指定压缩后图片大小，单位kb
     * @param toSize 指定压缩后图片大小，单位kb
     * @return
     */
    public static Bitmap compressImage(Bitmap bmp, int toSize) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
       //如果小于100kb，就设置为100kb
        if (toSize<100) {
            toSize=100;
        }
        int options = 70;
        while ( baos.toByteArray().length / 1024>toSize) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if(options<90) {
                options += 5;//每次都增加10
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 1;

        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, bitmapOptions);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    public static boolean savePic(Bitmap bitmap , String filename){
        boolean result=false;
        //使用此流读取
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //第二个参数影响的是图片的质量，但是图片的宽度与高度是不会受影响滴
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        //把数据转为为字节数组
        byte[] byteArray = baos.toByteArray();
        try {
            File file= new File( filename  );
            if(!file.exists())
            {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(filename );
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
            result=true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return  result;
    }


}
