package test.origination;

import ac.origination.trade_table.DAO.Model.Trade;
import ac.origination.trade_table.DAO.TradeDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.origination.trade.TestEnDao.TestTheDao;
import test.origination.trade.TestHandleTrade;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");
        TestTheDao testTheDao=(TestTheDao)applicationContext.getBean("testTheDao");


        for (int i = 1; i <=10000; i++) {
            List<Trade> trade = tradeDao.getListById("tno", i);
            //System.out.println("要插入的数据\n" + trade.get(0).toString());
            if (trade.size()>0){
                TestHandleTrade testHandleTrade= new TestHandleTrade();
                testHandleTrade.setTrade(trade.get(0));
                testHandleTrade.setTestEnTrade();
                testTheDao.insert(testHandleTrade.getTestEnTrade());
            }
        }

    }
}
