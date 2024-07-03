package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.LegalEntityRegistrationDTO;
import bg.codeacademy.cakeShop.dto.LegalEntityResponse;
import bg.codeacademy.cakeShop.dto.OfferResponceDTO;
import bg.codeacademy.cakeShop.enums.Role;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.Offer;
import bg.codeacademy.cakeShop.security.AuthenticatedUser;
import bg.codeacademy.cakeShop.service.LegalEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@EnableMethodSecurity
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RestController
@RequestMapping("/api/v1/legal-entities")
public class LegalEntityController {
    private final LegalEntityService legalEntityService;
    private final Mapper mapper;

    public LegalEntityController(LegalEntityService legalEntityService, Mapper mapper) {
        this.legalEntityService = legalEntityService;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@Valid @RequestBody LegalEntityRegistrationDTO dto) {
        LegalEntity legalEntity = mapper.mapToLegalEntity(dto);
        return new ResponseEntity<>("http://localhost:8080/api/v1/legal-entities/?id="
                + legalEntityService.createLegalEntity(legalEntity).getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LegalEntityResponse>> getAll() {
        List<LegalEntityResponse> responseList = mapper.mapLegalEntityToResponseList(
                legalEntityService.getLegalEntities()
        );
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<LegalEntityResponse> get(@RequestParam("id") int id) {
        LegalEntityResponse response = mapper.mapLegalEntityToResponse(
                legalEntityService.getLegalEntity(id)
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/offers")
    public ResponseEntity<Map<String, List<OfferResponceDTO>>> getOffers(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        Map<String, List<Offer>> offers = legalEntityService.getOffers(user.getId());
        Map<String, List<OfferResponceDTO>> dtoList = mapper.mapToOfferResponceDTOList(offers);
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
