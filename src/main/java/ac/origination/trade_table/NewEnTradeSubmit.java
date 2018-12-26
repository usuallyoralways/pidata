package ac.origination.trade_table;

import ac.common.BlockId;
import ac.common.HashValue;
import ac.common.endecrpyt.DES;
import ac.origination.DataSubmit;
import ac.origination.EnItem;
import ac.origination.EnItemString;
import ac.origination.trade_table.DAO.EnTradeDaoImpl;
import ac.origination.trade_table.DAO.Model.EnTrade;
import ac.origination.trade_table.DAO.Model.Trade;
import ac.origination.trade_table.DAO.TradeDaoImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class NewEnTradeSubmit implements DataSubmit {
    static long timeReadAndInsert = 0;    //  记录索取数据时间
    static long timeEnAndDe = 0;    //  记录加密解密时间
    static long timeProcess = 0;    //  记录后续处理时间
    static int ablockid = -1;
    long time1;
    long time2;
    static int addFalse=1234;
    static int FALSESHU=-1;

    EnTrade enTrade;
    Logger logger = Logger.getLogger(ac.origination.trade_table.EnTradeDataSubmit.class.getName());

    ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public EnTrade getEnTrade() {
        return enTrade;
    }

    public void setEnTrade(EnTrade enTrade) {
        this.enTrade = enTrade;
    }

    //数据加密变换


    //先按一个元素插入属性  // ret =0,插入首位置， ret=1, 插入尾位置 ret=2 即是首，又是尾巴 3中间位置
    public int insertItemByAID(Trade trade, List<EnTrade> enTrades, HashValue hv) {
        int ret = 3;
        //bl 要和在enTrades 中的标致相反，这样可以知道更新那些

        EnTrade insertEnItem = getFistHandle(trade);


        if (enTrades.size() == 0) {
            insertEnItem.setA_hashvalue(hv.getHashValue(String.valueOf(trade.getTno())));
            enTrades.add(insertEnItem);
            ret = 2;
            return ret;
        }

        List<inerCalssTno> inerCalssTnos = new ArrayList<>();

        for (int i = 0; i < enTrades.size(); i++) {

            inerCalssTno temp= new inerCalssTno();
            temp.setEnTrade(enTrades.get(i));
            temp.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(i).getA())));

            inerCalssTnos.add(temp);
        }



        int fisrtpoint = 0;
        if (trade.getTno() < inerCalssTnos.get(0).getTno()) {
            ret = 0;

            List<inerCalssTno> inerCalssTnoList = new ArrayList<>();

            inerCalssTno inerCalssTno=new inerCalssTno();
            inerCalssTno.setEnTrade(getFistHandle(trade));
            inerCalssTno.setTno(trade.getTno());

            inerCalssTnoList.add(inerCalssTno);

            inerCalssTnoList.addAll(inerCalssTnos);
            inerCalssTnos.clear();
            inerCalssTnos.addAll(inerCalssTnoList);


        } else if (trade.getTno() > inerCalssTnos.get(inerCalssTnos.size() - 1).getTno()) {
            ret = 1;
            enTrades.add(getFistHandle(trade));

            inerCalssTno inerCalssTno=new inerCalssTno();
            inerCalssTno.setEnTrade(getFistHandle(trade));
            inerCalssTno.setTno(trade.getTno());
            inerCalssTnos.add(inerCalssTno);

            fisrtpoint = enTrades.get(0).getA_hashpoint();
        } else {
            fisrtpoint = enTrades.get(0).getA_hashpoint();

            inerCalssTno inerCalssTno=new inerCalssTno();
            inerCalssTno.setEnTrade(getFistHandle(trade));
            inerCalssTno.setTno(trade.getTno());
            inerCalssTnos.add(inerCalssTno);
            Collections.sort(inerCalssTnos, new Comparator<inerCalssTno>() {
                @Override
                public int compare(inerCalssTno o1, inerCalssTno o2) {
                    return o1.getTno()-o2.getTno();
                }
            });
            ret = 3;
        }
        System.out.println("ddddd"+inerCalssTnos.size());

        List<EnTrade> results = new ArrayList<>();
        int hashvalue = hv.getHashValue(String.valueOf(inerCalssTnos.get(0).getTno()));
        EnTrade temp = inerCalssTnos.get(0).getEnTrade();
        temp.setA_hashvalue(hashvalue);
        temp.setA_hashpoint(fisrtpoint);
        results.add(temp);
        int sum = hashvalue;
        for (int i = 1; i < inerCalssTnos.size(); i++) {
            hashvalue = hv.getHashValue(String.valueOf(inerCalssTnos.get(i).getTno()));
            EnTrade temp1 = inerCalssTnos.get(i).getEnTrade();
            sum += hashvalue;
            temp1.setA_hashvalue(sum);
            temp1.setA_hashpoint(hv.getHashValue(inerCalssTnos.get(i).getEnTrade().toString() + inerCalssTnos.get(i - 1).getEnTrade()));
            results.add(temp1);
        }
        enTrades.clear();
        enTrades.addAll(results);

        return ret;
    }

    public EnTrade getFistHandle(Trade trade) {
        EnTrade enTrade = new EnTrade();

        EnItem<Integer> enItem = new EnItem<>();
        BlockId ttt = new TradeBlockIdTno();
        enItem.setBd(ttt);
        enItem.setEnItem(trade.getTno());


        enTrade.setA(enItem.getEnItem());
        enTrade.setA_blockid(enItem.getBlockId());
        enTrade.setA_hashvalue(0);

        EnItem enItem1 = new EnItem<Double>();
        TradeBlockIdCost ttt1 = new TradeBlockIdCost();
        enItem1.setBd(ttt1);
        enItem1.setEnItem(trade.getCost());


//        enTrade.setB(enItem1.getEnItem());
        enTrade.setB(String.valueOf(trade.getCost()));

        enTrade.setB_blockid(enItem1.getBlockId());

        enTrade.setB_hashvalue(-1);


        EnItemString<String> enItemString = new EnItemString();
        enItemString.setEnItem("2018-12-13");
        enTrade.setC(enItem.getEnItem());

        enTrade.setA_hashpoint(0);
        enTrade.setB_hashpoint(0);
        return enTrade;
    }

    //对B进行处理 ret=0 在区间前面，1在去见后面，2第一个数据，3,中间
    public int inserB(String enId, EnTradeDaoImpl enTradeDao, HashValue hv, List<EnTrade> result) {
        int ret;
        List<EnTrade> enTrades1 = enTradeDao.getListById("a", enId);
        System.out.println("INsertB:  enTades1.size: " + enTrades1.size());
        int bid = enTrades1.get(0).getB_blockid();
        TradeBlockIdCost.sum[bid]++;

        List<EnTrade> enTrades = enTradeDao.getListById("b_blockid", bid);
        System.out.println("INsertB:  enTades.size: " + enTrades.size());
        System.out.println("INsertB:  enTades.size: " + enTrades);
        double theValue = 0.0;
        if (enTrades.get(0).getB_hashvalue() == -1) {
            //新插入的数直
//a            theValue = Double.valueOf(DES.decryptBasedDes(enTrades.get(0).getB()));
            theValue=Double.valueOf(enTrades.get(0).getB());

        } else{
            System.out.println("！！！！！还真有！！！！！！！！！！！！！！！！！！");
            return -1;
        }
        BlockId<Double> blockId = new TradeBlockIdCost();
        //倍数和假直的值
        int beishu = blockId.beiShu(theValue);
        Double shu = (beishu) * Parameters.COST_ACC + blockId.getFistValueInId(bid);


        int flag = 0;//看是否需要插入假的值


        List<inerCalssCost> tempCosts = new ArrayList<>();
        inerCalssCost temp = new inerCalssCost();
        temp.setCost(theValue);
        temp.setEnTrade(enTrades.get(0));
        tempCosts.add(temp);


        double tempValue;

        enTrades.get(0).setB_hashvalue(0);
        for (int i = 1; i < enTrades.size(); i++) {
            if (!enTrades.get(i).getB().equals("ThisIsBFalseData")) {

//                tempValue = Double.valueOf(DES.decryptBasedDes(enTrades.get(i).getB()));
                tempValue = Double.valueOf(enTrades.get(i).getB());


            } else {
                //C存倍数
                int theBeishu;
                theBeishu = Integer.valueOf(enTrades.get(i).getC());
                if (theBeishu == beishu) {
                    flag = 1;
                    System.out.println("beishu===="+theBeishu+"..."+theBeishu+"\n");
                }
                tempValue = (theBeishu) * Parameters.COST_ACC + blockId.getFistValueInId(bid);


            }
            inerCalssCost inerCost = new inerCalssCost();
            inerCost.setEnTrade(enTrades.get(i));//0位置时新插入的数据
            inerCost.setCost(tempValue);
            tempCosts.add(inerCost);
        }

        if (flag == 0) {
            List<inerCalssCost> inerCalssCostList = new ArrayList<>();
            EnTrade enTrade = new EnTrade();
            enTrade.setB_blockid(bid);
            enTrade.setB("ThisIsBFalseData");
            enTrade.setA_blockid(-1);
            enTrade.setC(String.valueOf(beishu));
            enTrade.setA((String.valueOf(shu)));
            //FALSESHU--;


            temp = new inerCalssCost();
            temp.setEnTrade(enTrade);
            temp.setCost(shu);

            inerCalssCostList.add(temp);
            inerCalssCostList.addAll(tempCosts);
            tempCosts.clear();
            tempCosts.addAll(inerCalssCostList);
        }


        Collections.sort(tempCosts, new Comparator<inerCalssCost>() {
            public int compare(inerCalssCost o1, inerCalssCost o2) {
                return o1.getCost().compareTo(o2.getCost());
            }
        });
        List<inerCalssCost> inerCalssCostList = new ArrayList<>();

        int sumhash=0;
        if (!tempCosts.get(0).getEnTrade().getB().equals("ThisIsBFalseData")) {
            sumhash+=hv.getHashValue(String.valueOf(tempCosts.get(0).getCost()));
            tempCosts.get(0).getEnTrade().setB_hashvalue(sumhash);

            inerCalssCostList.add(tempCosts.get(0));
        } else {
            sumhash+=addFalse;
            tempCosts.get(0).getEnTrade().setB_hashvalue(sumhash);
        }


        for (int i = 1; i < tempCosts.size(); i++) {
            if (!tempCosts.get(i).getEnTrade().getB().equals("ThisIsBFalseData")) {
                sumhash+=hv.getHashValue(String.valueOf(tempCosts.get(i).getCost()));
                tempCosts.get(i).getEnTrade().setB_hashvalue(sumhash);
                inerCalssCostList.add(tempCosts.get(i));

            } else {
                sumhash+=addFalse;
                tempCosts.get(i).getEnTrade().setB_hashvalue(sumhash);
            }
        }


        List<Trade> tradeList = new ArrayList<>();
        Trade tempTrade = new Trade();
        for (int i = 0; i < inerCalssCostList.size(); i++) {
//            tempTrade.setTno(Integer.valueOf(DES.decryptBasedDes(inerCalssCostList.get(i).getEnTrade().getA())));
//            tempTrade.setCost(Double.valueOf(DES.decryptBasedDes(inerCalssCostList.get(i).getEnTrade().getB())));
//            tempTrade.setDate((DES.decryptBasedDes(inerCalssCostList.get(i).getEnTrade().getC())));
            tempTrade.setTno(Integer.valueOf(DES.decryptBasedDes(inerCalssCostList.get(i).getEnTrade().getA())));
            tempTrade.setCost(Double.valueOf(inerCalssCostList.get(i).getEnTrade().getB()));
            tempTrade.setDate((DES.decryptBasedDes(inerCalssCostList.get(i).getEnTrade().getC())));
            tradeList.add(tempTrade);
        }


        for (int i = 1; i < inerCalssCostList.size(); i++) {
            inerCalssCostList.get(i).getEnTrade().setB_hashpoint(hv.getHashValue(tradeList.get(i).toString() + tradeList.get(i - 1).toString()));
        }

        if (inerCalssCostList.size() == 1) {
            ret = 2;
        } else if (inerCalssCostList.get(0).getCost().equals(theValue)) {
            ret = 0;
        } else if (inerCalssCostList.get(inerCalssCostList.size() - 1).getCost().equals(theValue)) {
            ret = 1;
        } else {
            ret = 3;
        }



        for (int i = 0; i < tempCosts.size(); i++) {
            result.add(tempCosts.get(i).getEnTrade());
        }

        return ret;
    }

    public void insertEnItem1(String enId, EnTradeDaoImpl enTradeDao, HashValue hv) {

        List<EnTrade> enTrades = new ArrayList<>();
        int ret = inserB(enId, enTradeDao, hv, enTrades);


        int aid = enTrades.get(0).getB_blockid();

        if (ret == 0) {
            BlockId blockId = new TradeBlockIdCost();
            int bid = blockId.getLastBlockId(aid);
            List<EnTrade> enTrades1 = enTradeDao.getListById("b_blockid", bid);
            while (enTrades1.size() == 0) {
                bid = blockId.getLastBlockId(bid);
                enTrades1 = enTradeDao.getListById("b_blockid", bid);
            }

            int i,j;
            for(i=enTrades1.size()-1;i>=0;i--){
                if (!enTrades1.get(i).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }
            EnTrade enTrade = enTrades1.get(i);
            Trade trade1 = new Trade();
            trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));
//a            trade1.setCost(Double.valueOf(DES.decryptBasedDes(enTrade.getB())));
            trade1.setCost(Double.valueOf((enTrade.getB())));

            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));

            for(j=0;j<enTrades.size();j++){
                if (!enTrades.get(j).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }

            Trade trade2 = new Trade();
            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(j).getA())));
