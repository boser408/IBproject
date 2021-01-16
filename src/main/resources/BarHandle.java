package src.main.resources;

import com.ib.client.Bar;

import java.util.List;

public interface BarHandle {
    void saveStringList(List<String> stringList,String savePath);
    List<String> readStringList(String savePath);
    void saveBarList(List<Bar> barList,String downloadPath);

}
