package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.ResponseDto;
import com.eazybytes.accounts.service.IAccountsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {

    private IAccountsService iAccountsService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccount(
            @Pattern(regexp = "($|[0-9]{10})", message = "Mobile number should be 10 digits")
            @RequestParam String mobileNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(iAccountsService.fetchAccount(mobileNumber));

    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if (isUpdated)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(
            @Pattern(regexp = "($|[0-9]{10})", message = "Mobile number should be 10 digits")
            @RequestParam String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));
    }
}
