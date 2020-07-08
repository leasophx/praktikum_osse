package de.hfu.myResidentTests;

import de.hfu.residents.domain.Resident;
import de.hfu.residents.repository.ResidentRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ResidentRepositoryStub implements ResidentRepository {

	private List<Resident> residents;
	public ResidentRepositoryStub(List<Resident> liste){
		residents = liste;
	}

	

	@Override
	public List<Resident> getResidents() {
		return residents;
	}
	

}