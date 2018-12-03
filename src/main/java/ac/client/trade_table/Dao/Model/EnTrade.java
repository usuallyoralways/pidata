package ac.client.trade_table.Dao.Model;

import java.util.Date;

//生成数据
public class EnTrade {
    String a;
    int a_blockid;
    int a_hashvalue;
    String b;
    int b_blockid;
    int b_hashvalue;
    Date c;
    boolean a_flag;


    public boolean isA_flag() {
        return a_flag;
    }

    public void setA_flag(boolean a_flag) {
        this.a_flag = a_flag;
    }



    public EnTrade(){

    }

    public EnTrade(String a, int a_blockid, int a_hashvalue, String b, int b_blockid, int b_hashvalue, Date c) {
        this.a = a;
        this.a_blockid = a_blockid;
        this.a_hashvalue = a_hashvalue;
        this.b = b;
        this.b_blockid = b_blockid;
        this.b_hashvalue = b_hashvalue;
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getA_blockid() {
        return a_blockid;
    }

    public void setA_blockid(int a_blockid) {
        this.a_blockid = a_blockid;
    }

    public int getA_hashvalue() {
        return a_hashvalue;
    }

    public void setA_hashvalue(int a_hashvalue) {
        this.a_hashvalue = a_hashvalue;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public int getB_blockid() {
        return b_blockid;
    }

    public void setB_blockid(int b_blockid) {
        this.b_blockid = b_blockid;
    }

    public int getB_hashvalue() {
        return b_hashvalue;
    }

    public void setB_hashvalue(int b_hashvalue) {
        this.b_hashvalue = b_hashvalue;
    }

    public Date getC() {
        return c;
    }

    public void setC(Date c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "EnTrade{" +
                "a='" + a + '\'' +
                ", a_blockid=" + a_blockid +
                ", a_hashvalue=" + a_hashvalue +
                ", b='" + b + '\'' +
                ", b_blockid=" + b_blockid +
                ", b_hashvalue=" + b_hashvalue +
                ", c=" + c +
                ", a_flag=" + a_flag +
                '}';
    }
}
