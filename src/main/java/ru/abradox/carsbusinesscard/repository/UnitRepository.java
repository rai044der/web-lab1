package ru.abradox.carsbusinesscard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.abradox.carsbusinesscard.entity.UnitEntity;

import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {

    Optional<UnitEntity> findByExternalId(UUID externalId);

    Boolean existsByName(String name);

    Optional<UnitEntity> findByName(String name);

    @Modifying
    @Query("DELETE FROM UnitEntity u WHERE u.externalId = :externalId")
    Integer deleteByExternalId(UUID externalId);
}
