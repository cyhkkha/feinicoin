package name.feinimouse.feinicoin.block;

// 默克尔树接口
public interface MerkelTree <T> {
    // 获得树根的hash
    public String getRoot();
    // 加入元素
    public void push(T t);
    // 获取元素
    public T get(String hash);
    public T get(int i);
    // 搜索元素
    public int search(String hash);
    // 删除元素
    public int remove(String hash);
    public int remove(int i);
    // 获取总长度
    public int size();
}