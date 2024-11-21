package com.moreira.picpaychallenge.presentation.controllers;

import com.moreira.picpaychallenge.application.dto.TransferRequestDTO;
import com.moreira.picpaychallenge.application.dto.TransferResponseDTO;
import com.moreira.picpaychallenge.application.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(@Valid @RequestBody TransferRequestDTO transferRequestDTO) {
        TransferResponseDTO transferResponseDTO = transferService.transfer(transferRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transferResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(transferResponseDTO);
    }
}
