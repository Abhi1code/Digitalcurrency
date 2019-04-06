package Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Utils {

    public static final String cryptocurrencyapi = "https://api.coinmarketcap.com/v1/ticker/";


    public static String getString(String tagname,JSONObject jsonObject) throws JSONException{
        return jsonObject.getString(tagname);
    }

    public static int getRandomColor() {
        Random rand = new Random();
        String color[] = {"#ff0000","#000080","#191970","#FF4500","#4169E1","#8B4513","#FA8072","#2E8B57","#FFFF00","#0000ff",
                "#006400","#800000","#6B8E23","#800080","#C71585","#B22222","#D2691E","#2F4F4F","#008B8B","#FF1493"};

        return Color.parseColor(color[rand.nextInt(20)]);
    }

    public static void sharedata(String name, String exchangerate, String change, String rank, Context c){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Cryptocurrency : "+name);
        intent.putExtra(Intent.EXTRA_TEXT,"Cryptocurrency : "+name+"\n"+"Rank : "+rank+"\n"+"USD Rate : "+exchangerate+"\n"+"Percent_change_1h : "+change);
        c.startActivity(Intent.createChooser(intent,"Share rate using"));
    }
}
