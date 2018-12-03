import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ZuoYe {

    public static  int maxSum(ArrayList<Integer> data){
        //d(j) = max data[1:i]
        ArrayList<Integer> dj = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            dj.add(0);
        }
        if (data.get(0)>0){
            dj.set(0,data.get(0));
        }
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i)>0){
                dj.set(i,Math.max(dj.get(i),data.get(i)+dj.get(i-1)));
            }

        }
        Collections.sort(dj);
        return dj.get(dj.size()-1);
    }

    public static void main(String args[]){
        Random rm = new Random();
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(rm.nextInt(20)-5);
            System.out.print(data.get(i)+" , ");
        }
        System.out.println();
        System.out.println(maxSum(data));
    }

}
