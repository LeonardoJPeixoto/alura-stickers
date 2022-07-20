public class Util {

    private Util() {
        // Classe utilitária - não instanciável
    }

    public static boolean stringVazia(String s) {
        if (s == null) {
            return true;
        }
        return "".equals(s.trim());
    }

}
