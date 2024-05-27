package com.example.hello.descriptor;

import com.example.hello.descriptor.exam.CryptoCurrency;
import com.example.hello.descriptor.exam.SampleData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConsumerDescriptor {

    @Test
    void test(){
        List<CryptoCurrency> cryptoCurrencies = SampleData.cryptoCurrencies;
        List<CryptoCurrency> filtered = filter(cryptoCurrencies ,
                    cc-> cc.getUnit() == CryptoCurrency.CurrencyUnit.BTC ||
                    cc.getUnit() == CryptoCurrency.CurrencyUnit.ETH
            );

        addBookmark(filtered , cc-> saveBookmark(cc));
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

    private static void addBookmark(List<CryptoCurrency> cryptoCurrencies , Consumer<CryptoCurrency> consumer){
        for(CryptoCurrency cc : cryptoCurrencies){
            consumer.accept(cc);
        }
    }

    private static void saveBookmark(CryptoCurrency cryptoCurrency){
        System.out.println("# Save " + cryptoCurrency.getUnit());
    }
}
