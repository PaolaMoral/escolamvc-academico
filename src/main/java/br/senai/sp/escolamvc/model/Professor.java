package br.senai.sp.escolamvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;


@DiscriminatorValue(value = "P")
@Entity
public class Professor extends Pessoa {
}
