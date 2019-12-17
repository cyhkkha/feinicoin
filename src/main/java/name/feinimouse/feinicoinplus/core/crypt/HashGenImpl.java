package name.feinimouse.feinicoinplus.core.crypt;


import de.greenrobot.common.hash.Murmur3A;
import de.greenrobot.common.hash.Murmur3F;
import name.feinimouse.feinicoinplus.core.block.HashObj;
import name.feinimouse.feinicoinplus.core.block.Jsobj;

import java.nio.charset.StandardCharsets;
import java.util.zip.Checksum;

public class HashGenImpl implements HashGen {
    
    private Checksum murmur;
    
    public HashGenImpl(int seed) {
        this(seed, false);
    }
    
    public HashGenImpl(int seed, boolean isLong) {
        if (isLong) {
            murmur = new Murmur3F(seed);
        } else {
            murmur = new Murmur3A(seed);
        }
    }

    @Override
    public String hash(String content) {
        murmur.reset();
        murmur.update(content.getBytes(StandardCharsets.UTF_8));
        return String.valueOf(murmur.getValue());
    }

    @Override
    public String hash(Jsobj t) {
        return hash(t.toJson().toString());
    }

    @Override
    public HashObj genHashObj(Jsobj t) {
        return new HashObj(t, hash(t));
    }
}
