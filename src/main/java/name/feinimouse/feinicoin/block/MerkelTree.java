package name.feinimouse.feinicoin.block;

// 默克尔树接口
public interface MerkelTree <T extends MerkelTreeNode> {
    // 获得树根的hash
    public String getRoot();
    // 加入元素
    public void push(T t);
    // 添加完所有元素后重新生成树
    public boolean createHash();
    // 获取元素
    public T get(String hash);
    public T get(int i);
    // 搜索元素
    public int search(String hash);
    // 删除元素
    public T remove(String hash);
    public T remove(int i);
    // 获取总长度
    public int size();
}