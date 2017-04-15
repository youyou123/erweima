/**
*Copyright 2016 
*娣卞湷椤虹洘绉戞妧鏈夐檺鍏徃
*寮�鍙戣�� james   
*/
package com.zijunlin.Zxing.Demo;

import java.util.Hashtable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class MainActivity extends Activity {
    private ImageView code;
    private final int  QR_WIDTH=600;
    private final int  QR_HEIGHT=600;
   
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  code=(ImageView) findViewById(R.id.code);
//调用方法createCode生成二维码
  final Bitmap logo=BitmapFactory.decodeResource(super.getResources(),R.drawable.ic_launcher);
	
//	findViewById(R.id.erweima).setOnClickListener(new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			try {
//				 Bitmap 	bm = createImage("http://120.25.67.85:8099/syn/uploadApp/1774935-WiFiCarDV.apk",300,300,logo);
//				code.setImageBitmap(bm);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}	
//		}
//	});

	//ImageView img=(ImageView)findViewById(R.id.imgCode) ;
	//将二维码在界面中显示
	
  createQRImage("https://itunes.apple.com/cn/app/hzty/id1226253844?l=en&mt=8");//http://120.25.67.85:8099/syn/uploadApp/1774935-WiFiCarDV.apk
 }

  //  https://itunes.apple.com/cn/app/meet-dvr/id1224838129?l=en&mt=8
    public void createQRImage(String url)
    {
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            code.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 生成二维码
     * @param string 二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     * @return Bitmap 位图
     * @throws WriterException
     */
    private static final int IMAGE_HALFWIDTH = 40;//宽度值，影响中间图片大小
    /**
     * 生成二维码
     * @param string 二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     * @return Bitmap 位图
     * @throws WriterException
     */
    public static  Bitmap createImage(String text,int w,int h,Bitmap logo) {
        try {
            Bitmap scaleLogo = getScaleLogo(logo,w,h);
            int offsetX = 0;
            int offsetY = 0;
            if(scaleLogo != null){
                offsetX = (w - scaleLogo.getWidth())/2;
                offsetY = (h - scaleLogo.getHeight())/2;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    //判断是否在logo图片中
                    if(offsetX != 0 && offsetY != 0 && x >= offsetX && x < offsetX+scaleLogo.getWidth() && y>= offsetY && y < offsetY+scaleLogo.getHeight()){
                        int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
                        //如果logo像素是透明则写入二维码信息
                        if(pixel == 0){
                            if(bitMatrix.get(x, y)){
                                pixel = 0xff000000;
                            }else{
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * w + x] = pixel;
 
                    }else{
                        if (bitMatrix.get(x, y)) {
                            pixels[y * w + x] = 0xff000000;
                        } else {
                            pixels[y * w + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
 
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 缩放logo到二维码的1/5
     * @param logo
     * @param w
     * @param h
     * @return
     */
    private static Bitmap getScaleLogo(Bitmap logo,int w,int h){
        if(logo == null)return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 / logo.getHeight());
        matrix.postScale(scaleFactor,scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        return result;
    }
}