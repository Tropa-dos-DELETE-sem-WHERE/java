import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            consoles("Aplicação Iniciando: ");
            Thread.sleep(1000);
            for (int i = 0; i < 5; i++) {
                consoles("Log da aplicação: ");
                Thread.sleep(1000);
            }
            consoles("Aplicação Encerrando: ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static void consoles(String mensagem){
        DateTimeFormatter formatacaoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dataHora = LocalDateTime.now();
        String dataHoraFormatada = dataHora.format(formatacaoDataHora);
        System.out.println(mensagem+dataHoraFormatada);
    }
}
//debug
//trace
//info
//erro
//sucess
//warning