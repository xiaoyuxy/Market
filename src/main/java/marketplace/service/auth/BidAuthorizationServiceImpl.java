package marketplace.service.auth;

import marketplace.datastore.BidDataStore;
import marketplace.model.Bid;

import java.util.UUID;

/**
 * @author xiaoyuliang
 */
public class BidAuthorizationServiceImpl implements AuthorizationService {
    private final BidDataStore bidDateStore;
    public BidAuthorizationServiceImpl(BidDataStore bidDataStore) {
        this.bidDateStore = bidDataStore;
    }

    @Override
    public boolean authorize(UUID bidId, String userId) {
        Bid bid = bidDateStore.getBidById(bidId);
        if (bid == null || !bid.getOwnerId().equals(userId)) {
            return false;
        }
        return true;
    }
}
