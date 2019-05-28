package name.feinimouse.feinicoin.block;

public interface Hashable {
    default String getHash() {
        return String.valueOf(this.hashCode());
    }
}