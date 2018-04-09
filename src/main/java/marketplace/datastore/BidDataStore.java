package marketplace.datastore;

import marketplace.model.Bid;

import java.util.List;
import java.util.UUID;

/**
 * @author xiaoyuliang
 */
public interface BidDataStore {


    Bid getBidById(UUID bidId);

    List<Bid> getBidByUserid(String userid);

    List<Bid> getBidByProject(UUID projectid);

    void saveBid(Bid persistedBid);
    
    List<Bid> getAllBids();

    void delete(UUID bidId);


}
