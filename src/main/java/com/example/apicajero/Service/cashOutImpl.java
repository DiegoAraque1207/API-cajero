package com.example.apicajero.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class cashOutImpl implements cashOutService {
    private int saldo = 1000000;
    private int cant10 = 20;
    private int cant20 = 20;
    private int cant50 = 10;
    private int cant100 = 2;
    @Override
    public Map<String, String> generateCashOut(int amount){
        int billetesDe10=0;
        int billetesDe20=0;
        int billetesDe50=0;
        int billetesDe100=0;
        //se normaliza eliminando los miles para los calculos
        amount = amount / 1000;
        //reason: 1 -> saldo insuficiente
        //reason: 2 -> no tiene los billetes necesarios
        Map<String, String> money = new HashMap<>();
        //si la cantidad solicitada es mayor al saldo se retorna error
        if(amount > saldo){
            money.put("reason", "1");
            return money;
        }
        //Si la cantidad es mayor al valor que se obtiene al sumar todos los billetes en inventario retorna error
        if(amount > (10000 * cant10 + 20000 * cant20 + 50000 * cant50 + 100000 * cant100)){
            money.put("reason", "2");
            return money;
        }
        //Se calcula la cantidad de billetes de 100 a entregar
        billetesDe100 = (int) amount / 100;
        amount = amount % 100;

        if (billetesDe100 >= cant100){//No hay billetes suficientes, se entrega los disponibles y se calcula nuevamente el monto
            amount = amount + (billetesDe100 - cant100) * 100;
            billetesDe100 = cant100;
        }
        billetesDe50 = (int) amount / 50;
        amount = amount % 50;
        if (billetesDe50 >= cant50){//No hay billetes suficientes, se entrega los disponibles y se calcula nuevamente el monto
            amount = amount + (billetesDe50 - cant50) * 50;
            billetesDe50 = cant50;
        }
        billetesDe20 = (int) amount / 20;
        amount = amount % 20;
        if (billetesDe20 >= cant20){//No hay billetes suficientes, se entrega los disponibles y se calcula nuevamente el monto
            amount = amount + (billetesDe20 - cant20) * 20;
            billetesDe20 = cant20;
        }
        billetesDe10 = (int) amount / 10;
        amount = amount % 10;
        if (billetesDe10 >= cant10){//No hay billetes suficientes, se entrega los disponibles y se calcula nuevamente el monto
            amount = amount + (billetesDe10 - cant10) * 10;
            billetesDe10 = cant10;
        }
        if (amount == 0){
            cant100 = cant100 - billetesDe100;
            cant50 = cant50 - billetesDe50;
            cant20 = cant20 - billetesDe20;
            cant10 = cant10 - billetesDe10;
            saldo = saldo - 100 * cant10 + 50 * cant50 + 20 * cant20 + 10 *cant10;
            money.put("10000", String.valueOf(billetesDe10));
            money.put("20000", String.valueOf(billetesDe20));
            money.put("50000", String.valueOf(billetesDe50));
            money.put("100000", String.valueOf(billetesDe100));
            return money;
        }else{
            money.put("reason", "2");
            return money;
        }

    }

    public int getSaldo(){
        return saldo;
    }
}
