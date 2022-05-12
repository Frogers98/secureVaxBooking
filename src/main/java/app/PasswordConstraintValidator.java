package app;

import org.passay.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PasswordConstraintValidator {

    public static String CheckValid(String password) throws FileNotFoundException, IOException {
        List<Rule> rules = new ArrayList<>();
        //Rule 1: Password length should be in between 8 and 16 characters
        rules.add(new LengthRule(8, 32));
        //Rule 2: No whitespace allowed
        rules.add(new WhitespaceRule());
        //Rule 3.a: At least one Upper-case character
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        //Rule 3.b: At least one Lower-case character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        //Rule 3.c: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        //Rule 3.d: At least one special character
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));

        PasswordValidator validator = new PasswordValidator(rules);
        PasswordData pass = new PasswordData(password);
        RuleResult result = validator.validate(pass);

        if (result.isValid()) {
            return "1";
        } else {
            StringBuilder sb = new StringBuilder("Invalid Password: ");
            List <String> results = validator.getMessages(result);
            for (int i = 0; i < results.size(); i++)
                sb.append(results.get(i) + " ");
            return sb.toString();
        }
    }
}
