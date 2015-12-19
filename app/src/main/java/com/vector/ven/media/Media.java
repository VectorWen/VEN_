package com.vector.ven.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;

import com.vector.ven.constant.Constants;
import com.vector.ven.util.FileUtils;

import org.litepal.util.Const;

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
	 * 播放MP3，先判断是否有sdcard ，在判断文件时候存在
	 */
	public int play(String word){
		//判断是否有sdcard 卡
		if(!FileUtils.hasSDCard()){
			return -1;
		}

		//判断文件是否存在
		if(!FileUtils.ifFileExist(word+".mp3", "ven")){
			//下载去
			return -2;
		}

		mediaPlayer = MediaPlayer.create(context, Uri.parse(rootPath+word+".mp3"));
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
