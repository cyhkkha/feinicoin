package name.feinimouse.feinicoinplus.core.data;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;

@Data
public class SimResult {
    private String name;
    private String xName;
    private String yName;
    private LinkedList<String> xList = new LinkedList<>();
    private HashMap<String, String> resultMap = new HashMap<>();

    public void put(String x, String y) {
        xList.add(x);
        resultMap.put(x, y);
    }
}
