package school.sptech;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LeitorExcel {

//    Criando uma lista de registros para salvar todos os registros
    List<Registro> registros = new ArrayList<>();

    public List<Registro> lerExcel(String caminho) {
        // try catch para tratar erros e armazenar os logs
        try (InputStream arquivo = new FileInputStream(caminho);
             Workbook workbook = new XSSFWorkbook(arquivo)) {

//          Selecionando a primeira folha do arquivo xlsx
            Sheet sheet = workbook.getSheetAt(0);

//          for para percorrer as linhas
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Registro registro = new Registro();

//                for para percorrer as colunas de cada linha
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);

//                    1. Switch Case para identificar qual COLUNA o for está
//                     2. Valor padrão de cada tipo:
//                       - int ou long = 0
//                       - Integer = null
//                       - double = 0.0
//                       - String = ""

                    switch (j) {
//                      caso seja a coluna X defina o valor de REGISTRO para (valide se a célula está NULA e se é NUMERIC, se for NUMERIC então
//                      transforme para int, se não defina o valor como 0)
                        case 0 -> registro.setAno(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 0);
                        case 1 -> registro.setCodigoUfEscola(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 0);
                        case 2 -> registro.setSiglaUfEscola(cell != null ? cell.toString() : "");
                        case 3 -> registro.setCodigoMunicipioEscola(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 0);
                        case 4 -> registro.setNomeMunicipioEscola(cell != null ? cell.toString() : "");
                        case 5 -> registro.setCodigoEscolaEducacenso(cell != null && cell.getCellType() == CellType.NUMERIC ? (long) cell.getNumericCellValue() : 0L);
                        case 6 -> registro.setNomeEscolaEducacenso(cell != null ? cell.toString() : "");
                        case 7 -> registro.setDependenciaAdm(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 0);
                        case 8 -> registro.setLocalizacaoEscola(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 0);
                        case 9 -> registro.setMatriculas(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 0);
                        case 10 -> registro.setParticipantesNecEsp(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : null);
                        case 11 -> registro.setParticipantes(cell != null && cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 0);
                        case 12 -> registro.setTaxaParticipacao(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 13 -> registro.setMediaCN(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 14 -> registro.setMediaCH(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 15 -> registro.setMediaLP(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 16 -> registro.setMediaMT(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 17 -> registro.setMediaRED(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 18 -> registro.setMediaOBJ(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 19 -> registro.setMediaTOT(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 20 -> registro.setInse(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 21 -> registro.setPcFormacaoDocente(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 22 -> registro.setTaxaPermanencia(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 23 -> registro.setTaxaAprovacao(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 24 -> registro.setTaxaReprovacao(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 25 -> registro.setTaxaAbandono(cell != null && cell.getCellType() == CellType.NUMERIC ? cell.getNumericCellValue() : 0.0);
                        case 26 -> registro.setPorteEscola(cell != null ? cell.toString() : "");
                    }
                }

//              Adicionando o registro na lista de registros
                registros.add(registro);
            }

//          Exibindo todos os registros
            for (Registro r : registros) {
                System.out.println("========================================");
                System.out.println("Ano: " + r.getAno());
                System.out.println("UF Escola: " + r.getSiglaUfEscola() + " | Código Município: " + r.getCodigoMunicipioEscola());
                System.out.println("Nome Município: " + r.getNomeMunicipioEscola());
                System.out.println("Código Escola: " + r.getCodigoEscolaEducacenso());
                System.out.println("Nome Escola: " + r.getNomeEscolaEducacenso());
                System.out.println("Dependência Adm: " + r.getDependenciaAdm() + " | Localização: " + r.getLocalizacaoEscola());
                System.out.println("Matrículas: " + r.getMatriculas() + " | Participantes NEC: " + r.getParticipantesNecEsp() + " | Participantes: " + r.getParticipantes());
                System.out.println("Taxa Participação: " + r.getTaxaParticipacao());
                System.out.println("Média CN: " + r.getMediaCN() + " | CH: " + r.getMediaCH() + " | LP: " + r.getMediaLP() + " | MT: " + r.getMediaMT());
                System.out.println("Média RED: " + r.getMediaRED() + " | OBJ: " + r.getMediaOBJ() + " | TOT: " + r.getMediaTOT());
                System.out.println("INSE: " + r.getInse() + " | Formação Docente: " + r.getPcFormacaoDocente());
                System.out.println("Taxa Permanência: " + r.getTaxaPermanencia() + " | Aprovação: " + r.getTaxaAprovacao() + " | Reprovação: " + r.getTaxaReprovacao() + " | Abandono: " + r.getTaxaAbandono());
                System.out.println("Porte Escola: " + r.getPorteEscola());
            }


            System.out.println("Leitura realizada com sucesso ✅");

        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo ❌: " + e);
            e.printStackTrace();
        }

        return registros;
    }
}
