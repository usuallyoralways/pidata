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

    public void insertEnItem(T object){

    }

    public List<Integer> getHashsByBlockId(int id){
        return null;
    }

    public List<EnTrade> getList(){
        return null;
    }

}
