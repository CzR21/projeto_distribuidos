package validators;

import java.util.regex.Pattern;

import enums.Key;

public class Validator {

	public static boolean validar(Key key, String valor) {
		if (valor == null || valor.isEmpty()) {
            return false; 
        }

        switch (key) {
            case ra: return Pattern.matches("^[0-9]{7}$", valor);
            case nome: return Pattern.matches("^[A-Z ]{1,50}$", valor);
            case senha: return Pattern.matches("^[a-zA-Z]{8,20}$", valor);
            case titulo: return Pattern.matches("^[A-Z ]{1,100}$", valor);
            case descricao: return valor.length() <= 500;
            case id: try {
                return Integer.parseInt(valor) >= 0;
            } catch (NumberFormatException e) {
                return false; 
            }
            default: throw new IllegalArgumentException("Chave de validação desconhecida: " + key);
        }
    }
}
