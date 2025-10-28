package school.sptech;

public class Main {

    public static void main(String[] args) {

        // definindo caminho do XLSX
        String caminhoArquivo = "src/main/resources/base.xlsx";

        // criando leitor
        LeituraInsercaoExcel leitor = new LeituraInsercaoExcel();

        // chamando o metodo para ler e inserir os registros no banco de dados
        leitor.lerExcel();
    }
}