package ac.common.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lijun on 2018/7/12.
 */
public class Utility {

    public enum Staus{
        LOG,ERROR,DEBUG,OUTPUT
    }

    public static void log(String who,Staus staus,String what){
        String date=getTimeNow();
        //.out.printf("%8s","["+staus.toString()+"] ");
        String newStatus= StringFormat.formatLeftS(staus.toString(),8);
        System.out.print(newStatus);
        String newWho= StringFormat.formatLeftS(who,18);
        System.out.print(newWho);
        //System.out.print(date);
        System.out.println(" "+what);
    }

    public static String getTimeNow(){
        Date date=new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss ");
        String re =ft.format(date);
        return re;
    }
    //
//    public static void main(String[] args){
//        Utility.log("Lijun",Staus.LOG,"try try try");
//    }
    public static List<Double> reFileToList(String fileName){
        List<Double> result = new ArrayList<Double>();
        try {
            File f = new File(fileName);
            FileReader filein=new FileReader(f);
            BufferedReader buf=new BufferedReader(filein);
            String temp;
            while((temp=buf.readLine())!=null)
            {
                String[] tokens = temp.split(",");
                for (int i = 0; i < tokens.length; i++) {
                    result.add(Double.parseDouble(tokens[i]));
                }
            }
            filein.close();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        System.out.println(result);
        return result;
    }

}

class StringFormat {
    /**
     * 格式化输出 字符串
     * [*]左对齐,右补空格

     *
     * @param str
     * @param min_length : 最小输出长度
     * @return
     */
    public static String formatLeftS(String str, int min_length) {
        String format = "%-" + (min_length < 1 ? 1 : min_length) + "s";
        return String.format(format, str);
    }

    /**
     * 格式化输出 整数
     * [*]右对齐,左补0

     *
     * @param num
     * @param min_length : 最小输出长度
     * @return
     */
    public static String format0Right(long num, int min_length) {
        String format = "%0" + (min_length < 1 ? 1 : min_length) + "d";
        return String.format(format, num);
    }

    /**
     * 格式化输出 浮点数
     * [*]右对齐,左补0

     *
     * @param d
     * @param min_length : 最小输出长度
     * @param precision : 小数点后保留位数
     * @return
     */
    public static String format0Right(double d, int min_length, int precision) {
        String format = "%0" + (min_length < 1 ? 1 : min_length) + "."
                + (precision < 0 ? 0 : precision) + "f";
        return String.format(format, d);
    }
}