//a            trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(j).getB())));
            trade2.setCost(Double.valueOf(enTrades.get(j).getB()));

            trade2.setDate(DES.decryptBasedDes(enTrades.get(j).getC()));

            enTrades.get(j).setB_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));


        } else if (ret == 1) {
            BlockId blockId = new TradeBlockIdCost();
            int bid = blockId.getNextBlockId(aid);
            List<EnTrade> enTrades1 = enTradeDao.getListById("b_blockid", bid);
            while (enTrades1.size() == 0) {
                bid = blockId.getNextBlockId(bid);
                enTrades1 = enTradeDao.getListById("b_blockid", bid);
            }

            int i,j;

            for(i=0;i<enTrades1.size();i++){
                if (!enTrades1.get(i).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }

            EnTrade enTrade = enTrades1.get(i);
            Trade trade1 = new Trade();
            trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));
//a            trade1.setCost(Double.valueOf(DES.decryptBasedDes(enTrade.getB())));
            trade1.setCost(Double.valueOf(enTrade.getB()));

            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));


            for (j=enTrades.size()-1;j>=0; j--) {
                if (!enTrades.get(j).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }

            Trade trade2 = new Trade();
            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(j).getA())));
//a            trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(j).getB())));
            trade2.setCost(Double.valueOf(enTrades.get(j).getB()));

            trade2.setDate(DES.decryptBasedDes(enTrades.get(j).getC()));




            enTrades1.get(i).setB_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));

            enTradeDao.insertEnItems(enTrades1);
            enTradeDao.insertEnItems(enTrades);
        } else if (ret == 2) {
            BlockId blockId = new TradeBlockIdCost();
            int bid = blockId.getLastBlockId(aid);
            List<EnTrade> enTrades1 = enTradeDao.getListById("b_blockid", bid);

            while (enTrades1.size() == 0) {

                bid = blockId.getLastBlockId(bid);
                enTrades1 = enTradeDao.getListById("b_blockid", bid);
            }
            int i,j;
            for (i = enTrades1.size()-1; i>=0 ; i--) {
                if (!enTrades1.get(i).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }
            EnTrade enTrade = enTrades1.get(i);
            Trade trade1 = new Trade();
            trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));
