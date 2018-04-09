package marketplace.datastore;

import marketplace.model.Bid;

import java.util.List;
import java.util.UUID;

/**
 * @author xiaoyuliang
 */
public interface BidDataStore {

    /**
     * Get a bid using bidid
     * @param bidId the bid id to be fetched
     * @return bid the new bid to be persisted
     */
    Bid getBidById(UUID bidId);

    /**
     * Get all bids for user
     * @param userid the user id to be fetched
     * @return all the bids belong to this user
     */
    List<Bid> getBidByUserid(String userid);

    /**
     * Get all the bids for this project
     * @param projectid the project id to be fetched
     * @return all the bids belong to this project
     */
    List<Bid> getBidByProject(UUID projectid);

    /**
     Persist a new bid
     *
     * @param persistedBid the new bid to be persisted
     */
    void saveBid(Bid persistedBid);

    /**
     * Get all the existing bids
     * @return all the bids
     */
    List<Bid> getAllBids();

    /**
     * Delete a bid using the bidid
     * @param bidId bidid to be delete
     */
    void delete(UUID bidId);


}
