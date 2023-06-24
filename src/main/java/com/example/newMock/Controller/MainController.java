package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Random;

@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper =new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public Object postBalances(@RequestBody RequestDTO requestDTO){
        try{
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String currency = new String();

            if(firstDigit == '8') {
                maxLimit = new BigDecimal(2000.00);
                currency = ("US");
            } else if (firstDigit == '9'){
                maxLimit = new BigDecimal(1000.00);
                currency = ("EU");
            }else {
                maxLimit = new BigDecimal(10000.00);
                currency = ("RUB");
            }

            BigDecimal minLimit = new BigDecimal("1");
            Random random = new Random();
            BigDecimal balance = minLimit.add(new BigDecimal(String.valueOf(random.nextInt(maxLimit.intValue() - minLimit.intValue() + 1) + minLimit.intValue())));

            String RqUID = requestDTO.getRqUID();

            ResponseDTO responseDTO = new ResponseDTO();

            responseDTO.setRqUID(RqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(balance.toString());
            responseDTO.setMaxLimit(maxLimit);

            log.error("****** RequestDTO(Запрос) ******" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("****** ResponseDTO(Ответ) ******" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
