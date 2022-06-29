package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.CreateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.DeleteOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.UpdateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.responses.orderedAdditionalItems.GetAllOrderedAdditionalItemsResponse;
import com.kodlamaio.rentACar.business.responses.orderedAdditionalItems.GetOrderedAdditionalItemResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.entities.concretes.OrderedAdditionalItem;

public interface OrderedAdditionalItemService {
	
	Result add(CreateOrderedAdditionalItemRequest createOrderedAdditionalItemRequest);
	Result update(UpdateOrderedAdditionalItemRequest updateOrderedAdditionalItemRequest);
	Result delete(DeleteOrderedAdditionalItemRequest deleteOrderedAdditionalItemRequest);
	
	DataResult<List<GetAllOrderedAdditionalItemsResponse>> getAll();
	DataResult<GetOrderedAdditionalItemResponse> getById(int id);
	
	OrderedAdditionalItem getByOrderedAdditionalItemId(int orderedAdditionalItemId);
	List<OrderedAdditionalItem> getByRentalId(int rentalId);
	
}
