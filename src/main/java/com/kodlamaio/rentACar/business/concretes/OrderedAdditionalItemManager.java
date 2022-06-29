package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AdditionalItemService;
import com.kodlamaio.rentACar.business.abstracts.OrderedAdditionalItemService;
import com.kodlamaio.rentACar.business.abstracts.RentalService;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.CreateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.DeleteOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.requests.orderedAdditionalItems.UpdateOrderedAdditionalItemRequest;
import com.kodlamaio.rentACar.business.responses.orderedAdditionalItems.GetAllOrderedAdditionalItemsResponse;
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
	private AdditionalItemService additionalItemService;
	private RentalService rentalService;
	

	@Autowired
	public OrderedAdditionalItemManager(OrderedAdditionalItemRepository orderedAdditionalItemRepository,
			ModelMapperService modelMapperService,
			RentalService rentalService,
			AdditionalItemService additionalItemService) {

		this.modelMapperService = modelMapperService;
		this.orderedAdditionalItemRepository = orderedAdditionalItemRepository;
		this.rentalService = rentalService;
		this.additionalItemService = additionalItemService;
	}

	@Override
	public Result add(CreateOrderedAdditionalItemRequest createOrderedAdditionalItemRequest) {
		
		checkIfRentalIdExists(createOrderedAdditionalItemRequest.getRentalId());
		checkIfAdditionalItemIdExists(createOrderedAdditionalItemRequest.getAdditionalItemId());
		checkIfDatesAreCorrect(createOrderedAdditionalItemRequest.getPickUpDate(),createOrderedAdditionalItemRequest.getReturnDate());
		
		AdditionalItem additionalItem = this.additionalItemService
				.getByAdditionalItemId(createOrderedAdditionalItemRequest.getAdditionalItemId());
		OrderedAdditionalItem orderedAdditionalItem = this.modelMapperService.forRequest()
				.map(createOrderedAdditionalItemRequest, OrderedAdditionalItem.class);
	
	

		orderedAdditionalItem.setTotalPrice(calculateTotalPrice(orderedAdditionalItem, additionalItem.getPrice()));
		orderedAdditionalItem.setTotalDay(calculateTotalDays(orderedAdditionalItem));
		
		this.orderedAdditionalItemRepository.save(orderedAdditionalItem);
		return new SuccessResult("ADDITIONAL.SERVICE.ADDED");
	}

	@Override
	public Result update(UpdateOrderedAdditionalItemRequest updateAdditionalFeatureServiceRequest) {
		
		checkIfOrderedAdditionalItemIdExists(updateAdditionalFeatureServiceRequest.getId());
		checkIfRentalIdExists(updateAdditionalFeatureServiceRequest.getRentalId());
		checkIfAdditionalItemIdExists(updateAdditionalFeatureServiceRequest.getAdditionalItemId());
		checkIfDatesAreCorrect(updateAdditionalFeatureServiceRequest.getPickUpDate(),updateAdditionalFeatureServiceRequest.getReturnDate());
		
	
		OrderedAdditionalItem orderedAdditionalItem = this.modelMapperService.forRequest()
				.map(updateAdditionalFeatureServiceRequest, OrderedAdditionalItem.class);
		
		AdditionalItem additionalItem = this.additionalItemService
				.getByAdditionalItemId(orderedAdditionalItem.getAdditionalItem().getId());
		orderedAdditionalItem.setTotalPrice(calculateTotalPrice(orderedAdditionalItem, additionalItem.getPrice()));
		orderedAdditionalItem.setTotalDay(calculateTotalDays(orderedAdditionalItem));
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
	public DataResult<List<GetAllOrderedAdditionalItemsResponse>> getAll() {
		List<OrderedAdditionalItem> additionalFeatureServices = this.orderedAdditionalItemRepository.findAll();
		List<GetAllOrderedAdditionalItemsResponse> response = additionalFeatureServices.stream()
				.map(service -> this.modelMapperService.forResponse().map(service,
						GetAllOrderedAdditionalItemsResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllOrderedAdditionalItemsResponse>>(response, "SERVICES.LISTED");
	}

	@Override
	public DataResult<GetOrderedAdditionalItemResponse> getById(int id) {
		checkIfOrderedAdditionalItemIdExists(id);
		
		OrderedAdditionalItem orderedAdditionalItem = this.orderedAdditionalItemRepository.findById(id);
		GetOrderedAdditionalItemResponse response = this.modelMapperService.forResponse()
				.map(orderedAdditionalItem, GetOrderedAdditionalItemResponse.class);
		return new SuccessDataResult<GetOrderedAdditionalItemResponse>(response);
	}
	
	@Override
	public OrderedAdditionalItem getByOrderedAdditionalItemId(int orderedAdditionalItemId) {
		checkIfAdditionalItemIdExists(orderedAdditionalItemId);
		OrderedAdditionalItem orderedAdditionalItem = this.orderedAdditionalItemRepository.findById(orderedAdditionalItemId);
		return orderedAdditionalItem;
	}
	
	@Override
	public List<OrderedAdditionalItem> getByRentalId(int rentalId) {
		checkIfRentalIdExists(rentalId);
		List<OrderedAdditionalItem> orderedAdditionalItems = this.orderedAdditionalItemRepository.findByRentalId(rentalId);
		return orderedAdditionalItems;
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
		return totalPrice;
	}
	
	private int calculateTotalDays(OrderedAdditionalItem orderedAdditionalItem) {
		int daysDifference = (int) ChronoUnit.DAYS.between(orderedAdditionalItem.getPickUpDate(), orderedAdditionalItem.getReturnDate());
		return daysDifference;
	}
	
	private void checkIfRentalIdExists(int rentalId) {
		Rental rental = this.rentalService.getByRentalId(rentalId);
		if(rental == null) {
			throw new BusinessException("THERE.IS.NOT.RENTAL"); 
		}
	}
	
	private void checkIfAdditionalItemIdExists(int additionalItemId) {
		AdditionalItem additionalItem = this.additionalItemService.getByAdditionalItemId(additionalItemId);
		if(additionalItem == null) {
			throw new BusinessException("THERE.IS.NOT.ADDITIONAL.ITEM"); 

		}
	}
	
	private void checkIfDatesAreCorrect(LocalDate pickUpDate, LocalDate returnDate) {
		if (!pickUpDate.isBefore(returnDate) || pickUpDate.isBefore(LocalDate.now())) {
			throw new BusinessException("DATE.ERROR"); 
		}
	}

	

}
