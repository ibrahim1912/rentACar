package com.kodlamaio.rentACar.business.abstracts;

import java.util.List;

import com.kodlamaio.rentACar.business.requests.invoices.CreateInvoiceRequest;
import com.kodlamaio.rentACar.business.requests.invoices.DeleteInvoiceRequest;
import com.kodlamaio.rentACar.business.requests.invoices.UpdateInvoiceRequest;
import com.kodlamaio.rentACar.business.responses.invoices.GetAllInvoicesResponse;
import com.kodlamaio.rentACar.business.responses.invoices.GetInvoiceResponse;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureItem;
import com.kodlamaio.rentACar.entities.concretes.AdditionalFeatureService;

public interface InvoiceService {

	Result add(CreateInvoiceRequest createInvoiceRequest);

	Result delete(DeleteInvoiceRequest deleteInvoiceRequest);

	Result update(UpdateInvoiceRequest updateInvoiceRequest);

	DataResult<List<GetAllInvoicesResponse>> getAll();

	DataResult<GetInvoiceResponse> getById(int id);

	DataResult<List<AdditionalFeatureItem>> getAllAdditionalFeatureItemsTest(int id);

}
