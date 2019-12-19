package name.feinimouse.feinicoinplus.core.crypt;


import de.greenrobot.common.hash.Murmur3A;
import de.greenrobot.common.hash.Murmur3F;
import name.feinimouse.feinicoinplus.core.obj.*;

import java.nio.charset.StandardCharsets;
import java.util.zip.Checksum;

public class MurmurHashGen implements HashGen {
    
    private Checksum murmur;
    
    public MurmurHashGen(int seed) {
        this(seed, false);
    }
    
    public MurmurHashGen(int seed, boolean isLong) {
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
    public String hash(SummaryObj t) {
        return hash(t.summary());
    }

    @Override
    public HashObj genHashObj(OrdinaryObj obj) {
        return new OrdinaryHashObj(obj, hash(obj.summary()));
    }

    @Override
    public HashObj genHashObj(HashObj[] objArr) {
        if (objArr.length == 1) {
            return new MerkelObj(objArr, new String[] { objArr[0].gainHash() });
        }
        String[] hashTree = new String[objArr.length * 2 - 1];
        for (int i = objArr.length - 1; i >= 0; i--) {
            hashTree[i] = objArr[i].gainHash();
        }
        genMerkelHash(0, hashTree);
        return new MerkelObj(objArr, hashTree);
    }
    

    private String genMerkelHash(int root, String[] hashTree) {
        if (2 * root > hashTree.length) {
            return hashTree[root];
        }
        return hash(genMerkelHash(2 * root, hashTree) + genMerkelHash(2 * root + 1, hashTree));
    }
    
}
