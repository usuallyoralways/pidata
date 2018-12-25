package ac.origination.trade_table;

import ac.common.BlockId;
import ac.common.DAO.Dao;
import ac.common.endecrpyt.DES;
import ac.origination.DataSubmit;
import ac.origination.EnItem;
import ac.common.HashValue;
import ac.origination.EnItemString;
import ac.origination.trade_table.DAO.EnTradeDaoImpl;
import ac.origination.trade_table.DAO.Model.EnTrade;
import ac.origination.trade_table.DAO.Model.Trade;
import ac.origination.trade_table.DAO.TradeDaoImpl;
import org.checkerframework.checker.units.qual.A;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lijun on 2018/10/4.
 */
public class EnTradeDataSubmit implements DataSubmit {
    static long timeReadAndInsert=0;    //  记录索取数据时间
    static long timeEnAndDe=0;    //  记录加密解密时间
    static long timeProcess=0;    //  记录后续处理时间
    static int ablockid=-1;
    long time1;
    long time2;
    EnTrade enTrade;
    Logger logger = Logger.getLogger(EnTradeDataSubmit.class.getName());

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
    public EnTrade getFistHandle(Trade trade) {
        EnTrade enTrade = new EnTrade();

        EnItem enItem = new EnItem();
        BlockId ttt = new TradeBlockIdTno();
        enItem.setBd(ttt);
        enItem.setEnItem(String.valueOf(trade.getTno()));

        enTrade.setA(enItem.getEnItem());
        enTrade.setA_blockid(enItem.getBlockId());
        enTrade.setA_hashvalue(0);

        TradeBlockIdCost ttt1 = new TradeBlockIdCost();
        enItem.setBd(ttt1);
        enItem.setEnItem(String.valueOf(trade.getCost()));

        enTrade.setB(enItem.getEnItem());
        enTrade.setB_blockid(enItem.getBlockId());
        enTrade.setB_hashvalue(0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
        String dateString = formatter.format(trade.getDate());

        EnItemString enItemString = new EnItemString();
        enItemString.setEnItem(dateString);
        enTrade.setC(enItem.getEnItem());

        enTrade.setA_hashpoint(0);
        enTrade.setB_hashpoint(0)
        ;
        return enTrade;
    }

    //先按一个元素插入属性  // ret =4 ，即使有又是尾巴
    public int insertItemByAID(Trade trade, List<EnTrade> enTrades, HashValue hv) {
        boolean bl = false;

        time1=System.currentTimeMillis();
        EnTrade insertEnItem = getFistHandle(trade);
        time2=System.currentTimeMillis();
        timeEnAndDe+=time2-time1;


        if (enTrades.size() != 0) {
            bl = !enTrades.get(0).isA_flag();
        }

        insertEnItem.setA_flag(bl);


        int ret = 0;
        for (EnTrade item : enTrades) {
            item.setA_flag(bl);
        }
        if (enTrades.size() == 0) {
            insertEnItem.setA_hashvalue(hv.getHashValue(String.valueOf(trade.getTno())));
            enTrades.add(insertEnItem);
            ret = 3;
            return ret;
        }
        time1=System.currentTimeMillis();
        timeProcess+=time1-time2;

        List<inerCalssTno> tempTnos = new ArrayList<>();
        for (int i = 0; i < enTrades.size(); i++) {
            inerCalssTno tempClass = new inerCalssTno();
            tempClass.setEnTrade(enTrades.get(i));
            tempClass.setTno(Integer.valueOf(DES.decryptBasedDes(enTrades.get(i).getA())));
            tempTnos.add(tempClass);
        }
        time2=System.currentTimeMillis();
        timeEnAndDe+=time2-time1;

        //返回数据，返回的是1，则插入数据是分区最后一个， 0,，中间，2 前面,3,即使中间又是后面

        for (int i = 0; i < tempTnos.size(); i++) {
            //更新，或者和a的假数据重合
            if (tempTnos.get(i).getTno().equals(trade.getTno())) {
                tempTnos.get(i).setEnTrade(insertEnItem);
                break;
            }
            //新数据插入
            if (i == tempTnos.size() - 1) {
                ret = 1;
                inerCalssTno temp = new inerCalssTno();
                temp.setEnTrade(insertEnItem);
                temp.setTno(trade.getTno());
                tempTnos.add(temp);
                //排序
                Collections.sort(tempTnos, new Comparator<inerCalssTno>() {
                    public int compare(inerCalssTno o1, inerCalssTno o2) {
                        return o1.getTno() - (o2.getTno());
                    }
                });
            }
        }

        if (tempTnos.get(0).getTno().equals(trade.getTno())) {
            ret = 2;
        }
        int hashvalue = 0;
        for (inerCalssTno item : tempTnos) {

            int tempvalue = hv.getHashValue(String.valueOf(item.getTno()));
            hashvalue += tempvalue;
            item.getEnTrade().setA_hashvalue(hashvalue);
        }
        if (tempTnos.size()>1){
            for (int i = 1; i < tempTnos.size(); i++) {
                //hashpoint
                tempTnos.get(i).getEnTrade().setA_hashpoint(hv.getHashValue(tempTnos.get(i-1).getEnTrade().getA()+String.valueOf(tempTnos.get(i-1).getEnTrade().getA_hashvalue())));

            }
        }

        List<EnTrade> enTrades1 = new ArrayList<>();
        for (int i = 0; i < tempTnos.size(); i++) {
            enTrades1.add(tempTnos.get(i).getEnTrade());
        }
        enTrades.clear();
        for (EnTrade item : enTrades1) {
            enTrades.add(item);
        }
        time1=System.currentTimeMillis();
        timeProcess+=time1-time2;
        return ret;
    }


    public int beishuB(int bid, Double value) {
        BlockId blockId = new TradeBlockIdCost();
        Double fistValue = (Double) blockId.getFistValueInId(bid);
        if (value.equals(fistValue))
            return 0;
        return (int) ((value - fistValue) / Parameters.COST_ACC);
    }

    //按A插入后对
    public int doItemByBID(List<EnTrade> enTrades, HashValue hv) {


        Random rm= new Random();

        int bid = enTrades.get(0).getB_blockid();
        Double theValue = 0.0;
        //找到新插入的数据
        time1=System.currentTimeMillis();
        if (enTrades.get(0).getB_hashvalue() == 0) {
            theValue = Double.valueOf(DES.decryptBasedDes(enTrades.get(0).getB()));

        } else
            return -1;

        time2=System.currentTimeMillis();
        timeEnAndDe+=time2-time1;
        int beishu = beishuB(bid, theValue);
        //System.out.println(beishu+"beishu"+"\n"+enTrades.size());
        BlockId blockId = new TradeBlockIdCost();
        Double shu = (beishu) * Parameters.COST_ACC + (Double) blockId.getFistValueInId(bid);


        List<inerCalssCost> tempCosts = new ArrayList<>();

        List<Double> datas=new ArrayList<>();
        //List<Double> beishus=new ArrayList<>();
        Double tempValue;
        //第一个数据

        inerCalssCost inerCost1 = new inerCalssCost();
        inerCost1.setEnTrade(enTrades.get(0));//0位置时新插入的数据
        time1=System.currentTimeMillis();
        timeProcess+=time1-time2;
        tempValue = Double.valueOf(DES.decryptBasedDes(enTrades.get(0).getB()));
        time2=System.currentTimeMillis();
        timeEnAndDe+=time2-time1;
        inerCost1.setCost(tempValue);
        tempCosts.add(inerCost1);
        datas.add(tempValue);



        if (enTrades.size()>1) {
            for (int i = 1; i < enTrades.size(); i++) {

                time1=System.currentTimeMillis();
                if (!enTrades.get(i).getB().equals("ThisIsBFalseData")) {

                    tempValue = Double.valueOf(DES.decryptBasedDes(enTrades.get(i).getB()));
                    time2=System.currentTimeMillis();
                    timeEnAndDe+=time2-time1;
                    datas.add(tempValue);
                } else {
                    int theBeishu;
                    theBeishu = enTrades.get(i).getB_hashvalue() - enTrades.get(i - 1).getB_hashvalue();
                    //System.out.println("看倍数"+theBeishu+"\t"+enTrades.get(i).getB_hashvalue());

                    tempValue = (theBeishu) * Parameters.COST_ACC + (Double) blockId.getFistValueInId(bid);
                    datas.add(tempValue);
                    time2=System.currentTimeMillis();
                    timeProcess+=time2-time1;
                }
                time1=System.currentTimeMillis();
                inerCalssCost inerCost = new inerCalssCost();
                inerCost.setEnTrade(enTrades.get(i));//0位置时新插入的数据
                inerCost.setCost(tempValue);
                tempCosts.add(inerCost);
                time2=System.currentTimeMillis();
                timeProcess+=time2-time1;

            }
        }

        time1=System.currentTimeMillis();
        if (datas.contains(shu)) {

            if(shu.equals(theValue)){
                if (beishu>0) {
                    beishu--;
                }
                shu = (beishu) * Parameters.COST_ACC + (Double)     blockId.getFistValueInId(bid);

                //System.out.println(shu+"datas.contain"+datas.size()+beishuB(bid,shu)+"\t"+beishu);
                if (!datas.contains(shu)) {
                    EnTrade falsedata = new EnTrade();
                    falsedata.setB("ThisIsBFalseData");
                    falsedata.setA("justjoin");
                    falsedata.setA_blockid(ablockid);
                    ablockid--;
                    falsedata.setB_blockid(bid);
                    falsedata.setB_hashvalue(beishu);
                    enTrades.add(falsedata);

                    inerCalssCost inerCost = new inerCalssCost();
                    inerCost.setEnTrade(falsedata);//0位置时新插入的数据
                    inerCost.setCost(shu);
                    tempCosts.add(inerCost);
                }
            }
        }else {
            EnTrade falsedata = new EnTrade();
            falsedata.setB("ThisIsBFalseData");
            falsedata.setA("justjoin");
            falsedata.setA_blockid(ablockid);
            ablockid--;
            falsedata.setB_blockid(bid);
            falsedata.setB_hashvalue(beishu);
            enTrades.add(falsedata);


            inerCalssCost inerCost = new inerCalssCost();
            inerCost.setEnTrade(falsedata);//0位置时新插入的数据
            inerCost.setCost(shu);
            tempCosts.add(inerCost);

        }




        int aid = enTrades.get(0).getA_blockid();
        int ahash = enTrades.get(0).getA_hashvalue();

        Collections.sort(tempCosts, new Comparator<inerCalssCost>() {
            public int compare(inerCalssCost o1, inerCalssCost o2) {
                return o1.getCost().compareTo(o2.getCost());
            }
        });

//        for (int i = 0; i < tempCosts.size(); i++) {
//            System.out.println("tempCosts:"+tempCosts.get(i).getCost());
//        }
        int flag=0;

        for (int i = 0; i < tempCosts.size(); i++) {

            if (tempCosts.get(i).getEnTrade().getA_blockid() == aid &&
                    tempCosts.get(i).getEnTrade().getA_hashvalue() == ahash) {
                flag = i;
                break;
            }
        }
        int ret = 0;
//0 中间  1 后面  2 前面  3 都是
        if (flag==tempCosts.size()-1){
            if (tempCosts.size()==2)
                ret=3;
            else
                ret=1;
        }
        if (flag==2)
            ret=2;





        if (!tempCosts.get(0).getEnTrade().getB().equals("ThisIsBFalseData")) {
            tempCosts.get(0).getEnTrade().setB_hashvalue(hv.getHashValue(String.valueOf(tempCosts.get(0).getCost())));
        }else {
            tempCosts.get(0).getEnTrade().setB_hashvalue(beishuB(bid,tempCosts.get(0).getCost()));
        }

        if (tempCosts.size() > 1) {
            for (int i = 1; i < tempCosts.size(); i++) {
                if (!tempCosts.get(i).getEnTrade().getB().equals("ThisIsBFalseData")) {
                    tempCosts.get(i).getEnTrade().setB_hashvalue(tempCosts.get(i - 1).getEnTrade().getB_hashvalue() +
                            hv.getHashValue(String.valueOf(tempCosts.get(i).getCost())));
                } else {
                    tempCosts.get(i).getEnTrade().setB_hashvalue(tempCosts.get(i - 1).getEnTrade().getB_hashvalue() +
                            beishuB(bid,tempCosts.get(i).getCost()));
                }

                //hashpoint
                tempCosts.get(i).getEnTrade().setB_hashpoint(hv.getHashValue(tempCosts.get(i-1).getEnTrade().getB()+String.valueOf(tempCosts.get(i-1).getEnTrade().getB_hashvalue())));
            }

        }
        List<EnTrade> result = new ArrayList<>();

        for (int i = 0; i < tempCosts.size(); i++) {
            result.add(tempCosts.get(i).getEnTrade());
        }

        enTrades.clear();
        enTrades.addAll(result);
        time2=System.currentTimeMillis();
        timeProcess+=time2-time1;
        return ret;
    }


    public void insertEnItem(Object object,EnTradeDaoImpl enTradeDao) {


        //  a or b
        time1=System.currentTimeMillis();
        Trade trade = (Trade) object;
        int aid = new TradeBlockIdTno().blockIdFunction(trade.getTno());
        int bid = new TradeBlockIdCost().blockIdFunction(trade.getCost());
        time2=System.currentTimeMillis();
        timeProcess+=time2-time1;

        List<EnTrade> enTrades = enTradeDao.getListById("a_blockid", aid);
        time1=System.currentTimeMillis();
        timeReadAndInsert+=time1-time2;



//        System.out.println(Level.INFO.toString() + "A 插入前 {");
//        for (int i = 0; i < enTrades.size(); i++) {
//            System.out.println(enTrades.get(i));
//        }
//        System.out.println(Level.INFO.toString() + "A 插入前 }\n");
        boolean bl = true;
        if (enTrades.size() > 0) {
            bl = enTrades.get(0).isA_flag();
        }
        HashValue hv = new HMacMD5();


        //aret 标志插入数据的位置，0，中间 1，前面，2，后面

        time2=System.currentTimeMillis();
        timeProcess+=time2-time1;

        int aret = insertItemByAID((Trade) trade, enTrades, hv);
//        System.out.println(Level.INFO.toString() + "A 插入后 {");
//        for (int i = 0; i < enTrades.size(); i++) {
//            System.out.println(enTrades.get(i));
//        }
//        System.out.println(Level.INFO.toString() + "A 插入后 }\n");


        time1=System.currentTimeMillis();
        enTradeDao.insertEnItems(enTrades);
        enTradeDao.deleteByIdAndFlag("a_blockid", aid, bl, "a_flag");
        //按A插入后对
//更新hashpoint


        enTrades = enTradeDao.getListById("b_blockid", bid);

        time2=System.currentTimeMillis();
        timeReadAndInsert+=time2-time1;


        //System.out.println("bid:::" + bid);
//        for (EnTrade item:enTrades) {
//            System.out.println("look:"+item);
//        }

        doItemByBID(enTrades, hv);
//        System.out.println(enTrades.size()+"\n"+enTrades);

        time1 =System.currentTimeMillis();
        enTradeDao.insertEnItems(enTrades);
        time2=System.currentTimeMillis();
        timeReadAndInsert+=time2-time1;
    }


    public void handleAHP(HashValue hv){
        EnTradeDaoImpl enTradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        List<EnTrade> enTrades0=enTradeDao.getListById("a_blockid",0);
        int hashpoint=hv.getHashValue(enTrades0.get(enTrades0.size()-1).getA()+enTrades0.get(enTrades0.size()-1).getA_hashvalue());
        for (int i = 1; i <= Parameters.COST_N; i++) {
            //System.out.println(getClass().getName()+i+"AA");
            List<EnTrade> enTrades=enTradeDao.getListById("a_blockid",i);
            enTrades.get(0).setA_hashpoint(hashpoint);
            hashpoint=hv.getHashValue(enTrades.get(enTrades.size()-1).getA()+enTrades.get(enTrades.size()-1).getA_hashvalue());
            enTradeDao.insertEnItem(enTrades.get(0));
        }
        enTrades0.get(0).setA_hashpoint(hashpoint);
        enTradeDao.insertEnItem(enTrades0.get(0));
    }

    public void checkByA(HashValue hv){
        EnTradeDaoImpl enTradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        List<EnTrade> enTrades0=enTradeDao.getListById("a_blockid",0);
        int hashpoint=hv.getHashValue(enTrades0.get(enTrades0.size()-1).getA()+enTrades0.get(enTrades0.size()-1).getA_hashvalue());
        for (int i = 1; i <= Parameters.TNO_N; i++) {

            List<EnTrade> enTrades=enTradeDao.getListById("a_blockid",i);
            for (int j = 0; j < enTrades.size(); j++) {
                if (enTrades.get(j).getA_hashpoint()!=hashpoint){
                    System.out.println("AAA:a error："+i+"__"+j);
                }
                hashpoint=hv.getHashValue(enTrades.get(j).getA()+enTrades.get(j).getA_hashvalue());
            }
        }

    }

    public void checkByB(HashValue hv){
        EnTradeDaoImpl enTradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        List<EnTrade> enTrades0;
        int hashpoint, i;
        for (i = 0; ; i++) {
            enTrades0 = enTradeDao.getListById("b_blockid", 0);
            if (enTrades0.size() > 0) {
                hashpoint = hv.getHashValue(enTrades0.get(enTrades0.size() - 1).getB() + enTrades0.get(enTrades0.size() - 1).getB_hashvalue());
                break;
            }

        }
        i++;
        for (; i <= Parameters.COST_N; i++) {

            List<EnTrade> enTrades = enTradeDao.getListById("b_blockid", i);
            if (enTrades.size() > 0) {
                for (int j = 0; j < enTrades.size(); j++) {
                    if (enTrades.get(j).getB_hashpoint()!=hashpoint){
                        System.out.println("BBB:a error："+i+"__"+j);
                    }
                    hashpoint=hv.getHashValue(enTrades.get(j).getB()+enTrades.get(j).getB_hashvalue());
                }

            }
        }

    }

    public void handleBHP(HashValue hv) {
        EnTradeDaoImpl enTradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        List<EnTrade> enTrades0;
        int hashpoint, i;
        for (i = 0; ; i++) {
            enTrades0 = enTradeDao.getListById("b_blockid", 0);
            if (enTrades0.size() > 0) {
                hashpoint = hv.getHashValue(enTrades0.get(enTrades0.size() - 1).getB() + enTrades0.get(enTrades0.size() - 1).getB_hashvalue());
                break;
            }

        }
        i++;
        for (; i <= Parameters.COST_N; i++) {

            List<EnTrade> enTrades = enTradeDao.getListById("b_blockid", i);
            if (enTrades.size() > 0) {
                enTrades.get(0).setB_hashpoint(hashpoint);
                enTradeDao.insertEnItem(enTrades.get(0));
                hashpoint = hv.getHashValue(enTrades.get(enTrades.size() - 1).getB() + enTrades.get(enTrades.size() - 1).getB_hashvalue());
            }
        }
        enTrades0.get(0).setB_hashpoint(hashpoint);
        enTradeDao.insertEnItem(enTrades0.get(0));
    }

    public void copyFrom() {
        TradeDaoImpl tradeDao = (TradeDaoImpl) applicationContext.getBean("tradeDao");
        EnTradeDaoImpl enTradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        for (int i = 1; i <=10000; i++) {
            List<Trade> trade = tradeDao.getListById("tno", i);
            //System.out.println("要插入的数据\n" + trade.get(0).toString());
            insertEnItem(trade.get(0),enTradeDao);
        }
    }


    public void look(int bid){
        EnTradeDaoImpl enTradeDao = (EnTradeDaoImpl) applicationContext.getBean("entradeDao");
        List<EnTrade> enTrades = enTradeDao.getListById("b_blockid",bid);
        List<Double> datas=new ArrayList<>();
        List<inerCalssCost> tempCosts= new ArrayList<>();
        //List<Double> beishus=new ArrayList<>();
        Double tempValue;
        //第一个数据
        BlockId blockId= new TradeBlockIdCost();

        inerCalssCost inerCost1 = new inerCalssCost();

        if (!enTrades.get(0).getB().equals("ThisIsBFalseData")) {
            tempValue = Double.valueOf(DES.decryptBasedDes(enTrades.get(0).getB()));
        } else {
            int theBeishu;
            theBeishu = enTrades.get(0).getB_hashvalue();
            tempValue = (theBeishu) * Parameters.COST_ACC + (Double) blockId.getFistValueInId(bid);

        }
        inerCost1.setEnTrade(enTrades.get(0));
        inerCost1.setCost(tempValue);
        tempCosts.add(inerCost1);
        datas.add(tempValue);




        if (enTrades.size()>1) {
            for (int i = 1; i < enTrades.size(); i++) {

                if (!enTrades.get(i).getB().equals("ThisIsBFalseData")) {
                    tempValue = Double.valueOf(DES.decryptBasedDes(enTrades.get(i).getB()));
                    datas.add(tempValue);
                } else {
                    int theBeishu;
                    theBeishu = enTrades.get(i).getB_hashvalue() - enTrades.get(i - 1).getB_hashvalue();


                    tempValue = (theBeishu) * Parameters.COST_ACC + (Double) blockId.getFistValueInId(bid);
                    datas.add(tempValue);
                }
                inerCalssCost inerCost = new inerCalssCost();
                inerCost.setEnTrade(enTrades.get(i));//0位置时新插入的数据
                inerCost.setCost(tempValue);
                tempCosts.add(inerCost);
            }
        }



        System.out.println("\n\n\n");
        for (inerCalssCost item:tempCosts) {
            if (item.getEnTrade().getB().equals("ThisIsBFalseData")){
                System.out.println("ThisIsBFalseData"+"\n"+item.getCost()+"\n"+item.getEnTrade()+"\n");
            }else {
                System.out.println(DES.decryptBasedDes(item.getEnTrade().getA()));
                System.out.println(item.getEnTrade() + "\n" + item.getCost() + "\n");
            }
        }
    }
    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        EnTradeDataSubmit shi = new EnTradeDataSubmit();
        shi.setApplicationContext(applicationContext);
        long time1 =System.currentTimeMillis();
       shi.copyFrom();
       long time2 =System.currentTimeMillis();
       System.out.println("copyttime:" +(time2-time1));
       System.out.println("timeEnAndDe:"+timeEnAndDe);
       System.out.println("timeReadAndInsert:"+timeReadAndInsert);
       System.out.println("timeProcess:"+timeProcess);

       shi.look(234);
       HashValue hv= new HMacMD5();
       shi.handleAHP(hv);
       shi.handleBHP(hv);
        shi.checkByA(hv);
        shi.checkByB(hv);
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


}