//            trade1.setCost(Double.valueOf(DES.decryptBasedDes(enTrade.getB())));
            trade1.setCost(Double.valueOf((enTrade.getB())));

            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));


            for (j =0; j<enTrades.size() ; j++) {
                if (!enTrades.get(j).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }

            Trade trade2 = new Trade();
            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(j).getA())));
//            trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(j).getB())));
            trade2.setCost(Double.valueOf(enTrades.get(j).getB()));

            trade2.setDate(DES.decryptBasedDes(enTrades.get(j).getC()));

            enTrades.get(j).setB_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));


            ////////////////////
            bid = blockId.getNextBlockId(aid);
            enTrades1 = enTradeDao.getListById("b_blockid", bid);
            while (enTrades1.size() == 0) {
                bid = blockId.getNextBlockId(bid);
                enTrades1 = enTradeDao.getListById("b_blockid", bid);
            }

            for (i =0; i<enTrades1.size() ; i++) {
                if (!enTrades1.get(i).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }

            enTrade = enTrades1.get(i);
            trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));
            trade1.setCost(Double.valueOf(enTrade.getB()));

//            trade1.setCost(Double.valueOf(DES.decryptBasedDes(enTrade.getB())));
            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));



            for (j = enTrades.size()-1; j>=0 ; j--) {
                if (!enTrades.get(j).getB().equals("ThisIsBFalseData")){
                    break;
                }
            }

            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(j).getA())));
            trade2.setCost(Double.valueOf(enTrades.get(j).getB()));
  //          trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(j).getB())));

            trade2.setDate(DES.decryptBasedDes(enTrades.get(j).getC()));



            enTrades1.get(i).setB_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));

            enTradeDao.insertEnItems(enTrades1);

        }

        enTradeDao.insertEnItems(enTrades);

    }

    public void insertEnItem(Trade trade, EnTradeDaoImpl enTradeDao) {
        int aid = new TradeBlockIdTno().blockIdFunction(trade.getTno());
        List<EnTrade> enTrades = enTradeDao.getListById("a_blockid", aid);
        System.out.println("insert A befor:" + enTrades.size());
        HashValue hv = new HMacMD5();
        int ret = insertItemByAID(trade, enTrades, hv);
        TradeBlockIdTno.sum[aid]++;
        System.out.println("insert A afte:" + enTrades.size());


        if (ret == 0) {
            BlockId blockId = new TradeBlockIdTno();
            int bid = blockId.getLastBlockId(aid);
            List<EnTrade> enTrades1 = enTradeDao.getListById("a_blockid", bid);
            while (enTrades1.size() == 0) {
                bid = blockId.getLastBlockId(bid);
                enTrades1 = enTradeDao.getListById("a_blockid", bid);
            }
            EnTrade enTrade = enTrades1.get(enTrades1.size() - 1);
            Trade trade1 = new Trade();
            trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));
