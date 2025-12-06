package br.com.quale.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("PRIVATE") // Valor salvo no campo "_class"
@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateChat extends Chat {
}