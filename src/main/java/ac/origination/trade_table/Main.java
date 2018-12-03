package ac.origination.trade_table;

import ac.common.DAO.Dao;
import ac.common.HashValue;
import ac.origination.trade_table.DAO.EnTradeDaoImpl;
import ac.origination.trade_table.DAO.Model.Trade;
import ac.origination.trade_table.DAO.TradeDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args){



        EnTradeDataSubmit etds= new EnTradeDataSubmit();
//        etds.copyFrom();
        //
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Dao<Trade> tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");
        EnTradeDaoImpl entradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        HashValue hv = new HMacMD5();



    }
}
