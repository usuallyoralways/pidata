package ac.origination;
import ac.common.endecrpyt.DES;


//加密的信息

public class EnItemString<T> {
    String enItem;
    T data;
    public String getEnItem() {
        return enItem;
    }

    public void setEnItem(T data) {
        this.data=data;
//        enItem= String.valueOf(data);

        enItem= DES.encryptBasedDes(String.valueOf(data));
    }
    @Override
    public String toString() {
        return "enItem: "+enItem;
    }


}
