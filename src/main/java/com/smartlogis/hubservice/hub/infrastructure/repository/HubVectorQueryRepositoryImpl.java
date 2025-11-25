package com.smartlogis.hubservice.hub.infrastructure.repository;

import com.smartlogis.hubservice.hub.domain.Hub;
import com.smartlogis.hubservice.hub.domain.repository.HubVectorQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HubVectorQueryRepositoryImpl implements HubVectorQueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Hub> findNearestHubsByLocation(double latitude, double longitude, int limit) {
        // pgvector: '[lat,lon]' 형식의 vector literal
        String vectorLiteral = String.format("[%f,%f]", latitude, longitude);

        String sql = """
                SELECT *
                FROM p_hub h
                WHERE h.deleted_at IS NULL
                  AND h.status = 'ACTIVE'
                ORDER BY h.location_vec <-> CAST(:point AS vector)
                LIMIT :limit
                """;

        return em.createNativeQuery(sql, Hub.class)
                .setParameter("point", vectorLiteral)
                .setParameter("limit", limit)
                .getResultList();
    }
}
