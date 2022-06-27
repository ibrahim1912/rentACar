package com.kodlamaio.rentACar.business.concretes;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.OrderedAdditionalItemService;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.CreateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.DeleteOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.UpdateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.responses.orderedAdditionalItems.GetAllOrderedAdditionalItemResponse;
import com.kodlamaio.rentACar.business.responses.orderedAdditionalItems.GetOrderedAdditionalItemResponse;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.OrderedAdditionalItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.entities.concretes.AdditionalItem;
import com.kodlamaio.rentACar.entities.concretes.OrderedAdditionalItem;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class OrderedAdditionalItemManager implements OrderedAdditionalItemService {

	private OrderedAdditionalItemRepository orderedAdditionalItemRepository;
	private ModelMapperService modelMapperService;
	private AdditionalItemRepository additionalItemRepository;
	private RentalRepository rentalRepository;

	@Autowired
	public OrderedAdditionalItemManager(OrderedAdditionalItemRepository orderedAdditionalItemRepository,
			ModelMapperService modelMapperService, AdditionalItemRepository additionalItemRepository,
			RentalRepository rentalRepository) {

		this.additionalItemRepository =additionalItemRepository;
		this.modelMapperService = modelMapperService;
		this.orderedAdditionalItemRepository = orderedAdditionalItemRepository;
		this.rentalRepository = rentalRepository;
	}

	@Override
	public Result add(CreateOrderedAdditionalItemRequest createOrderedAdditionalItemRequest) {
		AdditionalItem additionalItem = this.additionalItemRepository
				.findById(createOrderedAdditionalItemRequest.getAdditionalItemId()).get();
		OrderedAdditionalItem orderedAdditionalItem = this.modelMapperService.forRequest()
				.map(createOrderedAdditionalItemRequest, OrderedAdditionalItem.class);
	
	

		orderedAdditionalItem.setTotalPrice(calculateTotalPrice(orderedAdditionalItem, additionalItem.getPrice()));
		this.orderedAdditionalItemRepository.save(orderedAdditionalItem);
		return new SuccessResult("ADDITIONAL.SERVICE.ADDED");
	}

	//burası kaldı
	@Override
	public Result update(UpdateOrderedAdditionalItemRequest updateAdditionalFeatureServiceRequest) {
		checkIfOrderedAdditionalItemIdExists(updateAdditionalFeatureServiceRequest.getId());
		//checkIfRentalIdSame(updateAdditionalFeatureServiceRequest.getId(),updateAdditionalFeatureServiceRequest.getRentalId());
		OrderedAdditionalItem orderedAdditionalItem = this.modelMapperService.forRequest()
				.map(updateAdditionalFeatureServiceRequest, OrderedAdditionalItem.class);
		Rental rental = this.rentalRepository.findById(orderedAdditionalItem.getRental().getId());
		
		AdditionalItem additionalItem = this.additionalItemRepository
				.findById(orderedAdditionalItem.getAdditionalItem().getId()).get();
		
		checkIfRentalIdSame(orderedAdditionalItem.getId(),rental.getId());
		
		
		
		orderedAdditionalItem.setTotalPrice(calculateTotalPrice(orderedAdditionalItem, additionalItem.getPrice()));
		this.orderedAdditionalItemRepository.save(orderedAdditionalItem);
		return new SuccessResult("SERVICES.UPDATE");
	}

	@Override
	public Result delete(DeleteOrderedAdditionalItemRequest deleteorderedAdditionalItem) {
		checkIfOrderedAdditionalItemIdExists(deleteorderedAdditionalItem.getId());
		
		this.orderedAdditionalItemRepository.deleteById(deleteorderedAdditionalItem.getId());
		return new SuccessResult("SERVICE.DELETED");
	}

	@Override
	public DataResult<List<GetAllOrderedAdditionalItemResponse>> getAll() {
		List<OrderedAdditionalItem> additionalFeatureServices = this.orderedAdditionalItemRepository.findAll();
		List<GetAllOrderedAdditionalItemResponse> response = additionalFeatureServices.stream()
				.map(service -> this.modelMapperService.forResponse().map(service,
						GetAllOrderedAdditionalItemResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllOrderedAdditionalItemResponse>>(response, "SERVICES.LISTED");
	}

	@Override
	public DataResult<GetOrderedAdditionalItemResponse> getById(int id) {
		checkIfOrderedAdditionalItemIdExists(id);
		
		OrderedAdditionalItem orderedAdditionalItem = this.orderedAdditionalItemRepository.findById(id);
		GetOrderedAdditionalItemResponse response = this.modelMapperService.forResponse()
				.map(orderedAdditionalItem, GetOrderedAdditionalItemResponse.class);
		return new SuccessDataResult<GetOrderedAdditionalItemResponse>(response);
	}
	
	private void checkIfRentalIdSame(int orderId,int rentalId) {
		Rental rental = this.rentalRepository.findById(rentalId);
		if(rental.getId() != orderId || rental==null) {
			throw new BusinessException("ERROR"); //ismi düzelt 
		}
		
	}
	
	private void checkIfOrderedAdditionalItemIdExists(int id) {
		OrderedAdditionalItem orderedAdditionalItem = this.orderedAdditionalItemRepository.findById(id);
		if(orderedAdditionalItem == null) {
			throw new BusinessException("THERE.IS.NOT.ORDERED.ADDITIONAL.ITEM");
		}
	}
	
	private double calculateTotalPrice(OrderedAdditionalItem orderedAdditionalItem,double price) {
		double totalPrice = 0;
		int daysDifference = (int) ChronoUnit.DAYS.between(orderedAdditionalItem.getPickUpDate(), orderedAdditionalItem.getReturnDate());
		totalPrice = price * daysDifference;
		orderedAdditionalItem.setTotalDay(daysDifference);
		return totalPrice;
	}
	

}
