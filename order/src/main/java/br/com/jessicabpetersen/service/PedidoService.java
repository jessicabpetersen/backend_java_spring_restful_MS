package br.com.jessicabpetersen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jessicabpetersen.client.ItensPedidoFeignClient;
import br.com.jessicabpetersen.client.ProdutoFeignClient;
import br.com.jessicabpetersen.exception.ResourceNotFoundException;
import br.com.jessicabpetersen.model.Pedido;
import br.com.jessicabpetersen.modelVO.ItensPedidoVO;
import br.com.jessicabpetersen.modelVO.PedidoVO;
import br.com.jessicabpetersen.modelVO.ProdutoServicoVo;
import br.com.jessicabpetersen.modelVO.Situacao;
import br.com.jessicabpetersen.modelVO.Tipo;
import br.com.jessicabpetersen.repository.PedidoRepository;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repository;

	
	@Autowired
	private ItensPedidoFeignClient itensClient;
	
	@Autowired
	private ProdutoFeignClient produtoClient;
	
	@Autowired 
	private ModelMapper mapper;

	@Transactional
	public PedidoVO save(PedidoVO vo) {
		PedidoVO voReturn = new PedidoVO();
		
		Pedido pedido = new Pedido();
		pedido.setDesconto(vo.getDesconto());
		pedido.setSituacao(vo.getSituacao());
		
		var entityPedido = repository.save(pedido);
		atualizaVo(entityPedido, voReturn);
		voReturn.setProdutoServico(vo.getProdutoServico());
		return saveItensPedido(voReturn);
	}
	
	private PedidoVO saveItensPedido(PedidoVO vo) {
		ItensPedidoVO itens = new ItensPedidoVO();
		itens.setIdPedido(vo.getId());
		List<UUID> idProdutoServico = new ArrayList<>();
		List<ProdutoServicoVo> produtoServico = new ArrayList<>();
		for(ProdutoServicoVo produto: vo.getProdutoServico()) {
			idProdutoServico.add(produto.getId());
			ProdutoServicoVo produtoVo = produtoClient.findById(produto.getId().toString());
			produtoServico.add(produtoVo);
			
		}
		itens.setIdProdutoServico(idProdutoServico);
		itensClient.save(itens);
		vo.setProdutoServico(produtoServico);
		vo.setTotal(calculaTotalPedido(produtoServico, vo.getDesconto(), vo.getSituacao()));
		return vo;
		
	}
	
	protected void atualizaVo(Pedido pedido, PedidoVO vo) {
		vo.setDesconto(pedido.getDesconto());
		vo.setSituacao(pedido.getSituacao());
		vo.setId(pedido.getId());
	}
	
	protected Double calculaTotalPedido(List<ProdutoServicoVo> list, Double desconto, Situacao pedido) {
		Double totalProduto = 0D;
		Double totalServico = 0D;
		Double total;
		for(ProdutoServicoVo ps : list) {
			if(ps.getTipo() == Tipo.PRODUTO) {
				totalProduto += ps.getValor();
			}else {
				totalServico += ps.getValor();
			}
		}
		if(pedido == Situacao.ABERTO && desconto != null && desconto != 0D) {
			total = totalServico + (totalProduto - ((totalProduto * desconto)/100));
		}else {
			total = totalServico + totalProduto;
		}
		
		return total;
	}

	@Transactional
	public PedidoVO update(PedidoVO vo) {
		Pedido pedido = repository.findById(vo.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this order ID"));
		verificaCamposPedidoUpdate(pedido, vo);
		repository.save(pedido);
		PedidoVO voRetorno = new PedidoVO();
		atualizaVo(pedido, voRetorno);
		List<ProdutoServicoVo> lista = new ArrayList<>();
		ItensPedidoVO item = new ItensPedidoVO();
		item.setIdPedido(vo.getId());
		List<UUID> idProdutoServico = new ArrayList<>();
		for(ProdutoServicoVo produtoVo : vo.getProdutoServico()) {
			idProdutoServico.add(produtoVo.getId());
			ProdutoServicoVo produtoServicoVo = produtoClient.findById(produtoVo.getId().toString());
			lista.add(produtoServicoVo);
		}
		item.setIdProdutoServico(idProdutoServico);
		ItensPedidoVO itensPedidoVo = itensClient.updateByOrderId(item);
		voRetorno.setProdutoServico(lista);
		voRetorno.setTotal(calculaTotalPedido(voRetorno.getProdutoServico(), voRetorno.getDesconto(), pedido.getSituacao()));
		return voRetorno;
	}

	private void verificaCamposPedidoUpdate(Pedido pedido, PedidoVO vo) {
		if(vo.getDesconto() != null) {
			pedido.setDesconto(vo.getDesconto());			
		}
		if(vo.getSituacao() != null) {
			pedido.setSituacao(vo.getSituacao());			
		}
		
	}
	
	@Transactional
	public PedidoVO findById(String id) {
		UUID uuid = UUID.fromString(id);
		Pedido pedidoEntity = repository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("No records found for this order ID"));
		ItensPedidoVO itens = itensClient.getItensOrderByIdPedido(id);
		List<ProdutoServicoVo> produtoServico = new ArrayList();
		for(UUID uuidProduto: itens.getIdProdutoServico()) {
			ProdutoServicoVo produtoServicoVo = produtoClient.findById(uuidProduto.toString());
			produtoServico.add(produtoServicoVo);
		}
		PedidoVO vo = new PedidoVO();
		atualizaVo(pedidoEntity, vo);
		vo.setProdutoServico(produtoServico);
		vo.setTotal(calculaTotalPedido(vo.getProdutoServico(), vo.getDesconto(), pedidoEntity.getSituacao()));
		return vo;
	}

	@Transactional
	public List<PedidoVO> findAll() {
		List<PedidoVO> lista = new ArrayList<>();
		var pedidos = repository.findAll();
		for(Pedido pedido: pedidos) {
			PedidoVO po = new PedidoVO();
			List<ProdutoServicoVo> listaProdutos = new ArrayList<>();
			atualizaVo(pedido, po);
			ItensPedidoVO itensPedidoVo = itensClient.getItensOrderByIdPedido(po.getId().toString());
			for(UUID uuid: itensPedidoVo.getIdProdutoServico()) {
				ProdutoServicoVo produtoServicoVo = produtoClient.findById(uuid.toString());
				listaProdutos.add(produtoServicoVo);
			}
			po.setProdutoServico(listaProdutos);
			po.setTotal(calculaTotalPedido(listaProdutos, po.getDesconto(), po.getSituacao()));
			lista.add(po);
		}
		
		return lista;
	}

	@Transactional
	public void deleteById(String id) {
		UUID uuid = UUID.fromString(id);
		var pedido = repository.findById(uuid);
		itensClient.deleteOrderByIdPedido(id);
		repository.delete(pedido.get());
		
	}

	
}
