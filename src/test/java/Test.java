import ac.common.BlockId;
import ac.origination.trade_table.DAO.EnTradeDaoImpl;
import ac.origination.trade_table.DAO.Model.Trade;
import ac.origination.trade_table.DAO.TradeDaoImpl;
import ac.origination.trade_table.EnTradeDataSubmit;
import ac.origination.trade_table.HMacMD5;
import ac.origination.trade_table.TradeBlockIdCost;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Test {


    public static void main(String args[]) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");

        BlockId<Double> blockId=new TradeBlockIdCost();
        System.out.println(blockId.beiShu(8191651.5 ));
        System.out.println(blockId.beiShu(8192715.0  ));

    }

};
