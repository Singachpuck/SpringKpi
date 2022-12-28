package com.kpi.tendersystem.controller.rest;

import com.kpi.tendersystem.model.Offer;
import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.User;
import com.kpi.tendersystem.model.form.FormOffer;
import com.kpi.tendersystem.service.OfferService;
import com.kpi.tendersystem.service.ResponseHelper;
import com.kpi.tendersystem.service.TenderService;
import com.kpi.tendersystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Tag(
        name = "Offer",
        description = "Operations with offer"
)
@RestController
@RequestMapping("${rest.default-base}")
public class OfferRestController {

    @Value("${rest.default-base}")
    private String BASE;

    @Autowired
    private UserService userService;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private ResponseHelper responseHelper;

    @Operation(
            summary = "Read all Offer objects that was created by current user",
            description = "Operation to read all Offer objects that was created by current user"
    )
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Offer.class))))
    @GetMapping("/offers")
    public ResponseEntity<Collection<Offer>> readYours(Principal principal) {
        return ResponseEntity.ok(offerService.getByUsername(principal.getName()));
    }

    @Operation(
            summary = "Read an Offer object by its id",
            description = "Operation to read an Offer object by id. Can access only objects owned by current user"
    )
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = Offer.class)))
    @ApiResponse(responseCode = "404", description = "If offerId is invalid", content = @Content)
    @ApiResponse(responseCode = "403", description = "If user doesn't have permission to read offer", content = @Content)
    @GetMapping("/offers/{offerId}")
    public ResponseEntity<?>  readYour(Principal principal, @PathVariable int offerId) {
        final Optional<Offer> offer = offerService.getById(offerId);

        if (offer.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "No offer with id: " + offerId));
        }

        if (!Objects.equals(offer.get().getUser().getUsername(), principal.getName())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(responseHelper.composeError(HttpStatus.FORBIDDEN, "User: " + principal.getName() + " is not an owner of offer: " + offerId));
        }

        return ResponseEntity.ok(offer.get());
    }

    @Operation(
            summary = "Read all offers created for a specific tender",
            description = "Operation to read all Offer objects that was assigned to a Tender. Only owner of a Tender can access these offers"
    )
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Offer.class))))
    @ApiResponse(responseCode = "404", description = "If tenderId is not valid", content = @Content)
    @ApiResponse(responseCode = "403", description = "If user is not an owner of a Tender", content = @Content)
    @GetMapping("/tenders/{tenderId}/offers")
    public ResponseEntity<?> readAll(Principal principal, @PathVariable int tenderId) {
        final String username = principal.getName();
        final Optional<Tender> tender = tenderService.getTenderById(tenderId);

        if (tender.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "No tender with id: " + tenderId));
        }

        if (!Objects.equals(tender.get().getOwner().getUsername(), username)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(responseHelper.composeError(HttpStatus.FORBIDDEN, "User: " + username + " is not an owner of tender: " + tenderId));
        }

        return ResponseEntity
                .ok(offerService.getAllByTender(principal.getName(), tenderId));
    }

    @Operation(
            summary = "Read an offer created for a specific tender by specific user",
            description = "Operation to read an offer created for a specific tender by specific user. Only owner of a Tender can access this offer"
    )
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = Offer.class)))
    @ApiResponse(responseCode = "404", description = "If tenderId is not valid or if username is not valid", content = @Content)
    @ApiResponse(responseCode = "403", description = "If user is not an owner of a Tender", content = @Content)
    @GetMapping("/tenders/{tenderId}/offers/{username}")
    public ResponseEntity<?> read(Principal principal, @PathVariable int tenderId, @PathVariable String username) {
        final String currentUsername = principal.getName();
        final Optional<Tender> tender = tenderService.getTenderById(tenderId);

        if (tender.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "No tender with id: " + tenderId));
        }

        if (!Objects.equals(tender.get().getOwner().getUsername(), currentUsername)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(responseHelper.composeError(HttpStatus.FORBIDDEN, "User: " + currentUsername + " is not an owner of tender: " + tenderId));
        }

        final Optional<Offer> offer = offerService.getByUsernameAndTenderId(username, tenderId);

        if (offer.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "No offers for tender: " + tenderId + " and username: " + username));
        }

        return ResponseEntity.ok(offer.get());
    }

    @Operation(
            summary = "Create an offer for a specific tender by current user",
            description = "Operation to create an offer for a specific tender by current user. Only owner of a Tender can call this operation"
    )
    @ApiResponse(responseCode = "201", description = "Success", content = @Content)
    @ApiResponse(responseCode = "404", description = "If tenderId is not valid", content = @Content)
    @ApiResponse(responseCode = "403", description = "If user is not an owner of a Tender", content = @Content)
    @ApiResponse(responseCode = "401", description = "If such offer already exists", content = @Content)
    @PostMapping("/tenders/{tenderId}/offers")
    public ResponseEntity<?> create(Principal principal, @PathVariable int tenderId, @RequestBody FormOffer formOffer) {
        final String username = principal.getName();
        final Optional<Tender> tender = tenderService.getTenderById(tenderId);

        if (tender.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "No tender with id: " + tenderId));
        }

        if (Objects.equals(tender.get().getOwner().getUsername(), username)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(responseHelper.composeError(HttpStatus.FORBIDDEN, "User: " + username + " is an owner of tender: " + tenderId));
        }

        final Optional<Offer> offer = offerService.getByUsernameAndTenderId(username, tenderId);

        if (offer.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(responseHelper.composeError(HttpStatus.BAD_REQUEST, "Offer for tender " + tenderId + " from user " + username + " already exists"));
        }

        final Optional<User> user = userService.getByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity
                    .internalServerError()
                    .body(responseHelper.composeError(HttpStatus.INTERNAL_SERVER_ERROR, "Can not resolve user: " + username));
        }
        final int offerId = offerService.addOffer(tender.get(), user.get(), formOffer);

        final URI location = ServletUriComponentsBuilder
                .fromUriString(BASE)
                .path("/offers/{offerId}")
                .buildAndExpand(offerId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }
}
