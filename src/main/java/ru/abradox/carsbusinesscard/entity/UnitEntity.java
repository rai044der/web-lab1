package ru.abradox.carsbusinesscard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "unit")
public class UnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Builder.Default
    @Column(name = "external_id", nullable = false)
    private UUID externalId = UUID.randomUUID();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "photo_link", length = 2048)
    private String photoLink;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}