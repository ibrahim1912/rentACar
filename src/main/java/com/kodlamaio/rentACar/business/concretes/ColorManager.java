package com.kodlamaio.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.business.abstracts.ColorService;
import com.kodlamaio.rentACar.business.requests.colors.CreateColorRequest;
import com.kodlamaio.rentACar.business.requests.colors.DeleteColorRequest;
import com.kodlamaio.rentACar.business.requests.colors.UpdateColorRequest;
import com.kodlamaio.rentACar.business.responses.colors.GetAllColorsResponse;
import com.kodlamaio.rentACar.business.responses.colors.GetColorResponse;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.ColorRepository;
import com.kodlamaio.rentACar.entities.concretes.Brand;
import com.kodlamaio.rentACar.entities.concretes.Color;


@Service
public class ColorManager implements ColorService{

	private ColorRepository colorRepository;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public ColorManager(ColorRepository colorRepository,ModelMapperService modelMapperService) {
		this.colorRepository = colorRepository;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result add(CreateColorRequest createColorRequest) {
		checkIfColorNameExists(createColorRequest.getName());
		
		Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);
		this.colorRepository.save(color);
		return new SuccessResult("COLOR.ADDED");		
	}

	@Override
	public Result delete(DeleteColorRequest deleteColorRequest) {
		checkIfColorIdExists(deleteColorRequest.getId());
		
		this.colorRepository.deleteById(deleteColorRequest.getId());
		return new SuccessResult("COLOR.DELETED");
	}

	@Override
	public Result update(UpdateColorRequest updateColorRequest) {
		checkIfColorIdExists(updateColorRequest.getId());
		checkIfColorNameExists(updateColorRequest.getName());
	
		Color color = this.modelMapperService.forRequest().map(updateColorRequest,Color.class);
		this.colorRepository.save(color);
		return new SuccessResult("COLOR.UPDATED");
		
	}

	@Override
	public DataResult<List<GetAllColorsResponse>> getAll() {
		List<Color> colors = this.colorRepository.findAll();
		List<GetAllColorsResponse> response = colors.stream().map(color->this.modelMapperService.forResponse().map(color, GetAllColorsResponse.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllColorsResponse>>(response,"COLORS.LISTED");
	}

	@Override
	public DataResult<GetColorResponse> getById(int id) {
		checkIfColorIdExists(id);
		
		Color color = colorRepository.findById(id);
		GetColorResponse response = this.modelMapperService.forResponse().map(color,GetColorResponse.class);
		return new SuccessDataResult<GetColorResponse>(response);
	}
	
	@Override
	public Color getByColorId(int colorId) {
		checkIfColorIdExists(colorId);
		Color color = this.colorRepository.findById(colorId);
		return color;
	}
	
	private void checkIfColorIdExists(int id) {
		Color color = this.colorRepository.findById(id);
		if(color == null) {
			throw new BusinessException("THERE.IS.NOT.COLOR"); 
		}
	}
	
	private void checkIfColorNameExists(String name) { 
		Color currentColor = this.colorRepository.findByName(name);
		if(currentColor != null) {
			throw new BusinessException("COLOR.NAME.EXISTS");
		}
	}



}
