package com.example.apicajero.Service;

import java.util.Map;

public interface cashOutService {
    Map<String, String> generateCashOut(int amount);
    int getSaldo();
}
