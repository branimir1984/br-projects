package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.InvalidOfferException;
import bg.codeacademy.cakeShop.error_handling.exception.OfferExistException;
import bg.codeacademy.cakeShop.model.Offer;
import bg.codeacademy.cakeShop.repository.OfferRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
    public final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer addOffer(Offer offer) {
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
