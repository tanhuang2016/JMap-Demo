package xyz.hashdog.jmap.demo.simple;

import xyz.hashdog.dm.bean.*;
import xyz.hashdog.dm.bean.base.Position;
import xyz.hashdog.dm.draw.JMap;
import xyz.hashdog.dm.draw.JMaps;
import xyz.hashdog.dm.util.ImageUtil;
import xyz.hashdog.jmap.demo.Utils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChongQingHousingPrice {
    private final static String out= Utils.getOutDataDir(ChongQingHousingPrice.class) + "/重庆房价test.png";
    private final static String cqjson= Utils.getGeoJson("ChongQing") ;
    private final static String fjjson= Utils.getJsonString("2022-03重庆房价") ;
    public static void main(String[] args) throws IOException {
        //创建主图
        MainPic mainPic = new MainPic();
        //设置图片宽高
        mainPic.setWidth(2000);
        mainPic.setHight(2000);
        //设置背景色
        mainPic.setFaceColor("#DCDCDC");
        //设置透明度
        mainPic.setAlpha(0.0f);

        List<SubPicLayer> subPicLayerList = new ArrayList<>();
        //设置地图图层
        subPicLayerList.add(new SubPicLayer(){{
            setFaceColor("#FFC0CB");
            setLeft(100);
            setTop(300);
            setWidth(1600);
            setHight(1600);
            setAlpha(0.0f);
            setZorder(50);
        }});
        //设置标题图层
        subPicLayerList.add(new SubPicLayer(){{
            setFaceColor("#00008B");
            setLeft(0);
            setTop(50);
            setWidth(2000);
            setHight(250);
            setAlpha(0.0f);
        }});
        //设置图例图层
        subPicLayerList.add(new SubPicLayer(){{
            setFaceColor("#2da7ff");
            setLeft(1780);
            setTop(1100);
            setWidth(200);
            setHight(750);
            setAlpha(0.0f);
        }});



        mainPic.setSubPicLayers(subPicLayerList);
        mainPic.setMapPics(new ArrayList<MapPic>());
        mainPic.setTexts(new ArrayList<Text>());
        mainPic.setPics(new ArrayList<Pic>());
        //边界
        final List<MapBorder> mapBorders = Utils.mapBordersByGeo(cqjson);
        //县名
        final List<MapText> mapCounty = Utils.mapCountyNameByGeo(cqjson);
        //房价
        final List<MapText> mapFj = Utils.mapFj(cqjson,fjjson);
        //县名和房价叠加
        mapCounty.addAll(mapFj);
        //按房价填色
        Utils.pullColor(fjjson,mapBorders);
        List<MapPic> mapPicList = new ArrayList<>();
        mapPicList.add(new MapPic(){{
            setMapBorders(mapBorders);
            setMapTexts(mapCounty);
            setZoomWeight(1);
        }});

        //标题
        List<Text> texts = new ArrayList<>();
        texts.add(new Text(){{
            setTitle("重庆各区市县房价查询[2022-3]");
            setFontFamily("微软雅黑");
            setFontSize(80);
            setFontColor("#1C1C1C");
            setAlpha(1);
            setSubPicIndex(1);
            setAlignment(Text.CENTER_CENTER);
            setPosition(new Position(1000,100));
        }});
        //贴在主图上
        mainPic.setTexts(texts);
        //图片
        List<Pic> pics = new ArrayList<>();
        pics.add(new Pic(){{
            setPic(ImageIO.read(new File(Utils.getTempDataPath("len2.png"))));
            setSubPicIndex(2);
            setPosition(new Position(0,0));
        }});
        //图片贴在主图层上
        mainPic.setPics(pics);
        mainPic.setMapPics(mapPicList);

        long start = System.currentTimeMillis();
        JMap jMap= JMaps.newSimpleMapHelper(mainPic);
        jMap.plot().out(out);
        long end = System.currentTimeMillis();
        System.out.println("耗时:"+(end-start));

    }



}
