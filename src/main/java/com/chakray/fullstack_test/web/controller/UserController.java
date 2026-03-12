package com.chakray.fullstack_test.web.controller;

import com.chakray.fullstack_test.exception.GlobalExceptionHandler;
import com.chakray.fullstack_test.service.UserService;
import com.chakray.fullstack_test.web.dto.request.UserCreateRequest;
import com.chakray.fullstack_test.web.dto.request.UserPatchRequest;
import com.chakray.fullstack_test.web.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "API REST para gestión de usuarios")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@code GET /users} : Retrieve the list of users.
     * <p>
     * This endpoint allows retrieving all registered users. It supports optional
     * query parameters to sort and filter the results.
     *
     * @param sortedBy optional field used to sort the results. Supported values:
     *                 id, email, name, phone, tax_id, created_at.
     * @param filter   optional filter expression using the format:
     *                 field+operator+value.
     *                 Supported operators: co (contains), eq (equals),
     *                 sw (starts with), ew (ends with).
     * @return a list of users matching the criteria.
     */
    @Operation(
            summary = "Obtener lista de usuarios",
            description = "Obtiene la lista de usuarios registrados con opciones de ordenamiento y filtrado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
                    @ApiResponse(responseCode = "400", description = "Error de cliente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@Parameter(description = "Campo para ordenar resultados (id, email, name, phone, tax_id, created_at)", example = "name") @RequestParam(required = false) String sortedBy,
                                                       @Parameter(description = "Filtro en formato field+operator+value (ej: name+co+user)", example = "name+co+user") @RequestParam(required = false) String filter) {
        LOG.info("Fetching users (sortedBy={}, filter={})", sortedBy, filter);
        List<UserResponse> userResponseList = userService.getUsers(sortedBy, filter);
        return ResponseEntity.ok(userResponseList);
    }

    /**
     * {@code POST /users} : Create a new user.
     * <p>
     * This endpoint registers a new user in the system. The request body must
     * contain the required user attributes such as email, name, phone,
     * password, tax_id, and the list of addresses.
     * <p>
     * The password will be encrypted before being stored, and the tax_id must
     * be unique in the system.
     *
     * @param request the user creation request payload containing the
     *                information required to register a new user.
     * @return the {@link UserResponse} representing the created user.
     */
    @Operation(
            summary = "Crear usuario",
            description = "Registra un nuevo usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Creación exitosa",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Error de cliente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class))),
                    @ApiResponse(responseCode = "409", description = "Conflictos con datos ingresados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
            }
    )
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse userResponse = userService.createUser(request);
        LOG.info("Created user with id={}", userResponse.getId());
        return ResponseEntity.created(URI.create("/users/" + userResponse.getId())).body(userResponse);
    }

    /**
     * {@code PATCH /users/{id}} : Partially update an existing user.
     * <p>
     * This endpoint updates one or more attributes of an existing user
     * identified by its unique ID.
     *
     * @param id      the unique identifier of the user to update.
     * @param request the partial update payload with the fields to modify.
     * @return the {@link UserResponse} representing the updated user.
     */
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza parcialmente un usuario existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Error de cliente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "No encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class))),
                    @ApiResponse(responseCode = "409", description = "Conflictos con datos ingresados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
            }
    )
    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserResponse> patchUser(@Parameter(description = "ID del usuario", example = "ab5512b9-17e5-4d3c-9e15-ac6d8766e030", required = true) @PathVariable UUID id,
                                                  @Valid @RequestBody UserPatchRequest request) {
        UserResponse userResponse = userService.patchUser(id, request);
        LOG.info("Updated user with id={}", userResponse.getId());
        return ResponseEntity.ok(userResponse);
    }

    /**
     * {@code DELETE /users/{id}} : Delete an existing user.
     * <p>
     * This endpoint removes a user from the system using its unique ID.
     *
     * @param id the unique identifier of the user to delete.
     * @return an empty response with HTTP status 204 when the deletion is successful.
     */
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario por su identificador",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
                    @ApiResponse(responseCode = "400", description = "Error de cliente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class))),
                    @ApiResponse(responseCode = "404", description = "No encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID del usuario", example = "ab5512b9-17e5-4d3c-9e15-ac6d8766e030", required = true) @PathVariable UUID id) {
        userService.deleteUser(id);
        LOG.info("Deleted user with id={}", id);
        return ResponseEntity.noContent().build();
    }

}
