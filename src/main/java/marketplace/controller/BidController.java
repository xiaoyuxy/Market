package marketplace.controller;

import marketplace.datastore.BidDataStore;
import marketplace.datastore.InMemoryBidDataStore;
import marketplace.model.Bid;
import marketplace.service.InputValidationService;
import marketplace.service.auth.AuthenticationService;
import marketplace.service.auth.FakeAuthenticationService;
import marketplace.service.business.BidService;
import marketplace.service.business.BidServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * @author Xiaoyu Liang
 */
@RestController
@RequestMapping("/bid")
public class BidController {

    private static final Logger logger = LoggerFactory.getLogger(BidController.class);
    private final BidService bidService;
    private final AuthenticationService authenticationService;

    @Autowired
    public BidController(JdbcTemplate jdbcTemplate) {
        //BidDataStore bidDataStore = new InMemoryBidDataStore();//new DbProjectDataStore(jdbcTemplate);
        this.bidService = new BidServiceImpl();
        this.authenticationService = new FakeAuthenticationService();
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> createBid(@RequestBody Bid bid, @RequestHeader("user-api-key") String apiKey, UriComponentsBuilder ucBuilder) {
        logger.info("Create bid: {}, owner: {}", bid, apiKey);

        String userId = authenticationService.authenticate(apiKey);

        if (!InputValidationService.validateBidForCreate(bid)) {
            logger.warn("Unable to create this bid. Bid not valid {}", bid);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        UUID bidId = bidService.createBid(bid, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/bid/{bidId}").buildAndExpand(bidId.toString()).toUri());

        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Bid>> getAllBids() {
        logger.info("Get all the bids");
        List<Bid> bids =bidService.getAllBids();

        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{bidId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getBidById(@PathVariable("bidId") String bidId) {
        if (!InputValidationService.validateBidUUID(bidId)) {
            logger.error("Invalid input uuid {}", bidId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        logger.info("Get the Bid with bid id {}", bidId);
        Bid bid = bidService.getBidById(UUID.fromString(bidId));

        if (bid == null) {
            logger.error("Unable to find bid with id {}", bidId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bid, HttpStatus.OK);
    }
    @RequestMapping(value = "/user/{ownerId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Bid>> getBidByUser(@PathVariable("ownerId") String ownerId) {

        logger.info("Get the Bid with bid id {}", ownerId);
        List<Bid> bid = bidService.getBidByUserId(ownerId);

        if (bid == null) {
            logger.error("Unable to find bid with id {}", ownerId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bid, HttpStatus.OK);
    }
    @RequestMapping(value = "/project/{projectid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Bid>> getBidByProject(@PathVariable("projectid") String projectid) {

        logger.info("Get the Bid with project {}", projectid);
        List<Bid> bid = bidService.getBidByProjectId(UUID.fromString(projectid));

        if (bid == null) {
            logger.error("Unable to find bid with project {}", projectid);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bid, HttpStatus.OK);
    }

    @RequestMapping(value = "/{bidId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> deleteBid(@PathVariable("bidId") String bidId,
                                           @RequestHeader("user-api-key") String apiKey) {
        logger.info("Delete the Bid: {}", bidId);
        String userId = authenticationService.authenticate(apiKey);

        bidService.deleteBid(UUID.fromString(bidId), userId);
        return new ResponseEntity<Bid>(HttpStatus.NO_CONTENT);
    }
}