//a            trade1.setCost(Double.valueOf(DES.decryptBasedDes(enTrade.getB())));
            trade1.setCost(Double.valueOf(enTrade.getB()));

            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));

            Trade trade2 = new Trade();
            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(0).getA())));
//a            trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(0).getB())));
            trade2.setCost(Double.valueOf(enTrades.get(0).getB()));

            trade2.setDate(DES.decryptBasedDes(enTrade.getC()));

            enTrades.get(0).setA_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));


        } else if (ret == 1) {
            BlockId blockId = new TradeBlockIdTno();
            int bid = blockId.getNextBlockId(aid);
            List<EnTrade> enTrades1 = enTradeDao.getListById("a_blockid", bid);
            while (enTrades1.size() == 0) {
                bid = blockId.getNextBlockId(bid);
                enTrades1 = enTradeDao.getListById("a_blockid", bid);
            }
            EnTrade enTrade = enTrades1.get(0);
            Trade trade1 = new Trade();
           trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));

//a            trade1.setTno(Integer.valueOf(enTrade.getA()));
            trade1.setCost(Double.valueOf(enTrade.getB()));

            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));

            Trade trade2 = new Trade();
            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(enTrades.size() - 1).getA())));
//a            trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(enTrades.size() - 1).getB())));

            trade2.setCost(Double.valueOf(enTrades.get(enTrades.size() - 1).getB()));
            trade2.setDate(DES.decryptBasedDes(enTrade.getC()));

            enTrades1.get(0).setA_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));

            enTradeDao.insertEnItems(enTrades1);
            enTradeDao.insertEnItems(enTrades);
        } else if (ret == 2) {
            BlockId blockId = new TradeBlockIdTno();
            int bid = blockId.getLastBlockId(aid);
            List<EnTrade> enTrades1 = enTradeDao.getListById("a_blockid", bid);

            while (enTrades1.size() == 0) {

                bid = blockId.getLastBlockId(bid);

                enTrades1 = enTradeDao.getListById("a_blockid", bid);
            }
            EnTrade enTrade = enTrades1.get(enTrades1.size() - 1);
            Trade trade1 = new Trade();
            trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));
