package com.cmlx.commons.support;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:35
 * @Desc -> 生成解析二维码
 **/
@Slf4j
@UtilityClass
public class QRCodeUtility {

    private final static String prefix = "data:image/png;base64,";

    /**
     * 创建二维码
     * @param url
     * @param fileDirectory
     * @param fileName
     * @return 返回图片base64编码
     * @throws IOException
     */
    public String createQRCode(String url,String fileDirectory,String fileName){
        int width = 500;
        int height = 500;
        String format = "png";
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        String filePath = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            FileUtility.createDirectory(fileDirectory);
            Path file = new File(fileDirectory,fileName+".png").toPath();
            filePath = file.toString();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。
            ImageIO.write(image, format, os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            byte b[] = os.toByteArray();//从流中获取数据数组。
            String str = prefix + new BASE64Encoder().encode(b);
            IOUtils.closeQuietly(os);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(filePath!=null) FileUtility.deleteFile(new File(filePath));
        }
        return null;
    }

    /**
     * 解析出二维码的url
     * @param file
     * @param fileDirectory
     * @throws NotFoundException
     */
    public void anlaysisQRCode(File file,String fileDirectory) throws NotFoundException {
        MultiFormatReader formatReader=new MultiFormatReader();
        BufferedImage image=null;
        try {
            image = ImageIO.read(file);
            BinaryBitmap binaryBitmap =new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            HashMap hints=new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            Result result=formatReader.decode(binaryBitmap,hints);
            log.info("解析结果："+result.toString());
            log.info("解析格式:"+result.getBarcodeFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = "http://www.baidu.com";
        String fileDirectory = "/usr/local/workspace/";
        String fileName = "qr_1";
        //生成二维码图片
        String qrcode = createQRCode(url,fileDirectory,fileName);
        System.out.println(qrcode);
    }
}

