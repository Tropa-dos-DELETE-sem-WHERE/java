package school.sptech;

public class Main {

    public static void main(String[] args) {
        // criando leitor
        LeituraInsercaoExcel leitor = new LeituraInsercaoExcel();

        // chamando o metodo para ler e inserir os registros no banco de dados
        leitor.lerExcel();
    }
}