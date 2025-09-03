import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int logAleatorio = ThreadLocalRandom.current().nextInt(1,5);
        String[] logs = new String[]
                {
                        "[DEBUG] SDADADADS",
                        "[WARNING] SDADADADS",
                        "[BD] SDADADADS",
                        "[INFO] SDADADADS"
                };
        try{
            consoles("Aplicação Iniciando: ");
            Thread.sleep(1000);
            for (int i = 0; i < 5; i++) {
                logAleatorio = ThreadLocalRandom.current().nextInt(1,5);
                consoles(logs[logAleatorio-1]);
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