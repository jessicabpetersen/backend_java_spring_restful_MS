package br.com.jessicabpetersen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.jessicabpetersen.model.ItensPedido;
@Repository
public interface ItensPedidoRepository extends JpaRepository<ItensPedido, UUID>{
	
	Optional<List<ItensPedido>> findByIdProdutoServico(UUID produto);
	
	Optional<List<ItensPedido>> findByIdPedido(UUID pedido);
	
	@Transactional
	Integer deleteByIdPedido(UUID pedido);
	
	Optional<List<ItensPedido>> findAllByIdPedido(UUID pedido);
	
	List<ItensPedido> findAllByOrderByIdPedido();
	
}
