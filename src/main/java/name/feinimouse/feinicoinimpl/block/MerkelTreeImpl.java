package name.feinimouse.feinicoinimpl.block;

import name.feinimouse.feinicoin.block.MerkelTree;
import name.feinimouse.feinicoin.block.Hashable;

import java.util.List;

/**
 * Create by 菲尼莫斯 on 2019/4/15
 * Email: cyhkkha@gmail.com
 * File name: MerkelTreeImpl
 * Program : feinicoin
 * Description :
 */
public class MerkelTreeImpl <T extends Hashable> implements MerkelTree<T> {

    @Override
    public String getRoot() {
        return null;
    }

    @Override
    public void addChild(T t) {

    }

    @Override
    public void resetRoot() {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public List<T> getList() {
        return null;
    }

}
