package com.cmlx.commons.support;

import com.cmlx.commons.exception.EXPF;
import com.cmlx.commons.file.FileType;
import com.cmlx.commons.file.FileTypeJudge;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:25
 * @Desc -> 文件处理
 **/
@Slf4j
@UtilityClass
public class FileUtility {

    protected static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public String getFileCharset(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File not found.");
        }
        // 默认编码格式为GBK
        String charset = "GBK";

        FileInputStream is = null;
        BufferedInputStream bis = null;

        try {
            byte[] first3Bytes = new byte[3];
            boolean checked = false;
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (-1 == read) {
                charset = "GBK";
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0) {
                        break;
                    }
                    if (0x80 <= read && read <= 0xBF) {
                        // 单独出现BF以下的,也算GBK
                        break;
                    }
                    if (0x80 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            // GBK
                            continue;
                        } else {
                            break;
                        }
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            log.error(EXPF.getExceptionMsg(e));
        } catch (IOException e) {
            log.error(EXPF.getExceptionMsg(e));
        } catch (Exception e) {
            log.error(EXPF.getExceptionMsg(e));
        } finally {
            if (null != is) try {
                is.close();
            } catch (IOException e) {
                log.error(EXPF.getExceptionMsg(e));
            }

            if (null != bis) try {
                bis.close();
            } catch (IOException e) {
                log.error(EXPF.getExceptionMsg(e));
            }
        }
        return charset;
    }

    /**
     * 获取文件md5值,32位小写字符md5
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public String getMd5ByFile(File file) throws FileNotFoundException {
        if(!file.exists()) {
            log.info("【file doesn't exist or is not a file】");
            return null;
        }
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            value = bytes2HexString(md5.digest());
           /* BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toLowerCase();*/
        } catch (Exception e) {
            log.error(EXPF.getExceptionMsg(e));
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(EXPF.getExceptionMsg(e));
                }
            }
        }
        return value.toLowerCase();
    }
    public  String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * 获取文件大小
     * @param file
     * @return
     */
    public Long getFileSize(File file){
        if(file.exists() && file.isFile()){
            return file.length();
        }else {
            log.info("【file doesn't exist or is not a file】");
            return 0L;
        }
    }

    public String getFileExtName(File file){
        if(file.exists() && file.isFile()){
            String filename = file.getName();
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
            return null;
        }else {
            log.info("【file doesn't exist or is not a file】");
            return null;
        }
    }

    public boolean deleteFile(File file){
        if(file.exists() && file.isFile()){
            return file.delete();
        }else {
            log.info("【file doesn't exist or is not a file】");
        }
        return false;
    }
    /**
     * 新建目录.
     *
     * @param path 文件路径
     * @throws Exception
     */
    public void createDirectory(String path) throws Exception {
        if (StringUtility.isEmpty(path)) {
            return;
        }
        try {
            // 获得文件对象
            File f = new File(path);
            if (!f.exists()) {
                // 如果路径不存在,则创建
                f.mkdirs();
            }
        } catch (Exception e) {
            log.error("创建目录错误.path=" + path, e);
            throw e;
        }
    }
    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len=inStream.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            return outStream.toByteArray();
        }catch (Exception e){
            log.error(EXPF.getExceptionMsg(e));
        }finally {
            if(outStream!=null){
                outStream.close();
            }
        }
        return null;
    }

    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public byte[] getImageFromNetByUrl(String strUrl){
        HttpURLConnection conn = null;
        InputStream inStream = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            log.error(EXPF.getExceptionMsg(e));
        }finally {
            if(conn!=null){
                conn.disconnect();
            }
            if(inStream!=null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    log.error(EXPF.getExceptionMsg(e));
                }
            }
        }
        return null;
    }

    /**
     * 将图片写入到磁盘
     * @param img 图片数据流
     * @param fileName 文件保存时的名称
     */
    public static void writeImageToDisk(byte[] img,String path, String fileName){
        FileOutputStream fops = null;
        try {
            File file = new File(path +File.separator+ fileName);
            fops = new FileOutputStream(file);
            fops.write(img);
        } catch (Exception e) {
            log.error(EXPF.getExceptionMsg(e));
        }finally {
            if(fops!=null){
                try {
                    fops.flush();
                    fops.close();
                } catch (IOException e) {
                    log.error(EXPF.getExceptionMsg(e));
                }
            }
        }
    }
    /**
     * 获取网络文件后缀名
     * @param strUrl
     * @return
     */
    public static String getFileSuffix(String strUrl) throws Exception {
        HttpURLConnection conn = null;
        InputStream inStream = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            inStream = conn.getInputStream();//通过输入流获取图片数据
            //获取文件头
            byte[] b = new byte[28];
            inStream.read(b, 0, 28);
            String fileHead = FileTypeJudge.bytesToHexString(b);
            if (fileHead == null || fileHead.length() == 0) {
                return "";
            }
            fileHead = fileHead.toUpperCase();
            FileType[] fileTypes = FileType.values();
            for (FileType type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type.toString().toLowerCase();
                }
            }
        } catch (IOException e) {
            log.error(EXPF.getExceptionMsg(e));
        } finally {
            if(conn!=null){
                conn.disconnect();
            }
            if(inStream!=null){
                try {
                    inStream.close();
                } catch (IOException e) {
                    log.error(EXPF.getExceptionMsg(e));
                }
            }
        }
        return null;
    }

    /**
     * 下载文件到本地
     * @param url  文件网络地址
     * @param dir  文件保存目录
     * @return  返回文件地址
     * @throws Exception
     */
    public static String downloadFile(String url,String dir,String fileName) throws Exception {
        if (!StringUtility.hasLength(dir)) return null;
        createDirectory(dir);
        String fileSuffix = getFileSuffix(url);
        byte[] fileBtArr = getImageFromNetByUrl(url) ;
        //String fileName  = StringUtility.hasLength(fileSuffix) ? ("."+fileSuffix) : "db";
        FileUtility.writeImageToDisk(fileBtArr,dir,fileName);
        String filePath = dir + File.separator + fileName;
        return filePath;
    }

    /**
     * 获取文件编码格式
     * @param file
     * @return
     */
    public static String get_charset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            ;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                // int len = 0;
                int loc = 0;

                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            log.error(EXPF.getExceptionMsg(e));
        }
        return charset;
    }

    //解压.gz文件
    public String unGzipFile(String sourcedir) {
        String ouputfile = "";
        try {
            //建立gzip压缩文件输入流
            FileInputStream fin = new FileInputStream(sourcedir);
            //建立gzip解压工作流
            GZIPInputStream gzin = new GZIPInputStream(fin);
            //建立解压文件输出流
            ouputfile = sourcedir.substring(0,sourcedir.lastIndexOf('.'));
            //ouputfile = ouputfile.substring(0,ouputfile.lastIndexOf('.'));
            FileOutputStream fout = new FileOutputStream(ouputfile);

            int num;
            byte[] buf=new byte[1024];

            while ((num = gzin.read(buf,0,buf.length)) != -1)
            {
                fout.write(buf,0,num);
            }

            gzin.close();
            fout.close();
            fin.close();
        } catch (Exception ex){
            System.err.println(ex.toString());
        }
        return ouputfile;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://minikservice.oss-cn-shenzhen.aliyuncs.com/dance/dance.dev.db.gz";
        String dir = "/Users/yangran/Downloads";
        String path = downloadFile(url,dir,"dance.dev.db.gz");
        System.out.println(path);
        //解压
        unGzipFile(path);
        //删除压缩包
        deleteFile(new File(path));
    }
}

