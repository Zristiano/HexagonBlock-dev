package com.example.yuanmengzeng.imageloader;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 *   异步加载网络图片
 * Created by yuanmengzeng on 2016/5/9.
 */
public class ImageLoadAsyncTask extends AsyncTask<String,Integer,Bitmap> {

    private WeakReference<ImageView> imageViewRef ;

    private String url;

    private ImageLoader.ImgLoaderCallBack callBack;

    public ImageLoadAsyncTask(ImageView imageView, ImageLoader.ImgLoaderCallBack callBack) {
        this.imageViewRef = new WeakReference<>(imageView);
        if(callBack!=null){
            this.callBack = callBack;
        }else {
            this.callBack = new ImageLoader.ImgLoaderCallBack() {
                @Override
                public void onImgLoadSuccess() {
                    Log.i("ZYM","Image load Success");
                }

                @Override
                public void onImgLoadFail() {
                    Log.i("ZYM","Image load Fair");
                }
            };
        }
    }


    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap bitmap ;
        InputStream is ;
        url = params[0];
        try {
            URL imageUrl = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection)imageUrl.openConnection();
            connection.setConnectTimeout(3000);
            if(connection.getResponseCode()==200){
                is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                connection.disconnect();
                is.close();
                return bitmap;
            }
        }catch (Exception e){
            e.printStackTrace();
            callBack.onImgLoadFail();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap s) {
        super.onPostExecute(s);
        if(s==null)return;
        if (imageViewRef.get()!=null){
            imageViewRef.get().setImageBitmap(s);
        }
        callBack.onImgLoadSuccess();
        /** 加入文件缓存 */
        FileCache.getInstance().putBitmap(url,s);
       /* if (imageView.getTag().equals(mUrl)) {
            mImageView.setImageBitmap(result);
        }*/
    }


}
