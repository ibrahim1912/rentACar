package com.kodlamaio.rentACar.business.concretes;


import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



import com.kodlamaio.rentACar.business.abstracts.UserService;
import com.kodlamaio.rentACar.business.requests.users.CreateUserRequest;
import com.kodlamaio.rentACar.business.requests.users.DeleteUserRequest;
import com.kodlamaio.rentACar.business.requests.users.UpdateUserRequest;
import com.kodlamaio.rentACar.business.responses.users.GetAllUsersResponse;
import com.kodlamaio.rentACar.business.responses.users.GetUserResponse;
import com.kodlamaio.rentACar.core.adapters.UserValidationService;
import com.kodlamaio.rentACar.core.utilities.exceptions.BusinessException;
import com.kodlamaio.rentACar.core.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentACar.core.utilities.results.DataResult;
import com.kodlamaio.rentACar.core.utilities.results.Result;
import com.kodlamaio.rentACar.core.utilities.results.SuccessDataResult;
import com.kodlamaio.rentACar.core.utilities.results.SuccessResult;
import com.kodlamaio.rentACar.dataAccess.abstracts.UserRepository;
import com.kodlamaio.rentACar.entities.concretes.User;

@Service
public class UserManager implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapperService modelMapperService;
	@Autowired
	private UserValidationService userValidationService;
	
	@Override
	public Result add(CreateUserRequest createUserRequest) throws NumberFormatException, RemoteException {
		checkIfRealPerson(createUserRequest);
		checkIfIdentityIsSame(createUserRequest.getIdentityNumber());
		checkIfEmailIsSame(createUserRequest.getEmail());
		
		User user = this.modelMapperService.forRequest().map(createUserRequest, User.class);
		this.userRepository.save(user);
		return new SuccessResult("USER.ADDED");

	}

	@Override
	public DataResult<List<GetAllUsersResponse>> getAll() {
		List<User> users = this.userRepository.findAll();
		List<GetAllUsersResponse> response = users.stream()
				.map(user -> this.modelMapperService.forResponse().map(user, GetAllUsersResponse.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetAllUsersResponse>>(response);
	}

	@Override
	public DataResult<List<GetAllUsersResponse>> getAll(Integer pageNo, Integer pageSize) {
		Pageable pageable =  PageRequest.of(pageNo-1,pageSize);
		
		List<User> users = this.userRepository.findAll(pageable).getContent();
		List<GetAllUsersResponse> response = users.stream()
				.map(user -> this.modelMapperService.forResponse().map(user, GetAllUsersResponse.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<GetAllUsersResponse>>(response);
	}

	private void checkIfIdentityIsSame(String identity) {
		User user = this.userRepository.findByIdentityNumber(identity);
		if(user != null) {
			throw new BusinessException("USER.EXIST");
		}
	}
	
	@Override
	public Result delete(DeleteUserRequest deleteUserRequest) {
		this.userRepository.deleteById(deleteUserRequest.getId());
		return new SuccessResult("USER.DELETED");
	}

	@Override
	public Result update(UpdateUserRequest updateUserRequest) {
		checkIfEmailIsSame(updateUserRequest.getEmail());
		User user = this.modelMapperService.forRequest().map(updateUserRequest, User.class);
		User userFromDb =this.userRepository.findById(user.getId());
		user.setIdentityNumber(userFromDb.getIdentityNumber());
		this.userRepository.save(user);
		return new SuccessResult("USER.UPDATED");
	}

	@Override
	public DataResult<GetUserResponse> getById(int id) {
		User user = this.userRepository.findById(id);
		GetUserResponse response = this.modelMapperService.forResponse().map(user, GetUserResponse.class);
		return new SuccessDataResult<GetUserResponse>(response);
	}
	
	private void checkIfRealPerson(CreateUserRequest user) throws NumberFormatException, RemoteException {
		if(!userValidationService.checkIfRealPerson(user)) {
			throw new BusinessException("USER.VALIDATION.ERROR");
		}
	}
	
	private void checkIfEmailIsSame(String email) {
		User user = this.userRepository.findByEmail(email);
		if(user != null) {
			throw new BusinessException("EMAIL.EXIST");
		}
	}
}
