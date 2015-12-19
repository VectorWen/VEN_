package com.vector.ven.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

/**
 * 一些关于文件的工具方法
 * @author vector
 *
 */
public class FileUtils {
	/**
	 * sdcard 的根目录 
	 */
	public static String sSDCardRoot = "";
	/**
	 * 初始化一些东西
	 */
	static {
		// 得到当前文件外部存储目录
		sSDCardRoot = Environment.getExternalStorageDirectory()+File.separator;
	}
	
	/**
	 * 不能实例化
	 */
	private FileUtils(){}
	
	/**
	 * 在SD卡上创建文件
	 * @param fileName  要创建的文件名
	 * @param dir      要创建文件的目录
	 * @return
	 * @throws IOException
	 */
	public static  File createFileInSDCard(String fileName, String dir)
			throws IOException {
		File file = new File(sSDCardRoot + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static  File createSDDir(String dir) throws IOException {
		File dirFile = new File(sSDCardRoot + dir + File.separator);
		dirFile.mkdirs();
		return dirFile;
	}
	
	/**
	 * 判断sdcard 时候存在
	 * @return
	 */
	public static boolean hasSDCard(){
		if(sSDCardRoot !=null&&!"".equals(sSDCardRoot)){
			return true;
		}
		return false;
	}

	/**
	 * 判断SD卡上的文件夹或文件是否存在
	 * @param fileName
	 * @param path
	 * @return
	 */
	public static  boolean ifFileExist(String fileName, String path) {
		File file = new File(sSDCardRoot + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 把InputStream里面的数据写进SD卡里面
	 * 
	 * @param path
	 *            --放文件的路径
	 * @param fileName
	 *            --文件的名字
	 * @param input
	 *            --就是这里面的数据写进SD卡中
	 */
	public static  File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			// 创建目录，有了就算是打开
			createSDDir(path);
			// 生成文件，
			file = createFileInSDCard(fileName, path);
			// 把管道插入新生成的文中，准备往里面写数据
			output = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			int temp;

			// 开始读写数据
			// read(b)是乱写点数据进去，不知道多少个的，
			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 读取目录中的MP3文件的名字和大小
	 */
//	public static  List<Mp3Info> getMp3Files(String path) {
//		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
//		File file = new File(sSDCardRoot + path);
//		File[] files = file.listFiles();
//		for (int i = 0; i < files.length; i++) {
//			if (files[i].getName().endsWith("mp3")) {
//				Mp3Info mp3Info = new Mp3Info();
//				mp3Info.setMp3name(files[i].getName());
//				mp3Info.setMp3Size(files[i].length() + "");
//				mp3Infos.add(mp3Info);
//			}
//		}
//
//		return mp3Infos;
//	}

}