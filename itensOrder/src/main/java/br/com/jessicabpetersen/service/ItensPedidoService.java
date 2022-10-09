package br.com.jessicabpetersen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jessicabpetersen.exception.ResourceNotFoundException;
import br.com.jessicabpetersen.model.ItensPedido;
import br.com.jessicabpetersen.modelVO.ItensPedidoVO;
import br.com.jessicabpetersen.repository.ItensPedidoRepository;
import jakarta.transaction.Transactional;

@Service
public class ItensPedidoService {
	
	@Autowired
	private ItensPedidoRepository repository;
	
	@Autowired 
	private ModelMapper mapper;

	public ItensPedidoVO save(ItensPedidoVO itens) {
		for(UUID uuidProduto : itens.getIdProdutoServico()) {
			ItensPedido itensPedido = new ItensPedido();
			itensPedido.setIdPedido(itens.getIdPedido());
			itensPedido.setIdProdutoServico(uuidProduto);
			itensPedido.setId(UUID.randomUUID());
			repository.save(itensPedido);
		}
		return itens;
	}

	@Transactional
	public ItensPedidoVO updateByOrderId(ItensPedidoVO itens) {
		repository.deleteByIdPedido(itens.getIdPedido());
		
		return this.save(itens);
	}

	public List<ItensPedidoVO> getAllItensPedido() {
		var retorno =  repository.findAll();
		List<ItensPedidoVO> itens = new ArrayList();
		ItensPedidoVO itemsPedido = new ItensPedidoVO();
		List<UUID> uuidProdutos = new  ArrayList();
		for(ItensPedido item : retorno) {
			if(itemsPedido.getIdPedido() == null) {
				itemsPedido.setIdPedido(item.getIdPedido());
				uuidProdutos.add(item.getIdProdutoServico());
			}else {
				if(!itemsPedido.getIdPedido().toString().equals(item.getIdPedido().toString())) {
					itemsPedido.setIdProdutoServico(uuidProdutos);
					itens.add(itemsPedido);
					uuidProdutos = new  ArrayList();
					itemsPedido = new ItensPedidoVO();
					itemsPedido.setIdPedido(item.getIdPedido());
				}
				uuidProdutos.add(item.getIdProdutoServico());
			}
			
		}
		return itens;
	}

	public ItensPedidoVO getItensOrderByIdPedido(String id) {
		var retorno = repository.findAllByIdPedido(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("No records found for this Order ID"));
		ItensPedidoVO itemRetorno = new ItensPedidoVO();
		itemRetorno.setIdPedido(UUID.fromString(id));
		List<UUID> uuidProdutos = new  ArrayList();
		for(ItensPedido item: retorno) {
			uuidProdutos.add(item.getIdProdutoServico());
		}
		itemRetorno.setIdProdutoServico(uuidProdutos);
		return itemRetorno;
	}

	public void deleteOrderByIdPedido(String id) {
		repository.deleteByIdPedido(UUID.fromString(id));
	}

	

}
