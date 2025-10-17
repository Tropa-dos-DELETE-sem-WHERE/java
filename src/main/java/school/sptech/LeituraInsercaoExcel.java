package school.sptech;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class LeituraInsercaoExcel {

    public void lerExcel(String caminho) {

        // Definindo variáveis de ambiente
        String url = "jdbc:mysql://localhost:3306/educadata"; // caminho do banco
        String user = "root";       // usuario do MySQL
        String password = "admin";   // senha da conexão

        // Definindo o insert que será feito
        String insert = "INSERT INTO registro (" +
                "ano, codigo_uf_escola, sigla_uf_escola, codigo_municipio_escola, " +
                "nome_municipio_escola, codigo_escola_educacenso, nome_escola_educacenso, " +
                "dependencia_adm, localizacao_escola, matriculas, participantes_nec_esp, participantes, " +
                "taxa_participacao, media_cn, media_ch, media_lp, media_mt, media_red, media_obj, media_tot, " +
                "inse, pc_formacao_docente, taxa_permanencia, taxa_aprovacao, taxa_reprovacao, taxa_abandono, porte_escola" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);

             // Criando um "PreparedStatement" para fazer comandos SQL de forma segura
             PreparedStatement stmt = conn.prepareStatement(insert);
             InputStream arquivo = new FileInputStream(caminho);
             Workbook workbook = new XSSFWorkbook(arquivo)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Contador que será usado para executar todos os inserts de uma vez só, a cada XXX vezes
            int contador = 0;

            // For para percorrer o XLSX
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Definindo os "?" do insert
                stmt.setInt(1, getIntCell(row.getCell(0)));
                stmt.setInt(2, getIntCell(row.getCell(1)));
                stmt.setString(3, getStringCell(row.getCell(2)));
                stmt.setInt(4, getIntCell(row.getCell(3)));
                stmt.setString(5, getStringCell(row.getCell(4)));
                stmt.setLong(6, getLongCell(row.getCell(5)));
                stmt.setString(7, getStringCell(row.getCell(6)));
                stmt.setInt(8, getIntCell(row.getCell(7)));
                stmt.setInt(9, getIntCell(row.getCell(8)));
                stmt.setInt(10, getIntCell(row.getCell(9)));
                stmt.setObject(11, getIntegerCell(row.getCell(10)), java.sql.Types.INTEGER);
                stmt.setInt(12, getIntCell(row.getCell(11)));
                stmt.setObject(13, getDoubleCell(row.getCell(12)), java.sql.Types.DOUBLE);
                stmt.setObject(14, getDoubleCell(row.getCell(13)), java.sql.Types.DOUBLE);
                stmt.setObject(15, getDoubleCell(row.getCell(14)), java.sql.Types.DOUBLE);
                stmt.setObject(16, getDoubleCell(row.getCell(15)), java.sql.Types.DOUBLE);
                stmt.setObject(17, getDoubleCell(row.getCell(16)), java.sql.Types.DOUBLE);
                stmt.setObject(18, getDoubleCell(row.getCell(17)), java.sql.Types.DOUBLE);
                stmt.setObject(19, getDoubleCell(row.getCell(18)), java.sql.Types.DOUBLE);
                stmt.setObject(20, getDoubleCell(row.getCell(19)), java.sql.Types.DOUBLE);
                stmt.setObject(21, getDoubleCell(row.getCell(20)), java.sql.Types.DOUBLE);
                stmt.setObject(22, getDoubleCell(row.getCell(21)), java.sql.Types.DOUBLE);
                stmt.setObject(23, getDoubleCell(row.getCell(22)), java.sql.Types.DOUBLE);
                stmt.setObject(24, getDoubleCell(row.getCell(23)), java.sql.Types.DOUBLE);
                stmt.setObject(25, getDoubleCell(row.getCell(24)), java.sql.Types.DOUBLE);
                stmt.setObject(26, getDoubleCell(row.getCell(25)), java.sql.Types.DOUBLE);
                stmt.setString(27, getStringCell(row.getCell(26)));

                // Adicionando o insert no Batch (pacote)
                stmt.addBatch();

                // Aumentando contador para indicar que adicionamos um insert no Batch
                contador++;

                // Validação para executar o Batch a cada XXX vezes
                if (contador % 1000 == 0) {
                    stmt.executeBatch();
                    System.out.println(contador + " registros inseridos...");
                }
            }

            // Executando registros que sobraram
            stmt.executeBatch();
            System.out.println("Inserido " + contador + " registros remanescentes");
            System.out.println("Processo finalizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao ler ou inserir o arquivo: " + e);
            e.printStackTrace();
        }
    }

    // metodos para tratar os nulls
    private int getIntCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return 0;
        return (int) cell.getNumericCellValue();
    }

    private Integer getIntegerCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;
        return (int) cell.getNumericCellValue();
    }

    private long getLongCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return 0L;
        return (long) cell.getNumericCellValue();
    }

    private double getDoubleCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return 0.0;
        return cell.getNumericCellValue();
    }

    private String getStringCell(Cell cell) {
        if (cell == null) return "";
        return cell.toString();
    }
}
