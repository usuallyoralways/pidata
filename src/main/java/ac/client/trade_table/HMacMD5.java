package ac.client.trade_table;

import ac.common.HashValue;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Random;

public class HMacMD5 implements HashValue,Serializable
{
    public static final String KEY_MAC = "HmacMD5";
    String key;

    public HMacMD5(){
        key="010203";
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * HMAC加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {

        SecretKey secretKey = new SecretKeySpec(key.getBytes(), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }
    static String encodeBase64(byte[] source) throws Exception{
        return new String(Base64.encodeBase64(source), "UTF-8");
    }
    /*byte数组转换为HexString*/
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }
    public static String encryptHMAC2String(String  data, String key)throws Exception {
        byte[] b = encryptHMAC(data.getBytes("UTF-8"), key);
        return byteArrayToHexString(b);
    }

    public static int byteArrayToInt(byte[] b) {
        int result=0;
        for (int i = 0; i < b.length; i++) {
            int temp = (int)Math.pow(2,i)*(b[i] & 0xff);
            result += temp;
        }
        return result;
    }


    public  int getHashValue(String data) {
        try{
            return byteArrayToInt(encryptHMAC(data.getBytes(),key))%100000;
        }catch (Exception e){
            return -1;

        }
    }

    public static void main(String[] args) throws Exception{
        String str="340";
        HMacMD5 hm = new HMacMD5();
        Random rm= new Random();
        for (int i = 0; i < 340; i++) {
            System.out.println(hm.getHashValue(String.valueOf(i)));
        }

    }
}
