package ac.origination.project.Models;

public class EnData {
    String  en_id;
    String en_series_id;
    String en_year;
    String en_period;
    String en_value;
    String footnote_codes;
    double a_blockId;   //value 的
    double a_hashvalue; //value的

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

    public String getFootnote_codes() {
        return footnote_codes;
    }

    public void setFootnote_codes(String footnote_codes) {
        this.footnote_codes = footnote_codes;
    }

    public double getA_blockId() {
        return a_blockId;
    }

    public void setA_blockId(double a_blockId) {
        this.a_blockId = a_blockId;
    }

    public double getA_hashvalue() {
        return a_hashvalue;
    }

    public void setA_hashvalue(double a_hashvalue) {
        this.a_hashvalue = a_hashvalue;
    }
}
