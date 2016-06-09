package com.vector.ven.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

/**
 * 文件工具类
 *
 * @author vector
 */
public class FileUtils {
    /**
     * sdcard 根目录
     */
    public static String sSDCardRoot = "";

    static {
        sSDCardRoot = Environment.getExternalStorageDirectory() + File.separator;
    }

    /**
     * 不能实例化
     */
    private FileUtils() {
    }

    /**
     * 创建一级或多级目录
     *
     * @param dir 格式 dir/dir1/dir
     * @return true 如果创建了必要的目录或目标目录已经存在，目录一定有了
     * <p/>
     * false 如果不能创建目录。
     */
    public static boolean createDir(String dir) {
        File dirFile = new File(sSDCardRoot + dir + File.separator);
        if (existes(dir)) {
            return true;
        }
        return dirFile.mkdirs();
    }

    /**
     * 删除文件，或文件夹以及文件夹里面全部文件
     *
     * @param file
     * @return true 如果删除成功或者目标文件不存在，文件一定不存在了
     * <p/>
     * false 如果删除失败。
     */
    public static boolean delete(File file) {

        if (!file.exists()) {
            return true;
        }

        if (file.isFile()) {
            return file.delete();
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
        }
        return file.delete();
    }

    /**
     * 删除文件，或文件夹以及文件夹里面全部文件
     *
     * @param path 格式 dir/dir1
     *             <p/>
     *             dir/dir/file.jpg
     * @return true 如果删除成功或者目标文件不存在，文件一定不存在了
     * <p/>
     * false 如果删除失败。
     */
    public static boolean delete(String path) {
        return delete(new File(sSDCardRoot + path));
    }

    /**
     * 判断一个文件、或文件夹是否存在
     *
     * @param path ven/  ven/namefile.txt
     * @return
     */
    public static boolean existes(String path) {
        File file = new File(sSDCardRoot + path);
        return file.exists();
    }

    /**
     * 创建文件，如果目录不存在会先创建目录
     *
     * @param fileName 要创建的文件
     * @param dir      所在的目录
     * @return
     * @throws IOException
     */
    public static File createFile(String fileName, String dir)
            throws IOException {
        File dirFile = new File(sSDCardRoot + dir);
        if (!dirFile.exists()) {
            createDir(dir);
        }
        File file = new File(sSDCardRoot + dir + File.separator + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 判断是否有sdcard
     *
     * @return
     */
    public static boolean hasSDCard() {
        if (sSDCardRoot != null && !"".equals(sSDCardRoot)) {
            return true;
        }
        return false;
    }

    /**
     * 写文件，路径和文件如果没有就新创建，如果有就覆盖
     *
     * @param path     目录  ven/video/
     * @param fileName 文件名
     * @param input    输入流，不关闭这个流
     */
    public static File writeFile(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            file = createFile(fileName, path);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int temp;

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
     * 把字节数组的数据写进文件中，如果没有这个文件或路径，就新建
     *
     * @param fileName
     * @param path
     * @param data
     * @return
     */
    public static boolean writeFile(String path, String fileName, byte[] data) {
        FileOutputStream fos = null;
        File file = null;
        try {
            file = createFile(fileName, path);
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e1) {
                return false;
            }
        }

        return true;

    }

    /**
     * 判断一个文件是的内容是否为空
     *
     * @param pathName ven/video/1.1.1.json
     * @return 如果文件不存在，或者文件的内容为空，return true，如果文件含有数据，返回false
     */
    public static boolean isEmpty(String pathName) {

        boolean flag = true;
        File file = new File(sSDCardRoot + pathName);
        try {
            InputStream input = new FileInputStream(file);

            int isEmp = input.read();
            if (isEmp == -1) {
                //说明是空的
                flag = true;
            } else {
                flag = false;
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 读取文件，以字符串的形式返回
     *
     * @param path ven/ ven/namefile.txt
     * @return
     */
    public static String read(String path) {
        if (!existes(path)) {
            return null;
        }

        StringBuffer sb = new StringBuffer();

        File file = new File(FileUtils.sSDCardRoot + path);
        InputStream input = null;
        BufferedReader reader = null;
        try {
            input = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(input));
            String line;

            if ((line = reader.readLine()) != null) {
                sb.append(line);
                System.out.println(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static File copyFile(File src, String path, String name) {
        File dest = null;
        if (!src.exists()) {
            return dest;
        } else {
            dest = new File(path);
            if (!dest.exists()) {
                dest.mkdirs();
            }

            dest = new File(path + name);

            try {
                FileInputStream e = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];

                int length;
                while ((length = e.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }

                fos.flush();
                fos.close();
                e.close();
                return dest;
            } catch (IOException var8) {
                var8.printStackTrace();
                return dest;
            }
        }
    }
}
