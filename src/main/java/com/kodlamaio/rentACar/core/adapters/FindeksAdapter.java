package com.kodlamaio.rentACar.core.adapters;

import org.springframework.stereotype.Service;

import com.kodlamaio.rentACar.core.outServices.Findeks;
@Service
public class FindeksAdapter  implements FindeksValidationService{

	@Override
	public int calculateFindeksScoreOfUser(String identityNumber) {

		Findeks findeks = new Findeks();
		int result = findeks.calculateFindeksScoreOfUser(identityNumber);
		return result;
	}

}
