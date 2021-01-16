package src.main.resources;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import src.main.java.ContractSamples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class GlobalControlImpl implements GlobalControl {
    public static final String temPath="E:\\Data\\tempData\\symbols.csv";
    @Override
    public String getTemPath() {
        return temPath;
    }
    @Override
    public void dataUpdate(EClientSocket eClientSocket, List<String> symbols, String duration, String barSize,String priceBar) {
        BarHandle barHandle=new BarHandleImpl();
        symbols.add(duration);
        symbols.add(priceBar);
        barHandle.saveStringList(symbols,temPath);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String formatted = form.format(cal.getTime());
        int id=100;
        for(int n=0;n<symbols.size()-2;n++){
            Contract contract=new Contract();
            String symbol=symbols.get(n);
            if(symbol=="YM"){
                contract=ContractSamples.SimpleFuture(symbol,"202103");
            }else if(symbol=="GC"){
                symbol=symbol+"G1";
                contract=ContractSamples.FutureWithLocalSymbol(symbol,"NYMEX");
            }else {
                symbol=symbol+"H1";
                contract=ContractSamples.FutureWithLocalSymbol(symbol,"GLOBEX");
            }
            eClientSocket.reqHistoricalData(id, contract,formatted, duration, barSize, "TRADES", 0, 1, false, null);
            id++;
        }

    }
}
