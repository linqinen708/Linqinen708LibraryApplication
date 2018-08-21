package com.linqinen.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 图片异步加载
 *
 * @author lin
 */
public class ImageManager {


    //	submit有返回值，而execute没有
    public ExecutorService threadPool;
    // 图片缓存用于存储图片
    private LruCache<String, Bitmap> imageLruCache;
    private static final String TAG = "ImageManager";
    private int progress;
    public static int dp;
    public final static int IMAGE_ALBUM = 0,//相册
            IMAGE_CAPTURE = 1,//拍照
            IMAGE_CROP = 2;//裁剪

    private Context context;

    public static int getDp() {
        if (dp == 0) {
//            LogT.i("dp:"+dp);
            dp = Resources.getSystem().getDisplayMetrics().densityDpi / 160;
            if (dp < 2) {
                dp = 2;
            }
        }
        return dp;
    }

    // 在程序消息队列中排队的消息保持了对目标Handler类的应用。
    // 如果Handler是个内部类，那么它也会保持它所在的外部类（通常是Activity或者Service等）的引用。
    // 那么这时只要有消息在队列中，那么Handler便无法被回收，
    // 如果Handler不是static那么使用Handler的Service和Activity就也无法被回收，
    // 这就可能导致内存泄露。
    // 那么什么情况下会发生泄漏？
    // 通常只有在你发送了一个延时很长的消息的时候才可能会发生。
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    ImageLoad imageLoad = (ImageLoad) msg.obj;
//				Log.i(tag, msg)
                    imageLoad.imageView.setImageBitmap(imageLoad.bitmap);
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
//            MyProgress progress = (MyProgress) msg.obj;
//            progress.setProgress(msg.what);
//            if (msg.what == 100) {
//                progress.setVisibility(View.GONE);
//            }
        }
    };

    class ImageLoad {
        public ImageView imageView;
        public Bitmap bitmap;
        // public String imagePath;
    }

    public ImageManager(int threadPoolNumber, Context context) {
        dp = Resources.getSystem().getDisplayMetrics().densityDpi / 160;
        if (dp < 2) {
            dp = 2;
        }
        this.context = context;
        getCacheFolder(context);
        Log.i(TAG, "dp:" + dp);
        if (threadPoolNumber <= 0) {
            threadPoolNumber = 1;
        }
        threadPool = Executors.newFixedThreadPool(threadPoolNumber);

        // 获取app进程最大内存大小,单位byte
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        Log.i("maxMemory", maxMemory / 1024 / 1024 + "MB");

        imageLruCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }

            //必须重写此方法，来测量Bitmap的大小,系统默认为图片数量
            @Override
            protected int sizeOf(String key, Bitmap value) {
//                Log.i(TAG, "sizeOf:" + size());
                return value.getRowBytes() * value.getHeight();
            }

            @Override
            public void trimToSize(int maxSize) {
                super.trimToSize(maxSize);
//                Log.i(TAG, "trimToSize:" + maxSize);
            }

        };

    }

    /**
     * 添加Bitmap到内存缓存
     *
     * @param key
     * @param bitmap
     */
    private synchronized void addBitmapToImageLruCache(String key, Bitmap bitmap) {
        if (getBitmapFromImageLruCache(key) == null && bitmap != null) {
//        	Log.i("TAG", "addBitmapToImageLruCache:"+key);
            imageLruCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取一个Bitmap
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromImageLruCache(String key) {
        return imageLruCache.get(key);
    }

    /**
     * 移除所有内存缓存
     */
    public void removeImageCache() {
        if (imageLruCache != null && imageLruCache.size() > 0) {
            Log.i(TAG, imageLruCache.size() + "");
            imageLruCache.evictAll();
            imageLruCache = null;
        }
    }

    /**
     * myHeight,myWidth 为需要压缩时的指定高宽（固定比例缩放）进行压缩(与当前的系统Dpi相关),
     * 如果myHeight,myWidth值为0则不压缩
     */
    public synchronized void loadBigBitmap(String imageName2, final ImageView imageView,
                                           final String path, final int myHeight, final int myWidth) {
//		Log.i("loadBigBitmap", "imageName2:"+imageName2+"\npath:"+path);
//		Log.i("imageView", imageView.toString()+"");
        if (imageName2 == null || path == null || path.equals("") || imageView == null) {
            return;
        }
        final String imageName = imageName2 + ".png";
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
//				Log.i(TAG, "run: loadBigBitmap:"+"imageName:"+imageName);
                Bitmap image = null;
                File file = new File(getCacheFolder(context), imageName);

                if (imageLruCache.get(imageName) != null) {
//					Log.i("imageLruCache.get", imageName);
                    image = imageLruCache.get(imageName);
                }

                // 构造URL
                try {
                    if (!file.exists()) {
                        URL url = new URL(path);
                        // 打开连接
                        URLConnection con = url.openConnection();
                        con.setConnectTimeout(5000);// 设置超时
                        // 获得文件的长度
                        int contentLength = con.getContentLength();
                        // System.out.println("长度 :"+contentLength);
                        if (contentLength > 10) {
                            // 输入流
                            InputStream is = con.getInputStream();

//							//流只能使用一次，如果在这里转换图片之后就无法保存到本地了
//							Bitmap bitmap = BitmapFactory.decodeStream(is);

                            // 1K的数据缓冲
                            byte[] bs = new byte[1024];
                            // 读取到的数据长度
                            int len;
                            // 输出的文件流
                            OutputStream os = new FileOutputStream(file);
                            // 开始读取
                            while ((len = is.read(bs)) != -1) {
                                os.write(bs, 0, len);
                            }
                            // 完毕，关闭所有链接
                            os.close();
                            is.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (image == null && file.exists()) {
                    image = ratio(file.getPath(), myHeight, myWidth);
                    if (image != null) {
                        addBitmapToImageLruCache(imageName, image);

                    }
                }

                if (image != null && imageView != null) {
                    ImageLoad imageLoad = new ImageLoad();
                    imageLoad.bitmap = image;
                    imageLoad.imageView = imageView;

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = imageLoad;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * myHeight,myWidth 为需要压缩时的指定高宽（固定比例缩放）进行压缩，如果值为0，则不压缩
     */
    public Bitmap loadBitmap(String imageName, int myHeight, int myWidth) {
        imageName = imageName + ".png";
        Bitmap image = null;

        if (imageLruCache.get(imageName) != null) {
            image = imageLruCache.get(imageName);
            if (image != null) {
                return image;
            }
        }


        File file = new File(getCacheFolder(context), imageName);
        if (file.exists()) {
            image = ratio(file.getPath(), myHeight, myWidth);
            if (image != null) {
                imageLruCache.put(imageName, image);
                return image;
            }
        }
        return image;
    }


    /**
     * 下载文件
     */
    public String downloadFile(final File folder, final String urlPath, final String fileName) {
        if (folder == null || urlPath == null || fileName == null) {
            return null;
        }
        final File file = new File(folder, fileName);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                // 构造URL
                try {

                    int downloadCount = 0;
                    if (!file.exists()) {
                        URL url = new URL(urlPath);
//						Log.i("urlPath", urlPath);
                        // 打开连接
                        URLConnection con = url.openConnection();
                        // 获得文件的长度
                        int contentLength = con.getContentLength();
                        System.out.println("长度 :" + contentLength);
                        // 输入流
                        InputStream is = con.getInputStream();
                        // 1K的数据缓冲
                        byte[] bs = new byte[1024];
                        // 读取到的数据长度
                        int len;
                        // 输出的文件流
                        OutputStream os = new FileOutputStream(file);
                        // 开始读取
                        while ((len = is.read(bs)) != -1) {

                            downloadCount += len;
                            int progress = downloadCount * 100 / contentLength;
//							Log.i(TAG, "run: progress:"+progress);
//
//							handler.post(new Runnable() {
//								@Override
//								public void run() {
//
//								}
//							});

                            os.write(bs, 0, len);
                        }
                        // 完毕，关闭所有链接
                        os.close();
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return file.getPath();
    }

    /**
     * 下载大文件并显示进度条
     */
    public File downloadBigFileAndShowProgress(final File folder, final String urlPath, final String fileName, final TextView progressText, final ProgressDialog progressDialog) {
        if (folder == null || urlPath == null || fileName == null) {
            return null;
        }
        final File file = new File(folder, fileName);
        try {
            int downloadCount = 0;
            if (!file.exists()) {
                URL url = new URL(urlPath);
//						Log.i("urlPath", urlPath);
                // 打开连接
                URLConnection con = url.openConnection();
                // 获得文件的长度
                int contentLength = con.getContentLength();
                System.out.println("长度 :" + contentLength);
                // 输入流
                InputStream is = con.getInputStream();
                // 1K的数据缓冲
                int data = 1024 * 2;
                byte[] bs = new byte[data];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                OutputStream os = new FileOutputStream(file);
                // 开始读取
                long a = System.currentTimeMillis();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (progressText != null) {
                            progressText.setVisibility(View.VISIBLE);
                        }

                    }
                });

                while ((len = is.read(bs)) != -1) {

                    downloadCount += len;
                    progress = downloadCount * 100 / contentLength;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (progressText != null) {
                                progressText.setText(progress + "%");
                            }
                            if (progressDialog != null) {
                                progressDialog.setMessage(progress + "%");
                            }
                        }
                    });

                    os.write(bs, 0, len);
                }
                if (progressDialog != null) {
                    progressDialog.setMessage("下载完毕，正在加载。。。");
                }
                long b = System.currentTimeMillis();
                Log.i(TAG, "downloadBigFileAndShowProgress: " + (b - a) / 1000);
                // 完毕，关闭所有链接
                os.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 下载并替换文件
     */
    public String downloadAndCoverFile(final File folder, final String urlPath, final String fileName) {
        if (folder == null || urlPath == null || fileName == null) {
            return null;
        }
        final File file = new File(folder, fileName);
//		threadPool.execute(new Runnable() {
//			@Override
//			public void run() {
        // 构造URL
        try {

            int downloadCount = 0;
            if (!file.exists()) {
                URL url = new URL(urlPath);
//						Log.i("urlPath", urlPath);
                // 打开连接
                URLConnection con = url.openConnection();
                // 获得文件的长度
                int contentLength = con.getContentLength();
                System.out.println("长度 :" + contentLength);
                // 输入流
                InputStream is = con.getInputStream();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                OutputStream os = new FileOutputStream(file);
                // 开始读取
                while ((len = is.read(bs)) != -1) {

//							downloadCount += len;
//							int progress = downloadCount*100/contentLength;
//
//							handler.post(new Runnable() {
//								@Override
//								public void run() {
//
//								}
//							});

                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//			}
//		});
        return file.getPath();
    }

    /**
     * myHeight,myWidth 为需要压缩时的指定高宽（固定比例缩放）进行压缩，如果值为0，则不压缩
     */
    /*public void downloadImage(final String url, final String imageName,
                              final int myHeight, final int myWidth) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                File file = new File(getCacheFolder(), imageName + ".png");
                try {
                    if (!file.exists()) {
                        // 根据路径下载图片
                        HttpGet httpGet_image = new HttpGet(url);
                        HttpResponse httpResponse = defaultHttpClient.execute(httpGet_image);
                        Log.i("download" + imageName, httpResponse.getStatusLine().getStatusCode() + "");
                        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            // 图片接收方式
                            InputStream is = httpResponse.getEntity().getContent();
                            // 将字节流转换成bitmap,并压缩
                            Bitmap bitmap = ratio(is, myWidth, myHeight);

                            // 将图片保存在SD卡中
                            // 参数1 图片格式 2 图片质量0--100
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(CompressFormat.PNG, 100, fos);
//							Log.i("bitmap-----2", bitmap.getByteCount() + "");
                            fos.flush();

                            if (is != null) {
                                is.close();
                            }
                            if (fos != null) {
                                fos.close();
                            }
                        }
                    }
                } catch (Exception e) {

                } finally {
                    defaultHttpClient.getConnectionManager().shutdown();
                }
            }
        });
    }*/
    public boolean isTerminated() {
        threadPool.shutdown();
        Log.i("threadPool.isShutdown()", threadPool.isShutdown() + "");
        return threadPool.isTerminated();
    }

    /**
     * 如果myWidth或myHeight值为0，不压缩
     *
     * @param myWidth  压缩图片的宽度
     * @param myHeight 压缩图片的高度
     */
    public Bitmap ratio(InputStream is, int myWidth, int myHeight) {
        if (is == null) return null;
        if (myWidth == 0 || myHeight == 0) {
            return BitmapFactory.decodeStream(is);
        }
        myHeight = myHeight * dp;
        myWidth = myWidth * dp;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, newOpts);

        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;

        int compressRatio = 1;
        if (width > height && width > myWidth) {
            compressRatio = width / myWidth;
        } else if (width < height && height > myHeight) {
            compressRatio = height / myHeight;
        }
        newOpts.inSampleSize = compressRatio;
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return BitmapFactory.decodeStream(is, null, newOpts);
    }

    /**
     * 可以直接使用createScaledBitmap来使用压缩
     *
     * 对图片进行压缩，如果myHeight或者myWidth值为0，则不压缩,压缩大小根据系统Dpi决定
     */
    public static Bitmap ratio(Bitmap image, int myHeight, int myWidth) {
        if (image == null) return null;
        if (myHeight == 0 || myWidth == 0) {
            return image;
        }
        myHeight = myHeight * dp;
        myWidth = myWidth * dp;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, os);
//		if (os.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//			os.reset();// 重置baos即清空baos
//			image.compress(Bitmap.CompressFormat.PNG, 50, os);// 这里压缩50%，把压缩后的数据存放到os中
//		}
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        /*
        开始读入图片，此时把options.inJustDecodeBounds 设回true
        如果该值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了。
          但是允许我们查询图片的信息这其中就包括图片大小信息*/
        newOpts.inJustDecodeBounds = true;

        /* newOpts.inPreferredConfig = Config.RGB_565; *//*
         * 通过设置Options.
         * inPreferredConfig值来降低内存消耗
         * ： 默认为ARGB_8888:
         * 每个像素4字节. 共32位。
         * Alpha_8:
         * 只保存透明度，共8位，1字节。
         * ARGB_4444: 共16位，2字节。
         * RGB_565:共16位，2字节。
         * 如果不需要透明度
         * ，可把默认值ARGB_8888改为RGB_565
         * ,节约一半内存。
         */

        BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        Log.i("newOpts.outWidth", newOpts.outWidth + "");
        Log.i("newOpts.outHeight", newOpts.outHeight + "");
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int compressRatio = 1;// =1表示不缩放
        if (width > height && width > myWidth) {// 如果宽度大的话根据宽度固定大小缩放
            compressRatio = width / myWidth;
        } else if (width < height && height > myHeight) {// 如果高度高的话根据宽度固定大小缩放
            compressRatio = height / myHeight;
        }
        newOpts.inSampleSize = compressRatio;// 设置缩放比例
        // 重新读入图片，注意此时已经把op tions.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        // 压缩好比例大小后再进行质量压缩
        // return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        try {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 对图片进行压缩，如果myHeight或者myWidth值为0，则不压缩,压缩大小根据系统Dpi决定
     *
     * @param format Bitmap.CompressFormat.PNG,JPEG,WEBP，默认使用JPEG
     */
    public static Bitmap ratio(Bitmap image, Bitmap.CompressFormat format, int myHeight, int myWidth) {
        if (image == null) return null;

        if (myHeight == 0 || myWidth == 0) {
            return image;
        }
        if (format == null) {
            format = Bitmap.CompressFormat.JPEG;
        }
        myHeight = myHeight * getDp();
        myWidth = myWidth * getDp();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(format, 100, os);
//		if (os.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//			os.reset();// 重置baos即清空baos
//			image.compress(Bitmap.CompressFormat.PNG, 50, os);// 这里压缩50%，把压缩后的数据存放到os中
//		}
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;// 如果该
        // 值设为true那么将不返回实际的bitmap，也不给其分配内存空间这样就避免内存溢出了。
        // 但是允许我们查询图片的信息这其中就包括图片大小信息

        /* newOpts.inPreferredConfig = Config.RGB_565; *//*
         * 通过设置Options.
         * inPreferredConfig值来降低内存消耗
         * ： 默认为ARGB_8888:
         * 每个像素4字节. 共32位。
         * Alpha_8:
         * 只保存透明度，共8位，1字节。
         * ARGB_4444: 共16位，2字节。
         * RGB_565:共16位，2字节。
         * 如果不需要透明度
         * ，可把默认值ARGB_8888改为RGB_565
         * ,节约一半内存。
         */

        BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        Log.i("newOpts.outWidth", newOpts.outWidth + "");
        Log.i("newOpts.outHeight", newOpts.outHeight + "");
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int compressRatio = 1;// =1表示不缩放
        if (width > height && width > myWidth) {// 如果宽度大的话根据宽度固定大小缩放
            compressRatio = width / myWidth;
        } else if (width < height && height > myHeight) {// 如果高度高的话根据宽度固定大小缩放
            compressRatio = height / myHeight;
        }
        newOpts.inSampleSize = compressRatio;// 设置缩放比例
        // 重新读入图片，注意此时已经把op tions.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        // 压缩好比例大小后再进行质量压缩
        // return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        try {
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 对图片进行压缩，如果myHeight,myWidth值为0则不压缩,压缩大小根据系统Dpi决定
     */
    public static Bitmap ratio(String pathName, int myHeight, int myWidth) {
        if (pathName == null) return null;

        if (myHeight == 0 || myWidth == 0) return BitmapFactory.decodeFile(pathName);


        myHeight = myHeight * getDp();
        myWidth = myWidth * getDp();

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, newOpts);
        Log.i("newOpts.outWidth", newOpts.outWidth + "");
        Log.i("newOpts.outHeight", newOpts.outHeight + "");
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int compressRatio = 1;// =1表示不缩放
        if (width > height && width > myWidth) {// 如果宽度大的话根据宽度固定大小缩放
            Log.i("(int) (width / myWidth)", (width / myWidth) + "");
            compressRatio = width / myWidth;
        } else if (width < height && height > myHeight) {// 如果高度高的话根据宽度固定大小缩放
            Log.i("height / myHeight", (height / myHeight) + "");
            compressRatio = height / myHeight;
        }
        // Log.i("compressRatio", compressRatio+"");
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = compressRatio;

        // Log.i("newOpts.outWidth22222", newOpts.outWidth+"");
        // Log.i("newOpts.outHeight22222", newOpts.outHeight+"");
        return BitmapFactory.decodeFile(pathName, newOpts);
    }

    /**
     * 对图片进行压缩，如果myHeight,myWidth值为0则不压缩,压缩大小根据系统Dpi决定
     *
     * @param size 需要压缩的大小值，单位为MB
     */
    public static Bitmap ratio(String pathName, int size) {
        if (pathName == null) return null;

        if (size <= 0) return BitmapFactory.decodeFile(pathName);


        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, newOpts);
        Log.i("newOpts.outWidth", newOpts.outWidth + ",outHeight:" + newOpts.outHeight);
        Log.i("newOpts.inDensity", newOpts.inDensity + ",newOpts.inPreferredConfig:" + newOpts.inPreferredConfig);
//        int width = newOpts.outWidth;
//        int height = newOpts.outHeight;

        /*一般默认是Bitmap.Config.ARGB_8888，也就是4个bit，所以真是的图片大小为：
         * outWidth * outHeight * 4 / 8 /1024 /1024 =   * M
         * */


        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int compressRatio;// =1表示不缩放
//        newOpts.inSampleSize = 2
        compressRatio = newOpts.outWidth * newOpts.outHeight * 4 / 8 / 1024 / 1024 / size;
        Log.i("压缩比例compressRatio：", compressRatio + "");
        /*压缩比例如果为0，表示小于指定大小，不需要压缩*/
        if (compressRatio == 0) {
            return BitmapFactory.decodeFile(pathName);
        }
        newOpts.inJustDecodeBounds = false;
        newOpts.inSampleSize = compressRatio;

        // Log.i("newOpts.outWidth22222", newOpts.outWidth+"");
        // Log.i("newOpts.outHeight22222", newOpts.outHeight+"");
        return BitmapFactory.decodeFile(pathName, newOpts);
    }

    /**
     * 默认压缩类型是jpg
     */
    public static File bitmapToFile(Bitmap bitmap, File outputFile) {
        return bitmapToFile(bitmap, null, outputFile);
    }

    /**
     * @param format 默认压缩类型是jpg
     */
    public static File bitmapToFile(Bitmap bitmap, Bitmap.CompressFormat format, File outputFile) {
        // 图像保存到文件中
        if (outputFile == null) return null;

        if (format == null) {
            format = Bitmap.CompressFormat.JPEG;
        }

        FileOutputStream foutput = null;
        try {
//                        foutput = new FileOutputStream(this.imageFile);
            foutput = new FileOutputStream(outputFile);
            bitmap.compress(format, 100, foutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != foutput) {
                try {
                    foutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputFile;
    }


    /**
     * 判断图片类型是jpg还是png
     */
    public static Bitmap.CompressFormat judgeBitmapType(String filePath) {
        if (filePath.toUpperCase().endsWith("JPEG")
                || filePath.toUpperCase().endsWith("JPG")) {
            return Bitmap.CompressFormat.JPEG;
        } else if (filePath.toUpperCase().endsWith("PNG")) {
            return Bitmap.CompressFormat.PNG;
        }
        return null;
    }
    /**
     * 判断图片类型是jpg还是png
     */
    public static String judgeImageType(String filePath) {
        if (filePath.toUpperCase().endsWith("JPEG")
                || filePath.toUpperCase().endsWith("JPG")) {
            return ".jpg";
        } else if (filePath.toUpperCase().endsWith("PNG")) {
            return ".png";
        }
        return null;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * 将图片中间部分裁剪出来，类似于imageView属性的android:scaleType="centerCrop"；
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 获取每个像素所占用的bite数
     *
     * @param config
     * @return
     */
    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * 默认为图片的缓存路径
     */
    public static File getCacheFolder(Context context) {
        if (context == null) {
            return null;
        }
        File folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + "android" + File.separator + "data"
                + File.separator + context.getPackageName()
                + File.separator + "cache" + File.separator + "image");

        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    /**
     * @param name 是子目录的文件夹名称，不是文件名称，请注意
     */
    public static File getCacheFolder(String name, Context context) {
        File folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + "android" + File.separator + "data"
                + File.separator + context.getPackageName()
                + File.separator + "cache", name);

        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    /**
     * @param fileName 是目录下的文件名称
     */
    public static File getCacheFolderToFile(String fileName, Context context) {
        File folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + "android" + File.separator + "data"
                + File.separator + context.getPackageName()
                + File.separator + "cache");

        if (!folder.exists()) {
            folder.mkdirs();
        }
        folder = new File(Environment.getExternalStorageDirectory()
                + File.separator + "android" + File.separator + "data"
                + File.separator + context.getPackageName()
                + File.separator + "cache", fileName);

        return folder;
    }

    /**
     * 获得相机拍照路径
     */
    public static File getCameraFolder(Context context) {

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + context.getPackageName() + File.separator + "camera");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String filePaths = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
        folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + context.getPackageName() + File.separator + "camera", filePaths);
        return folder;
    }


    public void deleteOldFile() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                File directory = getCacheFolder(context);
                if (directory != null && directory.exists() && directory.isDirectory()) {
                    for (File item : directory.listFiles()) {
                        if (item.exists()) {
//							String dynamic[] = item.getName().split("--");
                            long currentTime = System.currentTimeMillis();
                            long lastModified = item.lastModified();
                            long fixedTime = 1000 * 60 * 60 * 24;
                            int myTime = (int) ((currentTime - lastModified) / fixedTime);
//							Log.i("time", (currentTime - lastModified)/(60*1000)+"");
//							Log.i("item.getName()", item.getName());
                            if (item.getName().contains("dynamic") && myTime > 1) {
                                item.delete();
                            } else if (item.getName().contains("friend") && myTime > 3) {
                                item.delete();
                            } else if (item.getName().contains("banner") && myTime > 20) {
                                item.delete();
                            }
                        }
                    }
                }
            }
        });
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees == 0) {
            return b;
        }
        if (b != null) {

            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth(),
                    (float) b.getHeight());
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle(); // Android开发网再次提示Bitmap操作完应该显示的释放
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return b;
    }

    /**
     * 裁剪图片
     */
    public static void cropImage(Context mContext, Uri mUri, int outputX, int outputY) {
        if (null == mUri || mContext == null || outputX <= 0 || outputY <= 0) return;

        Intent intent = new Intent();

        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX * getDp());// 输出图片大小
        intent.putExtra("outputY", outputY * getDp());
        intent.putExtra("return-data", true);

        ((Activity) mContext).startActivityForResult(intent, IMAGE_CROP);
    }


    /**
     * 如果outputFile 为null，则不输出，只返回bitmap
     */
    public static Bitmap cropImageResult(Intent data, File outputFile) {
        if (data == null) return null;
        // 拿到剪切数据
        Bitmap bm = data.getParcelableExtra("data");
        Log.i(TAG, "onActivityResult: 裁剪成功");
        // 图像保存到文件中
        if (outputFile == null) return bm;

        FileOutputStream foutput = null;
        try {
//                        foutput = new FileOutputStream(this.imageFile);
            foutput = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.PNG, 100, foutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != foutput) {
                try {
                    foutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    /**
     * 调用相机拍照
     */
    public static void cameraPhoto(Context mContext, File outputFile) {

        if (outputFile == null || mContext == null) return;
        String sdStatus = Environment.getExternalStorageState();
        /* 检测sd是否可用 */
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mContext, "SD卡不可用！", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);

        ((Activity) mContext).startActivityForResult(intent, IMAGE_CAPTURE);

    }

//    public void loadImageSchedule(final String path, final ImageView iv,
//                                  final MyProgress progress) {
//        Bitmap image = null;
//
//        if (imageLruCache.get(path) != null) {
//            image = imageLruCache.get(path);
//            if (image != null) {
//                iv.setImageBitmap(image);
//                return;
//            }
//        }
//
//
//        final File file = new File(getCacheFolder(), "dynamicBigImage" + MD5Util.MD5(path) + ".png");
//        if (file.exists()) {
//            image = BitmapFactory.decodeFile(file.getPath());
//            if (image != null) {
//                imageLruCache.put(path, image);
//                iv.setImageBitmap(image);
//                return;
//            }
//        }
//
//        progress.setVisibility(View.VISIBLE);
//        threadPool.execute(new Runnable() {
//
//            @Override
//            public void run() {
//                URL url = null;
//                int downloadCount = 0;
//                try {
//                    url = new URL(path);
//                    // 打开连接
//                    URLConnection con = url.openConnection();
//                    // 获得文件的长度
//                    int contentLength = con.getContentLength();
//                    // 输入流
//                    InputStream is = con.getInputStream();
//                    // 1K的数据缓冲
//                    byte[] bs = new byte[1024];
//                    // 读取到的数据长度
//                    int len;
//                    // 输出的文件流
//                    OutputStream os = new FileOutputStream(file);
//                    // 开始读取
//                    while ((len = is.read(bs)) != -1) {
//
//                        Message msg = new Message();
//
//                        downloadCount += len;
//                        msg.what = downloadCount * 100 / contentLength;
//                        msg.obj = progress;
//
//                        os.write(bs, 0, len);
//
//                        mHandler.sendMessage(msg);
//
//                    }
//                    if (file.exists()) {
//                        Bitmap image = ratio(file.getPath(), 250, 250);
//                        if (image != null) {
//                            imageLruCache.put(path, image);
//                            ImageLoad imageLoad = new ImageLoad();
//                            imageLoad.bitmap = image;
//                            imageLoad.imageView = iv;
//
//                            Message msg = new Message();
//                            msg.what = 1;
//                            msg.obj = imageLoad;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                    // 完毕，关闭所有链接
//                    os.close();
//                    is.close();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }


}
