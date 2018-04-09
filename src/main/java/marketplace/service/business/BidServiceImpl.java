package marketplace.service.business;

import marketplace.datastore.BidDataStore;
import marketplace.model.Bid;
import marketplace.service.auth.AuthorizationException;
import marketplace.service.auth.BidAuthorizationServiceImpl;
import marketplace.service.auth.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by xiaoyuliang on 4/6/18.
 */
public class BidServiceImpl implements BidService {
    private final BidDataStore bidDataStore;
    private final AuthorizationService authorizationService;

    public BidServiceImpl(BidDataStore bidDataStore) {
        this.bidDataStore = bidDataStore;
        this.authorizationService = new BidAuthorizationServiceImpl(bidDataStore);
    }

    @Override
    public UUID createBid(Bid bid, String ownerId) {
        if (bid.getBidId() != null) {
            throw new IllegalArgumentException("Bid to be created should not have id.");
        }
        if (bid.getOwnerId() != null && !bid.getOwnerId().equals("ownerId")) {
            throw new IllegalArgumentException("User cannot create bid for others");
        }
        UUID id = UUID.randomUUID();
        Bid persistedBid = Bid.createBid(bid, id, ownerId);
        bidDataStore.saveBid(persistedBid);

        return id;
    }

    @Override
    public List<Bid> getAllBids() {
        return bidDataStore.getAllBids();
    }

    @Override
    public Bid getBidById(UUID id) {
        return bidDataStore.getBidById(id);
    }

    @Override
    public List<Bid> getBidByUserId(String userid) {
        return bidDataStore.getBidByUserid(userid);
    }

    @Override
    public List<Bid> getBidByProjectId(UUID projectid) {
        return bidDataStore.getBidByProject(projectid);
    }

    @Override
    public String getBidWinnerForProject(UUID projectId) {
        List<Bid> bids = getBidByProjectId(projectId);
        Collections.sort(bids, (a, b) -> {
            if (b.getBidPrice() > a.getBidPrice()) {
                return 1;
            } else if (b.getBidPrice() == a.getBidPrice()){
                return 0;
            } else {
                return -1;
            }
        });
        if (bids != null) {
            return bids.get(0).getOwnerId();
        } else {
            return null;
        }
    }

    @Override
    public void deleteBid(UUID bidId, String userId) {
        if (getBidById(bidId) == null) {
            throw new IllegalArgumentException("Unknown bid id.");
        }
        if (!authorizationService.autohrizeBid(bidId, userId)) {
            throw new AuthorizationException();
        }
        bidDataStore.delete(bidId);
    }

}

