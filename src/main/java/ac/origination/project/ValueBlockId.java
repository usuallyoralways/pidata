package ac.origination.project;

import ac.common.BlockId;
import ac.origination.BlockInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ValueBlockId extends BlockId<Double> {


    List<BlockInfo<Double>> blockInfoList;


    public ValueBlockId(){
        blockInfoList= new ArrayList<>();
        setBlockInfoList();
    }


    public List<BlockInfo<Double>> getBlockInfoList() {
        return blockInfoList;
    }

    public void setBlockInfoList() {
        try {
            File file = new File("data/mpValueBlock.infor");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line!=null) {
                String lines[] = line.split("\t");

                BlockInfo<Double> info = new BlockInfo<>(Integer.valueOf(lines[0]),Double.valueOf(lines[1]),Double.valueOf(lines[2]),Double.valueOf(lines[3]));
                blockInfoList.add(info);

                line=reader.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Collections.sort(blockInfoList, new Comparator<BlockInfo<Double>>() {
            @Override
            public int compare(BlockInfo<Double> o1, BlockInfo<Double> o2) {
                if (o1.getLeftValue()<o2.getLeftValue()){
                    return -1;
                }else
                    return 1;
            }
        });

        for (BlockInfo<Double> item:blockInfoList) {
            System.out.println(item);
        }


    }

    @Override
    public Double getAcc(int id) {

        for (BlockInfo<Double> item: blockInfoList) {
            if (item.getId()==id){
                return item.getAcc();
            }
        }
        return 0.0;
    }

    public static void main(String[] args){
        ValueBlockId valueBlockId= new ValueBlockId();
        valueBlockId.setBlockInfoList();
    }

    public int blockIdFunction(String data) {
        Double value=Double.valueOf(data);

        for (BlockInfo<Double> blockInfo:blockInfoList) {
            if ((value<blockInfo.getRightValue())||(value.equals(blockInfo.getRightValue())&&blockInfo.getLeftValue().equals(blockInfo.getRightValue()))){
                return blockInfo.getId();
            }
        }

        return -1;
    }

    public int beiShu(Double object,int id){
        Double fistValue = getFistValueInId(id);
        double acc=getAcc(id);
        if ((object).equals(fistValue))
            return 0;
        return (int) (((object) - fistValue) / acc);
    }

    public List<Integer> blockIdGetRange(String left, String right){return null;}

    @Override
    public int getNextBlockId(int blockId) {
        int flag=0;
        for (BlockInfo blockInfo:blockInfoList) {
            if (flag==1){
                return blockInfo.getId();
            }
            if (blockInfo.getId()==blockId){
                flag=1;
            }
        }
        return -1;
    }

    @Override
    public int getLastBlockId(int blockId) {

        int flag=0;
        for (int i=0;i<blockInfoList.size();i++) {
            if (blockInfoList.get(i).getId()==blockId){
                if (i>0){
                    return blockInfoList.get(i-1).getId();
                }else
                    return -1;
            }
        }
        return -1;
    }


    public Double getFistValueInId(int blockId){
        for (BlockInfo<Double> item:blockInfoList) {
            if (item.getId()==blockId){
                return item.getLeftValue();
            }
        }

        return 0.0;
    }

    public Double getLeftLimit(int blockId){
        for (BlockInfo<Double> item:blockInfoList) {
            if (item.getId()==blockId){
                return item.getLeftValue();
            }
        }
        return 0.0;
    }
    public Double getRightLimit(int blockId){
        for (BlockInfo<Double> item:blockInfoList) {
            if (item.getId()==blockId){
                return item.getRightValue();
            }
        }
        return 0.0;
    }


}
