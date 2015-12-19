package com.vector.ven.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;

import com.vector.ven.constant.Constants;
import com.vector.ven.util.FileUtils;

/**
 * 播放音频文件，主要是针对单词音频来写的逻辑
 * @author vector
 *
 */
public class Media {
	/**
	 * 谁调用的
	 */
	private Context context;
	/**
	 * 播放者
	 */
	private MediaPlayer mediaPlayer;
	/**
	 * 要播放的文件路径
	 */
	private String rootPath = "file://"+ FileUtils.sSDCardRoot+Constants.SYS_PATH;

	/**
	 * 初始化对象
	 * @param context
	 */
	public Media(Context context) {
		this.context = context;
	}

	/**
	 * 播放一个音频文件
	 *
	 * @param path
	 *            要播放的文件 格式 ven/add.mp3
	 * @return
	 */
	public int play(String path) {
		if (!FileUtils.hasSDCard()) {
			return -2;
		}
		// 如果文件不存在
		if (!FileUtils.existes(path)) {
			return -1;
		}

		mediaPlayer = MediaPlayer.create(context,
				Uri.parse(FileUtils.sSDCardRoot + path));

		// 当文件是坏的时候
		if (mediaPlayer == null) {
			Log.i("ven", FileUtils.sSDCardRoot + path + " 文件损坏");
			FileUtils.delete(path);
			return -1;
		}
		mediaPlayer.setOnCompletionListener(new CompletionListener(mediaPlayer));
		mediaPlayer.setLooping(false);
		mediaPlayer.start();
		return 1;
	}

	/**
	 * 监听播完
	 * @author vector
	 *
	 */
	class CompletionListener implements OnCompletionListener {
		MediaPlayer mediaPlayer;
		int playCount = 1;

		public CompletionListener(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
		}
		@Override
		public void onCompletion(MediaPlayer mp) {
			if(playCount<3){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mediaPlayer.start();
				playCount++;
			}
		}
	}
}
