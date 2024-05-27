package com.example.hello.descriptor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SupplierDescriptor {


    @Test
    void test (){
        String mnemonic = createMnemonic();
        System.out.println(mnemonic);
    }

    private static String createMnemonic(){
        return Stream.generate(() -> getMnemonic())
                .limit(12)
                .collect(Collectors.joining(" "));
    }

    private static String getMnemonic(){
        List<String> mnemonic = Arrays.asList(
                "alpha" , "bravo" , "charlie" ,
                "alpha1" , "bravo1" , "charlie1" ,
                "alpha2" , "bravo2" , "charlie2" ,
                "alpha3" , "bravo3" , "charlie3"
        );

        Collections.shuffle(mnemonic);
        return mnemonic.get(0);
    }

}
