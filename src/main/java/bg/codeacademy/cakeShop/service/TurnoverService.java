package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Turnover;
import bg.codeacademy.cakeShop.repository.TurnoverRepository;
import org.springframework.stereotype.Service;
import static java.time.LocalDate.now;

@Service
public class TurnoverService {
    public final TurnoverRepository turnoverRepository;
    public final LegalEntityService legalEntityService;

    public TurnoverService(TurnoverRepository turnoverRepository, LegalEntityService LegalEntityService) {
        this.turnoverRepository = turnoverRepository;
        this.legalEntityService = LegalEntityService;
    }

    public Turnover updateTurnover(int shopId, float amount) {
        Turnover turnover = turnoverRepository.findTurnoverByDate(now());
        if (turnover == null) {
            Turnover to = new Turnover();
            to.setAmount(amount);
            to.setDate(now());
            to.setOwner(legalEntityService.getLegalEntity(shopId));
            turnoverRepository.save(to);
            return to;
        }
        turnover.setAmount(amount);
        turnoverRepository.save(turnover);
        return turnover;
    }
}
