package ac.origination.trade_table;


import ac.origination.trade_table.DAO.Model.Trade;
import ac.origination.trade_table.DAO.TradeDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.Random;

public class GenetrateData {

    public void genetrateData(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");

        Trade trade= new Trade();
        Random rm = new Random();
        int N=Parameters.TNO_MAX;

        Double b=3400000.0;

        Double a=5000000.0;
//        double a=0,b=1;
        for (int i=0;i<Parameters.TNO_MAX;i++) {
            double temp= rm.nextGaussian();

            double tempcost=(b*temp);
//            System.out.println(temp+"\n"+b*temp+"\n"+tempcost);
//            tempcost+=a;
//            System.out.println(tempcost+"\n");
            while (tempcost>10000000||tempcost<=0){
                tempcost=Math.sqrt(b)*rm.nextGaussian()+a;
            }

            trade.setCost(tempcost);
            trade.setDate(new Date());
            tradeDao.addTrade(trade);
        }
    }
    public static void main(String[] args){
        GenetrateData genetrateData= new GenetrateData();
        genetrateData.genetrateData();
    }

}
