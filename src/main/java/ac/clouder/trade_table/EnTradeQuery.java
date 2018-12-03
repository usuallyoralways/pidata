package ac.clouder.trade_table;

import ac.clouder.Query;
import ac.clouder.trade_table.DAO.CEnTradeDaoImpl;
import ac.clouder.trade_table.DAO.Model.EnTrade;
import ac.common.DAO.Dao;

import java.util.ArrayList;
import java.util.List;

public class EnTradeQuery extends Query{

    private void moveFalse(List<EnTrade> enTrades){
        List<EnTrade> results= new ArrayList<>();
        for (int i = enTrades.size()-1;i >0; i--) {
            if (enTrades.get(i-1).getB().equals("ThisIsBFalseData")){
                enTrades.get(i).setB_hashpoint(enTrades.get(i-1).getB_hashpoint());
                results.add(enTrades.get(i));
            }
        }
        enTrades.clear();
        enTrades.addAll(results);
    }

    public List<EnTrade> getResults(){
        CEnTradeDaoImpl enTradeDao = (CEnTradeDaoImpl) applicationContext.getBean("entradeDaoClouder");
        List<EnTrade> results= new ArrayList<>();
        if(input.getIds().size()!=0){
            for (int i = 0; i < input.getIds().size(); i++) {
                List<EnTrade> temp=enTradeDao.getListById("b_blockid",(Integer) input.getIds().get(i));
                moveFalse(temp);
                results.addAll(temp);
            }
        }
        if (input.getFistId()!=input.getLastId()){
            //找到右边的数据
            List<EnTrade> temp=enTradeDao.getListById("b_blockid",input.getLastId());
            List<EnTrade> tempresult=new ArrayList<>();
            int value=0;
            int lastHashPoint=0;
            for (int i = 0; i < temp.size(); i++) {
                value=temp.get(i).getB_hashvalue()-value;
                if (temp.get(i).getB().equals("ThisIsBFalseData")){
                    if (input.getLastHashBS()<value){
                        for (int j = 0; j <i ; j++) {
                            tempresult.add(temp.get(j));
                        }
                        moveFalse(tempresult);
                        results.addAll(tempresult);
                        if (i>0) {
                            lastHashPoint = temp.get(i-1).getB_hashpoint();
                        }
                        break;
                    }
                }else if (value==input.getLastHash()){
                    for (int j = 0; j <=i ; j++) {
                        tempresult.add(temp.get(j));
                    }
                    moveFalse(tempresult);
                    results.addAll(tempresult);
                    if (i<temp.size()-1){
                        lastHashPoint=temp.get(i+1).getB_hashpoint();
                    }
                    break;
                }
                value=temp.get(i).getB_hashvalue();
                if (i==temp.size()-1){
                    moveFalse(temp);
                    results.addAll(temp);
                }
            }

            //找到左边数据
            tempresult.clear();
            value=0;
            temp=enTradeDao.getListById("b_blockid",input.getFistId());
            for (int i = 0; i < temp.size(); i++) {
                value=temp.get(i).getB_hashvalue()-value;
                if (temp.get(i).getB().equals("ThisIsBFalseData")){
                    if (input.getFirstHashBS()<=value){
                        for (int j = i; j <temp.size() ; j++) {
                            tempresult.add(temp.get(j));
                        }
                        moveFalse(tempresult);
                        if(tempresult.size()>0) {
                            tempresult.get(0).setB_hashpoint(lastHashPoint);
                        }
                        results.addAll(tempresult);
                        break;
                    }
                }else if (value==input.getFirstHash()){
                    for (int j = i; j <temp.size() ; j++) {
                        tempresult.add(temp.get(j));
                    }
                    moveFalse(tempresult);
                    tempresult.get(0).setB_hashpoint(lastHashPoint);
                    results.addAll(tempresult);
                    break;
                }
                value=temp.get(i).getB_hashvalue();
            }

        }else {
            List<EnTrade> temp=enTradeDao.getListById("b_blockid",input.getLastId());
            int value=0;
            temp=enTradeDao.getListById("b_blockid",input.getLastId());
            int p=0,q=0,lastHashPoint=0;
            for (int i = 0; i < temp.size(); i++) {
                value=temp.get(i).getB_hashvalue()-value;
                if (temp.get(i).getB().equals("ThisIsBFalseData")){
                    if (input.getLastHashBS()<value){
                        p=i;
                        lastHashPoint=temp.get(i).getB_hashpoint();
                        break;
                    }
                }
                if (value==input.getLastHash()){
                    p=i;
                    if (i<temp.size()-1){
                        lastHashPoint=temp.get(i+1).getB_hashpoint();
                    }
                    break;
                }
                value=temp.get(i).getB_hashvalue();
            }
            for (int i = 0; i < temp.size(); i++) {
                value=temp.get(i).getB_hashvalue()-value;
                if (temp.get(i).getB().equals("ThisIsBFalseData")){
                    if (input.getFirstHashBS()<value){
                        q=i;
                        break;
                    }
                }
                if (value==input.getLastHash()){
                    q=i;
                    break;
                }
                value=temp.get(i).getB_hashvalue();
            }

            for (int i=p;i<=q;i++){
                results.add(temp.get(i));
            }
            moveFalse(results);
            results.get(0).setB_hashpoint(lastHashPoint);
        }
        return results;
    }
}
