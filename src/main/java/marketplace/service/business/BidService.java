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
    /**
     * Create a new Bid
     * @param bid the detail of this bid
     * @param userId the owner of this bid
     * @return the bidid that created
     */
    UUID createBid(Bid bid, String userId);

    /**
     * Get all the existing bids
     * @return all the bids
     */
    List<Bid> getAllBids();

    /**
     * Get the bid using the bidid
     * @param id the bidid to be get
     * @return the bid
     */
    Bid getBidById(UUID id);

    /**
     * Delete the bid using uuid
     * @param uuid the bid id to be deleted
     * @param userId the owner of this delete action
     */
    void deleteBid(UUID uuid, String userId);

    /**
     * Get all bids using the userid
     * @param userId the user's id
     * @return all the bids
     */
    List<Bid> getBidByUserId(String userId);

    /**
     * Get all bids for this project
     * @param projectId the project id
     * @return all the bids for this project
     */
    List<Bid> getBidByProjectId(UUID projectId);

    /**
     * Get the winner of this project
     * @param projectId the project id
     * @return the winner bid
     */
    Bid getBidWinnerForProject(UUID projectId);
}

