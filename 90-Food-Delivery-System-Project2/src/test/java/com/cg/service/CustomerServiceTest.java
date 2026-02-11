package com.cg.service;

import com.cg.dto.CustomerDto;
import com.cg.entity.Customer;
import com.cg.mapper.CustomerMapper;
import com.cg.repository.CustomerRepository;
<<<<<<< HEAD
=======

>>>>>>> 94b12fa (food delivery)
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

<<<<<<< HEAD
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // ---------------------------------------------------------------------
    // TEST 1: register() positive
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("register(): positive → saves and returns DTO")
    void register_positive() {

        CustomerDto input = new CustomerDto();
        input.setEmail("test@gmail.com");
        input.setPassword("123");

        Customer mappedEntity = new Customer();
        mappedEntity.setEmail("test@gmail.com");

        Customer saved = new Customer();
        saved.setCustomerId(1L);
        saved.setEmail("test@gmail.com");

        try (MockedStatic<CustomerMapper> mapper = mockStatic(CustomerMapper.class)) {

            mapper.when(() -> CustomerMapper.fromCreateDto(eq(input), any()))
                    .thenReturn(mappedEntity);

            mapper.when(() -> CustomerMapper.toDto(saved))
                    .thenReturn(new CustomerDto(1L, null, null,
                            "test@gmail.com", null, null, "ROLE_USER"));

            when(customerRepository.save(mappedEntity)).thenReturn(saved);

            CustomerDto result = customerService.register(input);

            assertThat(result.getCustomerId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo("test@gmail.com");
        }
    }

    // ---------------------------------------------------------------------
    // TEST 2: register() negative
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("register(): negative → mapper throws exception")
    void register_negative() {

        CustomerDto input = new CustomerDto();

        try (MockedStatic<CustomerMapper> mapper = mockStatic(CustomerMapper.class)) {

            mapper.when(() -> CustomerMapper.fromCreateDto(eq(input), any()))
                    .thenThrow(new RuntimeException("Mapping failed"));

            assertThatThrownBy(() -> customerService.register(input))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Mapping failed");
        }
    }

    // ---------------------------------------------------------------------
    // TEST 3: getById() positive
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("getById(): positive → returns DTO")
    void getById_positive() {

        Customer entity = new Customer();
        entity.setCustomerId(5L);

        when(customerRepository.findById(5L)).thenReturn(Optional.of(entity));

        try (MockedStatic<CustomerMapper> mapper = mockStatic(CustomerMapper.class)) {

            mapper.when(() -> CustomerMapper.toDto(entity))
                    .thenReturn(new CustomerDto(5L, null, null, null, null, null, null));

            CustomerDto result = customerService.getById(5L);

            assertThat(result.getCustomerId()).isEqualTo(5L);
        }
    }

    // ---------------------------------------------------------------------
    // TEST 4: getById() negative
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("getById(): negative → throws NoSuchElementException")
    void getById_negative() {

        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getById(99L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
=======
	@InjectMocks
	private CustomerService customerService;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	// TEST 1: register() positive
	@Test
	@DisplayName("register(): positive → saves and returns DTO")
	void register_positive() {
		CustomerDto input = new CustomerDto();
		input.setEmail("test@gmail.com");
		input.setPassword("123");
		Customer mappedEntity = new Customer();
		mappedEntity.setEmail("test@gmail.com");
		Customer saved = new Customer();
		saved.setCustomerId(1L);
		saved.setEmail("test@gmail.com");

		try (MockedStatic<CustomerMapper> mapper = mockStatic(CustomerMapper.class)) {
			mapper.when(() -> CustomerMapper.fromCreateDto(eq(input), any())).thenReturn(mappedEntity);
			mapper.when(() -> CustomerMapper.toDto(saved))
					.thenReturn(new CustomerDto(1L, null, null, "test@gmail.com", null, null, "ROLE_USER"));
			when(customerRepository.save(mappedEntity)).thenReturn(saved);
			CustomerDto result = customerService.register(input);
			assertThat(result.getCustomerId()).isEqualTo(1L);
			assertThat(result.getEmail()).isEqualTo("test@gmail.com");
		}
	}

	// TEST 2: register() negative
	@Test
	@DisplayName("register(): negative → mapper throws exception")
	void register_negative() {
		CustomerDto input = new CustomerDto();
		try (MockedStatic<CustomerMapper> mapper = mockStatic(CustomerMapper.class)) {
			mapper.when(() -> CustomerMapper.fromCreateDto(eq(input), any()))
					.thenThrow(new RuntimeException("Mapping failed"));
			assertThatThrownBy(() -> customerService.register(input)).isInstanceOf(RuntimeException.class)
					.hasMessage("Mapping failed");
		}
	}

	// TEST 3: getById() positive
	@Test
	@DisplayName("getById(): positive → returns DTO")
	void getById_positive() {
		Customer entity = new Customer();
		entity.setCustomerId(5L);
		when(customerRepository.findById(5L)).thenReturn(Optional.of(entity));
		try (MockedStatic<CustomerMapper> mapper = mockStatic(CustomerMapper.class)) {
			mapper.when(() -> CustomerMapper.toDto(entity))
					.thenReturn(new CustomerDto(5L, null, null, null, null, null, null));
			CustomerDto result = customerService.getById(5L);
			assertThat(result.getCustomerId()).isEqualTo(5L);
		}
	}

	// TEST 4: getById() negative
	@Test
	@DisplayName("getById(): negative → throws NoSuchElementException")
	void getById_negative() {
		when(customerRepository.findById(99L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> customerService.getById(99L)).isInstanceOf(NoSuchElementException.class);
	}

}
>>>>>>> 94b12fa (food delivery)
