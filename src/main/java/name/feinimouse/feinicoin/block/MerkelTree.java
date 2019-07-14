package name.feinimouse.feinicoin.block;

import java.util.Collection;
import java.util.List;

// 默克尔树接口
public interface MerkelTree <T extends Hashable> extends Hashable {
    // 获得树根的hash
    String getRoot();

    // 添加孩子
    void addChild(T t);

    // 刷新树根的哈希
    void resetRoot();

    // 获取孩子的个数
    int size();

    // 清空
    void clear();
    
    List<T> getList();
}