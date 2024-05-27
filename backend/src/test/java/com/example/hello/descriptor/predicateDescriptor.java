package com.example.hello.descriptor;

import com.example.hello.descriptor.exam.CryptoCurrency;
import com.example.hello.descriptor.exam.SampleData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class predicateDescriptor {

    @Test // Predicate
    void test (){
        List<CryptoCurrency> cryptoCurrencies = SampleData.cryptoCurrencies;
        List<CryptoCurrency> result = filter(cryptoCurrencies , cc -> cc.getPrice() > 500000);
    }


    private static List<CryptoCurrency> filter(List<CryptoCurrency> cryptoCurrencies  , Predicate<CryptoCurrency> p){
        List<CryptoCurrency> result = new ArrayList<>();
        for (CryptoCurrency cc : cryptoCurrencies){
            if(p.test(cc)){
                result.add(cc);
            }
        }

        return result;
    }


}
