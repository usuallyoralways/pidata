package ac.origination.project.Models;
//series_id	year	period	value	footnote_codes
public class RawData {
    int id;
    String series_id;
    int year;
    String period;
    double value;
    String footnote_codes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeries_id() {
        return series_id;
    }

    public void setSeries_id(String series_id) {
        this.series_id = series_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getFootnote_codes() {
        return footnote_codes;
    }

    public void setFootnote_codes(String footnote_codes) {
        this.footnote_codes = footnote_codes;
    }

    public static void main(String args[]){
        System.out.println("hello");
    }
}
