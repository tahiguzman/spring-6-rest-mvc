package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Rollback
    @Transactional
    @Test
    void saveNewCostumerTest() {

     CustomerDTO customerDTO = CustomerDTO.builder()
             .name("New Costumer")
             .build();

     ResponseEntity responseEntity = customerController.handlePostCustomer(customerDTO);

     assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
     assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

     String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
     UUID savedUUID = UUID.fromString(locationUUID[4]);

     Customer customer = customerRepository.findById(savedUUID).get();
     assertThat(customer).isNotNull();

    }

    @Test
    void testByIdNotFound() {
        assertThrows(NotFoundException.class, () ->{
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerController.getCustomerById(customer.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testListAll() {
        List<CustomerDTO> dtos = customerController.listAllCustomers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testListAllEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listAllCustomers();
        assertThat(dtos.size()).isEqualTo(0);
    }
}