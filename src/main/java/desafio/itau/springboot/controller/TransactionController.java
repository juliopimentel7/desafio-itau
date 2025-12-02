package desafio.itau.springboot.controller;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import desafio.itau.springboot.dto.TransactionRequest;
import desafio.itau.springboot.model.Transaction;
import desafio.itau.springboot.service.TransactionService;

@RestController
@RequestMapping("/transacao")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@Valid @RequestBody TransactionRequest request) {

        // validação: timestamp não pode ser no futuro (regra do desafio Itaú)
        if (request.getDataHora().isAfter(OffsetDateTime.now()) || request.getValor() <= 0) {
            return ResponseEntity.unprocessableEntity().build(); // HTTP 422
        }

        transactionService.addTransaction(
            new Transaction(request.getValor(), request.getDataHora())
        );

        return ResponseEntity.status(HttpStatus.CREATED).build(); // HTTP 201
    }

    @DeleteMapping
    public ResponseEntity<Void> clearTransactions() {
        transactionService.clearTransactions();
        return ResponseEntity.ok().build(); // HTTP 200
    }
}
