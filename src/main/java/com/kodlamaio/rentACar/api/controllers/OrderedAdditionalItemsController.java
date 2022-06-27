package com.kodlamaio.rentACar.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.rentACar.business.abstracts.OrderedAdditionalItemService;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.CreateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.DeleteOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.UpdateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.responses.orderedAdditionalItems.GetOrderedAdditionalItemResponse;
import com.kodlamaio.rentACar.business.responses.orderedAdditionalItems.GetAllOrderedAdditionalItemResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;

@RestController
@RequestMapping("api/orderedadditionalitems")
public class OrderedAdditionalItemsController {

	private OrderedAdditionalItemService orderedAdditionalItemService;

	@Autowired
	public OrderedAdditionalItemsController(OrderedAdditionalItemService orderedAdditionalItemService) {
		this.orderedAdditionalItemService = orderedAdditionalItemService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody CreateOrderedAdditionalItemRequest createOrderedAdditionalItemRequest) {
		return this.orderedAdditionalItemService.add(createOrderedAdditionalItemRequest);
	}
	
	@PostMapping("/delete")
	public Result delete(@RequestBody DeleteOrderedAdditionalItemRequest deleteOrderedAdditionalItemRequest) {
		return this.orderedAdditionalItemService.delete(deleteOrderedAdditionalItemRequest);
	}
	
	@PostMapping("/update")
	public Result update(@RequestBody UpdateOrderedAdditionalItemRequest updateOrderedAdditionalItemRequest) {
		return this.orderedAdditionalItemService.update(updateOrderedAdditionalItemRequest);
	}
	
	@GetMapping("/getall")
	public DataResult<List<GetAllOrderedAdditionalItemResponse>> getAll(){
		return this.orderedAdditionalItemService.getAll();
	}
	
	@GetMapping("/getbyid")
	public DataResult<GetOrderedAdditionalItemResponse> getById(@RequestParam int id){
		return this.orderedAdditionalItemService.getById(id);
	}
}