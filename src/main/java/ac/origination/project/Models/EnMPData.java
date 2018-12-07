package ac.origination.project.Models;

public class EnMPData {
    String en_id;
    String en_series_id;
    String en_year;
    String en_period;
    String en_value;
    String en_footnote_codes;
    int a_blockId;   //value 的
    int a_hashvalue; //value的
    int a_hashpoint;    //value的

    public String getEn_id() {
        return en_id;
    }

    public void setEn_id(String en_id) {
        this.en_id = en_id;
    }

    public String getEn_series_id() {
        return en_series_id;
    }

    public void setEn_series_id(String en_series_id) {
        this.en_series_id = en_series_id;
    }

    public String getEn_year() {
        return en_year;
    }

    public void setEn_year(String en_year) {
        this.en_year = en_year;
    }

    public String getEn_period() {
        return en_period;
    }

    public void setEn_period(String en_period) {
        this.en_period = en_period;
    }

    public String getEn_value() {
        return en_value;
    }

    public void setEn_value(String en_value) {
        this.en_value = en_value;
    }

    public String getEn_footnote_codes() {
        return en_footnote_codes;
    }

    public void setEn_footnote_codes(String en_footnote_codes) {
        this.en_footnote_codes = en_footnote_codes;
    }

    public int getA_blockId() {
        return a_blockId;
    }


    public int  getA_hashvalue() {
        return a_hashvalue;
    }

    public void setA_blockId(int a_blockId) {
        this.a_blockId = a_blockId;
    }

    public void setA_hashvalue(int a_hashvalue) {
        this.a_hashvalue = a_hashvalue;
    }

    public int getA_hashpoint() {
        return a_hashpoint;
    }

    public void setA_hashpoint(int a_hashpoint) {
        this.a_hashpoint = a_hashpoint;
    }

    @Override
    public String toString() {
        return "EnMPData{" +
                "en_id='" + en_id + '\'' +
                ", en_series_id='" + en_series_id + '\'' +
                ", en_year='" + en_year + '\'' +
                ", en_period='" + en_period + '\'' +
                ", en_value='" + en_value + '\'' +
                ", footnote_codes='" + en_footnote_codes + '\'' +
                ", a_blockId=" + a_blockId +
                ", a_hashvalue=" + a_hashvalue +
                ", a_hashpoint=" + a_hashpoint +
                '}';
    }

    public String toStringVery() {
        return "EnMPData{" +
                "en_id='" + en_id + '\'' +
                ", en_series_id='" + en_series_id + '\'' +
                ", en_year='" + en_year + '\'' +
                ", en_period='" + en_period + '\'' +
                ", en_value='" + en_value + '\'' +
                ", footnote_codes='" + en_footnote_codes + '\'' +
                ", a_blockId=" + a_blockId +
                ", a_hashvalue=" + a_hashvalue +
                '}';
    }
}
