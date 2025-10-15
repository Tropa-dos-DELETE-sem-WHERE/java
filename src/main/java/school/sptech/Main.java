package school.sptech;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String caminho = "src/main/resources/base.xlsx";

        LeitorExcel leitor = new LeitorExcel();
        List<Registro> registros = leitor.lerExcel(caminho);
    }
}