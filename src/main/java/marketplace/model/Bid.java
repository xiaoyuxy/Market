package marketplace.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Bid entity.
 * This class is immutable.
 *
 * @author Xiaoyu Liang
 */
public class Bid {
    @JsonIgnore
    private final UUID bidId;
    @JsonIgnore
    private final String ownerId;

    @JsonProperty("projectid")
    private UUID projectId;
    @JsonProperty("bidPrice")
    private double bidPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date date;

    public Bid(){
        bidId = null;
        ownerId = null;
    }
    public Bid(UUID id, UUID projectId, String ownerId, double bidPrice, Date date){
        this.projectId = projectId;
        this.bidId = id;
        this.ownerId = ownerId;
        this.bidPrice = bidPrice;
        this.date = date;
    }
    @JsonGetter("bidId")
    public UUID getBidId() {
        return bidId;
    }

    @JsonGetter("projectId")
    @JsonIgnore
    public UUID getProjectId() {
        return projectId;
    }

    @JsonGetter("ownerId")
    public String getOwnerId() {
        return ownerId;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "bidId=" + bidId +
                ", ownerId='" + ownerId + '\'' +
                ", projectId=" + projectId +
                ", bidPrice=" + bidPrice +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Double.compare(bid.bidPrice, bidPrice) == 0 &&
                Objects.equals(bidId, bid.bidId) &&
                Objects.equals(ownerId, bid.ownerId) &&
                Objects.equals(projectId, bid.projectId) &&
                Objects.equals(date, bid.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bidId, ownerId, projectId, bidPrice, date);
    }

    static public Bid createBid(Bid bid, UUID bidId, String ownerId){
        return new Bid(bidId, bid.projectId, ownerId, bid.bidPrice, bid.date);
    }
    public boolean isValidBid() {
        return projectId != null && bidPrice >= 0 && date != null;
    }
}
