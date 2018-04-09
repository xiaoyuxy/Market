package marketplace.controller;

import marketplace.model.Bid;
import marketplace.service.InputValidationService;
import marketplace.service.auth.AuthenticationService;
import marketplace.service.auth.FakeAuthenticationService;
import marketplace.service.business.BidService;
import marketplace.service.infra.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * @author Xiaoyu Liang
 */
@RestController
@RequestMapping("/api/bid")
public class BidController {

    private static final Logger logger = LoggerFactory.getLogger(BidController.class);
    private final BidService bidService;
    private final RateLimitService rateLimitService;
    private final AuthenticationService authenticationService;

    @Autowired
    public BidController(BidService bidService, RateLimitService rateLimitService) {
        this.bidService = bidService;
        this.rateLimitService = rateLimitService;
        this.authenticationService = new FakeAuthenticationService();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<?> createBid(@RequestBody Bid bid, @RequestHeader("user-api-key") String apiKey, UriComponentsBuilder ucBuilder) {
        logger.info("Create bid: {}, owner: {}", bid, apiKey);
        rateLimitService.accept(apiKey);
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
    public ResponseEntity<List<Bid>> getAllBids(@RequestHeader("user-api-key") String apiKey) {
        logger.info("Get all the bids");
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);
        List<Bid> bids =bidService.getAllBids();

        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    @RequestMapping(value = "/{bidId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> getBidById(@PathVariable("bidId") String bidId,
                                        @RequestHeader("user-api-key") String apiKey) {
        logger.info("Get the Bid with bid id {}", bidId);
        rateLimitService.accept(apiKey);

        InputValidationService.validateUUID(bidId);
        String userId = authenticationService.authenticate(apiKey);

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
    public ResponseEntity<List<Bid>> getBidByUser(@PathVariable("ownerId") String ownerId,
                                                  @RequestHeader("user-api-key") String apiKey) {
        logger.info("Get the Bid with bid id {}", ownerId);
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);
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
    public ResponseEntity<List<Bid>> getBidByProject(@PathVariable("projectid") String projectid,
                                                     @RequestHeader("user-api-key") String apiKey) {
        logger.info("Get the Bid with project {}", projectid);
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);

        InputValidationService.validateUUID(projectid);
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
        rateLimitService.accept(apiKey);

        InputValidationService.validateUUID(bidId);
        String userId = authenticationService.authenticate(apiKey);

        bidService.deleteBid(UUID.fromString(bidId), userId);
        return new ResponseEntity<Bid>(HttpStatus.OK);
    }

    @RequestMapping(value = "/winner/{projectid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Bid> getBidWinner(@PathVariable("projectid") String projectId,
                                          @RequestHeader("user-api-key") String apiKey) {
        logger.info("Get bid winner for project: {}", projectId);
        rateLimitService.accept(apiKey);

        String userId = authenticationService.authenticate(apiKey);
        InputValidationService.validateUUID(projectId);

        Bid bid = bidService.getBidWinnerForProject(UUID.fromString(projectId));
        if (bid == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bid, HttpStatus.OK);
    }
}