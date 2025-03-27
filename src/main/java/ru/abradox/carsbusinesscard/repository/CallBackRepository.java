package ru.abradox.carsbusinesscard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.abradox.carsbusinesscard.entity.CallBackEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CallBackRepository extends JpaRepository<CallBackEntity, Integer> {

    Optional<CallBackEntity> findByExternalId(UUID externalId);

    @Query("SELECT c FROM CallBackEntity c WHERE c.processed in (false, :includeProcessed) ORDER BY c.created ASC")
    List<CallBackEntity> findAllWithIncludeDeletedAndSortByCreated(boolean includeProcessed);

    @Modifying
    @Query("UPDATE CallBackEntity c SET c.processed = true WHERE c.externalId = :externalId")
    Integer updateProcessed(UUID externalId);
}
