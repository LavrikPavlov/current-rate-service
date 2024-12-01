package ru.kazan.currencyrateservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency")
public class CurrencyEntity {
    @Id
    private String id;

    @Column(name = "num_code",unique = true, nullable = false)
    private String numCode;

    @Column(name = "char_code",unique = true, nullable = false)
    private String charCode;

    private Long nominal;

    private String name;

    private BigDecimal value;

    private BigDecimal previous;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
