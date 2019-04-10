package name.feinimouse.feinicoin.block;

import name.feinimouse.feinicoin.account.Property;

public interface PropertyTree {
    public String getHeader();
    public void addProperty(Property p);
    public Property getProperty(String hash);
    public int searchProperty(String hash);
}