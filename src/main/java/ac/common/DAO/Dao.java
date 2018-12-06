package ac.common.DAO;

import ac.origination.trade_table.DAO.Model.EnTrade;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;

public class Dao<T> extends JdbcDaoSupport {
    public void initDatabase(){
    }

    public List<T> getListById(String  id,int value){
        return null;
    }

    public void insertItem(T object){

    }
    public void insertItems(List<T> object){

    }
    public List<T> getListByIdOPValue(String  id, int value,String op){
        return null;
    }

    public List<Integer> getHashsByBlockId(int id){
        return null;
    }

    public List<T> getList(){
        return null;
    }

    //重新建表
    public void dropandCreate(){

    }

}
