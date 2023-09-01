package pet.store.service;

import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;

	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
		}
		
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId).orElseThrow(() 
				-> new NoSuchElementException("Pet store with ID=" + petStoreId + " does not exist."));
	}
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		
		Employee employee = findOrCreateEmployee(employeeId, petStore);
		
		copyEmployeeFields(employee, petStoreEmployee);
		
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		
		return new PetStoreEmployee(employeeDao.save(employee));
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		
	}
	
	private Employee findOrCreateEmployee(Long employeeId, PetStore petStore) {
		
		if (Objects.isNull(employeeId)) {
			return new Employee();
		} else {
			return findEmployeeById(employeeId, petStore);
		}
	}
	
	private Employee findEmployeeById(Long employeeId, PetStore petStore) {
		return employeeDao.findById(employeeId).orElseThrow(()
				-> new NoSuchElementException("Employee with ID=" + employeeId + " was not found."));
	}
	
	@Autowired
	private CustomerDao customerDao;
	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		
		Customer customer = findOrCreateCustomer(customerId, petStoreId);
		
		copyCustomerFields(customer, petStoreCustomer);
		
		petStore.getCustomers().add(customer);
		
		return new PetStoreCustomer(customerDao.save(customer));
	}
	
	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}
	
	private Customer findOrCreateCustomer(Long customerId, Long petStoreId) {
		
		if (Objects.isNull(customerId)) {
			return new Customer();
		} else {
			return findCustomerById(customerId, petStoreId);
		}
	}
	
	private Customer findCustomerById(Long customerId, Long petStoreId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(()
				-> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));
		
		boolean found = false;
		
		for (PetStore petStores : customer.getPetStores()) {
			if (petStores.getPetStoreId() == petStoreId) {
				found = true;
			}
		} 
		
		if (!found) {
			throw new IllegalArgumentException("Customer for pet store with ID=" + petStoreId + " was not found.");
		} else {
			return customer;
		}
		
	}
}
