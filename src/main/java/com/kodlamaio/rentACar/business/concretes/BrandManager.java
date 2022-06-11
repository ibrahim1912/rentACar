package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.BrandService;
import com.kodlamaio.rentACar.business.requests.brands.CreateBrandRequest;
import com.kodlamaio.rentACar.business.requests.brands.DeleteBrandRequest;
import com.kodlamaio.rentACar.business.requests.brands.UpdateBrandRequest;
import com.kodlamaio.rentACar.business.responses.brands.GetAllBrandsResponse;
import com.kodlamaio.rentACar.business.responses.brands.GetBrandResponse;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.BrandRepository;
import com.kodlamaio.rentACar.entities.concretes.Brand;
//BrnadServiceImpl

@Service
public class BrandManager implements BrandService {

	private BrandRepository brandRepository;
	private ModelMapperService modelMapperService;

	@Autowired // brandRepository onun somutunu bul bana ver new ler
	public BrandManager(BrandRepository brandRepository,ModelMapperService modelMapperService) {
		this.brandRepository = brandRepository;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateBrandRequest createBrandRequest) {
		// iş kuralları
		//Brand brand = new Brand();
		//brand.setName(createBrandRequest.getName()); // gelen requesti veritabanı nesnesine çevirdim mapledik
		Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);
		this.brandRepository.save(brand);
		return new SuccessResult("BRAND.ADDED");

	}

	@Override
	public DataResult<GetBrandResponse> getById(int id) {
		Brand brand = this.brandRepository.findById(id);
		GetBrandResponse response = this.modelMapperService.forRequest().map(brand,GetBrandResponse.class);
		return new SuccessDataResult<GetBrandResponse>(response,"BRAND.LISTED");
	}

	@Override
	public DataResult<List<GetAllBrandsResponse>> getAll() {

		List<Brand> brands = this.brandRepository.findAll();
		List<GetAllBrandsResponse> response = brands.stream().map(brand->this.modelMapperService.forResponse().map(brand, GetAllBrandsResponse.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllBrandsResponse>>(response,"BRAND.LISTED");
	}

	@Override
	public Result delete(DeleteBrandRequest deleteBrandRequest) {
		brandRepository.deleteById(deleteBrandRequest.getId());
		return new SuccessResult("BRAND.DELETED");
	}

	@Override
	public Result update(UpdateBrandRequest updateBrandRequest) {
		//Brand brand = brandRepository.findById(updateBrandRequest.getId());
		//brand.setName(updateBrandRequest.getName());
		Brand brand = this.modelMapperService.forRequest().map(updateBrandRequest,Brand.class);
		this.brandRepository.save(brand);
		return new SuccessResult("BRAND.UPDATE");

	}
	
}
