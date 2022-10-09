package br.com.jessicabpetersen.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "itens_pedido")
public class ItensPedido  implements Serializable, Persistable<UUID> {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator")
	@Column(name = "id", updatable = false, unique = true, nullable = false)
	private UUID id;
	
	private UUID idPedido;
	
	private UUID idProdutoServico;

	public ItensPedido() {}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(UUID idPedido) {
		this.idPedido = idPedido;
	}

	public UUID getIdProdutoServico() {
		return idProdutoServico;
	}

	public void setIdProdutoServico(UUID idProdutoServico) {
		this.idProdutoServico = idProdutoServico;
	}

	public ItensPedido(UUID id, UUID idPedido, UUID idProdutoServico) {
		super();
		this.id = id;
		this.idPedido = idPedido;
		this.idProdutoServico = idProdutoServico;
	}
	

	@Override
	public boolean isNew() {
		return this.id == null;
	}


	
}
