package br.com.jessicabpetersen.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.jessicabpetersen.modelVO.ProdutoServicoVo;

@FeignClient(name = "produto-ms", url="${ms.product}", path="/produto-servico")
public interface ProdutoFeignClient {
	
	@PostMapping
	ProdutoServicoVo save(@RequestBody @Validated ProdutoServicoVo vo);
	
	@PutMapping
	ProdutoServicoVo update(@RequestBody @Validated ProdutoServicoVo vo);
	
	@GetMapping("/{id}")
	ProdutoServicoVo findById(@PathVariable("id") final String id);
	
	@GetMapping
	List<ProdutoServicoVo> findAll();
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable("id") final String id);
	
	@GetMapping("/search")
	public Page<ProdutoServicoVo> search(@RequestParam("searchTerm") String searchTerm,
								@RequestParam(value = "page", required=false, defaultValue = "0") int page,
								@RequestParam(value = "size", required=false, defaultValue = "10") int size);

	
	@GetMapping("/pagination")
	public Page<ProdutoServicoVo> getPagination(@PageableDefault(direction = Direction.ASC, sort = "id") Pageable pageable);
	

}
