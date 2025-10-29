package school.sptech;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeituraInsercaoExcel {

    private void registrarLog(String tipo, String descricao, String erro) {
        String insertLog = "INSERT INTO log (tipo, descricao, erro) VALUES (?, ?, ?)";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://172.31.41.5:3306/educadata", "Caramico", "urubu100");
             PreparedStatement stmt = con.prepareStatement(insertLog)) {

            stmt.setString(1, tipo);
            stmt.setString(2, descricao);
            stmt.setString(3, erro);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("❌ ERRO: " + e.getMessage());
        }
    }

    public void lerExcel() {
        // Definindo variáveis de ambiente
        String url = "jdbc:mysql://172.31.41.5:3306/educadata"; // caminho do banco
        String user = "Caramico";       // usuario do MySQL
        String password = "urubu100";   // senha da conexão


        //Trazendo o o arquivo da S3;
        ConexaoAws conexaoS3 = new ConexaoAws();

        Workbook workbookS3;
        try {
            workbookS3 = conexaoS3.conexaoS3();
            registrarLog("INFO", "Arquivo Excel obtido com sucesso da S3", "Sem erro");
        } catch (Exception e) {
            registrarLog("ERRO_ARQUIVO", "Falha ao obter arquivo da S3", e.getMessage());
            System.out.println("❌ ERRO AO CARREGAR ARQUIVO: " + e.getMessage());
            return; // interrompendo o código antes de tentar inserir no banco
        }


        // Definindo o insert que será feito
        String insert = "INSERT INTO registro (" +
                "ano, codigo_uf_escola, sigla_uf_escola, codigo_municipio_escola, " +
                "nome_municipio_escola, codigo_escola_educacenso, nome_escola_educacenso, " +
                "dependencia_adm, localizacao_escola, matriculas, participantes_nec_esp, participantes, " +
                "taxa_participacao, media_cn, media_ch, media_lp, media_mt, media_red, media_obj, media_tot, " +
                "inse, pc_formacao_docente, taxa_permanencia, taxa_aprovacao, taxa_reprovacao, taxa_abandono, porte_escola" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = DriverManager.getConnection(url, user, password);


             // Criando um "PreparedStatement" para fazer comandos SQL de forma segura
             PreparedStatement stmt = con.prepareStatement(insert);
             Workbook workbook = conexaoS3.conexaoS3()) {


            Sheet sheet = workbook.getSheetAt(0);

            // Registrando o início do processo
            System.out.println("⏳ INICIANDO PROCESSO");
            registrarLog("INICIO_PROCESSO", "Iniciando leitura e inserção do arquivo", "Sem erro");

            // Contador para controlar o número de registros por batch (executa a cada XXX inserções)
            int contadorBatch = 0;

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
                contadorBatch++;

                // try catch para executar o batch e exibir/inserir logs
                try {
                // Validação para executar o Batch a cada 1000 vezes
                    if (contadorBatch % 1000 == 0) {
                        stmt.executeBatch(); // executando batch(pacote)
                        System.out.println("✅ INSERÇÃO: " + contadorBatch + " registros inseridos...");
                        String mensagem = "✅ Inserido " + contadorBatch + " registros";
                        registrarLog("SUCESSO_INSERCAO", mensagem, "Sem erro");
                    }
                } catch (SQLException e) {
                    System.err.println("❌ ERRO NA INSERÇÃO EM LOTE: " + e.getMessage());
                    String mensagemErro = "Erro ao inserir lote de " + contadorBatch + " registros";
                    registrarLog("ERRO_INSERCAO", mensagemErro, e.getMessage());
                } catch (Exception e) {
                    System.err.println("⚠️ ERRO GERAL: " + e.getMessage());
                    registrarLog("ERRO_GERAL", "Erro inesperado durante a inserção", e.getMessage());
                }
            }

            // Executa o último batch com os registros restantes que não completaram o último lote
            stmt.executeBatch();
            System.out.println("✅ INSERÇÃO: Inserido " + contadorBatch + " registros remanescentes");
            String mensagem = "✅ Inserido " + contadorBatch + "registros remanescentes";
            registrarLog("SUCESSO_INSERCAO", mensagem, "Sem erro");
            registrarLog("FIM_PROCESSO", "Processo concluído com sucesso. Total de registros inseridos: " + contadorBatch, "Sem erro");
        } catch (Exception e) {
            System.out.println("❌ Erro na conexão");
            registrarLog("ERRO", "Erro na conexão com o Banco de Dados", e.getMessage());
            System.out.println("❌ ERRO: " + e.getMessage());
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
