package com.zr.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author zhourui
 * @Date 2021/6/9 17:09
 */
abstract class Main {

    /*public static void main(String[] args) {
        String a = "黄 石 市 威 马 商 贸 有 限 公 司";
        System.out.println(a.length());
        a = StringUtils.normalizeSpace(a);
        a.replace(" ", "");
        System.out.println(a.length());
        *//*for (char c : a.toCharArray()) {
            System.out.println(Integer.valueOf(c));
        }*//*
        int length = a.length();
        for (int i = 0; i < length; i++) {
            if (Integer.valueOf(a.charAt(i)) == 32) {
                a = a.replace(a.substring(i, i + 1), "");
                length = a.length();
            }
        }
        System.out.println(a);
    }*/

    /*public static void main(String[] args) throws TesseractException {
        ITesseract instance = new Tesseract();
        // 指定训练数据集合的路径
        instance.setDatapath("C:\\Program Files (x86)\\Tesseract-OCR\\tessdata");
        // 指定为中文识别
        instance.setLanguage("chi_sim");

        // 指定识别图片
        File imgDir = new File("C:\\Users\\yuangong\\Desktop\\images\\2.png");
        long startTime = System.currentTimeMillis();
        String ocrResult = instance.doOCR(imgDir);


        int length = ocrResult.length();
        for (int i = 0; i < length; i++) {
            if (Integer.valueOf(ocrResult.charAt(i)) == 32) {
                ocrResult = ocrResult.replace(ocrResult.substring(i, i + 1), "");
                length = ocrResult.length();
            }
        }
        // 输出识别结果
        System.out.println("OCR Result: \n" + ocrResult + "\n 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }*/

    public static void main(String[] args) {
        ArrayList<Object> objects = Lists.newArrayList();
        System.out.println(objects.size());
    }
}