//a            trade1.setCost(Double.valueOf(DES.decryptBasedDes(enTrade.getB())));
            trade1.setCost(Double.valueOf(enTrade.getB()));

            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));

            Trade trade2 = new Trade();
            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(0).getA())));
// a           trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(0).getB())));
            trade2.setCost(Double.valueOf(enTrades.get(0).getB()));

            trade2.setDate(DES.decryptBasedDes(enTrade.getC()));

            enTrades.get(0).setA_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));


            bid = blockId.getNextBlockId(aid);
            enTrades1 = enTradeDao.getListById("a_blockid", bid);
            while (enTrades1.size() == 0) {

                bid = blockId.getNextBlockId(bid);
                enTrades1 = enTradeDao.getListById("a_blockid", bid);
            }
            enTrade = enTrades1.get(0);
            trade1.setTno(Integer.valueOf(DES.decryptBasedDes(enTrade.getA())));
//a            trade1.setCost(Double.valueOf(DES.decryptBasedDes(enTrade.getB())));
            trade1.setCost(Double.valueOf(enTrade.getB()));

            trade1.setDate(DES.decryptBasedDes(enTrade.getC()));

            trade2.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(enTrades.size() - 1).getA())));
//A            trade2.setCost(Double.valueOf(DES.decryptBasedDes(enTrades.get(enTrades.size() - 1).getB())));
            trade2.setCost(Double.valueOf(enTrades.get(enTrades.size() - 1).getB()));

            trade2.setDate(DES.decryptBasedDes(enTrade.getC()));

            enTrades1.get(0).setA_hashpoint(hv.getHashValue(trade1.toString() + trade2.toString()));

            enTradeDao.insertEnItems(enTrades1);

        }

        enTradeDao.insertEnItems(enTrades);


    }


    @Override
    public void copyFrom() {
        TradeBlockIdCost.initSum();
        TradeBlockIdTno.suminit();
        TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");
        EnTradeDaoImpl enTradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");

        HashValue hv = new HMacMD5();
//
//        //————————————————————————
//        int tno=2101;
//        List<Trade> trade = tradeDao.getListById("tno", tno);
//        System.out.println("copy__tno: "+(tno));
//        insertEnItem(trade.get(0), enTradeDao);
//        String ena = DES.encryptBasedDes(String.valueOf(trade.get(0).getCost()));
//        insertEnItem1(ena, enTradeDao, hv);
//        //——————————————————————————————————————————

        List<Trade> trades = tradeDao.getListById("tno", 1);

        BlockId<Integer> blockId = new TradeBlockIdTno();






        EnTrade enTrade = getFistHandle(trades.get(0));
        enTrade.setA_hashvalue(new HMacMD5().getHashValue(String.valueOf(trades.get(0).getTno())));

        List<EnTrade> enTrades = new ArrayList<>();
        enTrades.add(enTrade);
        enTradeDao.insertEnItems(enTrades);

        insertEnItem1(enTrade.getA(), enTradeDao, hv);


        int[] ids = {1, 10, 2, 3, 4, 5, 6, 7, 8, 9};



            for (int i = 2; i <=10000; i++) {

                List<Trade> trade = tradeDao.getListById("tno", i);
                System.out.println("copy__tno: "+(i));
                insertEnItem(trade.get(0), enTradeDao);
                String ena = DES.encryptBasedDes(String.valueOf(trade.get(0).getTno()));
                insertEnItem1(ena, enTradeDao, hv);
//                break;
            }


    }

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        NewEnTradeSubmit shi = new NewEnTradeSubmit();
        shi.setApplicationContext(applicationContext);
        long time1 = System.currentTimeMillis();
        shi.copyFrom();
        long time2 = System.currentTimeMillis();
        System.out.println("copyttime:" + (time2 - time1));
        System.out.println("timeEnAndDe:" + timeEnAndDe);
        System.out.println("timeReadAndInsert:" + timeReadAndInsert);
        System.out.println("timeProcess:" + timeProcess);

    }

    class inerCalssCost {
        EnTrade enTrade;
        Double cost;

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public void setEnTrade(EnTrade enTrade) {
            this.enTrade = enTrade;
        }

        public Double getCost() {
            return cost;
        }

        public EnTrade getEnTrade() {
            return enTrade;
        }
    }

    class inerCalssTno {
        EnTrade enTrade;
        Integer tno;

        public void setEnTrade(EnTrade enTrade) {
            this.enTrade = enTrade;
        }

        public void setTno(Integer tno) {
            this.tno = tno;
        }

        public EnTrade getEnTrade() {
            return enTrade;
        }

        public Integer getTno() {
            return tno;
        }
    }


}

