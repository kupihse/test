package com.example.s1k0de.entry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eugeen on 20/01/2018.
 */

public class Regular {
    //заготовка для регулярки
    public static final Pattern pattern = Pattern.compile
            ("[a-zA-Z]{1}[a-zA-Z\\d\\u002E\\u005F]+@([a-zA-Z]+\\u002E){1,2}((net)|(com)|(org)|(ru))");

    public static boolean doMatch(String word){
        Matcher matcher=pattern.matcher(word);
        return matcher.matches();
    }
}
