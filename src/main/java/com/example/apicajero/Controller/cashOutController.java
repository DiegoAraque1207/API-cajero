package com.example.apicajero.Controller;


import com.example.apicajero.Service.cashOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "cashout/")
@RestController
@CrossOrigin(origins ="*", allowedHeaders = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class cashOutController {
    @Autowired
    private cashOutService CashOutService;

    @RequestMapping(value = "withdrawal", method = RequestMethod.POST, consumes = { "application/json" }, produces = {
            "application/json" })
    public ResponseEntity<Map> cashOut(@RequestBody HashMap data){
        Map response;
        int amount = Integer.parseInt(data.get("Amount").toString());
        if(amount%10000 != 0){
            response = new HashMap();
            response.put("msg", "Error in the amount requested");
            return new ResponseEntity<Map>(response, HttpStatus.FORBIDDEN);
        }
        response = CashOutService.generateCashOut(amount);
        if(response.containsKey("reason")){
            //reason: 1 -> saldo insuficiente
            //reason: 2 -> no tiene los billetes necesarios
            if(response.get("reason") == "1"){
                response.remove("reason");
                response.put("msg", "Saldo insuficiente. Comuniquese con nuestras oficinas.");
            }else if(response.get("reason") == "2"){
                response.remove("reason");
                response.put("msg", "El cajero no tiene billetes disponibles para esa cantidad. Comuniquese con nuestras oficinas..");
            }

        }
        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }
    @RequestMapping(value = "balance", method = RequestMethod.GET, produces = {
            "application/json" })
    public ResponseEntity<Map> saldo(){
        Map response = new HashMap();
        response.put("Saldo", CashOutService.getSaldo());

        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }
}