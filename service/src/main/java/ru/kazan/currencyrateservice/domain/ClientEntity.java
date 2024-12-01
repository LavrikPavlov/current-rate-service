package ru.kazan.currencyrateservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class ClientEntity {

    @Id
    private String id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "first_date")
    private LocalDateTime firstDate;

    @Column(name = "last_date")
    private LocalDateTime lastDate;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<RequestEntity> requests;
}
