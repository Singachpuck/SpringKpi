package com.kpi.tendersystem.controller.rest;

import com.kpi.tendersystem.model.User;
import com.kpi.tendersystem.service.ResponseHelper;
import com.kpi.tendersystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@Tag(
        name = "User",
        description = "Operations with user"
)
@RestController
@RequestMapping("${rest.default-base}/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseHelper responseHelper;

    @Operation(
            summary = "Returns User data by id",
            description = "Accepts userId as parameter and tries to retrieve User data associated with this id. " +
                    "User can only view data about himself"
    )
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "404", description = "Invalid userId", content = @Content)
    @ApiResponse(responseCode = "403", description = "If try to access data of another User", content = @Content)
    @GetMapping("/{userId}")
    public ResponseEntity<?> read(final Principal principal, final @PathVariable int userId) {
        final Optional<User> requestedUser = userService.getById(userId);
        if (requestedUser.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        final Optional<User> user = userService.getByUsername(principal.getName());
        if (user.isEmpty()) {
            return ResponseEntity
                    .internalServerError()
                    .body(responseHelper.composeError(HttpStatus.INTERNAL_SERVER_ERROR, "Can not resolve user"));
        }
        if (user.get().getId() != requestedUser.get().getId()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(responseHelper.composeError(HttpStatus.FORBIDDEN, "You can get information only about current user!"));
        }
        return ResponseEntity.ok(requestedUser);
    }
}
