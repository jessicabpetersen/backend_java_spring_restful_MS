package br.com.jessicabpetersen.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.jessicabpetersen.modelVO.ItensPedidoVO;

@FeignClient(name = "itens-pedido-ms", url="${ms.item-pedido}", path="/itens-order")
public interface ItensPedidoFeignClient {
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	ItensPedidoVO save(@RequestBody @Validated ItensPedidoVO itens);
	
	@PutMapping
	ItensPedidoVO updateByOrderId(@RequestBody @Validated ItensPedidoVO itens);
	
	@GetMapping
	List<ItensPedidoVO> getAllItensPedido();
	
	@GetMapping("/{id}")
	ItensPedidoVO getItensOrderByIdPedido(@PathVariable("id") final String id);

	@DeleteMapping("/{id}")
	void deleteOrderByIdPedido(@PathVariable("id") final String id);
}
