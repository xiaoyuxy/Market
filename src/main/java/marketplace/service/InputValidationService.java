package marketplace.service;

import marketplace.model.Bid;
import marketplace.model.Project;

import java.util.UUID;

/**
 * @author xiaoyuliang
 */
public class InputValidationService {
    static public boolean validateProjectForCreate(Project project) {
        return project.isValidProject();
    }

    static public boolean validateUUID(String uuid) {
        try {
            UUID id = UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static boolean validateBidForCreate(Bid bid) {
        return bid.isValidBid();
    }
}
