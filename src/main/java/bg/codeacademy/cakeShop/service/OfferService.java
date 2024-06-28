package bg.codeacademy.cakeShop.service;

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
        if (!offerRepository.existsOfferByOfferorAndMoney(offer.getOfferor(), offer.getMoney())) {
            offerRepository.save(offer);
            return offer;
        }
        return offerRepository.findOfferByOfferorAndMoney(offer.getOfferor(), offer.getMoney());
    }
}
