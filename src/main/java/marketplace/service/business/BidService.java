package marketplace.service.business;

import marketplace.model.Bid;
import marketplace.model.Project;

import java.util.List;
import java.util.UUID;

/**
 * Service to manage bids.
 *
 * Please note that we do not allow users to update a bid.
 * They should either delete a bid or create a new bid.
 *
 * @author Xiaoyu Liang
 */
public interface BidService {

    UUID createBid(Bid bid, String userId);

    List<Bid> getAllBids();

    Bid getBidById(UUID id);

    void deleteBid(UUID uuid, String userId);

    List<Bid> getBidByUserId(String userId);

    List<Bid> getBidByProjectId(UUID projectId);

    String getBidWinnerForProject(UUID projectId);
}

