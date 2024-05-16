package com.example.hello.reactor.exam;

import java.util.List;

public interface CryptoCurrencyPriceListener {
    void onPrice(List<Integer> priceList);

    void onComplete();
}