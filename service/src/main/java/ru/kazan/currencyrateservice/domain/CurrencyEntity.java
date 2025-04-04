package ru.kazan.currencyrateservice.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.kazan.api.generated.model.StatusEnum;

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

    @Column(name = "is_sent")
    private Boolean isSent;

    @Column(name = "is_processed")
    private Boolean isProcessed;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
