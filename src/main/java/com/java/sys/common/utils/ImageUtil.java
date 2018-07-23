package com.java.sys.common.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtil {
	private int x;  
    private int y;
    private int width;
    private int height;
    private String srcpath;		// 源图片路径名称
    private String subpath;		// 剪切图片存放路径名称
    private String suffix;
    
	public ImageUtil(int x, int y, int width, int height,String srcpath,String subpath,String suffix) {  
        this.x = x;  
        this.y = y;  
        this.width = width;  
        this.height = height;
        this.srcpath = srcpath;
        this.subpath = subpath;
        this.suffix = suffix.replace(".", "");
    }
	
	public void cut() {
		try {
			FileInputStream is = null;  
			ImageInputStream iis = null;  
            is = new FileInputStream(srcpath);
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);  
            ImageReader reader = it.next();  
            iis = ImageIO.createImageInputStream(is);  
            reader.setInput(iis, true);  
            ImageReadParam param = reader.getDefaultReadParam();  
            Rectangle rect = new Rectangle(x, y, width, height);  
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0, param);  
            ImageIO.write(bi, suffix, new File(subpath));
            is.close();
            iis.close();
        } catch(Exception e){
        	e.printStackTrace();
        } 
    }
	
	/*public static void main(String[] args) {
        ImageUtil o = new ImageUtil(20, 20, 100, 100,"F:\\1.jpeg","F:\\11.jpeg","jpeg");  
        o.cut();
	}*/

	
	
}
