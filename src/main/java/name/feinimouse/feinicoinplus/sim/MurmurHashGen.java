package name.feinimouse.feinicoinplus.sim;


import de.greenrobot.common.hash.Murmur3A;
import de.greenrobot.common.hash.Murmur3F;
import name.feinimouse.feinicoinplus.core.*;
import name.feinimouse.feinicoinplus.core.HashGen;
import name.feinimouse.feinicoinplus.core.data.Packer;
import name.feinimouse.feinicoinplus.core.data.PackerArr;

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
    public <T> HashObj hashObj(T obj, String summary) {
        Packer packer = new Packer(obj);
        packer.setHash(hash(summary));
        packer.setSummary(summary);
        return packer;
    }

    @Override
    public <T> HashObj hashObj(T[] objArr, String[] summaryArr) {
        if (objArr.length != summaryArr.length) {
            return null;
        }
        if (objArr.length == 0) {
            return null;
        }
        PackerArr packerArr = new PackerArr(objArr, objArr[0].getClass());
        packerArr.setSummaryArr(summaryArr);
        if (objArr.length == 1) {
            packerArr.setHashTree(new String[] { hash(summaryArr[0]) });
            return packerArr;
        }
        String[] hashTree = new String[objArr.length * 2 - 1];
        for (int i = objArr.length - 1; i >= 0; i--) {
            hashTree[i] = hash(summaryArr[i]);
        }
        genMerkelHash(0, hashTree);
        packerArr.setHashTree(hashTree);
        return packerArr;
    }
    

    private String genMerkelHash(int root, String[] hashTree) {
        if (2 * root > hashTree.length) {
            return hashTree[root];
        }
        return hash(genMerkelHash(2 * root, hashTree) 
            + genMerkelHash(2 * root + 1, hashTree));
    }
    
}
