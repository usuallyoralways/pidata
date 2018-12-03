package test.origination.trade;


import ac.common.endecrpyt.DES;
import ac.origination.trade_table.DAO.Model.Trade;
import test.origination.trade.Models.TestEnTrade;


public class TestHandleTrade {
    Trade trade;
    TestEnTrade testEnTrade;


    double b;

    double bvalue;

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getBvalue() {
        return bvalue;
    }

    public void setBvalue(int bvalue) {
        this.bvalue = bvalue;
    }

    public TestHandleTrade(){
        testEnTrade=new TestEnTrade();
    }
    public TestHandleTrade(double b, double bvalue){
        this.b=b;
        this.bvalue=bvalue;
        testEnTrade=new TestEnTrade();
    }


    
    public int getAhashValue(int a){
        int sum=0;
        for (int i = Parameters.A_MIN; i <=a; i+=Parameters.diatA) {
            sum+=TestHashValue.getTestAhash(String.valueOf(a));
        }
        return sum;
    }

    public Double getBhashValue(Double a){
        if (a<=b)
            return bvalue;
        Double sum=bvalue;
        for (Double i = b+Parameters.dietB; i <=a; i+=Parameters.dietB) {
            sum+=TestHashValue.getTestBhash(String.valueOf(i));
        }
        bvalue=sum;
        b=a;
        return sum;
    }

    public void setTestEnTrade() {
        testEnTrade.setA(DES.encryptBasedDes(String.valueOf(trade.getTno())));
        testEnTrade.setB(DES.encryptBasedDes(String.valueOf(trade.getCost())));
        testEnTrade.setC(DES.encryptBasedDes(String .valueOf(trade.getDate())));
        Main.testtime1=System.currentTimeMillis();
        Main.testEnAndDe+=Main.testtime1-Main.testtime2;
        testEnTrade.setA_hashvalue(getAhashValue(trade.getTno()));
        testEnTrade.setB_hashvalue(getBhashValue(trade.getCost()));


    }

    public TestEnTrade getTestEnTrade() {
        return testEnTrade;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }
}
