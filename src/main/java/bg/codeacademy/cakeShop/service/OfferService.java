package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.InvalidOfferException;
import bg.codeacademy.cakeShop.error_handling.exception.OfferExistException;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.Offer;
import bg.codeacademy.cakeShop.repository.OfferRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
    public final OfferRepository offerRepository;
    public final LegalEntityService legalEntityService;

    public OfferService(OfferRepository offerRepository, LegalEntityService legalEntityService) {
        this.offerRepository = offerRepository;
        this.legalEntityService = legalEntityService;
    }

    public Offer createOffer(int principalId, int offeredId, float money) {

        LegalEntity principal = legalEntityService.getLegalEntity(principalId);
        LegalEntity offered = legalEntityService.getLegalEntity(offeredId);
        Offer offer = new Offer();
        offer.setOfferor(principal);
        offer.setOffered(offered);
        offer.setMoney(money);

        if (offer.getOfferor().getUin().equals(offer.getOffered().getUin())) {
            throw new InvalidOfferException("The offeror and offered can not be same!");
        }
        if (!offerRepository.existsOfferByOfferorAndMoney(offer.getOfferor(), offer.getMoney())) {
            offerRepository.save(offer);
            return offer;
        } else {
            throw new OfferExistException("Such offer already exist!");
        }
    }
}
