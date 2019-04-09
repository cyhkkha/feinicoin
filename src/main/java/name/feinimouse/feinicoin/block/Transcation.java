package name.feinimouse.feinicoin.block;

public abstract class Transcation {

    private String hash;
    private String from;
    private String to;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transcation) {
            return ((Transcation) obj).getHash() == this.hash;
        }
        return false;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    abstract protected void resetHash();

    public String getHash() {
        return hash;
    }


    
}