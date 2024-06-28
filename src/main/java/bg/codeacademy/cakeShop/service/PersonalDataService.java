package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.BankAccountExistException;
import bg.codeacademy.cakeShop.error_handling.exception.UserNameExistException;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.repository.BankAccountRepository;
import bg.codeacademy.cakeShop.repository.PersonalDataRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonalDataService {
    private final PersonalDataRepository personalDataRepository;

    public PersonalDataService(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    public String addPersonalData(PersonalData personalData) {
        if (personalDataRepository.existsPersonalDataByUserName(personalData.getUserName())) {
            throw new UserNameExistException("Personal data with user name:"
                    + personalData.getUserName() + " is already in use!");
        }
        personalDataRepository.save(personalData);
        return personalData.getUserName();
    }

    public PersonalData getByUserName(String principal) {
        return personalDataRepository.findPersonalDataByUserName(principal);
    }
}