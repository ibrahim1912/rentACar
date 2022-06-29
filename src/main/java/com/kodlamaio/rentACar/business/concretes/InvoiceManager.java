package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.AdditionalItemService;
import com.kodlamaio.rentACar.business.abstracts.InvoiceService;
import com.kodlamaio.rentACar.business.abstracts.OrderedAdditionalItemService;
import com.kodlamaio.rentACar.business.abstracts.RentalService;
import com.kodlamaio.rentACar.business.requests.invoices.CreateInvoiceRequest;
import com.kodlamaio.rentACar.business.requests.invoices.DeleteInvoiceRequest;
import com.kodlamaio.rentACar.business.requests.invoices.UpdateInvoiceRequest;
import com.kodlamaio.rentACar.business.responses.invoices.GetAllInvoicesResponse;
import com.kodlamaio.rentACar.business.responses.invoices.GetInvoiceResponse;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.OrderedAdditionalItemRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.InvoiceRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.entities.concretes.AdditionalItem;
import com.kodlamaio.rentACar.entities.concretes.OrderedAdditionalItem;
import com.kodlamaio.rentACar.entities.concretes.Brand;
import com.kodlamaio.rentACar.entities.concretes.Invoice;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class InvoiceManager implements InvoiceService {
	
	private InvoiceRepository invoiceRepository;
	private RentalService rentalService;
	private OrderedAdditionalItemService orderedAdditionalItemService;
	private AdditionalItemService additionaItemService;
	private ModelMapperService modelMapperService;

	@Autowired
	public InvoiceManager(InvoiceRepository invoiceRepository, RentalService rentalService,
			OrderedAdditionalItemService orderedAdditionalItemService, ModelMapperService modelMapperService,
			AdditionalItemService additionaItemService) {

		this.invoiceRepository = invoiceRepository;
		this.rentalService = rentalService;
		this.orderedAdditionalItemService = orderedAdditionalItemService;
		this.modelMapperService = modelMapperService;
		this.additionaItemService = additionaItemService;
	}

	@Override
	public Result add(CreateInvoiceRequest createInvoiceRequest) {
		checkIfInvoiceStatusZero(createInvoiceRequest.getRentalId());
		checkIfInvoiceNumberExists(createInvoiceRequest.getInvoiceNumber());
		checkIfRentalIdExists(createInvoiceRequest.getRentalId());
		
		Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
		invoice.setCurrentDate(LocalDate.now());
		invoice.setTotalPrice(calculateTotalPrice(invoice.getRental().getId()));
		invoice.setState(0); // invoice aktif (iptal edilmemiş)

		this.invoiceRepository.save(invoice);
		return new SuccessResult("INVOICE.CREATED");
	}

	@Override
	public Result delete(DeleteInvoiceRequest deleteInvoiceRequest) {
		checkIfInvoiceIdExists(deleteInvoiceRequest.getId());
		
		Invoice invoice = this.invoiceRepository.findById(deleteInvoiceRequest.getId());
		invoice.setState(1); //invoice iptal edildi
		this.invoiceRepository.save(invoice);
		return new SuccessResult("INVOICE.CANCELLED");
	}

	@Override
	public Result update(UpdateInvoiceRequest updateInvoiceRequest) {
		return null;
	}

	@Override
	public DataResult<List<GetAllInvoicesResponse>> getAll() {
		List<Invoice> invoices = this.invoiceRepository.findAll();
		List<GetAllInvoicesResponse> response = invoices.stream()
				.map(invoice -> this.modelMapperService.forResponse().map(invoice, GetAllInvoicesResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllInvoicesResponse>>(response);
	}

	@Override
	public DataResult<GetInvoiceResponse> getById(int id) {
		checkIfInvoiceIdExists(id);
		
		Invoice invoice = this.invoiceRepository.findById(id);
		GetInvoiceResponse response = this.modelMapperService.forResponse().map(invoice, GetInvoiceResponse.class);
		return new SuccessDataResult<GetInvoiceResponse>(response);
	}

	@Override
	public DataResult<List<AdditionalItem>> getAllAdditionalItemsByRentalId(int rentalId) { 
		checkIfRentalIdExists(rentalId);
		
		List<OrderedAdditionalItem> orderedAdditionalItems = this.orderedAdditionalItemService
				.getByRentalId(rentalId);
		List<AdditionalItem> additionalItems = new ArrayList<AdditionalItem>();

		for (OrderedAdditionalItem orderedAdditionalItem : orderedAdditionalItems) {
			AdditionalItem additionalItem = this.additionaItemService
					.getByAdditionalItemId(orderedAdditionalItem.getAdditionalItem().getId());
			additionalItems.add(additionalItem);
		}
		return new SuccessDataResult<List<AdditionalItem>>(additionalItems);
	}

	private double calculateTotalPrice(int rentalId) {
		Rental rental = this.rentalService.getByRentalId(rentalId);
		double price = calculateTotalPriceOfAdditionalFeatureServiceByRentalId(rentalId);
		double totalPrice = rental.getTotalPrice() + price;
		return totalPrice;
	}

	private double calculateTotalPriceOfAdditionalFeatureServiceByRentalId(int id) {
		Rental rental = this.rentalService.getByRentalId(id);
		List<OrderedAdditionalItem> liste = rental.getAdditionalFeatureServices();
		double totalPrice = 0;
		for (OrderedAdditionalItem additionalFeatureService : liste) {
			totalPrice += additionalFeatureService.getTotalPrice();
		}
		return totalPrice;

	}

	private void checkIfInvoiceStatusZero(int id) {
		Invoice invoicefromDb = this.invoiceRepository.findByRentalId(id);
		if ((invoicefromDb != null) && (invoicefromDb.getState() != 1)) {
			throw new BusinessException("RENTAL.HAVE.ALREADY.A.INVOICE");
		}
	}

	private void checkIfInvoiceNumberExists(int invoiceNumber) {
		Invoice invoice = this.invoiceRepository.findByInvoiceNumber(invoiceNumber);
		if (invoice != null) {
			throw new BusinessException("INVOICE.NUMBER.EXISTS");
		}
	}
	
	private void checkIfInvoiceIdExists(int id) {
		Invoice invoice = this.invoiceRepository.findById(id);
		if(invoice == null) {
			throw new BusinessException("THERE.IS.NOT.INVOICE");
		}
	}
	
	private void checkIfRentalIdExists(int rentalId) {
		Rental rental = this.rentalService.getByRentalId(rentalId);
		if(rental == null) {
			throw new BusinessException("THERE.IS.NO.RENTED.CAR");
		}
	}
	

	

}
