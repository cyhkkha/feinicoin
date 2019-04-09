package name.feinimouse.feinicoin.block;

public class Block {
    private String hash;
    private String preHash;
    private long timestamp;
    private int nonce;
    private BlockBody body;

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the body
     */
    public BlockBody getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(BlockBody body) {
        this.body = body;
    }

    /**
     * @return the nonce
     */
    public int getNonce() {
        return nonce;
    }

    /**
     * @param nonce the nonce to set
     */
    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the preHash
     */
    public String getPreHash() {
        return preHash;
    }

    /**
     * @param preHash the preHash to set
     */
    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

}