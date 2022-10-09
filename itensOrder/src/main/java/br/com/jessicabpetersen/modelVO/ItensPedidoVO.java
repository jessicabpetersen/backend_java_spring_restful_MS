package br.com.jessicabpetersen.modelVO;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItensPedidoVO {
	
	private UUID idPedido;
	
	private List<UUID> idProdutoServico;
	
	public ItensPedidoVO() {}

	public ItensPedidoVO( UUID idPedido, List<UUID> idProdutoServico) {
		super();
		this.idPedido = idPedido;
		this.idProdutoServico = idProdutoServico;
	}

	public UUID getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(UUID idPedido) {
		this.idPedido = idPedido;
	}

	public List<UUID> getIdProdutoServico() {
		return idProdutoServico;
	}

	public void setIdProdutoServico(List<UUID> idProdutoServico) {
		this.idProdutoServico = idProdutoServico;
	}
}
