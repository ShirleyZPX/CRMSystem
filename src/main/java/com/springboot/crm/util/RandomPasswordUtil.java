package com.springboot.crm.util;

public class RandomPasswordUtil {
    private static String[] symbol = {"/", ".", ",", "~", ";", "#", "@", "*", "$", "%", "&", "(", ")"};
    public static String getRandomPassword() {
        String password = "";
        for(int i = 0; i < 12; i++) {
            double rand = Math.random();
            double randTri = Math.random() * 4;
            if(randTri >= 0 && randTri < 1) {
                password += (char) (rand * ('9' - '0') + '0');
            } else if(randTri >= 1 && randTri <2) {
                password += (char) (rand * ('Z' - 'A') + 'A');
            } else if(randTri >= 2 && randTri <3){
                password += (char) (rand * ('z' - 'a') + 'a');
            } else {
                password += symbol[(int) (rand* symbol.length)];
            }
        }
        System.out.println(password);
        return password;
    }
}
