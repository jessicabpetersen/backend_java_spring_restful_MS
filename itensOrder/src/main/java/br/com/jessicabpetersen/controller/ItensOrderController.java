package br.com.jessicabpetersen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.jessicabpetersen.modelVO.ItensPedidoVO;
import br.com.jessicabpetersen.service.ItensPedidoService;

@RestController
@RequestMapping(path = "/v1/itens-order")
public class ItensOrderController {
	
	@Autowired
	private ItensPedidoService service;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItensPedidoVO save(@RequestBody @Validated ItensPedidoVO itens) {
		return service.save(itens);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public ItensPedidoVO updateByOrderId(@RequestBody @Validated ItensPedidoVO itens) {
		return service.updateByOrderId(itens);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ItensPedidoVO> getAllItensPedido() {
		return service.getAllItensPedido();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ItensPedidoVO getItensOrderByIdPedido(@PathVariable("id") final String id) {
		return service.getItensOrderByIdPedido(id);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteOrderByIdPedido(@PathVariable("id") final String id) {
		service.deleteOrderByIdPedido(id);
	}

}
