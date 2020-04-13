package com.podowd.domain;

public class UserShare {

    private String coin;
    private long timestampSeconds;
    private String username;
    private String workername;
    private long validShares;
    private long invalidShares;
    private long proofOfWork;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public long getTimestampSeconds() {
        return timestampSeconds;
    }

    public void setTimestampSeconds(long timestampSeconds) {
        this.timestampSeconds = timestampSeconds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkername() {
        return workername;
    }

    public void setWorkername(String workername) {
        this.workername = workername;
    }

    public long getValidShares() {
        return validShares;
    }

    public void setValidShares(long validShares) {
        this.validShares = validShares;
    }

    public long getInvalidShares() {
        return invalidShares;
    }

    public void setInvalidShares(long invalidShares) {
        this.invalidShares = invalidShares;
    }

    public long getProofOfWork() {
        return proofOfWork;
    }

    public void setProofOfWork(long proofOfWork) {
        this.proofOfWork = proofOfWork;
    }

    @Override
    public String toString() {
        return "UserShare{" +
                "coin='" + coin + '\'' +
                ", timestampSeconds=" + timestampSeconds +
                ", username='" + username + '\'' +
                ", workername='" + workername + '\'' +
                ", validShares=" + validShares +
                ", invalidShares=" + invalidShares +
                ", proofOfWork=" + proofOfWork +
                '}';
    }
}
