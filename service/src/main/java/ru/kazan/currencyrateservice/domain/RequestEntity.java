package ru.kazan.currencyrateservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class RequestEntity {

    @Id
    @Column(name = "request_id")
    private String id;

    @Column(name = "request_body")
    private String requestBody;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "clients_id", referencedColumnName = "id")
    private ClientEntity client;

}
