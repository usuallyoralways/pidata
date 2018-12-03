package ac.clouder;

import ac.client.QueryInput;
import ac.client.trade_table.HMacMD5;
import ac.client.trade_table.TradeBlockIdCost;
import ac.client.trade_table.TradeQueryInput;
import ac.clouder.trade_table.DAO.CEnTradeDaoImpl;
import ac.clouder.trade_table.DAO.Model.EnTrade;
import ac.clouder.trade_table.EnTradeQuery;
import ac.common.BlockId;
import ac.common.HashValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Query<T> {
    protected QueryInput input;
    protected ApplicationContext applicationContext ;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public QueryInput getInput() {
        return input;
    }

    public void setInput(QueryInput input) {
        this.input = input;
    }

    public List<T> getResults(){

        return null;
    }


    public static void main(String[] args){
        Query<EnTrade> query = new EnTradeQuery();
        QueryInput queryInput= new TradeQueryInput(Double.valueOf(1),Double.valueOf(800000));
        HashValue hv= new HMacMD5();
        BlockId bd= new TradeBlockIdCost();
        queryInput.setHv(hv);
        queryInput.setBd(bd);
        queryInput.init();
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        query.setApplicationContext(applicationContext);
        query.setInput(queryInput);
        System.out.println(queryInput);
        List<EnTrade> enTrades= query.getResults();
        System.out.println(enTrades.size()+"enter.size()");
        for (EnTrade item:enTrades) {
            System.out.println(item);
        }
    }
}
