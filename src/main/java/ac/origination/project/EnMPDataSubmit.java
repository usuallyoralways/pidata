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

public class EnMPDataSubmit  implements DataSubmit {
    public static int enIdofFalse=-1;

    ApplicationContext applicationContext;
    MPdataDao mPdataDao;
    EnMPdataDao enMPdataDao;
    public static ValueBlockId valueBlockId = new ValueBlockId();
    public static HashValue hashValue= new MPHashValue();

    public EnMPDataSubmit(){

    }


    @Override
    public void copyFrom() {

        for (int i = 1; i <10 ; i++) {
            List<MPRawData> mpRawDataList= mPdataDao.getListById("id",i);
            System.out.println(mpRawDataList.get(0));
            System.out.println(getFistHandle(mpRawDataList.get(0))+"\n");
        }

    }


    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        mPdataDao = (MPdataDao) applicationContext.getBean("mpDataDao");
        enMPdataDao=(EnMPdataDao)applicationContext.getBean("enMPDataDao");
    }


    public void insertEnItem(MPRawData mpRawData){



        EnMPData enMPData=getFistHandle(mpRawData);
        int A_blockid=enMPData.getA_blockId();
        int beishu=valueBlockId.beiShu(mpRawData.getValue(),enMPData.getA_blockId());
        Double shu= beishu*valueBlockId.getAcc(A_blockid)+valueBlockId.getFistValueInId(A_blockid);

        List<EnMPData> enMPDataList= enMPdataDao.getListById("a_blockid",enMPData.getA_blockId());
        List<InnerMP> innerMPList=new ArrayList<>();
        InnerMP temp=new InnerMP();
        temp.setData(enMPData);
        temp.setValue(mpRawData.getValue());
        innerMPList.add(temp);


        List<Double> values = new ArrayList<>();
        values.add(mpRawData.getValue());

        //假信息在En_series_id存放”ThisIsAFalseData“作为标志
        //在 en_year中存放beishu
        for (EnMPData item:enMPDataList) {
            if (!item.getEn_series_id().equals("ThisIsAFalseData")){
                temp.setData(item);
                temp.setValue(Double.valueOf(DES.decryptBasedDes(item.getEn_value())));
            }else {
                int thebeishu=Integer.valueOf(DES.decryptBasedDes(item.getEn_year()));
                double tempValue= thebeishu*valueBlockId.getAcc(A_blockid)+valueBlockId.getFistValueInId(A_blockid);
                temp.setData(item);
                temp.setValue(tempValue);
            }
            values.add(temp.getValue());
            innerMPList.add(temp);
        }

        if (values.contains(shu)){
            if (shu.equals(mpRawData.getValue())){
                if (beishu>0){
                    beishu=beishu-1;
                }
                shu=beishu*valueBlockId.getAcc(A_blockid)+valueBlockId.getFistValueInId(A_blockid);
            }if (!values.contains(shu)){
                EnMPData theen = new EnMPData();
                theen.setA_blockId(A_blockid);
                theen.setEn_series_id("ThisIsAFalseData");
                theen.setEn_year(DES.encryptBasedDes(String.valueOf(beishu)));
                theen.setEn_id(DES.encryptBasedDes(String.valueOf(enIdofFalse)));
                enIdofFalse-=1;
                temp.setData(theen);
                temp.setValue(shu);
                innerMPList.add(temp);
            }
        }else {
            EnMPData theen = new EnMPData();
            theen.setA_blockId(A_blockid);
            theen.setEn_series_id("ThisIsAFalseData");
            theen.setEn_year(DES.encryptBasedDes(String.valueOf(beishu)));
            theen.setEn_id(DES.encryptBasedDes(String.valueOf(enIdofFalse)));
            enIdofFalse-=1;
            temp.setData(theen);
            temp.setValue(shu);
            innerMPList.add(temp);
        }

        Collections.sort(innerMPList, new Comparator<InnerMP>() {
            @Override
            public int compare(InnerMP o1, InnerMP o2) {
                if (o1.getValue()<o2.getValue()){
                    return -1;
                }else
                    return 1;
            }
        });

        if (innerMPList.size()>1){
            for(int i=1;i<innerMPList.size();i++){
                innerMPList.get(i).getData().setA_hashpoint(hashValue.getHashValue(innerMPList.get(i-1).getData().toStringVery()));
            }
        }
        else
            innerMPList.get(0).getData().setA_hashpoint(hashValue.getHashValue(innerMPList.get(innerMPList.size()-1).getData().toStringVery()));
        List<EnMPData> enMPDataListIn = new ArrayList<>();
        for (InnerMP item:innerMPList) {
            enMPDataListIn.add(item.getData());
        }

        enMPdataDao.insertItems(enMPDataListIn);

    }

    public EnMPData getFistHandle(MPRawData mpRawData) {

        EnMPData enMPData=new EnMPData();

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

        enItemString=new EnItem();
        ((EnItem) enItemString).setBd(valueBlockId );
        ((EnItem) enItemString).setHv(hashValue);
        enItemString.setEnItem(String.valueOf(mpRawData.getValue()));
        enMPData.setEn_value(enItemString.getEnItem());
        enMPData.setA_blockId(((EnItem) enItemString).getBlockId());
        enMPData.setA_hashvalue(((EnItem) enItemString).getHashValue());

        return enMPData;
    }


    class InnerMP{
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

    public static void main(String[] args){
        EnMPDataSubmit enMPDataSubmit = new EnMPDataSubmit();
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        enMPDataSubmit.setApplicationContext(applicationContext);
        enMPDataSubmit.copyFrom();
    }

}
