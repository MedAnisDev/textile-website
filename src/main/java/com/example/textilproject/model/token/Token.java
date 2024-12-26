package com.example.textilproject.model.token;

import com.example.textilproject.model.Admin;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "tokens")
public class Token {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name ="id" , unique=true , nullable = false)
    private Long  id  ;

    @Column(name ="token" , unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    @Column(name ="expired" , nullable = false)
    private boolean expired ;

    @Column(name ="revoked" , nullable = false)
    private boolean revoked ;

    @ManyToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "id")
    Admin user ;
}
