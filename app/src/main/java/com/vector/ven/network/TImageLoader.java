package com.vector.ven.network;

import android.app.Application;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.vector.ven.R;
import com.vector.ven.constant.Constants;
import com.vector.ven.logger.Logger;

import java.io.File;

public class TImageLoader {
	private static Logger logger = Logger.getLogger();

    /**
     * 直接加載uri 圖像，不做判斷
     *
     * @param uri
     * @param iv
     */
    public static void display(String uri, ImageView iv) {
        ImageLoader.getInstance().displayImage(uri, iv);
    }

  /**
     * 直接加載uri 圖像，不做判斷
     *
     * @param uri
     * @param iv
     */
    public static void display(String uri, ImageView iv,int defaultRes) {
        ImageLoader.getInstance().displayImage(uri, iv,
                getDefaultImageOptions(defaultRes));
    }

	public static void init(Application app) {
		try {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    app.getApplicationContext())
                    .defaultDisplayImageOptions(
                            new DisplayImageOptions.Builder()
                                    .showImageForEmptyUri(
                                            R.drawable.ic_sentence)
                                    .showImageOnLoading(R.drawable.ic_sentence)
                                    .showImageOnFail(R.drawable.ic_sentence)
                                    .cacheOnDisk(true).cacheInMemory(true).build())
                    .threadPriority(Thread.MIN_PRIORITY)
                    .memoryCache(new WeakMemoryCache())
                    .memoryCacheSizePercentage(10)
							// 使用10% 的内存做缓存
					.memoryCacheExtraOptions(720, 1920) // default = device screen dimensions
//					.diskCacheExtraOptions(480, 800, null)
					.diskCache(
							new UnlimitedDiskCache(new File(Constants.SYS_PATH_IMAGE)))
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(50 * 1024 * 1024)
                    .denyCacheImageMultipleSizesInMemory().threadPoolSize(5)
                    .denyCacheImageMultipleSizesInMemory()
                    .tasksProcessingOrder(QueueProcessingType.LIFO).build();
			ImageLoader.getInstance().init(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static DisplayImageOptions getDefaultImageOptions(int res) {
		return new DisplayImageOptions.Builder().showImageOnLoading(res)
				.showImageForEmptyUri(res).showImageOnFail(res).build();
	}

}