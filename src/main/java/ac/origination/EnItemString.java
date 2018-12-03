package ac.origination;
import ac.common.endecrpyt.DES;


//加密的信息

public class EnItemString {
    String enItem;
    String data;
    public String getEnItem() {
        return enItem;
    }

    public void setEnItem(String data) {
        this.data=data;
        enItem= DES.encryptBasedDes(data);
    }
    @Override
    public String toString() {
        return "enItem: "+enItem;
    }


}
