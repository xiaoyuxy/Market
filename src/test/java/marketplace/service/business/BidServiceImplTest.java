package marketplace.service.business;

import marketplace.datastore.BidDataStore;
import marketplace.datastore.InMemoryBidDataStore;
import marketplace.model.Bid;
import marketplace.service.auth.AuthorizationException;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author xiaoyuliang
 */
public class BidServiceImplTest {

    private static final Date TEST_DATE1 = new Date(1523231387);
    private static final Date TEST_DATE2 = new Date(1525231387);
    private static final UUID PROJECT_ID = UUID.randomUUID();
    private static final UUID PROJECT_ID2 = UUID.randomUUID();


    private final BidServiceImpl bidService;

    public BidServiceImplTest() {
        BidDataStore bidDataStore = new InMemoryBidDataStore();
        bidService = new BidServiceImpl(bidDataStore);
    }

    @Test
    public void testCreateAndGet() throws Exception {
        Bid testBid1 = new Bid(null, PROJECT_ID, null, 100.0, TEST_DATE1);
        Bid testBid2 = new Bid(null, PROJECT_ID, null, 200.0, TEST_DATE1);
        Bid testBid3 = new Bid(null, PROJECT_ID2, null, 100.0, TEST_DATE1);


        // create a bid
        UUID bidId1 = bidService.createBid(testBid1, "user1");
        UUID bidId2 = bidService.createBid(testBid2, "user2");
        UUID bidId3 = bidService.createBid(testBid3, "user2");


        // Get bid by Id
        Bid bid1 = bidService.getBidById(bidId1);
        assertThat(bid1.getBidId(), Matchers.equalTo(bidId1));
        assertThat(bid1.getBidPrice(), Matchers.equalTo(100.0));
        assertThat(bid1.getOwnerId(), Matchers.equalTo("user1"));
        assertThat(bid1.getProjectId(), Matchers.equalTo(PROJECT_ID));

        Bid bid2 = bidService.getBidById(bidId2);
        assertThat(bid2.getBidId(), Matchers.equalTo(bidId2));
        assertThat(bid2.getBidPrice(), Matchers.equalTo(200.0));
        assertThat(bid2.getOwnerId(), Matchers.equalTo("user2"));
        assertThat(bid2.getProjectId(), Matchers.equalTo(PROJECT_ID));
        Bid bid3 = bidService.getBidById(bidId3);

        // Get all bids
        List<Bid> bids = bidService.getAllBids();
        assertThat(bids, Matchers.containsInAnyOrder(bid1, bid2, bid3));

        // Get all bids for user
        List<Bid> bidsForUser2 = bidService.getBidByUserId("user2");
        assertThat(bidsForUser2, Matchers.containsInAnyOrder(bid2, bid3));

        // Get all project for project
        List<Bid> bidsForProject = bidService.getBidByProjectId(PROJECT_ID);
        assertThat(bidsForProject, Matchers.containsInAnyOrder(bid1, bid2));
    }

    @Test
    public void testDelete() {
        Bid testBid = new Bid(null, PROJECT_ID, null, 100.0, TEST_DATE1);

        // create a bid
        UUID bidId = bidService.createBid(testBid, "user1");

        // delete bid
        bidService.deleteBid(bidId, "user1");

        Bid bid = bidService.getBidById(bidId);
        assertThat(bid, Matchers.nullValue());
    }

    /**
     * "user1" cannot create a bid with userid as "user2"
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUserCannotCreateBidForOthers() throws Exception {
        Bid bidForUser2 = new Bid(null, PROJECT_ID, "user2", 200.0, TEST_DATE1);
        bidService.createBid(bidForUser2, "user1");
    }

    /**
     * "user1" cannot delete "user2"'s bid
     */
    @Test(expected = AuthorizationException.class)
    public void testUserCannotDeleteBidForOthers() throws Exception {
        Bid testBid = new Bid(null, PROJECT_ID, null, 100.0, TEST_DATE1);

        // create a bid for user1
        UUID bidId = bidService.createBid(testBid, "user1");

        // user2 try delete user1's bid
        bidService.deleteBid(bidId, "user2");
    }


}
