package com.tsang.demo.registration.token;

import com.tsang.demo.appuser.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @SequenceGenerator(
            name = "confirmationToken_sequence",
            sequenceName = "confirmationToken_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmationToken_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token, LocalDateTime createAt, LocalDateTime expiredAt, AppUser appUser) {
        this.token = token;
        this.createAt = createAt;
        this.expiresAt = expiredAt;
        this.appUser = appUser;
    }
}
