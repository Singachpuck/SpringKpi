package com.kpi.tendersystem.controller.rest;

import com.kpi.tendersystem.model.Tender;
import com.kpi.tendersystem.model.User;
import com.kpi.tendersystem.model.form.FormTender;
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
        name = "Tender",
        description = "Operations with tender"
)
@RestController
@RequestMapping("${rest.default-base}/tenders")
public class TenderRestController {

    @Value("${rest.default-offset}")
    private int DEFAULT_OFFSET;

    @Value("${rest.default-limit}")
    private int DEFAULT_LIMIT;

    @Autowired
    private TenderService tenderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseHelper responseHelper;

    @Operation(
            summary = "Creates a new Tender for the current user",
            description = "Operation to create a new Tender from json data." +
                    " Location Header contains reference to a newly created object"
    )
    @ApiResponse(responseCode = "201", description = "Created", content = @Content)
    @ApiResponse(responseCode = "400", description = "If body contains invalid data", content = @Content)
    @PostMapping
    public ResponseEntity<?> create(Principal principal, @RequestBody FormTender formTender) {
        final String username = principal.getName();
        final Optional<User> user = userService
                .getByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity
                    .internalServerError()
                    .body(responseHelper.composeError(HttpStatus.INTERNAL_SERVER_ERROR, "Can not resolve user"));
        }
        final int tenderId = tenderService.addTender(user.get(), formTender);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(tenderId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Operation(
            summary = "Read all the Tender objects with optional pagination and filtration",
            description = "Operation to read all Tender objects. " +
                    "Specify search parameter to filter data. " +
                    "Specify offset adn limit parameters to use pagination"
    )
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Tender.class))))
    @GetMapping
    public ResponseEntity<Collection<Tender>> readAll(@RequestParam(required = false) String search,
                                                      @RequestParam(required = false) Optional<Integer> offset,
                                                      @RequestParam(required = false) Optional<Integer> limit) {
        final Collection<Tender> tenders = tenderService.getAll(search,
                offset.orElse(DEFAULT_OFFSET),
                limit.orElse(DEFAULT_LIMIT));
        return ResponseEntity.ok(tenders);
    }

    @Operation(
            summary = "Read specific Tender object",
            description = "Operation to read a Tender object by tenderId."
    )
    @ApiResponse(responseCode = "200", description = "Success",
            content = @Content(schema = @Schema(implementation = Tender.class)))
    @ApiResponse(responseCode = "404", description = "If tenderId is not valid", content = @Content)
    @GetMapping("/{tenderId}")
    public ResponseEntity<?> read(@PathVariable int tenderId) {
        final Optional<Tender> tender = tenderService.getTenderById(tenderId);

        if (tender.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "Tender not found"));
        }

        return ResponseEntity.ok(tender.get());
    }

    @Operation(
            summary = "Replace specific Tender",
            description = "Operation to replace a Tender using tenderId parameter by a new one."
    )
    @ApiResponse(responseCode = "204", description = "Success", content = @Content)
    @ApiResponse(responseCode = "404", description = "If tenderId is not valid", content = @Content)
    @ApiResponse(responseCode = "403", description = "If a user doesn't have permissions to change tender", content = @Content)
    @PutMapping("/{tenderId}")
    public ResponseEntity<?> update(Principal principal, @PathVariable int tenderId, @RequestBody FormTender formTender) {
        final Optional<Tender> tender = tenderService.getTenderById(tenderId);
        if (tender.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "Tender not found"));
        }
        final String username = principal.getName();
        if (!Objects.equals(username, tender.get().getOwner().getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(responseHelper.composeError(HttpStatus.FORBIDDEN, "Not an owner of tender: " + tenderId));
        }

        tenderService.updateTender(tenderId, formTender);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(
            summary = "Delete specific Tender",
            description = "Operation to delete a Tender using tenderId."
    )
    @ApiResponse(responseCode = "204", description = "Success", content = @Content)
    @ApiResponse(responseCode = "404", description = "If tenderId is not valid", content = @Content)
    @ApiResponse(responseCode = "403", description = "If a user doesn't have permissions to change tender", content = @Content)
    @DeleteMapping("/{tenderId}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable int tenderId) {
        final Optional<Tender> tender = tenderService.getTenderById(tenderId);
        if (tender.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(responseHelper.composeError(HttpStatus.NOT_FOUND, "Tender not found"));
        }
        final String username = principal.getName();
        if (!Objects.equals(username, tender.get().getOwner().getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(responseHelper.composeError(HttpStatus.FORBIDDEN, "Not an owner of tender: " + tenderId));
        }

        tenderService.deleteTender(tenderId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
