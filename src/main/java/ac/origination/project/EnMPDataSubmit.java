package ac.origination.project;

import ac.common.HashValue;
import ac.common.endecrpyt.DES;
import ac.origination.DataSubmit;
import ac.origination.EnItem;
import ac.origination.EnItemString;
import ac.origination.project.Dao.EnMPdataDao;
import ac.origination.project.Dao.MPdataDao;
import ac.origination.project.Models.EnMPData;
import ac.origination.project.Models.MPRawData;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EnMPDataSubmit implements DataSubmit {
    public static int enIdofFalse = -1;
    static int delatFalseHash=1;
    ApplicationContext applicationContext;
    MPdataDao mPdataDao;
    EnMPdataDao enMPdataDao;
    public static ValueBlockId valueBlockId = new ValueBlockId();
    public static HashValue hashValue = new MPHashValue();

    public static long mptime1;
    public static long mptime2;

    public static int enanddeTime = 0;
    public static int readInTime = 0;
    public static int processTime = 0;

    public EnMPDataSubmit() {

    }


    @Override
    public void copyFrom() {

        for (int i = 1; i <31515; i++) {
            System.out.println(i);
            List<MPRawData> mpRawDataList = mPdataDao.getListById("id", i);
            insertEnItem(mpRawDataList.get(0));
        }

        System.out.println("process time:\t" + processTime);
        System.out.println("enanddetime:\t" + enanddeTime);
        System.out.println("readandIotime:\t" + readInTime);

    }


    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        mPdataDao = (MPdataDao) applicationContext.getBean("mpDataDao");
        enMPdataDao = (EnMPdataDao) applicationContext.getBean("enMPDataDao");
    }


    public void insertEnItem(MPRawData mpRawData) {


        mptime1 = System.currentTimeMillis();
        EnMPData enMPData = getFistHandle(mpRawData);

//        System.out.println("raw data:\t"+mpRawData.toString());
//        System.out.println("ent data:\t"+enMPData.toString());
        mptime2 = System.currentTimeMillis();
        enanddeTime += mptime2 - mptime1;
        List<EnMPData> enMPDataList = enMPdataDao.getListById("a_blockid", enMPData.getA_blockId());
        mptime1 = System.currentTimeMillis();
        readInTime += mptime1 - mptime2;

        int A_blockid = enMPData.getA_blockId();
        if (valueBlockId.getAcc(A_blockid).equals(0.0)) {

            enMPDataList.add(enMPData);


            if (enMPDataList.size() > 1) {
                for (int i = 1; i < enMPDataList.size(); i++) {
                    enMPDataList.get(i).setA_hashpoint(hashValue.getHashValue(enMPDataList.get(i - 1).toStringVery()));
                }

                enMPDataList.get(0).setA_hashpoint(hashValue.getHashValue(enMPDataList.get(enMPDataList.size() - 1).toStringVery()));
            }
            mptime2 = System.currentTimeMillis();
            processTime += mptime2 - mptime1;
            enMPdataDao.insertItems(enMPDataList);
            mptime1 = System.currentTimeMillis();
            readInTime += mptime1 - mptime2;

            return;
        }

        List<InnerMP> innerMPList = new ArrayList<>();
        int beishu = valueBlockId.beiShu(mpRawData.getValue(), enMPData.getA_blockId());
        int times = ((int) (1 / (valueBlockId.getAcc(A_blockid)))) * 100;
        Double shu = (beishu * 100 + (int) ((valueBlockId.getFistValueInId(A_blockid)) * (times))) / (times * 1.0);

        InnerMP temp = new InnerMP();
        temp.setData(enMPData);
        temp.setValue(mpRawData.getValue());
        innerMPList.add(temp);


        List<Double> values = new ArrayList<>();
        values.add(mpRawData.getValue());

        mptime2 = System.currentTimeMillis();
        processTime += mptime2 - mptime1;
        //假信息在En_series_id存放”ThisIsAFalseData“作为标志
        //在 en_year中存放beishu
        for (EnMPData item : enMPDataList) {
            InnerMP tempthe = new InnerMP();
            if (!item.getEn_series_id().equals("ThisIsAFalseData")) {
                tempthe.setData(item);
                mptime1 = System.currentTimeMillis();
                tempthe.setValue(Double.valueOf(DES.decryptBasedDes(item.getEn_value())));
                mptime2 = System.currentTimeMillis();
                enanddeTime += mptime2 - mptime1;
            } else {
                mptime1 = System.currentTimeMillis();
                int thebeishu = Integer.valueOf(DES.decryptBasedDes(item.getEn_year()));
                mptime2 = System.currentTimeMillis();
                enanddeTime += mptime2 - mptime1;
//                int thebeishu=Integer.valueOf(Integer.valueOf(item.getEn_year()));

                //double tempValue= thebeishu*valueBlockId.getAcc(A_blockid)+valueBlockId.getFistValueInId(A_blockid);
                Double tempValue = (thebeishu * 100 + (int) ((valueBlockId.getFistValueInId(A_blockid)) * (times))) / (times * 1.0);

                tempthe.setData(item);
                tempthe.setValue(tempValue);
                mptime1 = System.currentTimeMillis();
                processTime += mptime1 - mptime2;
            }
            mptime1 = System.currentTimeMillis();
            values.add(tempthe.getValue());
            innerMPList.add(tempthe);
            mptime2 = System.currentTimeMillis();
            processTime += mptime2 - mptime1;
        }


        int temptime = 0;
        long time1 = System.currentTimeMillis();

//        for ( Double item :values) {
//            System.out.println(item);
//        }
//        System.out.println("\n");
        InnerMP tempthe = new InnerMP();


        if (values.contains(shu)) {

            if (shu.equals(mpRawData.getValue())) {
                if (beishu > 0) {
                    beishu = beishu - 1;
                }
                shu = (beishu * 100 + (int) ((valueBlockId.getFistValueInId(A_blockid)) * (times))) / (times * 1.0);
            }
            if (!values.contains(shu)) {
                EnMPData theen = new EnMPData();
                theen.setA_blockId(A_blockid);
                theen.setEn_series_id("ThisIsAFalseData");

                mptime1 = System.currentTimeMillis();
                theen.setEn_year(DES.encryptBasedDes(String.valueOf(beishu)));
                //theen.setEn_year(String.valueOf(beishu));
                theen.setEn_id(DES.encryptBasedDes(String.valueOf(enIdofFalse)));
                mptime2 = System.currentTimeMillis();
                temptime += mptime2 - mptime1;
                enIdofFalse -= 1;
                tempthe.setData(theen);
                tempthe.setValue(shu);
                innerMPList.add(tempthe);
            }
        } else {
            EnMPData theen = new EnMPData();
            theen.setA_blockId(A_blockid);
            theen.setEn_series_id("ThisIsAFalseData");
            mptime1 = System.currentTimeMillis();
            theen.setEn_year(DES.encryptBasedDes(String.valueOf(beishu)));
            theen.setEn_id(DES.encryptBasedDes(String.valueOf(enIdofFalse)));
            mptime2 = System.currentTimeMillis();
            temptime += mptime2 - mptime1;
            enIdofFalse -= 1;
            tempthe.setData(theen);
            tempthe.setValue(shu);
            innerMPList.add(tempthe);
        }


        Collections.sort(innerMPList, new Comparator<InnerMP>() {
            @Override
            public int compare(InnerMP o1, InnerMP o2) {
                if (o1.getValue() < o2.getValue()) {
                    return -1;
                } else if (o1.getValue().equals(o2.getValue()))
                    return 0;
                else return 1;
            }
        });
        enanddeTime += temptime;


        if (innerMPList.size() > 1) {
            for (int i = 1; i < innerMPList.size(); i++) {
                innerMPList.get(i).getData().setA_hashpoint(hashValue.getHashValue(innerMPList.get(i - 1).getData().toStringVery()));
            }
        }
        innerMPList.get(0).getData().setA_hashpoint(hashValue.getHashValue(innerMPList.get(innerMPList.size() - 1).getData().toStringVery()));

        int sum=0;



        List<EnMPData> enMPDataListIn = new ArrayList<>();
        for (InnerMP item : innerMPList) {
            if (item.getData().getEn_series_id().equals("ThisIsAFalseData")){
                sum+= EnMPDataSubmit.delatFalseHash;
            }else {
                sum+=hashValue.getHashValue(String.valueOf(item.getValue()));
            }
            item.getData().setA_hashvalue(sum);
            enMPDataListIn.add(item.getData());
        }
        mptime1 = System.currentTimeMillis();
        processTime += mptime1 - time1 - temptime;
        enMPdataDao.insertItems(enMPDataListIn);
        mptime2 = System.currentTimeMillis();
        readInTime += mptime2 - mptime1;

    }

    public EnMPData getFistHandle(MPRawData mpRawData) {

        EnMPData enMPData = new EnMPData();

        EnItemString enItemString = new EnItemString();
        enItemString.setEnItem(String.valueOf(mpRawData.getId()));
        enMPData.setEn_id(enItemString.getEnItem());

        enItemString.setEnItem(String.valueOf(mpRawData.getYear()));
        enMPData.setEn_year(enItemString.getEnItem());

        enItemString.setEnItem(mpRawData.getPeriod());
        enMPData.setEn_period(enItemString.getEnItem());

        enItemString.setEnItem(mpRawData.getSeries_id());
        enMPData.setEn_series_id(enItemString.getEnItem());

        enItemString.setEnItem(mpRawData.getFootnote_codes());
        enMPData.setEn_footnote_codes(mpRawData.getFootnote_codes());

        enItemString = new EnItem();
        ((EnItem) enItemString).setBd(valueBlockId);
        ((EnItem) enItemString).setHv(hashValue);
        enItemString.setEnItem(String.valueOf(mpRawData.getValue()));
        enMPData.setEn_value(enItemString.getEnItem());
        enMPData.setA_blockId(((EnItem) enItemString).getBlockId());
        enMPData.setA_hashvalue(((EnItem) enItemString).getHashValue());

        return enMPData;
    }


    class InnerMP {
        EnMPData data;
        Double value;

        public EnMPData getData() {
            return data;
        }

        public void setData(EnMPData data) {
            this.data = data;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        EnMPDataSubmit enMPDataSubmit = new EnMPDataSubmit();
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        enMPDataSubmit.setApplicationContext(applicationContext);
        enMPDataSubmit.copyFrom();
    }

}
