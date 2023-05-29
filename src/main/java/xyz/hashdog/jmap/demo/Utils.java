package xyz.hashdog.jmap.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import xyz.hashdog.dm.bean.MapBorder;
import xyz.hashdog.dm.bean.MapIcon;
import xyz.hashdog.dm.bean.MapText;
import xyz.hashdog.dm.bean.base.TextStyle;
import xyz.hashdog.dm.util.HttpClientUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String getSharedDataDir(Class c) {
        File dir = new File(System.getProperty("user.dir"));
        dir = new File(dir, "src");
        dir = new File(dir, "main");
        dir = new File(dir, "resources");

        return dir.toString() + File.separator;
    }




    public static String GetOutputFilePath(String inputFilePath) {
        String extension = "";
        int i = inputFilePath.lastIndexOf('.');
        if (i > 0) {
            extension = inputFilePath.substring(i + 1);
        }
        if (inputFilePath.contains(".")) {
            inputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf("."));
        }

        return inputFilePath + "_out_." + extension;
    }

    public static String getOutDataDir(Class<?> c) {
        String dir = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (!File.separator.equals("/")) {
            dir = dir.substring(1);
        }

        dir = dir.replace("/", File.separator);
        dir = new File(dir).getParent();

        try {
            dir = URLDecoder.decode(dir, "UTF-8");
        } catch (Exception var3) {
        }

        return dir+File.separator+c.getSimpleName()+File.separator;
    }


    public static String getGeoJson(String name) {
        String sharedDataDir = getSharedDataDir(Utils.class);
       return sharedDataDir+File.separator+"jeojson"+File.separator+name+".json";
    }
    public static String getJsonString(String name) {
        String sharedDataDir = getSharedDataDir(Utils.class);
        return sharedDataDir+File.separator+"temdata"+File.separator+name+".json";

    }
    public static String getTempDataPath(String name) {
        String sharedDataDir = getSharedDataDir(Utils.class);
        return sharedDataDir+File.separator+"temdata"+File.separator+name;

    }


    /**
     * 处理边界
     * @param geoJsonPath
     * @return
     * @throws IOException
     */
    public static List<MapBorder> mapBordersByGeo(String geoJsonPath) throws IOException {
        List<MapBorder> mapBorders = new ArrayList<>();
        String strFromUrl = FileUtils.readFileToString(new File(geoJsonPath));
        JSONObject jsonObject = JSON.parseObject(strFromUrl);
        JSONArray features = jsonObject.getJSONArray("features");

        for (Object feature : features) {
            MapBorder mapBorder = new MapBorder();
            mapBorder.setName(((JSONObject)feature).getJSONObject("properties").getString("name"));
            mapBorder.setAdcode(((JSONObject)feature).getJSONObject("properties").getString("adcode"));
            mapBorder.setLineWidth(0.1f);
            mapBorder.setAlpha(1f);
            mapBorder.setEdgeColor("#000000");
            mapBorder.setFaceColor("#FFFFFF");
            mapBorders.add(mapBorder);
            List<List<Double>> lonLists = new ArrayList<>();
            List< List<Double>> latLists = new ArrayList<>();
            mapBorder.setLonsList(lonLists);
            mapBorder.setLatsList(latLists);

            JSONObject geometry = ((JSONObject)feature).getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");

            for (int n = 0; n < coordinates.size(); n++) {
                JSONArray jsonArray = coordinates.getJSONArray(n);

                for (int m = 0; m < jsonArray.size(); m++) {
                    List<Double> lons=new ArrayList<>();
                    List<Double>lats=new ArrayList<>();
                    JSONArray jsonArray1 = jsonArray.getJSONArray(m);
                    for (int b = 0; b < jsonArray1.size(); b++) {
                        JSONArray jsonArray2 = jsonArray1.getJSONArray(b);
                        lons.add(jsonArray2.getDouble(0));
                        lats.add(jsonArray2.getDouble(1));
                    }
                    lonLists.add(lons);
                    latLists.add(lats);

                }

            }
        }
        return mapBorders;
    }

    public static List<MapText> mapCountyNameByGeo(String geoJsonPath) throws IOException {
        final List<MapText> mapCounty = new ArrayList<>();
        String strFromUrl = FileUtils.readFileToString(new File(geoJsonPath));
        JSONObject jsonObject = JSON.parseObject(strFromUrl);
        JSONArray features = jsonObject.getJSONArray("features");

        MapText mapText = new MapText();
        List<String> txts= new ArrayList<>();
        List<Double> tlons = new ArrayList<>();
        List<Double> tlats = new ArrayList<>();
        mapText.setText(txts);
        mapText.setOffsetY(-10);
        mapText.setLons(tlons);
        mapText.setLats(tlats);
        mapCounty.add(mapText);
        TextStyle textStyle = new TextStyle();
        textStyle.setFontColor("#000000");
        textStyle.setFontSize(18);
        mapText.setTextStyle(textStyle);



        for (Object feature : features) {
            JSONArray station = ((JSONObject) feature).getJSONObject("properties").getJSONArray("center");
            txts.add(((JSONObject)feature).getJSONObject("properties").getString("name"));
            tlons.add(station.getDouble(0));
            tlats.add(station.getDouble(1));
        }
        return mapCounty;
    }


    public static List<MapText> mapFj(String geoJsonPath, String fjjson) throws IOException {
        final List<MapText> mapFj = new ArrayList<>();
        String strFromUrl = FileUtils.readFileToString(new File(geoJsonPath));
        JSONObject jsonObject = JSON.parseObject(strFromUrl);
        JSONArray features = jsonObject.getJSONArray("features");

        List<Double> tlons = new ArrayList<>();
        List<Double> tlats = new ArrayList<>();

        TextStyle textStyle = new TextStyle();
        textStyle.setFontColor("#000000");
        textStyle.setFontSize(18);

        MapText mapc = new MapText();
        List<String> txtsc= new ArrayList<>();
        mapc.setText(txtsc);
        mapc.setOffsetY(10);
        mapc.setLons(tlons);
        mapc.setLats(tlats);
        mapFj.add(mapc);
        mapc.setTextStyle(textStyle);

        for (Object feature : features) {

            JSONArray station = ((JSONObject) feature).getJSONObject("properties").getJSONArray("center");
            txtsc.add(getFj(fjjson,((JSONObject)feature).getJSONObject("properties").getString("name")));
            tlons.add(station.getDouble(0));
            tlats.add(station.getDouble(1));
        }
        return mapFj;
    }
    private static String getFj(String path,String name) {
        JSONObject jsonObject = JSON.parseObject(loadStrFromFile(path,"utf-8"));
        String string = jsonObject.getString(name);
        return string==null?"null":string;

    }
    public static String loadStrFromFile(String filePath, String encoding) {
        try {
            FileInputStream fs = new FileInputStream(filePath);
            byte[] buffer = new byte[fs.available()];
            fs.read(buffer);
            fs.close();
            return encoding != null && !encoding.equals("") ? new String(buffer, encoding) : new String(buffer);
        } catch (Exception var4) {
            return "";
        }
    }

    public static void pullColor(String fjjson, List<MapBorder> mapBorders) {
        for (MapBorder mapBorder : mapBorders) {
            mapBorder.setFaceColor(getColor(getFj(fjjson,mapBorder.getName())));

        }
    }

    private static String getColor(String fj) {
        Double aDouble = Double.valueOf(fj.replace(",",""));
        if(aDouble>=20_000d){
            return "#F02DFF";
        }else if(aDouble>=18_000d){
            return "#AA2D2D";
        }else if(aDouble>=15_000d){
            return "#FF2D2D";
        }else if(aDouble>=12_000d){
            return "#FFA82D";
        }else if(aDouble>=10_000d){
            return "#FFFD2D";
        }else if(aDouble>=8_000d){
            return "#51A82D";
        }else if(aDouble>=5_000d){
            return "#2DA7FF";
        }else if(aDouble>=4_000d){
            return "#2DD4FD";
        }
        return "#2DD4FD";
    }
}
