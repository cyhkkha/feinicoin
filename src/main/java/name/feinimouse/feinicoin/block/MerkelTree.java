package name.feinimouse.feinicoin.block;

// 默克尔树接口
public interface MerkelTree <T extends MerkelTreeNode> {
    // 获得树根的hash
    String getRoot();
    // 加入元素
    void push(T t);
    // 添加完所有元素后重新生成树
    boolean createHash();
    // 获取元素
    T get(String hash);
    T get(int i);
    // 搜索元素
    int search(String hash);
    // 删除元素
    T remove(String hash);
    T remove(int i);
    // 获取总长度
    int size();
}