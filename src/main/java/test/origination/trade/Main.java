package test.origination.trade;

import ac.origination.trade_table.DAO.Model.Trade;
import ac.origination.trade_table.DAO.TradeDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import test.origination.trade.Models.TestEnTrade;
import test.origination.trade.TestEnDao.TestTheDao;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static long testtime1 = 0;
    public static long testtime2 = 0;
    public static long testProcess = 0;
    public static long testEnAndDe = 0;
    public static long testRead = 0;

    public static void main(String[] args) {

        getAllData();
        setFile();
    }

    public static void setFile() {
        try {
            File file = new File("/home/lijun/桌面/data1.csv");
            Writer writer = new FileWriter(file, true);
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
            TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");
            TestTheDao testTheDao = (TestTheDao) applicationContext.getBean("testTheDao");
            List<Trade> trades = new ArrayList<>();
            trades = tradeDao.getListByIdLessValue("tno", 10000);
            Collections.sort(trades, new Comparator<Trade>() {
                @Override
                public int compare(Trade o1, Trade o2) {
                    if (o1.getCost() - o2.getCost() <= 0) {
                        return -1;
                    } else
                        return 1;

                }
            });
            for (int i = 0; i <10000 ; i++) {
                writer.append(String.valueOf(trades.get(i).getCost())+",");
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void testValue() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");
        TestTheDao testTheDao = (TestTheDao) applicationContext.getBean("testTheDao");
        List<Trade> trades = new ArrayList<>();
        trades = tradeDao.getListByIdLessValue("tno", 10000);
        Collections.sort(trades, new Comparator<Trade>() {
            @Override
            public int compare(Trade o1, Trade o2) {
                if (o1.getCost() - o2.getCost() <= 0) {
                    return -1;
                } else
                    return 1;

            }
        });

        System.out.println(trades.get(0));
        System.out.println(trades.get(1));
        System.out.println(trades.get(2));
        double bvalue=0,b=0;
        List<TestEnTrade> testEnTrades=new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            TestHandleTrade testHandleTrade = new TestHandleTrade(b,bvalue);
            testHandleTrade.setTrade(trades.get(i));
            System.out.println(i);


            testHandleTrade.setTestEnTrade();
            b=testHandleTrade.getB();
            bvalue=testHandleTrade.getBvalue();
            testTheDao.insert(testHandleTrade.getTestEnTrade());

        }

    }


    public static void getAllData(){
        try {
            File file = new File( "/home/lijun/桌面/data.csv");
            Writer writer = new FileWriter(file, true);
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
            TestTheDao testTheDao = (TestTheDao) applicationContext.getBean("testTheDao");
            List<TestEnTrade> testEnTrades = testTheDao.getListById();
            for (int i = 0; i < 10000; i++) {
                writer.append(String.valueOf(testEnTrades.get(i).getB_hashvalue())+",");
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void testTime() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");
        TestTheDao testTheDao = (TestTheDao) applicationContext.getBean("testTheDao");

        long time1 = System.currentTimeMillis();


        for (int i = 5000; i < 5100; i++) {
            testtime1 = System.currentTimeMillis();
            List<Trade> trade = tradeDao.getListById("tno", i);
            testtime2 = System.currentTimeMillis();
            testRead += testtime2 - testtime1;
            System.out.println("要插入的数据\n" + trade.get(0).toString());
            if (trade.size() > 0) {
                testtime1 = System.currentTimeMillis();

                TestHandleTrade testHandleTrade = new TestHandleTrade();
                testHandleTrade.setTrade(trade.get(0));

                testtime2 = System.currentTimeMillis();
                testProcess += testtime2 - testtime1;
                testHandleTrade.setTestEnTrade();
                testtime2 = System.currentTimeMillis();
                testProcess += testtime2 - testtime1;

                System.out.println(testHandleTrade.getTestEnTrade());

                testTheDao.insert(testHandleTrade.getTestEnTrade());
                testtime1 = System.currentTimeMillis();
                testRead += testtime1 - testtime2;
            }
        }
        long time2 = System.currentTimeMillis();
        System.out.println("timetotal:\t" + (time2 - time1));
        System.out.println("timeRead:\t" + testRead);
        System.out.println("timeEntrypt:\t" + testEnAndDe);
        System.out.println("timeProcess:\t" + testProcess);
    }

}
