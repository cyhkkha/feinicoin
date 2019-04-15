package name.feinimouse.simplecoin.block;

import name.feinimouse.feinicoin.block.MerkelTree;
import name.feinimouse.feinicoin.block.MerkelTreeNode;

/**
 * Create by 菲尼莫斯 on 2019/4/15
 * Email: cyhkkha@gmail.com
 * File name: MerkelTreeImpl
 * Program : feinicoin
 * Description :
 */
public class MerkelTreeImpl <T extends MerkelTreeNode> implements MerkelTree<T> {
    @Override
    public String getRoot() {
        return null;
    }

    @Override
    public void push(T t) {

    }

    @Override
    public boolean createHash() {
        return false;
    }

    @Override
    public T get(String hash) {
        return null;
    }

    @Override
    public T get(int i) {
        return null;
    }

    @Override
    public int search(String hash) {
        return 0;
    }

    @Override
    public T remove(String hash) {
        return null;
    }

    @Override
    public T remove(int i) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
