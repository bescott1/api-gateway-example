package com.ippon.gateway.web.rest;

import com.ippon.gateway.domain.ApiKey;
import com.ippon.gateway.repository.ApiKeyRepository;
import com.ippon.gateway.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ippon.gateway.domain.ApiKey}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApiKeyResource {

    private final Logger log = LoggerFactory.getLogger(ApiKeyResource.class);

    private static final String ENTITY_NAME = "apiKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyResource(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * {@code POST  /api-keys} : Create a new apiKey.
     *
     * @param apiKey the apiKey to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apiKey, or with status {@code 400 (Bad Request)} if the apiKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/api-keys")
    public ResponseEntity<ApiKey> createApiKey(@Valid @RequestBody ApiKey apiKey) throws URISyntaxException {
        log.debug("REST request to save ApiKey : {}", apiKey);
        if (apiKey.getId() != null) {
            throw new BadRequestAlertException("A new apiKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApiKey result = apiKeyRepository.save(apiKey);
        return ResponseEntity.created(new URI("/api/api-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /api-keys} : Updates an existing apiKey.
     *
     * @param apiKey the apiKey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apiKey,
     * or with status {@code 400 (Bad Request)} if the apiKey is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apiKey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/api-keys")
    public ResponseEntity<ApiKey> updateApiKey(@Valid @RequestBody ApiKey apiKey) throws URISyntaxException {
        log.debug("REST request to update ApiKey : {}", apiKey);
        if (apiKey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApiKey result = apiKeyRepository.save(apiKey);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apiKey.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /api-keys} : get all the apiKeys.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apiKeys in body.
     */
    @GetMapping("/api-keys")
    public List<ApiKey> getAllApiKeys(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ApiKeys");
        return apiKeyRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /api-keys/:id} : get the "id" apiKey.
     *
     * @param id the id of the apiKey to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apiKey, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/api-keys/{id}")
    public ResponseEntity<ApiKey> getApiKey(@PathVariable Long id) {
        log.debug("REST request to get ApiKey : {}", id);
        Optional<ApiKey> apiKey = apiKeyRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(apiKey);
    }

    @GetMapping("/api-keys/roles/{clientId}")
    public ResponseEntity<ApiKey> getRolesForClientId(@PathVariable(name = "clientId") String clientId) {
        Optional<ApiKey> apiKey = apiKeyRepository.findOneByClientIdWithEagerRelationships(clientId);
        return ResponseUtil.wrapOrNotFound(apiKey);
    }

    /**
     * {@code DELETE  /api-keys/:id} : delete the "id" apiKey.
     *
     * @param id the id of the apiKey to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/api-keys/{id}")
    public ResponseEntity<Void> deleteApiKey(@PathVariable Long id) {
        log.debug("REST request to delete ApiKey : {}", id);

        apiKeyRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
