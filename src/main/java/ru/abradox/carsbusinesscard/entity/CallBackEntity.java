package ru.abradox.carsbusinesscard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "call_back")
public class CallBackEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Builder.Default
    @Column(name = "external_id", nullable = false)
    private UUID externalId = UUID.randomUUID();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 2048)
    private String description;

    @Column(name = "phone", nullable = false, length = 32)
    private String phone;

    @Builder.Default
    @Column(name = "processed", nullable = false)
    private Boolean processed = false;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private LocalDateTime created;
}