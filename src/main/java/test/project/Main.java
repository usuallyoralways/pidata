package test.project;

import ac.origination.BlockInfo;
import ac.origination.project.Dao.EnMPdataDao;
import ac.origination.project.Dao.MPdataDao;
import ac.origination.project.EnMPDataSubmit;
import ac.origination.project.Models.EnMPData;
import ac.origination.project.Models.MPRawData;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public void readTocsv(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        MPdataDao mPdataDao = (MPdataDao) applicationContext.getBean("mpDataDao");
        List<MPRawData> mpRawDataArrayList = new ArrayList<>();
        try {
            File file = new File( "mpdata.csv");
            Writer writer = new FileWriter(file, true);
            mpRawDataArrayList=mPdataDao.getList();
            System.out.println(mpRawDataArrayList.size());
            for (MPRawData item:mpRawDataArrayList) {
                writer.write(String.valueOf(item.getValue())+",\n");
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readTocsv2(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        EnMPdataDao enMPDataDao = (EnMPdataDao) applicationContext.getBean("enMPDataDao");
        List<EnMPData> enMPDataArrayList = new ArrayList<>();
        try {
            File file = new File( "enmpdata.csv");
            Writer writer = new FileWriter(file, true);
            List<Integer> ids = new ArrayList<>();
            for (BlockInfo item : EnMPDataSubmit.valueBlockId.getBlockInfoList()){
                ids.add(item.getId());
            }


            System.out.println(ids.get(ids.size()-1));

            int sum=0;
            int i=0;
            for(Integer id: ids){
                List<EnMPData> enMPDataList= enMPDataDao.getListById("a_blockid",id);
                for (EnMPData item: enMPDataList) {
                    if (!item.getEn_series_id().equals("ThisIsAFalseData")){
                        writer.write(String.valueOf(item.getA_hashvalue())+",\n");
                        sum++;
                    }
                }

            }

            System.out.println(sum);

            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        Main main= new Main();
        main.readTocsv2();
        main.readTocsv();
    }

}
