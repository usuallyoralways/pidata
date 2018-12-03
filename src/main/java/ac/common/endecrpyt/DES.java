package ac.common.endecrpyt;

import ac.common.Parameters;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DES {
    Parameters aa = new Parameters();


    /**
     * 数据加密，算法（DES）
     *
     * @param data
     *            要进行加密的数据
     * @return 加密后的数据
     */
    @SuppressWarnings("restriction")
    public static String encryptBasedDes(String data) {
        String encryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(Parameters.DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 加密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key, sr);
            // 加密，并把字节数组编码成字符串
            encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
//            log.error("加密错误，错误信息：", e);
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * 数据解密，算法（DES）
     *
     * @param cryptData
     *            加密数据
     * @return 解密后的数据
     */
    @SuppressWarnings("restriction")
    public static String decryptBasedDes(String cryptData) {
        String decryptedData = null;
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(Parameters.DES_KEY);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            // 解密对象
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            // 把字符串解码为字节数组，并解密
            decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));
        } catch (Exception e) {
            // log.error("解密错误，错误信息：", e);
            System.out.println(cryptData);
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }




    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String[] tnos={"1","2","3","4","5"};
        String[] costs={"567.131","12.842","456.212","34.623","234.124"};
        String[] dates ={"2018.09.06",
                "2018.09.07",
                "2018.09.08",
                "2018.09.09",
                "2018.09.10"};


        long startTime= System.currentTimeMillis();

        for (int i = 0; i < 5; i++) {
            String str=tnos[i];

            System.out.println("str:    "+str);
            // DES数据加密
            String s1=DES.encryptBasedDes(str);

            System.out.println(s1);


            long endTime=System.currentTimeMillis();
        }

        for (int i = 0; i < 5; i++) {
            String str=costs[i];

            System.out.println("str:    "+str);
            // DES数据加密
            String s1=DES.encryptBasedDes(str);

            System.out.println(s1);


            long endTime=System.currentTimeMillis();
        }

        for (int i = 0; i < 5; i++) {
            String str=dates[i];

            System.out.println("str:    "+str);
            // DES数据加密
            String s1=DES.encryptBasedDes(str);

            System.out.println(s1);


            long endTime=System.currentTimeMillis();
        }



    }


}
