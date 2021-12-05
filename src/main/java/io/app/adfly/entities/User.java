package io.app.adfly.entities;
import javax.persistence.*;

@Entity
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
