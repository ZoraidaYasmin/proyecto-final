package com.example.demo.consumer;


import com.example.demo.entity.BuyBootCoin;
import com.example.demo.entity.ExchangeEvent;
import com.example.demo.producer.ExchangeProducer;
import com.example.demo.service.ExchangeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ExchangeConsumer {

	@Autowired
	ExchangeService exchangeService;

	@Autowired
	ExchangeProducer exchangeProducer;

	@KafkaListener(topics = {"buy-bootcoin-events"})
	public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
		log.info("ConsumerRecord: {}", consumerRecord);
		BuyBootCoin buyBootCoin = new Gson().fromJson(consumerRecord.value(), BuyBootCoin.class);
		ExchangeEvent exEvent = new ExchangeEvent();
		exEvent.setMontoSoles(buyBootCoin.getMonto());
		exEvent.setBuyBootCoinId(buyBootCoin.getId());

		Mono<ExchangeEvent> exchangeEventMono = exchangeService.convertSolesBootcoin(exEvent).flatMap( exchangeEvent -> {
			exchangeEvent.setBuyBootCoin(buyBootCoin);
			try {
				exchangeProducer.sendExchangeEvent(exEvent);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			return Mono.just(exchangeEvent);
		});

		exchangeEventMono.subscribe( e -> log.info("Funcion sendExchangeEvent"));

	}


}
