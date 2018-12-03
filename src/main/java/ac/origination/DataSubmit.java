package ac.origination;

import ac.common.DAO.Dao;
import ac.common.HashValue;
import ac.origination.trade_table.DAO.Model.EnTrade;
import org.springframework.context.ApplicationContext;

import java.util.List;

public interface DataSubmit {
    void copyFrom();
   // void insertEnItem(Object object,Dao dao);
    void setApplicationContext(ApplicationContext applicationContext);

}
