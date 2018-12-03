package test.origination.trade.Models;

public class TestEnTrade {
    String a;
    String b;
    String c;
    Integer a_hashvalue;
    Double b_hashvalue;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public Integer getA_hashvalue() {
        return a_hashvalue;
    }

    public void setA_hashvalue(Integer a_hashvalue) {
        this.a_hashvalue = a_hashvalue;
    }

    public Double getB_hashvalue() {
        return b_hashvalue;
    }

    public void setB_hashvalue(Double b_hashvalue) {
        this.b_hashvalue = b_hashvalue;
    }

    @Override
    public String toString() {
        return "TestEnTrade{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", a_hashvalue=" + a_hashvalue +
                ", b_hashvalue=" + b_hashvalue +
                '}';
    }
}
