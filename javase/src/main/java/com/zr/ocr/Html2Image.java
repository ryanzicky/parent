package com.zr.ocr;

import gui.ava.html.parser.HtmlParser;
import gui.ava.html.parser.HtmlParserImpl;
import gui.ava.html.renderer.ImageRenderer;
import gui.ava.html.renderer.ImageRendererImpl;

/**
 * @Author zhourui
 * @Date 2021/7/12 17:58
 */
public class Html2Image {

    static String HtmlTemplateStr = "\t<div style=\"height: 500px;width: 500px;background: #aee0ff;\">\n" +
            "\t\t这个是一个div\n" +
            "\t\t<h1>标题</h1>\n" +
            "\t\t<ol>\n" +
            "\t\t\t<li>a</li>\n" +
            "\t\t</ol>\n" +
            "\t\t<img style=\"margin-left: 1500px;\" width=\"300px\" height=\"200px\" src=\"https://inews.gtimg.com/newsapp_bt/0/11911825373/1000\">\n" +
            "\t</div>";

    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParserImpl();
        htmlParser.loadHtml(HtmlTemplateStr);
        // html 是我的html代码
        ImageRenderer imageRenderer = new ImageRendererImpl(htmlParser);
        imageRenderer.saveImage("D:\\lcxq1.png");
    }
}
