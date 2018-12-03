package test.origination.trade;

import ac.client.trade_table.HMacMD5;

public class TestHashValue {
    public static Integer getTestAhash(String string){
//        if (HMacMD5.byteArrayToInt(string.getBytes())%2==0){
//            return 1;
//        }else {
//            return 2;
//        }

        if (string.hashCode()%2==0){
            return 1;
        }else
            return 2;

    }
    public static Double getTestBhash(String string){
//        if (HMacMD5.byteArrayToInt(string.getBytes())%2==0){
//            return 0.1;
//        }else {
//            return 0.2;
//        }
        if (string.hashCode()%2==0)
            return 0.1;
        else
            return 0.2;

    }

    public static void main(String[] args ){

    }
}
