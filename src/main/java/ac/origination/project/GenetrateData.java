package ac.origination.project;

import ac.origination.project.Dao.MPdataDao;
import ac.origination.project.Models.MPRawData;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenetrateData {
    public void genetrateData(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        MPdataDao mPdataDao = (MPdataDao) applicationContext.getBean("mpDataDao");
        mPdataDao.dropandCreate();
        List<MPRawData> MPRawDataList = new ArrayList<>();
        try {
            File file = new File("mp.data.1.AllData");

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line=reader.readLine();
            line=reader.readLine();
            while (line!=null){
                String temp[] =line.split("\\s+");
                MPRawData MPRawData = new MPRawData();
                MPRawData.setSeries_id(temp[0]);
                MPRawData.setYear(Integer.valueOf(temp[1]));
                MPRawData.setPeriod(temp[2]);
                MPRawData.setValue(Double.valueOf(temp[3]));
                MPRawData.setFootnote_codes("no");
                MPRawDataList.add(MPRawData);
                line=reader.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        mPdataDao.insertItems(MPRawDataList);


    }

    public static void main(String[] args){
        GenetrateData ge= new GenetrateData();
        ge.genetrateData();
    }

}
