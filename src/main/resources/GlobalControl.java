package src.main.resources;

import com.ib.client.EClientSocket;

import java.util.List;

public interface GlobalControl {
    String getTemPath();
    void dataUpdate(EClientSocket eClientSocket, List<String> symbols, String duration, String barSize,String priceBar);
}
