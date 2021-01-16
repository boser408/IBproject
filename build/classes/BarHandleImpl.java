package src.main.resources;

import com.ib.client.Bar;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BarHandleImpl implements BarHandle {
    @Override
    public void saveStringList(List<String> stringList, String savePath) {
        try{
            File writename = new File(savePath);
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            for(String string:stringList){
                out.write(string);
                out.newLine();
            }
            out.flush();
            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> readStringList(String savePath) {
         List<String> symbols=new ArrayList<>();
        try {
            File filename = new File(savePath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            String cvsSplitBy=",";
            while ((line = br.readLine())!= null) {
                String[] pricebar=line.split(cvsSplitBy);
                symbols.add(pricebar[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return symbols;
    }

    @Override
    public void saveBarList(List<com.ib.client.Bar> barList,String downloadPath) {
        try{
            File writename = new File(downloadPath);
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            int n=1;
            for(Bar bar:barList){
                out.write(n+","+bar.time()+","+bar.open()+","
                        +bar.high()+","+bar.low()+","+bar.close());
                out.newLine();
                n++;
            }
            out.flush();
            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
