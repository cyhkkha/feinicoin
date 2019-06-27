package name.feinimouse.simplecoin;

import lombok.Getter;
import lombok.Setter;
import name.feinimouse.feinicoin.block.Hashable;
import name.feinimouse.feinicoin.block.MerkelTree;

import java.util.ArrayList;
import java.util.List;

public class SimpleMerkelTree implements MerkelTree<SimpleTrans> {
    private List<SimpleTrans> transList;
    private String root;
    
    public SimpleMerkelTree() {
        this.transList = new ArrayList<>();
    }
    
    @Override
    public String getRoot() {
        return null;
    }

    @Override
    public void addChild(SimpleTrans trans) {
        this.transList.add(trans);
    }

    @Override
    public String resetRoot() {
        return null;
    }

    @Override
    public int size() {
        return this.transList.size();
    }

    @Override
    public void clear() {

    }
}
