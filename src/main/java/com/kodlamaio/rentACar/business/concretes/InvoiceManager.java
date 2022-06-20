package com.kodlamaio.rentACar.business.concretes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.InvoiceService;
import com.kodlamaio.rentACar.business.requests.invoices.CreateInvoiceRequest;
import com.kodlamaio.rentACar.business.requests.invoices.DeleteInvoiceRequest;
import com.kodlamaio.rentACar.business.requests.invoices.UpdateInvoiceRequest;
import com.kodlamaio.rentACar.business.responses.invoices.GetAllInvoicesResponse;
import com.kodlamaio.rentACar.business.responses.invoices.GetInvoiceResponse;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.AdditionalFeatureServiceRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.InvoiceRepository;
import com.kodlamaio.rentACar.dataAccess.abstracts.RentalRepository;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureService;
import com.kodlamaio.rentACar.entities.concretes.Invoice;
import com.kodlamaio.rentACar.entities.concretes.Rental;

@Service
public class InvoiceManager implements InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private RentalRepository rentalRepository;
	
	@Autowired
	private AdditionalFeatureServiceRepository additionalFeatureServiceRepository;
	
	@Autowired
	private ModelMapperService modelMapperService;
	
	@Override
	public Result add(CreateInvoiceRequest createInvoiceRequest) {
		Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
		invoice.setCurrentDate(LocalDate.now());
		invoice.setTotalPrice(calculateTotalPrice(invoice.getRental().getId(), invoice.getAdditionalFeatureService().getId()));
		this.invoiceRepository.save(invoice);
		return new SuccessResult("INVOICE.CREATED");
	}

	@Override
	public Result delete(DeleteInvoiceRequest deleteInvoiceRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result update(UpdateInvoiceRequest updateInvoiceRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataResult<List<GetAllInvoicesResponse>> getAll() {
		List<Invoice> invoices = this.invoiceRepository.findAll();
		List<GetAllInvoicesResponse> response = invoices.stream().map(invoice -> this.modelMapperService.forResponse().map(invoice, GetAllInvoicesResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllInvoicesResponse>>(response);
	}

	@Override
	public DataResult<GetInvoiceResponse> getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private double calculateTotalPrice(int rentalId,int additionalFeatureServiceId) {
		Rental rental = this.rentalRepository.findById(rentalId);
		AdditionalFeatureService additionalFeatureService = this.additionalFeatureServiceRepository.findById(additionalFeatureServiceId);
		double totalPrice = rental.getTotalPrice() + additionalFeatureService.getTotalPrice();
		return totalPrice;
	}

	

}
