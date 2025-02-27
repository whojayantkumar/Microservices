package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.rabbitmq.RabbitMqProducer;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {


    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private RabbitMqProducer rabbitMqProducer;

    /***
     *
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        CustomerDto finalCustomerDto = customerDto;
        customerRepository.findByMobileNumber(customerDto.getMobileNumber()).ifPresent(c -> {
            throw new CustomerAlreadyExistsException("Customer already registered with mobile number: " + finalCustomerDto.getMobileNumber());
        });
        customer = customerRepository.save(customer);
        Accounts accounts = accountsRepository.save(createNewAccount(customer));
        customerDto = CustomerMapper.mapToCustomerDto(customer, customerDto);
        customerDto.setAccounts(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        rabbitMqProducer.sendMessage(customerDto);
    }

    /***
     *
     * @param mobileNumber
     * @return customerDto - CustomerDto Object
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccounts(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
        return customerDto;
    }

    /***
     *
     * @param customerDto
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        if (customerDto != null && customerDto.getAccounts() != null) {
            Accounts account = accountsRepository.findById(customerDto.getAccounts().getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber", customerDto.getAccounts().getAccountNumber().toString()));
            Customer customer = customerRepository.findById(account.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", account.getCustomerId().toString()));
            accountsRepository.save(AccountsMapper.mapToAccounts(customerDto.getAccounts(), account));
            customerRepository.save(CustomerMapper.mapToCustomer(customerDto, customer));
            return true;
        }
        return false;
    }

    /***
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

}
