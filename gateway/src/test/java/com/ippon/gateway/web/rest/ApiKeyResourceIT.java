package com.ippon.gateway.web.rest;

import com.ippon.gateway.GatewayApp;
import com.ippon.gateway.config.TestSecurityConfiguration;
import com.ippon.gateway.domain.ApiKey;
import com.ippon.gateway.repository.ApiKeyRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ApiKeyResource} REST controller.
 */
@SpringBootTest(classes = { GatewayApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ApiKeyResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_ID = "BBBBBBBBBB";

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private ApiKeyRepository apiKeyRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApiKeyMockMvc;

    private ApiKey apiKey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiKey createEntity(EntityManager em) {
        ApiKey apiKey = new ApiKey()
            .description(DEFAULT_DESCRIPTION)
            .clientId(DEFAULT_CLIENT_ID);
        return apiKey;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApiKey createUpdatedEntity(EntityManager em) {
        ApiKey apiKey = new ApiKey()
            .description(UPDATED_DESCRIPTION)
            .clientId(UPDATED_CLIENT_ID);
        return apiKey;
    }

    @BeforeEach
    public void initTest() {
        apiKey = createEntity(em);
    }

    @Test
    @Transactional
    public void createApiKey() throws Exception {
        int databaseSizeBeforeCreate = apiKeyRepository.findAll().size();
        // Create the ApiKey
        restApiKeyMockMvc.perform(post("/api/api-keys").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apiKey)))
            .andExpect(status().isCreated());

        // Validate the ApiKey in the database
        List<ApiKey> apiKeyList = apiKeyRepository.findAll();
        assertThat(apiKeyList).hasSize(databaseSizeBeforeCreate + 1);
        ApiKey testApiKey = apiKeyList.get(apiKeyList.size() - 1);
        assertThat(testApiKey.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApiKey.getClientId()).isEqualTo(DEFAULT_CLIENT_ID);
    }

    @Test
    @Transactional
    public void createApiKeyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apiKeyRepository.findAll().size();

        // Create the ApiKey with an existing ID
        apiKey.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApiKeyMockMvc.perform(post("/api/api-keys").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apiKey)))
            .andExpect(status().isBadRequest());

        // Validate the ApiKey in the database
        List<ApiKey> apiKeyList = apiKeyRepository.findAll();
        assertThat(apiKeyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkClientIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = apiKeyRepository.findAll().size();
        // set the field null
        apiKey.setClientId(null);

        // Create the ApiKey, which fails.


        restApiKeyMockMvc.perform(post("/api/api-keys").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apiKey)))
            .andExpect(status().isBadRequest());

        List<ApiKey> apiKeyList = apiKeyRepository.findAll();
        assertThat(apiKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApiKeys() throws Exception {
        // Initialize the database
        apiKeyRepository.saveAndFlush(apiKey);

        // Get all the apiKeyList
        restApiKeyMockMvc.perform(get("/api/api-keys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apiKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].clientId").value(hasItem(DEFAULT_CLIENT_ID)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllApiKeysWithEagerRelationshipsIsEnabled() throws Exception {
        when(apiKeyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApiKeyMockMvc.perform(get("/api/api-keys?eagerload=true"))
            .andExpect(status().isOk());

        verify(apiKeyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllApiKeysWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(apiKeyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restApiKeyMockMvc.perform(get("/api/api-keys?eagerload=true"))
            .andExpect(status().isOk());

        verify(apiKeyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getApiKey() throws Exception {
        // Initialize the database
        apiKeyRepository.saveAndFlush(apiKey);

        // Get the apiKey
        restApiKeyMockMvc.perform(get("/api/api-keys/{id}", apiKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apiKey.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.clientId").value(DEFAULT_CLIENT_ID));
    }
    @Test
    @Transactional
    public void getNonExistingApiKey() throws Exception {
        // Get the apiKey
        restApiKeyMockMvc.perform(get("/api/api-keys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApiKey() throws Exception {
        // Initialize the database
        apiKeyRepository.saveAndFlush(apiKey);

        int databaseSizeBeforeUpdate = apiKeyRepository.findAll().size();

        // Update the apiKey
        ApiKey updatedApiKey = apiKeyRepository.findById(apiKey.getId()).get();
        // Disconnect from session so that the updates on updatedApiKey are not directly saved in db
        em.detach(updatedApiKey);
        updatedApiKey
            .description(UPDATED_DESCRIPTION)
            .clientId(UPDATED_CLIENT_ID);

        restApiKeyMockMvc.perform(put("/api/api-keys").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedApiKey)))
            .andExpect(status().isOk());

        // Validate the ApiKey in the database
        List<ApiKey> apiKeyList = apiKeyRepository.findAll();
        assertThat(apiKeyList).hasSize(databaseSizeBeforeUpdate);
        ApiKey testApiKey = apiKeyList.get(apiKeyList.size() - 1);
        assertThat(testApiKey.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApiKey.getClientId()).isEqualTo(UPDATED_CLIENT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingApiKey() throws Exception {
        int databaseSizeBeforeUpdate = apiKeyRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApiKeyMockMvc.perform(put("/api/api-keys").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(apiKey)))
            .andExpect(status().isBadRequest());

        // Validate the ApiKey in the database
        List<ApiKey> apiKeyList = apiKeyRepository.findAll();
        assertThat(apiKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApiKey() throws Exception {
        // Initialize the database
        apiKeyRepository.saveAndFlush(apiKey);

        int databaseSizeBeforeDelete = apiKeyRepository.findAll().size();

        // Delete the apiKey
        restApiKeyMockMvc.perform(delete("/api/api-keys/{id}", apiKey.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApiKey> apiKeyList = apiKeyRepository.findAll();
        assertThat(apiKeyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
