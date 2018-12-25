package ac.client.trade_table;

import ac.client.QueryInput;
import ac.client.trade_table.Dao.CEnTradeDao;
import ac.client.trade_table.Dao.Model.EnTrade;
import ac.client.trade_table.Dao.Model.Trade;
import ac.common.BlockId;
import ac.common.HashValue;
import ac.common.endecrpyt.DES;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryClient {
    // 搭建客户端
    public static void main(String[] args) throws IOException {

        // 1、创建客户端Socket，指定服务器地址和端口

        //下面是你要传输到另一台电脑的IP地址和端口

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        CEnTradeDao cEnTradeDao = (CEnTradeDao) applicationContext.getBean("cEnTradeDao");


        long timetime1=0;
        long timetime2=0;
//tno


                timetime1=System.currentTimeMillis();
        QueryInput<Integer> qi = new QueryInput(7, 20);
        HashValue hv = new HMacMD5();
        BlockId bd = new TradeBlockIdTno();
        qi.setHv(hv);
        qi.setBd(bd);
        qi.init();
        timetime2=System.currentTimeMillis();
        System.out.println(qi);
        List<EnTrade> enTrades= cEnTradeDao.query(qi,"a_blockid");
        System.out.println("pre process:"+(timetime2-timetime1));

////cost
        for (int i = 0; i < 10000000; i++) {
            int j=12;
            j=j*j;
        }

        //区间 20%，40%，60,，%80
        //10000000
        //19990000
        System.out.println("cost:");
        timetime1=System.currentTimeMillis();
        QueryInput<Double> cost = new QueryInput(1000.0, 7999000.0);
        HashValue costhv = new HMacMD5();
        BlockId cotbd = new TradeBlockIdCost();
        cost.setHv(costhv);
        cost.setBd(cotbd);
        cost.init();
        timetime2=System.currentTimeMillis();
        System.out.println(cost);
        List<EnTrade> costenTrades= cEnTradeDao.queryB(cost,"b_blockid");
        System.out.println("pre process:"+(timetime2-timetime1));
        List<Trade> trades = new ArrayList<>();
        Trade temp= new Trade();
        timetime1=System.currentTimeMillis();
        for (EnTrade item:costenTrades) {

            temp.setCost(Double.valueOf(DES.decryptBasedDes(item.getB())));
            temp.setTno(Integer.valueOf(DES.decryptBasedDes(item.getA())));
            temp.setDate(DES.decryptBasedDes(item.getC()));
            trades.add(temp);
        }
        System.out.println(costenTrades.size());
        timetime2=System.currentTimeMillis();
        int destime=0 ;
        destime+=(timetime2-timetime1);
        System.out.println("des process:"+destime);


        timetime1=System.currentTimeMillis();
        for (int i = 0; i < costenTrades.size(); i++) {
            if (costhv.getHashValue(String.valueOf(trades.get(i).getTno())+String.valueOf(trades.get(i).getCost()))==costenTrades.get(i).getB_hashpoint()){
                int j=12*12;
                Double A=12.0*12;
            }else {
                int j=12*12;
                Double A=12.0*12;
            }
        }
        timetime2=System.currentTimeMillis();
        System.out.println("check time:"+(timetime2-timetime1));


        
    }

}
