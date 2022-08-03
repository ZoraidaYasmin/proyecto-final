package com.project3.debitcard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project3.debitcard.entity.DebitCard;
import com.project3.debitcard.entity.Transaction;
import com.project3.debitcard.service.DebitCardService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/debitcard")
public class DebitCardController {
	
	private static final Logger LOG = LogManager
	          .getLogger(DebitCardController.class);
	
	@Autowired
	DebitCardService debitCardService;
	
	@GetMapping("/findAll")
	public Flux<DebitCard> getDebitCards() {
	  LOG.info("Service call FindAll - DebitCard");
	  return debitCardService.findAll();
	}

	@GetMapping("/find/{id}")
	public Mono<DebitCard> getDebitCard(@PathVariable String id) {
	  LOG.info("Service call FindById - DebitCard");
	  return debitCardService.findById(id);
	}

	@GetMapping("/accountDetail/{id}")
	public Flux<DebitCard> accountDetail(@PathVariable String id) {
	  LOG.info("Service call accountDetail - DebitCard");
	  return debitCardService.accountDetail(id);
	}
	
	@GetMapping("/principalDebitAccount/{id}")
	public Flux<DebitCard> principalDebitAccount(@PathVariable String id) {
	  LOG.info("Service call principalDebitAccount - DebitCard");
	  return debitCardService.principalDebitAccount(id);
	}

	@PostMapping("/create")
	public Mono<DebitCard> createDebitCard(@RequestBody DebitCard p) {
	  LOG.info("Service call create - DebitCard");
	  return debitCardService.create(p);
	}

	@PutMapping("/update/{id}")
	public Mono<DebitCard> updateDebitCard(@RequestBody DebitCard p, @PathVariable String id) {
	  LOG.info("Service call Update - DebitCard");
	  return debitCardService.update(p, id);
	}

	@DeleteMapping("/delete/{id}")
	public Mono<DebitCard> deleteDebitCard(@PathVariable String id) {
	  LOG.info("Service call delete - DebitCard");
	  return debitCardService.delete(id);
	}
}
