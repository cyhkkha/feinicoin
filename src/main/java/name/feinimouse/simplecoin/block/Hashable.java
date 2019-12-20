package name.feinimouse.simplecoin.block;

public interface Hashable {
    default String getHash() {
        return String.valueOf(this.hashCode());
    }
}