package pet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet-store")
@Slf4j
public class PetStoreController {
	
	@Autowired 
	private PetStoreService petStoreService;
	
	@PostMapping("/pet-store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData savePetStore(
			@RequestBody PetStoreData petStoreData) {
		
		log.info("Created", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("/pet-store/{petStoreId}")
	public PetStoreData updatePetStore(
			@PathVariable Long petStoreId,
			@RequestBody PetStoreData petStoreData) {
		
		petStoreData.setPetStoreId(petStoreId);
		
		log.info("Updated pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PostMapping("/pet-store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addEmployee(
			@PathVariable("petStoreId") Long petStoreId,
			@RequestBody PetStoreEmployee employee) {
		log.info("Adding employee to pet store with ID={petStoreId}", petStoreId);
		return petStoreService.saveEmployee(petStoreId, employee);
	}
	
	@PostMapping("/pet-store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer addCustomer(
			@PathVariable("petStoreId") Long petStoreId,
			@RequestBody PetStoreCustomer customer) {
		log.info("Adding customer to pet store with ID={petStoreId}", petStoreId);
		return petStoreService.saveCustomer(petStoreId, customer);
	}
}
