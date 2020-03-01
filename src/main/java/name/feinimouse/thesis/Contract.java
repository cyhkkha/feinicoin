package name.feinimouse.thesis;

import lombok.Data;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unused")
@Data

public abstract class Contract {

    String name;
    String[] exParams;

    void active(Object carrier, Map<String, Object> exsMap) {
        if (check(carrier, exsMap)) {
            run(carrier, exsMap);
        }
    }

    abstract boolean check(Object carrier, Map<String, Object> exsMap);

    abstract void run(Object carrier, Map<String, Object> exsMap);
}
