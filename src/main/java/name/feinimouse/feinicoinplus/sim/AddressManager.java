package name.feinimouse.feinicoinplus.sim;

public interface AddressManager {
    void genAddress(int number);
    boolean put(String address);
    boolean contains(String address);
    String remove(String address);
    void returnAddress(String address);
    String getAddress();
    int size();
    int usingSize();
    int retainSize();
}
