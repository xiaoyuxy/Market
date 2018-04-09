package marketplace.datastore;

import marketplace.datastore.BidDataStore;
import marketplace.model.Bid;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoyuliang
 */
@Repository("BidDataStore")
public class InMemoryBidDataStore implements BidDataStore {
    private final Map<UUID, Bid> idToBidMap;
    private final Map<String, List<Bid>> userToBidMap;
    private final Map<UUID, List<Bid>> projectToBidMap;

    public InMemoryBidDataStore() {
        this.idToBidMap = new ConcurrentHashMap<>();
        this.userToBidMap = new ConcurrentHashMap<>();
        this.projectToBidMap = new ConcurrentHashMap<>();
    }

    @Override
    public Bid getBidById(UUID bidId) {
        return idToBidMap.get(bidId);
    }

    @Override
    public List<Bid> getBidByUserid(String userid) {
        return userToBidMap.getOrDefault(userid, null);
    }

    @Override
    public void saveBid(Bid bid) {
        idToBidMap.put(bid.getBidId(), bid);

        String ownerId = bid.getOwnerId();
        List<Bid> bidForUser = userToBidMap.getOrDefault(ownerId, new ArrayList<>());
        bidForUser.add(bid);
        userToBidMap.put(ownerId, bidForUser);

        UUID projectid = bid.getProjectId();
        List<Bid> bidsForProject = projectToBidMap.getOrDefault(projectid, new ArrayList<>());
        bidsForProject.add(bid);
        projectToBidMap.put(projectid, bidsForProject);
    }

    @Override
    public List<Bid> getAllBids() {
        return new ArrayList<>(idToBidMap.values());
    }

    @Override
    public List<Bid> getBidByProject(UUID project) {
        return projectToBidMap.getOrDefault(project, null);
    }

    @Override
    public void delete(UUID bidId) {
        Bid bid = idToBidMap.get(bidId);
        List<Bid> bidsForUser = userToBidMap.get(bid.getOwnerId());
        int indexToRemove = 0;
        for (Bid b : bidsForUser) {
            if (b.getBidId().equals(bidId)) {
                break;
            }
            indexToRemove++;
        }
        bidsForUser.remove(indexToRemove);
        idToBidMap.remove(bidId);
    }
}
