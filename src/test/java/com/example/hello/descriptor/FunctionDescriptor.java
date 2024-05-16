package com.example.hello.descriptor;

import com.example.hello.descriptor.exam.CryptoCurrency;
import com.example.hello.descriptor.exam.SampleData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionDescriptor {

    @Test
    void test(){
        List<CryptoCurrency> cryptoCurrencies = SampleData.cryptoCurrencies;
        List<CryptoCurrency> filtered = filter(cryptoCurrencies ,
                    cc-> cc.getUnit() == CryptoCurrency.CurrencyUnit.BTC ||
                    cc.getUnit() == CryptoCurrency.CurrencyUnit.ETH
            );

        int totalPayment = calculatePayment(filtered , cc -> cc.getPrice() * 2);
        System.out.println(totalPayment);

//        addBookmark(filtered , cc-> saveBookmark(cc));
    }

    private static List<CryptoCurrency> filter(List<CryptoCurrency> cryptoCurrencies , Predicate<CryptoCurrency> p ){
        List<CryptoCurrency> result = new ArrayList<>();
        for (CryptoCurrency cc  : cryptoCurrencies){
            if(p.test(cc)){
                result.add(cc);
            }
        }
        return result;
    }

    private static int calculatePayment(List<CryptoCurrency> cryptoCurrencies , Function<CryptoCurrency , Integer> f){
        int totalPayment = 0;
        for (CryptoCurrency cc : cryptoCurrencies){
            totalPayment += f.apply(cc);
        }

        return totalPayment;
    }

}
