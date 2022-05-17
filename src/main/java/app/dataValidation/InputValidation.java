package app.dataValidation;

import java.util.regex.Pattern;

public class InputValidation {
    public static boolean ppsnValid(String ppsn) {
        String letters = ppsn.substring(0, 7);
        boolean isMatch = Pattern.compile("^\\d+$")
                .matcher(letters)
                .find();
        return (ppsn.length() == 8 && Character.isLetter(ppsn.charAt(7)) && isMatch);
    }


    public static boolean validateEmailFormat (String email) {
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(EMAIL_REGEX);
    }

    // only numbers permitted
    public static boolean validatePhoneNumberFormat (String phoneNum) {
        boolean isMatch = Pattern.compile("^\\d+$")
                .matcher(phoneNum)
                .find();
        String preface = phoneNum.substring(0, 2);
        System.out.println(preface);
        return (phoneNum.length() == 10 && preface.equals("08") && isMatch);
    }

    // only letters and numbers permitted
    public static boolean ValidateAddressFormat(String address) {
        boolean isMatch = Pattern.compile("^[a-zA-Z\\d]")
                .matcher(address)
                .find();
        return isMatch && address.length() < 100;
    }


    // only letters and numbers permitted
    public static boolean ValidateForenameInput(String name) {
        boolean isMatch = Pattern.compile("^[a-zA-Z\\d]")
                .matcher(name)
                .find();
        return isMatch && name.length() < 50;
    }


    // only letters and numbers permitted
    public static boolean ValidateSurnameInput(String name) {
        boolean isMatch = Pattern.compile("^[a-zA-Z0-9]")
                .matcher(name)
                .find();
        return isMatch && name.length() < 50;
    }
}
