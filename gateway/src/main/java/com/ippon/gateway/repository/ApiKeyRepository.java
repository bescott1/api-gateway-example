package com.ippon.gateway.repository;

import com.ippon.gateway.domain.ApiKey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ApiKey entity.
 */
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    @Query(value = "select distinct apiKey from ApiKey apiKey left join fetch apiKey.authorities",
        countQuery = "select count(distinct apiKey) from ApiKey apiKey")
    Page<ApiKey> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct apiKey from ApiKey apiKey left join fetch apiKey.authorities")
    List<ApiKey> findAllWithEagerRelationships();

    @Query("select apiKey from ApiKey apiKey left join fetch apiKey.authorities where apiKey.id =:id")
    Optional<ApiKey> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select apiKey from ApiKey apiKey left join fetch apiKey.authorities where apiKey.clientId =:clientId")
    Optional<ApiKey> findOneByClientIdWithEagerRelationships(@Param("clientId")String clientId);
}
