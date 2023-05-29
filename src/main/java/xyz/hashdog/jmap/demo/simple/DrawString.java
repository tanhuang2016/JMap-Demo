package xyz.hashdog.jmap.demo.simple;

import xyz.hashdog.dm.bean.Text;
import xyz.hashdog.dm.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawString {
    public static void main(String[] args) throws IOException {
        int width=200;
        int height=300;
        //创建图片
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.CYAN);
        //设置背景
        graphics.fillRect(0,0,width,height);
        graphics.dispose();;
        //定义字体
        Font font = new Font("微软雅黑", Font.PLAIN, 15);
        //定义文本
        String text="对齐方式";
        //Text.CENTER_CENTER 对齐方式,水平垂直居中
        ImageUtil.DrawString(bufferedImage,"水平居右垂直居中",100,150,font,"#FF3030", Text.RIGHT_CENTER);
        ImageUtil.DrawString(bufferedImage,"水平居左垂直居中",100,150,font,"#FFA500", Text.LEFT_CENTER);
        ImageUtil.DrawString(bufferedImage,"水平居中,垂直居上",100,150,font,"#C71585", Text.CENTER_TOP);
        ImageUtil.DrawString(bufferedImage,"水平居中,垂直居下",100,150,font,"#8B3E2F", Text.CENTER_BOTTOM);
        ImageIO.write(bufferedImage, "png", new File("D:\\test\\createimage\\alignment.png"));
    }
}
