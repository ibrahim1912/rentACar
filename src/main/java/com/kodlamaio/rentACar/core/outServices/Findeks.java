package com.kodlamaio.rentACar.core.outServices;

public class Findeks {

	public int calculateFindeksScoreOfUser(String identityNumber) {
		int max=1900;
		int min =650;
		int score = (int)Math.floor(Math.random() * (max - min + 1) + min);
		System.out.println("Findeks Score :" + score);
		return score;
	}
}
