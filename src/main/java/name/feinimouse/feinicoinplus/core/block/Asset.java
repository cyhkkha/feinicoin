package name.feinimouse.feinicoinplus.core.block;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class Asset extends Jsobj implements Cloneable{
    @Getter @Setter
    private String address;
    @Getter @Setter
    private String type;
    @Getter @Setter
    private String owner;
    @Getter @Setter
    private String issuer;
    @Getter @Setter
    private int number;
    @Getter @Setter
    private List<AssetHistory> histories;
    @Getter @Setter
    private Map<String, String> exFunc;

    @Override
    public JSONObject toJson() {
        return super.toJson()
            .put("histories", Jsobj.genJson(histories));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
