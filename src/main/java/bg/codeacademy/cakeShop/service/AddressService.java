package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Address;
import bg.codeacademy.cakeShop.repository.AddressRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    public final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address addAddress(Address address) {
        if (!addressRepository.existsAddressByCityAndStreet(address.getCity(), address.getStreet())) {
            addressRepository.save(address);
            return address;
        }
        return addressRepository.findAddressByCityAndStreet(address.getCity(), address.getStreet());
    }
}
