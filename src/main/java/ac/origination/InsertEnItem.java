package ac.origination;


import ac.common.DAO.Dao;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class InsertEnItem<T> {
    private EnItemString enitem;
    private String tableName;
    Dao dao;

    //得到相同桶id的元素
    public void getEnItemByBulkId(int id){

    }

    //处理数据
    public void dealData(){

    }
    //将数据插入
    public void InsertEnItems(){

    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public EnItemString getEnitem() {
        return enitem;
    }

    public void setEnitem(EnItemString enitem) {
        this.enitem = enitem;
    }
}
