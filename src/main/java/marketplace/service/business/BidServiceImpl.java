package marketplace.service.business;

import marketplace.datastore.BidDataStore;
import marketplace.model.Bid;
import marketplace.model.Project;
import marketplace.service.auth.AuthorizationException;
import marketplace.service.auth.BidAuthorizationServiceImpl;
import marketplace.service.auth.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by xiaoyuliang on 4/6/18.
 */
@Service("BidService")
public class BidServiceImpl implements BidService {
    private final BidDataStore bidDataStore;
    private final AuthorizationService authorizationService;
    private final ProjectService projectService;

    @Autowired
    public BidServiceImpl(BidDataStore bidDataStore, ProjectService projectService) {
        this.bidDataStore = bidDataStore;
        this.projectService = projectService;
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

        Project project = projectService.getProjectById(bid.getProjectId());

        // check bid project existing
        if (project == null) {
            throw new IllegalArgumentException("The project id is not existing");
        }

        // check bid owner with the project owner
        if (project.getOwnerId().equals(bid.getOwnerId())) {
            throw new IllegalArgumentException("Project owner is not allowed to bid this project");
        }

        //check whether the project is expiring
        if (project.getExpireTime().compareTo(bid.getDate()) < 0) {
            throw new IllegalArgumentException("Project is expiring, cannot bid");
        }

        // check the bid price is valid
        if (bid.getBidPrice() > project.getBudget()) {
            throw new IllegalArgumentException("Bid price should smaller than the project budget");
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
    public Bid getBidWinnerForProject(UUID projectId) {
        List<Bid> bids = getBidByProjectId(projectId);
        if (bids != null) {
            Collections.sort(bids, (a, b) -> {
                if (a.getBidPrice() > b.getBidPrice()) {
                    return 1;
                } else if (a.getBidPrice() == b.getBidPrice()){
                    return 0;
                } else {
                    return -1;
                }
            });
            return bids.get(0);
        } else {
            return null;
        }

    }

    @Override
    public void deleteBid(UUID bidId, String userId) {
        if (getBidById(bidId) == null) {
            throw new IllegalArgumentException("Unknown bid id.");
        }
        if (!authorizationService.authorize(bidId, userId)) {
            throw new AuthorizationException();
        }
        bidDataStore.delete(bidId);
    }

}

