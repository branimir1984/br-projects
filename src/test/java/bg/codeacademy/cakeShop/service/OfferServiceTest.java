package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.error_handling.exception.InvalidOfferException;
import bg.codeacademy.cakeShop.error_handling.exception.OfferExistException;
import bg.codeacademy.cakeShop.model.*;
import bg.codeacademy.cakeShop.repository.AddressRepository;
import bg.codeacademy.cakeShop.repository.OfferRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OfferServiceTest {
    static OfferService offerService;
    static OfferRepository offerRepository = mock(OfferRepository.class);

    @BeforeAll
    public static void setup() {
        offerService = new OfferService(offerRepository);
    }

    @Test
    void shouldAddOffer() {
        Offer offer = formOffer("A", "B");
        when(offerRepository.existsOfferByOfferorAndMoney(offer.getOfferor(), offer.getMoney()))
                .thenReturn(false);
        Offer response = offerService.addOffer(offer);
        Assertions.assertEquals(offer, response);
        verify(offerRepository, times(1)).save(offer);
    }

    @Test
    void shouldThrowInvalidOfferException() {
        Offer offer = formOffer("A", "A");
        Assertions.assertThrows(InvalidOfferException.class, () -> {
            offerService.addOffer(offer);
        });
    }

    @Test
    void shouldThrowOfferExistException() {
        Offer offer = formOffer("B", "A");
        when(offerRepository.existsOfferByOfferorAndMoney(offer.getOfferor(), offer.getMoney()))
                .thenReturn(true);
        Assertions.assertThrows(OfferExistException.class, () -> {
            offerService.addOffer(offer);
        });
    }

    private Offer formOffer(String oferrorUIN, String offeredUIN) {
        LegalEntity offeror = formLegalEntity(oferrorUIN);
        LegalEntity offered = formLegalEntity(offeredUIN);
        Offer offer = new Offer();
        offer.setOfferor(offeror);
        offer.setMoney(120);
        offer.setOffered(offered);
        return offer;
    }

    private LegalEntity formLegalEntity(String uin) {
        Address address = new Address();
        address.setCity("city");
        address.setStreet("street");

        BankAccount account = new BankAccount();
        account.setIban("BG18RZBB91550123456789");
        account.setCurrency(Currency.BG);

        PersonalData personalData = new PersonalData();
        personalData.setUserName("test");
        personalData.setAddress(address);
        personalData.setBankAccount(List.of(account));
        personalData.setUserPassword("password");
        personalData.setPersonalName("personalName");

        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setEmail("someEmail");
        legalEntity.setUin(uin);
        legalEntity.setPersonalData(personalData);
        return legalEntity;
    }
}